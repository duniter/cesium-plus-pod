package org.duniter.elasticsearch.util.bytes;

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

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.lucene.util.BytesRef;
import org.elasticsearch.ElasticsearchException;
import org.elasticsearch.common.bytes.BytesArray;
import org.elasticsearch.common.bytes.BytesReference;
import org.elasticsearch.common.io.stream.StreamInput;
import org.jboss.netty.buffer.ChannelBuffer;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.channels.GatheringByteChannel;
import java.util.Objects;

public class BytesJsonNode implements BytesReference {

    private JsonNode node;
    private BytesArray delegate;
    private ObjectMapper objectMapper;

    public BytesJsonNode(JsonNode node) {
        this(node, new ObjectMapper());
    }

    public BytesJsonNode(JsonNode node, ObjectMapper objectMapper) {
        this.node = node;
        this.objectMapper = objectMapper;
    }

    public byte get(int index) {
        return getOrInitDelegate().get(index);
    }

    public int length() {
        return getOrInitDelegate().length();
    }

    public BytesReference slice(int from, int length) {
        return getOrInitDelegate().slice(from, length);
    }

    public StreamInput streamInput() {
        return getOrInitDelegate().streamInput();
    }

    public void writeTo(OutputStream os) throws IOException {
        objectMapper.writeValue(os, node);
    }

    public void writeTo(GatheringByteChannel channel) throws IOException {
        getOrInitDelegate().writeTo(channel);
    }

    public byte[] toBytes() {
        try {
            return objectMapper.writeValueAsBytes(node);
        }
        catch(JsonProcessingException e) {
            throw new ElasticsearchException(e);
        }
    }

    public BytesArray toBytesArray() {
        return getOrInitDelegate();
    }

    public BytesArray copyBytesArray() {
        return getOrInitDelegate().copyBytesArray();
    }

    public ChannelBuffer toChannelBuffer() {
        return getOrInitDelegate().toChannelBuffer();
    }

    public boolean hasArray() {
        return true;
    }

    public byte[] array() {
        return toBytes();
    }

    public int arrayOffset() {
        return getOrInitDelegate().arrayOffset();
    }

    public String toUtf8() {
        return getOrInitDelegate().toUtf8();
    }

    public BytesRef toBytesRef() {
        return getOrInitDelegate().toBytesRef();
    }

    public BytesRef copyBytesRef() {
        return getOrInitDelegate().copyBytesRef();
    }

    public int hashCode() {
        return getOrInitDelegate().hashCode();
    }

    public JsonNode toJsonNode() {
        return node;
    }

    public JsonNode copyJsonNode() {
        return node.deepCopy();
    }

    public boolean equals(Object obj) {
        return Objects.equals(this, obj);
    }


    protected BytesArray getOrInitDelegate() {
        if (delegate == null) {
            try {
                this.delegate = new BytesArray(objectMapper.writeValueAsBytes(node));
            }
            catch(JsonProcessingException e) {
                throw new ElasticsearchException(e);
            }
        }
        return delegate;
    }
}
