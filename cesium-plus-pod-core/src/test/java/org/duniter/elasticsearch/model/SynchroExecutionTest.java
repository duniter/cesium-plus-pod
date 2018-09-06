package org.duniter.elasticsearch.model;

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

import org.duniter.core.client.model.bma.jackson.JacksonUtils;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;


public class SynchroExecutionTest {

    @Test
    public void deserialize() {
        String json = "{\n" +
                "        \"issuer\" : null,\n" +
                "        \"hash\" : null,\n" +
                "        \"signature\" : null,\n" +
                "        \"time\" : 1505836503,\n" +
                "        \"currency\" : \"g1\",\n" +
                "        \"peer\" : \"CA99448CDD90AB3772474A4CBCCC5A392F4E9AD3F9FA1C4018C6FB432BC04BA8\",\n" +
                "        \"result\" : {\n" +
                "          \"inserts\" : 2,\n" +
                "          \"updates\" : 0,\n" +
                "          \"invalidSignatures\" : 0,\n" +
                "          \"deletes\" : 0\n" +
                "        }\n" +
                "      }";

        try {
            SynchroExecution obj = JacksonUtils.getThreadObjectMapper().readValue(json, SynchroExecution.class);
            Assert.assertNotNull(obj);
        }
        catch(IOException e) {
            Assert.fail(e.getMessage());
        }
    }
}
