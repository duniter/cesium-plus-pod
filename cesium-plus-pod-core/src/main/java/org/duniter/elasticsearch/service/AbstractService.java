package org.duniter.elasticsearch.service;

/*
 * #%L
 * Duniter4j :: Core API
 * %%
 * Copyright (C) 2014 - 2015 EIS
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 3 of the 
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public 
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/gpl-3.0.html>.
 * #L%
 */


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.ImmutableSet;
import org.duniter.core.beans.Bean;
import org.duniter.core.client.model.bma.jackson.JacksonUtils;
import org.duniter.core.client.model.elasticsearch.Record;
import org.duniter.core.client.model.elasticsearch.Records;
import org.duniter.core.exception.TechnicalException;
import org.duniter.core.service.CryptoService;
import org.duniter.core.util.json.JsonAttributeParser;
import org.duniter.elasticsearch.PluginSettings;
import org.duniter.elasticsearch.client.Duniter4jClient;
import org.duniter.elasticsearch.exception.InvalidFormatException;
import org.duniter.elasticsearch.exception.InvalidSignatureException;
import org.duniter.elasticsearch.exception.InvalidTimeException;
import org.elasticsearch.ElasticsearchException;
import org.elasticsearch.common.logging.ESLogger;
import org.elasticsearch.common.logging.Loggers;
import org.nuiton.i18n.I18n;

import java.io.IOException;
import java.util.Objects;
import java.util.Set;

/**
 * Created by Benoit on 08/04/2015.
 */
public abstract class AbstractService implements Bean {

    protected static JsonAttributeParser<String> PARSER_HASH = new JsonAttributeParser<>(Record.PROPERTY_HASH, String.class);
    protected static JsonAttributeParser<String> PARSER_SIGNATURE = new JsonAttributeParser<>(Record.PROPERTY_SIGNATURE, String.class);
    protected static JsonAttributeParser<String> PARSER_READ_SIGNATURE = new JsonAttributeParser<>(Records.PROPERTY_READ_SIGNATURE, String.class);

    protected final ESLogger logger;
    protected Duniter4jClient client;
    protected PluginSettings pluginSettings;
    protected CryptoService cryptoService;

    private boolean ready = false;
    private final int retryCount;
    private final int retryWaitDuration;
    private final int documentTimeMaxPastDelta;
    private final int documentTimeMaxFutureDelta;

    public AbstractService(String loggerName, Duniter4jClient client, PluginSettings pluginSettings) {
        this(loggerName, client, pluginSettings, null);
    }

    public AbstractService(Duniter4jClient client, PluginSettings pluginSettings) {
        this(client, pluginSettings, null);
    }

    public AbstractService(Duniter4jClient client, PluginSettings pluginSettings, CryptoService cryptoService) {
        this("duniter", client, pluginSettings, cryptoService);
    }

    public AbstractService(String loggerName, Duniter4jClient client, PluginSettings pluginSettings, CryptoService cryptoService) {
        super();
        this.logger = Loggers.getLogger(loggerName, pluginSettings.getSettings(), new String[0]);
        this.client = client;
        this.pluginSettings = pluginSettings;
        this.cryptoService = cryptoService;
        this.retryCount = pluginSettings.getNodeRetryCount();
        this.retryWaitDuration = pluginSettings.getNodeRetryWaitDuration();
        this.documentTimeMaxPastDelta = pluginSettings.getDocumentTimeMaxPastDelta();
        this.documentTimeMaxFutureDelta = pluginSettings.getDocumentTimeMaxFutureDelta();
    }

    /* -- protected methods --*/

    protected void setIsReady(boolean ready) {
        this.ready = ready;
    }
    public boolean isReady() {
        return this.ready;
    }

    protected void waitReady() {
        try {
            while (!ready) {
                Thread.sleep(500);
            }
        } catch (InterruptedException e){
            // Silent
        }
    }

    protected ObjectMapper getObjectMapper() {
        return JacksonUtils.getThreadObjectMapper();
    }

    protected <T> T executeWithRetry(RetryFunction<T> retryFunction) throws TechnicalException{
        int retry = 0;
        while (retry < retryCount) {
            try {
                return retryFunction.execute();
            } catch (TechnicalException e) {
                retry++;

                if (retry == retryCount) {
                    throw e;
                }

                if (logger.isDebugEnabled()) {
                    logger.debug(I18n.t("duniter4j.service.waitThenRetry", e.getMessage(), retry, retryCount));
                }

                try {
                    Thread.sleep(retryWaitDuration); // waiting
                } catch (InterruptedException e2) {
                    throw new TechnicalException(e2);
                }
            }
        }

        throw new TechnicalException("Error while trying to execute a function with retry");
    }

    protected JsonNode readAndVerifyIssuerSignature(String recordJson) throws ElasticsearchException {
       return readAndVerifyIssuerSignature(recordJson, Records.PROPERTY_ISSUER);
    }

    protected JsonNode readAndVerifyIssuerSignature(String recordJson, String issuerFieldName) throws ElasticsearchException {

        try {
            JsonNode recordObj = getObjectMapper().readTree(recordJson);
            readAndVerifyIssuerSignature(recordJson, recordObj, issuerFieldName);
            return recordObj;
        }
        catch(IOException e) {
            throw new InvalidFormatException("Invalid record JSON: " + e.getMessage(), e);
        }
    }


    protected void readAndVerifyIssuerSignature(JsonNode actualObj, String issuerFieldName) throws ElasticsearchException, JsonProcessingException {
        // Remove hash and signature
        String recordJson = getObjectMapper().writeValueAsString(actualObj);
        readAndVerifyIssuerSignature(recordJson, actualObj, issuerFieldName);
    }

    protected void verifyTimeForUpdate(String index, String type, String id, JsonNode actualObj) {
        verifyTimeForUpdate(index, type, id, actualObj, Record.PROPERTY_TIME);
    }

    protected void verifyTimeForUpdate(String index, String type, String id, JsonNode actualObj, String timeFieldName) {
        verifyTimeForUpdate(index, type, id, actualObj, false, timeFieldName);
    }

    protected void verifyTimeForUpdate(String index, String type, String id, JsonNode actualObj, boolean allowOldDocuments, String timeFieldName) {
        // Check time has been increase - fix #27
        int actualTime = getMandatoryField(actualObj, timeFieldName).asInt();
        int existingTime = client.getMandatoryTypedFieldById(index, type, id, timeFieldName);
        if (actualTime <= existingTime) {
            throw new InvalidTimeException(String.format("Invalid '%s' value: can not be less or equal to the previous value.", timeFieldName, timeFieldName));
        }

        verifyTime(actualTime, allowOldDocuments, timeFieldName);
    }

    protected void verifyTimeForInsert(JsonNode actualObj) {
        verifyTimeForInsert(actualObj, Record.PROPERTY_TIME);
    }

    protected void verifyTimeForInsert(JsonNode actualObj, String timeFieldName) {
        verifyTime(actualObj, false, timeFieldName);
    }

    protected void verifyTime(JsonNode actualObj, boolean allowOldDocuments, String timeFieldName) {
        int actualTime = getMandatoryField(actualObj, timeFieldName).asInt();
        verifyTime(actualTime, allowOldDocuments, timeFieldName);
    }

    protected void verifyTime(int actualTime,
                              boolean allowOldDocuments,
                              String timeFieldName) {
        // Check time has been increase - fix #27
        long deltaTime = System.currentTimeMillis()/1000 - actualTime;

        // Past time
        if (!allowOldDocuments && (deltaTime > 0 && Math.abs(deltaTime) > documentTimeMaxPastDelta)) {
            throw new InvalidTimeException(String.format("Invalid '%s' value: too far (in the past) from the UTC server time. Check your device's clock.", timeFieldName));
        }

        // Future time
        if (deltaTime < 0 && Math.abs(deltaTime) > documentTimeMaxFutureDelta) {
            throw new InvalidTimeException(String.format("Invalid '%s' value: too far (in the future) from the UTC server time. Check your device's clock.", timeFieldName));
        }
    }

    protected String getIssuer(JsonNode actualObj) {
        return  getMandatoryField(actualObj, Records.PROPERTY_ISSUER).asText();
    }

    protected int getVersion(JsonNode actualObj) {
        JsonNode value = actualObj.get(Records.PROPERTY_VERSION);
        if (value == null || value.isMissingNode()) {
            return 1; // first version
        }
        return  value.asInt();
    }

    protected JsonNode getMandatoryField(JsonNode actualObj, String fieldName) {
        JsonNode value = actualObj.get(fieldName);
        if (value.isMissingNode()) {
            throw new InvalidFormatException(String.format("Invalid format. Expected field '%s'", fieldName));
        }
        return value;
    }

    public interface RetryFunction<T> {

        T execute() throws TechnicalException;
    }

    /* -- internal methods -- */

    private void readAndVerifyIssuerSignature(String recordJson, JsonNode recordObj, String issuerFieldName) throws ElasticsearchException {

        Set<String> fieldNames = ImmutableSet.copyOf(recordObj.fieldNames());
        if (!fieldNames.contains(issuerFieldName)
                || !fieldNames.contains(Records.PROPERTY_SIGNATURE)) {
            throw new InvalidFormatException(String.format("Invalid record JSON format. Required fields [%s,%s]", Records.PROPERTY_ISSUER, Records.PROPERTY_SIGNATURE));
        }
        String issuer = getMandatoryField(recordObj, issuerFieldName).asText();
        String signature = getMandatoryField(recordObj, Records.PROPERTY_SIGNATURE).asText();
        String hash = getMandatoryField(recordObj, Records.PROPERTY_HASH).asText();
        int version = getVersion(recordObj);

        boolean validSignature = false;

        // Remove hash and signature
        recordJson = PARSER_SIGNATURE.removeFromJson(recordJson);
        recordJson = PARSER_HASH.removeFromJson(recordJson);

        // Remove 'read_signature' attribute if exists (added AFTER signature)
        String readSignature = null;
        if (fieldNames.contains(Records.PROPERTY_READ_SIGNATURE)) {
            readSignature = getMandatoryField(recordObj, Records.PROPERTY_READ_SIGNATURE).asText();
            recordJson = PARSER_READ_SIGNATURE.removeFromJson(recordJson);
        }

        // Doc version == 1
        if (version == 1) {
            validSignature = cryptoService.verify(recordJson, signature, issuer);
        }

        // Doc version > 1
        else {
            // Remove hash and signature
            boolean validHash = Objects.equals(cryptoService.hash(recordJson), hash);
            if (!validHash) {
                throw new InvalidSignatureException("Invalid hash of JSON document");
            }

            // Validate signature on hash
            validSignature = cryptoService.verify(hash, signature, issuer);
        }

        if (!validSignature) {

            throw new InvalidSignatureException("Invalid signature of JSON string");
        }

        // Validate read signature on hash
        if (readSignature != null) {
            // TODO: validate read signature / recipient ?
        }

        // TODO: check issuer is in the WOT ?
    }
}
