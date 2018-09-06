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

public class TestResource extends org.duniter.core.test.TestResource {

    private static final Logger log = LoggerFactory.getLogger(TestResource.class);

    public static TestResource create() {
        return new TestResource(null);
    }
    
    public static TestResource create(String configName) {
        return new TestResource(configName);
    }

    private TestFixtures fixtures = new TestFixtures();

    private TestConfiguration testConfiguration;

    protected TestResource(String configName) {
        super(configName);
    }
    
    protected void before(Description description) throws Throwable {
        super.before(description);

        // Prepare ES home
        File esHomeDir = getResourceDirectory("es-home");

        System.setProperty("es.path.home", esHomeDir.getCanonicalPath());

        FileUtils.copyDirectory(new File("src/test/es-home"), esHomeDir);
        FileUtils.copyDirectory(new File("target/classes"), new File(esHomeDir, "plugins/cesium-plus-pod-user"));

        Elasticsearch.main(new String[]{"start"});

        // Init a configuration
        testConfiguration = new TestConfiguration(getConfigFileName());

    }

    public TestFixtures getFixtures() {
        return fixtures;
    }

    public TestConfiguration getConfiguration() {
        return testConfiguration;
    }

    /* -- protected method -- */

    protected String getConfigFilesPrefix() {
        return "cesium-plus-pod-user-test";
    }

}
