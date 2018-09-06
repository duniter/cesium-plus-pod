package org.duniter.elasticsearch.model;

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


import com.fasterxml.jackson.databind.JsonNode;
import org.apache.lucene.util.BytesRef;
import org.elasticsearch.common.bytes.BytesArray;
import org.elasticsearch.common.bytes.BytesReference;
import org.elasticsearch.common.io.stream.StreamInput;
import org.jboss.netty.buffer.ChannelBuffer;

import java.io.IOException;
import java.io.OutputStream;
import java.io.Serializable;
import java.nio.channels.GatheringByteChannel;
import java.util.Iterator;

/**
 * Created by eis on 05/02/15.
 */
public class SearchResponse implements Serializable {

    protected JsonNode node;

    public SearchResponse(JsonNode response) {
        this.node = response;
    }

    public SearchHits getHits() {
        return new SearchHits(node.get("hits"));
    }

    public class SearchHits implements Iterator<SearchHit>{

        protected JsonNode node;
        private Iterator<JsonNode> hits;
        SearchHits(JsonNode node) {
            this.node = node;
            this.hits = node == null ? null : node.get("hits").iterator();
        }

        public int getTotalHits() {
            return node == null ? 0 : node.get("total").asInt(0);
        }

        public boolean hasNext() {
            return hits != null && hits.hasNext();
        }
        public SearchHit next() {
            return hits == null ? null : new SearchHit(hits.next());
        }
    }

    public class SearchHit {

        private JsonNode node;
        SearchHit(JsonNode node) {
            this.node = node;
        }

        public String getId() {
            return node.get("_id").asText();
        }

        public JsonNode getSource() {
            return node.get("_source");
        }

    }
}