package org.duniter.elasticsearch.rest;

/*
 * #%L
 * duniter4j-elasticsearch-plugin
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

import org.duniter.elasticsearch.rest.attachment.RestImageAttachmentAction;
import org.duniter.elasticsearch.rest.blockchain.*;
import org.duniter.elasticsearch.rest.docstat.RestDocStatSearchAction;
import org.duniter.elasticsearch.rest.network.*;
import org.duniter.elasticsearch.rest.node.RestNodeModeratorsGetAction;
import org.duniter.elasticsearch.rest.node.RestNodeSummaryGetAction;
import org.duniter.elasticsearch.rest.node.RestNodeStatsGetAction;
import org.duniter.elasticsearch.rest.security.*;
import org.duniter.elasticsearch.rest.wot.RestWotLookupGetAction;
import org.duniter.elasticsearch.rest.wot.RestWotMembersGetAction;
import org.duniter.elasticsearch.rest.wot.RestWotPendingGetAction;
import org.duniter.elasticsearch.rest.wot.RestWotRequirementsGetAction;
import org.elasticsearch.common.inject.AbstractModule;
import org.elasticsearch.common.inject.Module;

public class RestModule extends AbstractModule implements Module {

    @Override protected void configure() {

        // Common
        bind(RestNodeSummaryGetAction.class).asEagerSingleton();
        bind(RestNodeStatsGetAction.class).asEagerSingleton();
        bind(RestNodeModeratorsGetAction.class).asEagerSingleton();

        // Authentication & Security
        bind(RestSecurityGetChallengeAction.class).asEagerSingleton();
        bind(RestSecurityAuthAction.class).asEagerSingleton();
        bind(RestSecurityController.class).asEagerSingleton();
        bind(RestQuotaController.class).asEagerSingleton();
        bind(RestSecurityFilter.class).asEagerSingleton();

        // Attachment as image
        bind(RestImageAttachmentAction.class).asEagerSingleton();

        // Currency
        //bind(RestCurrencyPostAction.class).asEagerSingleton();

        // Network
        bind(RestNetworkPeeringGetAction.class).asEagerSingleton();
        bind(RestNetworkPeeringPeersPostAction.class).asEagerSingleton();
        bind(RestNetworkPeersGetAction.class).asEagerSingleton();
        bind(RestNetworkWs2pHeadsGetAction.class).asEagerSingleton();
        bind(RestNetworkPeeringPeersGetAction.class).asEagerSingleton();

        // Blockchain
        bind(RestBlockchainParametersGetAction.class).asEagerSingleton();
        bind(RestBlockchainBlockGetAction.class).asEagerSingleton();
        bind(RestBlockchainWithUdAction.class).asEagerSingleton();
        bind(RestBlockchainWithNewcomersAction.class).asEagerSingleton();
        bind(RestBlockchainBlocksGetAction.class).asEagerSingleton();
        bind(RestBlockchainDifficultiesGetAction.class).asEagerSingleton();

        // Wot
        bind(RestWotLookupGetAction.class).asEagerSingleton();
        bind(RestWotMembersGetAction.class).asEagerSingleton();
        bind(RestWotPendingGetAction.class).asEagerSingleton();
        bind(RestWotRequirementsGetAction.class).asEagerSingleton();

        // Doc stats backward compatibility
        bind(RestDocStatSearchAction.class).asEagerSingleton();


    }
}