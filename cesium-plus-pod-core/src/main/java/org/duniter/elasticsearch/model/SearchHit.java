package org.duniter.elasticsearch.model;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.databind.JsonNode;
import org.duniter.elasticsearch.util.bytes.BytesJsonNode;
import org.elasticsearch.common.bytes.BytesArray;
import org.elasticsearch.common.bytes.BytesReference;
import org.elasticsearch.common.util.ByteArray;

import java.io.IOException;

public class SearchHit {

    protected String id;

    protected JsonNode source;

    public SearchHit() {
    }

    @JsonGetter("_id")
    public String getId() {
        return id;
    }

    @JsonSetter("_id")
    public void setId(String id) {
        this.id = id;
    }

    @JsonGetter("_source")
    public JsonNode getSource() {
        return source;
    }

    @JsonSetter("_source")
    public void setSource(JsonNode source) {
        this.source = source;
    }

    public BytesReference getSourceRef() throws IOException {
        return new BytesArray(source.binaryValue()); // source;
    }
}