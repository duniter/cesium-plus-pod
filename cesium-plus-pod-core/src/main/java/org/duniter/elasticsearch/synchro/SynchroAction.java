package org.duniter.elasticsearch.synchro;

/*-
 * #%L
 * Duniter4j :: ElasticSearch Core plugin
 * %%
 * Copyright (C) 2014 - 2017 EIS
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

import com.fasterxml.jackson.databind.JsonNode;
import com.google.common.collect.Lists;
import org.duniter.core.client.model.bma.EndpointApi;
import org.duniter.core.client.model.local.Peer;
import org.duniter.elasticsearch.model.SynchroResult;
import org.duniter.elasticsearch.service.changes.ChangeEvent;
import org.duniter.elasticsearch.service.changes.ChangeSource;

public interface SynchroAction {

    interface SourceConsumer {
        void accept(String id, JsonNode source, SynchroActionResult result) throws Exception;
    }

    EndpointApi getEndPointApi();

    ChangeSource getChangeSource();

    void handleSynchronize(Peer peer,
                      long fromTime,
                      SynchroResult result);

    void handleChange(Peer peer, ChangeEvent changeEvent);

    void addInsertionListener(SourceConsumer listener);

    void addUpdateListener(SourceConsumer listener);

    void addValidationListener(SourceConsumer listener);
}
