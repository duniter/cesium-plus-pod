package org.duniter.elasticsearch.user;/*
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


import org.apache.commons.io.FileUtils;
import org.elasticsearch.bootstrap.Elasticsearch;
import org.junit.runner.Description;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;

public class TestResource extends org.duniter.elasticsearch.test.TestResource<TestFixtures> {

    private static final Logger log = LoggerFactory.getLogger(TestResource.class);

    public static TestResource create() {
        return new TestResource(null, true);
    }
    
    public static TestResource create(String configName) {
        return new TestResource(configName, true);
    }

    private TestConfiguration testConfiguration;

    public TestConfiguration getConfiguration() {
        return testConfiguration;
    }

    protected TestResource(String configName, boolean startEsNode) {
        super(configName, "cesium-plus-pod-user", startEsNode, new TestFixtures());
    }

    @Override
    protected void prepareEsHome(File esHomeDir) throws Throwable {
        super.prepareEsHome(esHomeDir);

        // Copy dependencies plugins
        FileUtils.copyDirectory(new File("../cesium-plus-pod-core/target/classes"), new File(esHomeDir, "plugins/cesium-plus-pod-core"));
    }


}
