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
import com.fasterxml.jackson.databind.ObjectWriter;
import org.apache.lucene.util.BytesRef;
import org.duniter.core.client.model.bma.jackson.JacksonUtils;
import org.elasticsearch.ElasticsearchException;
import org.elasticsearch.common.bytes.BytesArray;
import org.elasticsearch.common.bytes.BytesReference;
import org.elasticsearch.common.io.stream.StreamInput;
import org.jboss.netty.buffer.ChannelBuffer;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.channels.GatheringByteChannel;
import java.util.Objects;

public class JsonNodeBytesReference implements BytesReference {

    /* -- Helper functions  --*/

    public static JsonNode readTree(BytesReference source) throws IOException {
        if (source  == null) return null;

        if (source instanceof JsonNodeBytesReference) {
            // Avoid new deserialization
            return ((JsonNodeBytesReference) source).toJsonNode();
        }

        return JacksonUtils.getThreadObjectMapper().readTree(source.streamInput());
    }

    public static JsonNode readTree(BytesReference source, ObjectMapper objectMapper) throws IOException {
        if (source  == null) return null;

        if (source instanceof JsonNodeBytesReference) {
            // Avoid new deserialization
            return ((JsonNodeBytesReference) source).toJsonNode();
        }

        return objectMapper.readTree(source.streamInput());
    }

    public static <T> T readValue(BytesReference source, Class<T> clazz) throws IOException {
        if (source  == null) return null;

        return new ObjectMapper().readValue(source.streamInput(), clazz);
    }

    private JsonNode node;
    private BytesArray delegate;
    private ObjectMapper objectMapper;

    public JsonNodeBytesReference(JsonNode node) {
        this(node, new ObjectMapper());
    }

    public JsonNodeBytesReference(JsonNode node, ObjectMapper objectMapper) {
        this.node = node;
        this.objectMapper = objectMapper;
    }

    public JsonNodeBytesReference(BytesArray delegate) {
        this(delegate, new ObjectMapper());
    }

    public JsonNodeBytesReference(BytesArray delegate, ObjectMapper objectMapper) {
        this.delegate = delegate;
        this.objectMapper = objectMapper;
    }

    public <T> JsonNodeBytesReference(T object) throws IOException{
        this(object, new ObjectMapper());
    }

    public <T> JsonNodeBytesReference(T object, ObjectMapper objectMapper) throws IOException{
        this.objectMapper = objectMapper;
        this.delegate = new BytesArray(this.objectMapper.writer().writeValueAsBytes(object));
    }
    public <T> JsonNodeBytesReference(T object, ObjectWriter writer) throws IOException{
        this.objectMapper = new ObjectMapper();
        this.delegate = new BytesArray(writer.writeValueAsBytes(object));
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
        if (delegate != null) {
            delegate.writeTo(os);
        }
        else if (node != null) {
            objectMapper.writeValue(os, node);
        }
        else {
            throw new ElasticsearchException("Missing node or bytes array.");
        }
    }

    public void writeTo(GatheringByteChannel channel) throws IOException {
        getOrInitDelegate().writeTo(channel);
    }

    public byte[] toBytes() {
        if (delegate != null) return delegate.toBytes();

        if (node != null) {
            try {
                return objectMapper.writeValueAsBytes(node);
            } catch (JsonProcessingException e) {
                throw new ElasticsearchException(e);
            }
        }

        return null;
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
        return getOrInitNode();
    }

    public JsonNode copyJsonNode() {
        return getOrInitNode().deepCopy();
    }

    public boolean equals(Object obj) {
        return Objects.equals(this, obj);
    }


    protected BytesArray getOrInitDelegate() {
        if (delegate != null) return delegate;
        if (node != null) {
            try {
                this.delegate = new BytesArray(objectMapper.writeValueAsBytes(node));
                return this.delegate;
            }
            catch(JsonProcessingException e) {
                throw new ElasticsearchException(e);
            }
        }
        throw new ElasticsearchException("Missing node or bytes array");
    }

    protected JsonNode getOrInitNode() {
        if (node != null) return node;
        if (delegate != null) {
            try {
                this.node = objectMapper.readTree(delegate.toBytes());
                return this.node;
            }
            catch(IOException e) {
                throw new ElasticsearchException(e);
            }
        }
        throw new ElasticsearchException("Missing node or bytes array");
    }

}
