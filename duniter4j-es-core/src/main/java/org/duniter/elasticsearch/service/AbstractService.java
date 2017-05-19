package org.duniter.elasticsearch.service;

/*
 * #%L
 * UCoin Java Client :: Core API
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
import org.duniter.elasticsearch.PluginSettings;
import org.duniter.elasticsearch.client.Duniter4jClient;
import org.duniter.elasticsearch.exception.InvalidFormatException;
import org.duniter.elasticsearch.exception.InvalidSignatureException;
import org.elasticsearch.ElasticsearchException;
import org.elasticsearch.common.logging.ESLogger;
import org.elasticsearch.common.logging.Loggers;
import org.nuiton.i18n.I18n;

import java.io.IOException;
import java.util.Set;

/**
 * Created by Benoit on 08/04/2015.
 */
public abstract class AbstractService implements Bean {

    protected final ESLogger logger;
    protected final ObjectMapper objectMapper;

    protected Duniter4jClient client;
    protected PluginSettings pluginSettings;
    protected CryptoService cryptoService;
    protected final int retryCount;
    protected final int retryWaitDuration;

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
        this.logger = Loggers.getLogger(loggerName);
        this.client = client;
        this.objectMapper = JacksonUtils.newObjectMapper();
        this.pluginSettings = pluginSettings;
        this.cryptoService = cryptoService;
        this.retryCount = pluginSettings.getNodeRetryCount();
        this.retryWaitDuration = pluginSettings.getNodeRetryWaitDuration();
    }

    /* -- protected methods --*/

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
            JsonNode recordObj = objectMapper.readTree(recordJson);
            readAndVerifyIssuerSignature(recordJson, recordObj, issuerFieldName);
            return recordObj;
        }
        catch(IOException e) {
            throw new InvalidFormatException("Invalid record JSON: " + e.getMessage(), e);
        }
    }


    protected void readAndVerifyIssuerSignature(JsonNode actualObj, String issuerFieldName) throws ElasticsearchException, JsonProcessingException {
        // Remove hash and signature
        String recordJson = objectMapper.writeValueAsString(actualObj);
        readAndVerifyIssuerSignature(recordJson, actualObj, issuerFieldName);
    }

    protected String getIssuer(JsonNode actualObj) {
        return  getMandatoryField(actualObj, Records.PROPERTY_ISSUER).asText();
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

        // Remove hash and signature
        recordJson = JacksonUtils.removeAttribute(recordJson, Records.PROPERTY_SIGNATURE);
        recordJson = JacksonUtils.removeAttribute(recordJson, Records.PROPERTY_HASH);

        // Remove 'read_signature' attribute if exists (added AFTER signature)
        if (fieldNames.contains(Records.PROPERTY_READ_SIGNATURE)) {
            recordJson = JacksonUtils.removeAttribute(recordJson, Records.PROPERTY_READ_SIGNATURE);
        }

        if (!cryptoService.verify(recordJson, signature, issuer)) {
            throw new InvalidSignatureException("Invalid signature of JSON string");
        }

        // TODO: check issuer is in the WOT ?
    }
}
