package org.duniter.elasticsearch.test;

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


import org.apache.commons.io.FileUtils;
import org.duniter.core.client.config.Configuration;
import org.elasticsearch.bootstrap.Elasticsearch;
import org.junit.runner.Description;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;

public abstract class TestResource<F extends TestFixtures> extends org.duniter.core.test.TestResource {

    private static final Logger log = LoggerFactory.getLogger(TestResource.class);

    private boolean startNode;

    private String pluginName;

    private F fixtures;

    protected TestResource(String configName, String pluginName, boolean startNode) {
        this(configName, pluginName, startNode, null);
    }

    protected TestResource(String configName, String pluginName, boolean startNode, F fixtures) {
        super(configName);
        this.pluginName = pluginName;
        this.startNode = startNode;
        this.fixtures = fixtures;
    }
    
    public F getFixtures() {
        return fixtures;
    }

    protected void before(Description description) throws Throwable {
        super.before(description);

        // Create an ES home dir
        File esHomeDir = getResourceDirectory("es-home");
        System.setProperty("es.path.home", esHomeDir.getCanonicalPath());

        // Prepare ES home dir
        prepareEsHome(esHomeDir);

        // Start the node
        if (startNode) {
            Elasticsearch.main(new String[]{"start"});
        }
        else {
            Configuration clientConfig = new org.duniter.core.client.config.Configuration(getConfigFilesPrefix() + ".properties");
            Configuration.setInstance(clientConfig);
        }
    }

    protected void prepareEsHome(File esHomeDir) throws Throwable {
        FileUtils.copyDirectory(new File("src/test/es-home"), esHomeDir);
        FileUtils.copyDirectory(new File("target/classes"), new File(esHomeDir, "plugins/" + pluginName));
    }

    /**
     * Need for Duniter4j compatibility. We use an empty file (not need to override it), because is loaded from config/elasticsearch.yml
     */
    protected final String getConfigFilesPrefix() {
        return "cesium-plus-pod-test";
    }


}
