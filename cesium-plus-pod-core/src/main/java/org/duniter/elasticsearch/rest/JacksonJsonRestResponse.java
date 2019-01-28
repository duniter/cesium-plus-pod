package org.duniter.elasticsearch.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import org.apache.http.entity.ContentType;
import org.duniter.core.client.model.bma.jackson.JacksonUtils;
import org.elasticsearch.rest.BytesRestResponse;
import org.elasticsearch.rest.RestRequest;
import org.elasticsearch.rest.RestStatus;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class JacksonJsonRestResponse extends BytesRestResponse {

    public JacksonJsonRestResponse(RestRequest request, RestStatus status, Object result) throws IOException {
        super(status, ContentType.APPLICATION_JSON.toString(), getBytes(request, result));
    }

    public static byte[] getBytes(RestRequest request, Object result) throws IOException {

        boolean pretty = request.hasParam("pretty");
        ObjectWriter writer;
        ObjectMapper mapper = JacksonUtils.getThreadObjectMapper();
        if (pretty) {
            writer = mapper.writerWithDefaultPrettyPrinter();
        }
        else {
            writer = mapper.writer();
        }

        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        writer.writeValue(bos, result);

        return bos.toByteArray();
    }
}
