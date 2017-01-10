package org.duniter.core.client.service.bma;

/*
 * #%L
 * UCoin Java :: Core Client API
 * %%
 * Copyright (C) 2014 - 2016 EIS
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

import org.duniter.core.client.model.ModelUtils;
import org.duniter.core.client.model.bma.*;
import org.duniter.core.client.model.local.Certification;
import org.duniter.core.client.model.local.Identity;
import org.duniter.core.client.model.local.Peer;
import org.duniter.core.client.model.local.Wallet;
import org.duniter.core.client.service.ServiceLocator;
import org.duniter.core.client.service.local.CurrencyService;
import org.duniter.core.exception.TechnicalException;
import org.duniter.core.service.CryptoService;
import org.duniter.core.util.CollectionUtils;
import org.duniter.core.util.ObjectUtils;
import org.duniter.core.util.Preconditions;
import org.duniter.core.util.crypto.CryptoUtils;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.message.BasicNameValuePair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.UnsupportedEncodingException;
import java.util.*;

public class WotRemoteServiceImpl extends BaseRemoteServiceImpl implements WotRemoteService {

    private static final Logger log = LoggerFactory.getLogger(BlockchainRemoteServiceImpl.class);

    public static final String URL_BASE = "/wot";

    public static final String URL_ADD = URL_BASE + "/add";

    public static final String URL_LOOKUP = URL_BASE + "/lookup/%s";

    public static final String URL_REQUIREMENT = URL_BASE+"/requirements/%s";

    public static final String URL_CERTIFIED_BY = URL_BASE + "/certified-by/%s";

    public static final String URL_CERTIFIERS_OF = URL_BASE + "/certifiers-of/%s";

    /**
     * See https://github.com/ucoin-io/ucoin-cli/blob/master/bin/ucoin
     * > var hash = res.current ? res.current.hash : 'DA39A3EE5E6B4B0D3255BFEF95601890AFD80709';
     */
    public static final String BLOCK_ZERO_HASH = "DA39A3EE5E6B4B0D3255BFEF95601890AFD80709";

    private CryptoService cryptoService;
    private BlockchainRemoteService bcService;
    private CurrencyService currencyService;

    public WotRemoteServiceImpl() {
        super();
    }

    @Override
    public void afterPropertiesSet() {
        super.afterPropertiesSet();
        cryptoService = ServiceLocator.instance().getCryptoService();
        bcService = ServiceLocator.instance().getBlockchainRemoteService();
        currencyService = ServiceLocator.instance().getCurrencyService();
    }

    public List<Identity> findIdentities(Set<Long> currenciesIds, String uidOrPubKey) {
        List<Identity> result = new ArrayList<Identity>();

        String path = String.format(URL_LOOKUP, uidOrPubKey);

        for (Long currencyId: currenciesIds) {

            WotLookup lookupResult = executeRequest(currencyId, path, WotLookup.class);

            addAllIdentities(result, lookupResult, currencyId);
        }

        return result;
    }

    public WotLookup.Uid find(long currencyId, String uidOrPubKey) {
        if (log.isDebugEnabled()) {
            log.debug(String.format("Try to find user by looking up on [%s]", uidOrPubKey));
        }

        // get parameter
        String path = String.format(URL_LOOKUP, uidOrPubKey);
        WotLookup lookupResults = executeRequest(currencyId, path, WotLookup.class);

        for (WotLookup.Result result : lookupResults.getResults()) {
            if (CollectionUtils.isNotEmpty(result.getUids())) {
                for (WotLookup.Uid uid : result.getUids()) {
                    return uid;
                }
            }
        }
        return null;

    }

    public void getRequirments(long currencyId, String pubKey) {
        if (log.isDebugEnabled()) {
            log.debug(String.format("Try to find user requirements on [%s]", pubKey));
        }
        // get parameter
        String path = String.format(URL_REQUIREMENT, pubKey);
        // TODO : managed requirements
        //WotLookup lookupResults = executeRequest(currencyId, path, WotLookup.class);

    }

    public WotLookup.Uid findByUid(long currencyId, String uid) {
        if (log.isDebugEnabled()) {
            log.debug(String.format("Try to find user info by uid: %s", uid));
        }

        // call lookup
        String path = String.format(URL_LOOKUP, uid);
        WotLookup lookupResults = executeRequest(currencyId, path, WotLookup.class);

        // Retrieve the exact uid
        WotLookup.Uid uniqueResult = getUid(lookupResults, uid);
        if (uniqueResult == null) {
            return null;
        }
        
        return uniqueResult;
    }

    public WotLookup.Uid findByUidAndPublicKey(long currencyId, String uid, String pubKey) {
        if (log.isDebugEnabled()) {
            log.debug(String.format("Try to find user info by uid [%s] and pubKey [%s]", uid, pubKey));
        }

        // call lookup
        String path = String.format(URL_LOOKUP, uid);
        WotLookup lookupResults = executeRequest(currencyId, path, WotLookup.class);

        // Retrieve the exact uid
        WotLookup.Uid uniqueResult = getUidByUidAndPublicKey(lookupResults, uid, pubKey);
        if (uniqueResult == null) {
            return null;
        }

        return uniqueResult;
    }

    public WotLookup.Uid findByUidAndPublicKey(Peer peer, String uid, String pubKey) {
        if (log.isDebugEnabled()) {
            log.debug(String.format("Try to find user info by uid [%s] and pubKey [%s]", uid, pubKey));
        }

        // call lookup
        String path = String.format(URL_LOOKUP, uid);
        WotLookup lookupResults = executeRequest(peer, path, WotLookup.class);

        // Retrieve the exact uid
        WotLookup.Uid uniqueResult = getUidByUidAndPublicKey(lookupResults, uid, pubKey);
        if (uniqueResult == null) {
            return null;
        }

        return uniqueResult;
    }

    public Identity getIdentity(long currencyId, String uid, String pubKey) {
        if (log.isDebugEnabled()) {
            log.debug(String.format("Get identity by uid [%s] and pubKey [%s]", uid, pubKey));
        }

        WotLookup.Uid lookupUid = findByUidAndPublicKey(currencyId, uid, pubKey);
        if (lookupUid == null) {
            return null;
        }
        return toIdentity(lookupUid);
    }

    public Identity getIdentity(long currencyId, String pubKey) {
//        Log.d(TAG, String.format("Get identity by uid [%s] and pubKey [%s]", uid, pubKey));

        WotLookup.Uid lookupUid = find(currencyId, pubKey);
        if (lookupUid == null) {
            return null;
        }
        Identity result = toIdentity(lookupUid);
        result.setPubkey(pubKey);
        result.setCurrencyId(currencyId);
        return result;
    }

    public Identity getIdentity(Peer peer, String uid, String pubKey) {
        if (log.isDebugEnabled()) {
            log.debug(String.format("Get identity by uid [%s] and pubKey [%s]", uid, pubKey));
        }

        WotLookup.Uid lookupUid = findByUidAndPublicKey(peer, uid, pubKey);
        if (lookupUid == null) {
            return null;
        }
        return toIdentity(lookupUid);
    }

    public Collection<Certification> getCertifications(long currencyId, String uid, String pubkey, boolean isMember) {
        Preconditions.checkNotNull(uid);
        Preconditions.checkNotNull(pubkey);

        if (isMember) {
            return getCertificationsByPubkeyForMember(currencyId, pubkey, true);
        }
        else {
            return getCertificationsByPubkeyForNonMember(currencyId, uid, pubkey);
        }
    }


    public WotCertification getCertifiedBy(long currencyId, String uid) {
        if (log.isDebugEnabled()) {
            log.debug(String.format("Try to get certifications done by uid: %s", uid));
        }

        // call certified-by
        String path = String.format(URL_CERTIFIED_BY, uid);
        WotCertification result = executeRequest(currencyId, path, WotCertification.class);
        
        return result;

    }

    public int countValidCertifiers(long currencyId, String pubkey) {
        if (log.isDebugEnabled()) {
            log.debug(String.format("Try to count valid certifications done by pubkey: %s", pubkey));
        }

        int count =0;

        // call certified-by
        Collection<Certification> certifiersOf = getCertificationsByPubkeyForMember(currencyId, pubkey, false/*only certifiers of*/);
        if (CollectionUtils.isEmpty(certifiersOf)) {
            return 0;
        }

        for(Certification certifier : certifiersOf){
            if(certifier.isValid()){
                count++;
            }
        }

        return count;

    }
    
    public WotCertification getCertifiersOf(long currencyId, String uid) {
        if (log.isDebugEnabled()) {
            log.debug(String.format("Try to get certifications done to uid: %s", uid));
        }

        // call certifiers-of
        String path = String.format(URL_CERTIFIERS_OF, uid);
        WotCertification result = executeRequest(currencyId, path, WotCertification.class);
        
        return result;
    }


    public void sendIdentity(long currencyId, byte[] pubKey, byte[] secKey, String userId, String blockUid) {
        // http post /wot/add
        HttpPost httpPost = new HttpPost(getPath(currencyId, URL_ADD));

        String currency = currencyService.getCurrencyNameById(currencyId);

        // compute the self-certification
        String identity = getSignedIdentity(currency, pubKey, secKey, userId, blockUid);

        List<NameValuePair> urlParameters = new ArrayList<NameValuePair>();
        urlParameters.add(new BasicNameValuePair("identity", identity));

        try {
            httpPost.setEntity(new UrlEncodedFormEntity(urlParameters));
        }
        catch(UnsupportedEncodingException e) {
            throw new TechnicalException(e);
        }

        // Execute the request
        executeRequest(httpPost, String.class);
    }

    public void sendIdentity(Peer peer, String currency, byte[] pubKey, byte[] secKey, String uid, String blockUid) {
        // http post /wot/add
        HttpPost httpPost = new HttpPost(getPath(peer, URL_ADD));

        // compute the self-certification
        String identity = getSignedIdentity(currency, pubKey, secKey, uid, blockUid);

        List<NameValuePair> urlParameters = new ArrayList<NameValuePair>();
        urlParameters.add(new BasicNameValuePair("identity", identity));

        try {
            httpPost.setEntity(new UrlEncodedFormEntity(urlParameters));
        }
        catch(UnsupportedEncodingException e) {
            throw new TechnicalException(e);
        }

        // Execute the request
        executeRequest(httpPost, String.class);
    }

    public String sendCertification(Wallet wallet,
                                    Identity identity) {
        return sendCertification(
                    wallet.getCurrencyId(),
                    wallet.getPubKey(),
                    wallet.getSecKey(),
                    wallet.getIdentity().getUid(),
                    wallet.getIdentity().getTimestamp(),
                    identity.getUid(),
                    identity.getPubkey(),
                    identity.getTimestamp(),
                    identity.getSignature());
    }

    public String sendCertification(long currencyId,
                                    byte[] pubKey, byte[] secKey,
                                  String uid, String timestamp,
                                  String userUid, String userPubKeyHash,
                                  String userBlockUid, String userSignature) {
        // http post /wot/add
        HttpPost httpPost = new HttpPost(getPath(currencyId, URL_ADD));

        // Read the current block (number and hash)
        BlockchainRemoteService blockchainService = ServiceLocator.instance().getBlockchainRemoteService();
        BlockchainBlock currentBlock = blockchainService.getCurrentBlock(currencyId);
        int blockNumber = currentBlock.getNumber();
        String blockHash = (blockNumber != 0)
                ? currentBlock.getHash()
                : BLOCK_ZERO_HASH;

        // Compute the pub key hash
        String pubKeyHash = CryptoUtils.encodeBase58(pubKey);

        // compute signed identity
        String identity = getIdentity(currentBlock.getCurrency(),
                pubKeyHash, userUid, userBlockUid, userSignature);

        // Compute the certification
        String certification = null; /* FIXME getCertification(pubKey, secKey,
                userUid, userBlockUid, userSignature,
                blockNumber, blockHash);*/
        String inlineCertification = toInlineCertification(pubKeyHash, userPubKeyHash, certification);

        List<NameValuePair> urlParameters = new ArrayList<NameValuePair>();
        urlParameters.add(new BasicNameValuePair("identity", identity));
        urlParameters.add(new BasicNameValuePair("self", identity));
        urlParameters.add(new BasicNameValuePair("other", inlineCertification));

        try {
            httpPost.setEntity(new UrlEncodedFormEntity(urlParameters));
        }
        catch(UnsupportedEncodingException e) {
            throw new TechnicalException(e);
        }
        String selfResult = executeRequest(httpPost, String.class);
        log.debug("received from /add: " + selfResult);

        return selfResult;
    }

    public void addAllIdentities(List<Identity> result, WotLookup lookupResults, Long currencyId) {
        String currencyName = null;
        if (currencyId != null) {
            currencyName = ServiceLocator.instance().getCurrencyService().getCurrencyNameById(currencyId);
        }

        for (WotLookup.Result lookupResult: lookupResults.getResults()) {
            String pubKey = lookupResult.getPubkey();
            for (WotLookup.Uid source: lookupResult.getUids()) {
                // Create and fill an identity, from a result row
                Identity target = new Identity();
                toIdentity(source, target);

                // fill the pub key
                target.setPubkey(pubKey);

                // Fill currency id and name
                // TODO
                //target.setCurrencyId(currencyId);
                //target.setCurrency(currencyName);

                result.add(target);
            }
        }
    }

    public Identity toIdentity(WotLookup.Uid source) {
        Identity target = new Identity();
        toIdentity(source, target);
        return target;
    }

    public void toIdentity(WotLookup.Uid source, Identity target) {

        target.setUid(source.getUid());
        target.setSelf(source.getSelf());
        String timestamp = source.getMeta() != null ? source.getMeta().getTimestamp() : null;
        target.setTimestamp(timestamp);
    }

    public String getSignedIdentity(String currency, byte[] pubKey, byte[] secKey, String uid, String blockUid) {

        // Compute the pub key hash
        String pubKeyHash = CryptoUtils.encodeBase58(pubKey);

        // Create identity to sign
        String unsignedIdentity = getUnsignedIdentity(currency, pubKeyHash, uid, blockUid);

        // Compute the signature
        String signature = cryptoService.sign(unsignedIdentity, secKey);

        // Append the signature
        return unsignedIdentity + signature + '\n';
    }

    /* -- Internal methods -- */

    protected Collection<Certification> getCertificationsByPubkeyForMember(long currencyId, String pubkey, boolean onlyCertifiersOf) {

        BlockchainParameters bcParameter = bcService.getParameters(currencyId, true);
        BlockchainBlock currentBlock = bcService.getCurrentBlock(currencyId, true);
        long medianTime = currentBlock.getMedianTime();
        int sigValidity = bcParameter.getSigValidity();
        int sigQty = bcParameter.getSigQty();

        Collection<Certification> result = new TreeSet<Certification>(ModelUtils.newWotCertificationComparatorByUid());

        // Certifiers of
        WotCertification certifiersOfList = getCertifiersOf(currencyId, pubkey);
        boolean certifiersOfIsEmpty = (certifiersOfList == null
                || certifiersOfList.getCertifications() == null);
        int validWrittenCertifiersCount = 0;
        if (!certifiersOfIsEmpty) {
            for (WotCertification.Certification certifier : certifiersOfList.getCertifications()) {

                Certification cert = toCertification(certifier, currencyId);
                cert.setCertifiedBy(false);
                result.add(cert);

                /*FIXME long certificationAge = medianTime - certifier.getTimestamp();
                if(certificationAge <= sigValidity) {
                    if (certifier.getIsMember() != null && certifier.getIsMember().booleanValue()
                            && certifier.getWritten()!=null && certifier.getWritten().getNumber()>=0) {
                        validWrittenCertifiersCount++;
                    }
                    cert.setValid(true);
                }
                else {
                    cert.setValid(false);
                }*/
            }
        }

        if (validWrittenCertifiersCount >= sigQty) {
            if (log.isDebugEnabled()) {
                log.debug(String.format("pubkey [%s] has %s valid signatures: should be a member", pubkey, validWrittenCertifiersCount));
            }
        }
        else {
            if (log.isDebugEnabled()) {
                log.debug(String.format("pubkey [%s] has %s valid signatures: not a member", pubkey, validWrittenCertifiersCount));
            }
        }

        if (!onlyCertifiersOf) {

            // Certified by
            WotCertification certifiedByList = getCertifiedBy(currencyId, pubkey);
            boolean certifiedByIsEmpty = (certifiedByList == null
                    || certifiedByList.getCertifications() == null);

            if (!certifiedByIsEmpty) {
                for (WotCertification.Certification certifiedBy : certifiedByList.getCertifications()) {

                    Certification cert = toCertification(certifiedBy, currencyId);
                    cert.setCertifiedBy(true);
                    result.add(cert);

                    // FIXME
                    /*
                    long certificationAge = medianTime - certifiedBy.getTimestamp();
                    if (certificationAge <= sigValidity) {
                        cert.setValid(true);
                    } else {
                        cert.setValid(false);
                    }
                    */
                }
            }

            // group certifications  by [uid, pubKey] and keep last timestamp
            result = groupByUidAndPubKey(result, true);

        }

        return result;
    }

    protected Collection<Certification> getCertificationsByPubkeyForNonMember(long currencyId, final String uid, final String pubkey) {
        // Ordered list, by uid/pubkey/cert time

        Collection<Certification> result = new TreeSet<Certification>(ModelUtils.newWotCertificationComparatorByUid());

        if (log.isDebugEnabled()) {
            log.debug(String.format("Get non member WOT, by uid [%s] and pubKey [%s]", uid, pubkey));
        }

        // call lookup
        String path = String.format(URL_LOOKUP, pubkey);
        WotLookup lookupResults = executeRequest(currencyId, path, WotLookup.class);

        // Retrieve the exact uid
        WotLookup.Uid lookupUId = getUidByUidAndPublicKey(lookupResults, uid, pubkey);

        // Read certifiers, if any
        Map<String, Certification> certifierByPubkeys = new HashMap<String, Certification>();
        if (lookupUId != null && lookupUId.getOthers() != null) {
            for(WotLookup.OtherSignature lookupSignature: lookupUId.getOthers()) {
                Collection<Certification> certifiers = toCertifierCertifications(lookupSignature, currencyId);
                result.addAll(certifiers);
            }
        }

        // Read certified-by
        if (CollectionUtils.isNotEmpty(lookupResults.getResults())) {
            for (WotLookup.Result lookupResult: lookupResults.getResults()) {
                if (lookupResult.getSigned() != null) {
                    for(WotLookup.SignedSignature lookupSignature : lookupResult.getSigned()) {
                        Certification certifiedBy = toCertifiedByCerticication(lookupSignature);

                        // Set the currency Id
                        certifiedBy.setCurrencyId(currencyId);

                        // If exists, link to other side certification
                        String certifiedByPubkey = certifiedBy.getPubkey();
                        if (certifierByPubkeys.containsKey(certifiedByPubkey)) {
                            Certification certified = certifierByPubkeys.get(certifiedByPubkey);
                            certified.setOtherEnd(certifiedBy);
                        }

                        // If only a certifier, just add to the list
                        else {
                            result.add(certifiedBy);
                        }
                    }
                }
            }
        }

        // group certifications  by [uid, pubKey] and keep last timestamp
        result = groupByUidAndPubKey(result, true);

        return result;
    }

    protected String toInlineCertification(String pubKeyHash,
                                           String userPubKeyHash,
                                           String certification) {
        // Read the signature
        String[] parts = certification.split("\n");
        if (parts.length != 5) {
            throw new TechnicalException("Bad certification document: " + certification);
        }
        String signature = parts[parts.length-1];

        // Read the block number
        parts = parts[parts.length-2].split(":");
        if (parts.length != 3) {
            throw new TechnicalException("Bad certification document: " + certification);
        }
        parts = parts[2].split("-");
        if (parts.length != 2) {
            throw new TechnicalException("Bad certification document: " + certification);
        }
        String blockNumber = parts[0];

        return new StringBuilder()
                .append(pubKeyHash)
                .append(':')
                .append(userPubKeyHash)
                .append(':')
                .append(blockNumber)
                .append(':')
                .append(signature)
                .append('\n')
                .toString();
    }

    public String getCertification(byte[] pubKey, byte[] secKey, String userUid,
                                   String userTimestamp,
                                   String userSignature) {
        // Create the self part to sign
        String unsignedCertification = getCertificationUnsigned(
                userUid, userTimestamp, userSignature);

        // Compute the signature
        String signature = cryptoService.sign(unsignedCertification, secKey);

        // Append the signature
        return new StringBuilder()
                .append(unsignedCertification)
                .append(signature)
                .append('\n')
                .toString();
    }

    protected String getCertificationUnsigned(String userUid,
                                      String userTimestamp,
                                      String userSignature) {
        // Create the self part to sign
        return new StringBuilder()
                .append("UID:")
                .append(userUid)
                .append("\nMETA:TS:")
                .append(userTimestamp)
                .append('\n')
                .append(userSignature)
                /*.append("\nMETA:TS:")
                .append(blockNumber)
                .append('-')
                .append(blockHash)*/
                .append('\n').toString();
    }

    protected String getUnsignedIdentity(String currency,
                                         String pubkey,
                                         String userId,
                                         String blockUid) {
        // see https://github.com/ucoin-io/ucoin/blob/master/doc/Protocol.md#identity
        return new StringBuilder()
                .append("Version: ").append(Protocol.VERSION)
                .append("\nType: ").append(Protocol.TYPE_IDENTITY)
                .append("\nCurrency: ").append(currency)
                .append("\nIssuer: ").append(pubkey)
                .append("\nUniqueID: ").append(userId)
                .append("\nTimestamp: ").append(blockUid)
                .append("\n")
                .toString();
    }

    protected String getIdentity(String currency,
                                         String pubkey,
                                         String userId,
                                         String blockUid,
                                         String signature) {
        return getUnsignedIdentity(currency, pubkey, userId, blockUid)
                + "\n" + signature;
    }

    protected WotLookup.Uid getUid(WotLookup lookupResults, String filterUid) {
        if (lookupResults.getResults() == null || lookupResults.getResults().length == 0) {
            return null;
        }

        for (WotLookup.Result result : lookupResults.getResults()) {
            if (result.getUids() != null && result.getUids().length > 0) {
                for (WotLookup.Uid uid : result.getUids()) {
                    if (filterUid.equals(uid.getUid())) {
                        return uid;
                    }
                }
            }
        }
        
        return null;
    }

    protected WotLookup.Uid getUidByUidAndPublicKey(WotLookup lookupResults,
                                                   String filterUid,
                                                   String filterPublicKey) {
        if (lookupResults.getResults() == null || lookupResults.getResults().length == 0) {
            return null;
        }

        for (WotLookup.Result result : lookupResults.getResults()) {
            if (filterPublicKey.equals(result.getPubkey())) {
                if (result.getUids() != null && result.getUids().length > 0) {
                    for (WotLookup.Uid uid : result.getUids()) {
                        if (filterUid.equals(uid.getUid())) {
                            return uid;
                        }
                    }
                }
                break;
            }
        }

        return null;
    }

    private Certification toCertification(final WotCertification.Certification source, final long currencyId) {
        Certification target = new Certification();
        target.setPubkey(source.getPubkey());
        target.setUid(source.getUid());
        target.setMember(source.getIsMember());
        target.setCurrencyId(currencyId);

        return target;
    }

    private Collection<Certification> toCertifierCertifications(final WotLookup.OtherSignature source, final long currencyId) {
        List<Certification> result = new ArrayList<Certification>();
        // If only one uid
        if (source.getUids().length == 1) {
            Certification target = new Certification();

            // uid
            target.setUid(source.getUids()[0]);

            // certifier
            target.setCertifiedBy(false);

            // Pubkey
            target.setPubkey(source.getPubkey());

            // Is member
            target.setMember(source.isMember());

            // Set currency Id
            target.setCurrencyId(currencyId);

            result.add(target);
        }
        else {
            for(String uid: source.getUids()) {
                Certification target = new Certification();

                // uid
                target.setUid(uid);

                // certified by
                target.setCertifiedBy(false);

                // Pubkey
                target.setPubkey(source.getPubkey());

                // Is member
                target.setMember(source.isMember());

                // Set currency Id
                target.setCurrencyId(currencyId);

                result.add(target);
            }
        }
        return result;
    }

    private Certification toCertifiedByCerticication(final WotLookup.SignedSignature source) {

        Certification target = new Certification();
        // uid
        target.setUid(source.getUid());

        // certifieb by
        target.setCertifiedBy(true);

        if (source.getMeta() != null) {

            // timestamp
            String timestamp = source.getMeta() != null ? source.getMeta().getTimestamp() : null;
            if (timestamp != null) {
                //FIXME target.setTimestamp(timestamp.longValue());
            }
        }

        // Pubkey
        target.setPubkey(source.getPubkey());

        // Is member
        target.setMember(source.isMember());

        // add to result list
        return target;
    }

    /**
     *
     * @param orderedCertifications a list, ordered by uid, pubkey, timestamp (DESC)
     * @return
     */
    private Collection<Certification> groupByUidAndPubKey(Collection<Certification> orderedCertifications, boolean orderResultByDate) {
        if (CollectionUtils.isEmpty(orderedCertifications)) {
            return orderedCertifications;
        }

        List<Certification> result = new ArrayList<>();

        StringBuilder keyBuilder = new StringBuilder();
        String previousIdentityKey = null;
        Certification previousCert = null;
        for (Certification cert : orderedCertifications) {
            String identityKey = keyBuilder.append(cert.getUid())
                    .append("~~")
                    .append(cert.getPubkey())
                    .toString();
            boolean certifiedBy = cert.isCertifiedBy();

            // Seems to be the same identity as previous entry
            if (identityKey.equals(previousIdentityKey)) {

                if (certifiedBy != previousCert.isCertifiedBy()) {
                    // merge with existing other End (if exists)
                    merge(cert, previousCert.getOtherEnd());

                    // previousCert = certifier, so keep it and link the current cert
                    if (!certifiedBy) {
                        previousCert.setOtherEnd(cert);
                    }

                    // previousCert = certified-by, so prefer the current cert
                    else {
                        cert.setOtherEnd(previousCert);
                        previousCert = cert;
                    }
                }

                // Merge
                else {
                    merge(previousCert, cert);
                }
            }

            // if identity changed
            else {
                // So add the previous cert to result
                if (previousCert != null) {
                    result.add(previousCert);
                }

                // And prepare next iteration
                previousIdentityKey = identityKey;
                previousCert = cert;
            }

            // prepare the next loop
            keyBuilder.setLength(0);

        }

        if (previousCert != null) {
            result.add(previousCert);
        }

        if (orderResultByDate) {
            Collections.sort(result, ModelUtils.newWotCertificationComparatorByDate());
        }

        return result;
    }

    private void merge(Certification previousCert, Certification cert) {
        if (cert != null && cert.getTimestamp() >  previousCert.getTimestamp()) {
            previousCert.setTimestamp(cert.getTimestamp());
        }
    }

}
