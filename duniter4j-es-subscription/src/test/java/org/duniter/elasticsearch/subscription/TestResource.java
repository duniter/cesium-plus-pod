package org.duniter.elasticsearch.subscription;

/*
 * #%L
 * UCoin Java Client :: Core API
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
        return new TestResource(null, true);
    }

    public static TestResource createNotStartEs() {
        return new TestResource(null, false);
    }

    public static TestResource create(boolean startES) {
        return new TestResource(null, startES);
    }

    public static TestResource create(String configName) {
        return new TestResource(configName, true);
    }

    public static TestResource create(String configName, boolean startES) {
        return new TestResource(configName, startES);
    }

    private TestFixtures fixtures = new TestFixtures();
    private final boolean startESNode;

    protected TestResource(String configName, boolean startESNode) {
        super(configName);
        this.startESNode = startESNode;
    }
    
    public TestFixtures getFixtures() {
        return fixtures;
    }

    public PluginSettings getPluginSettings() {
        return PluginSettings.instance();
    }

    protected void before(Description description) throws Throwable {
        super.before(description);

        // Prepare ES home
        File esHomeDir = getResourceDirectory("es-home");

        System.setProperty("es.path.home", esHomeDir.getCanonicalPath());

        FileUtils.copyDirectory(new File("src/test/es-home"), esHomeDir);
        FileUtils.copyDirectory(new File("target/classes"), new File(esHomeDir, "plugins/duniter4j-es-subscription"));

        // Copy dependencies plugins
        FileUtils.copyDirectory(new File("../duniter4j-es-core/target/classes"), new File(esHomeDir, "plugins/duniter4j-es-core"));
        FileUtils.copyDirectory(new File("../duniter4j-es-user/target/classes"), new File(esHomeDir, "plugins/duniter4j-es-user"));

        if (startESNode) {
            Elasticsearch.main(new String[]{"startScheduling"});
        }

        /*while(true) {
            Thread.sleep(10000);
        }*/
    }

    /**
     * Return configuration files prefix (i.e. 'allegro-test')
     * Could be override by external project
     *
     * @return the prefix to use to retrieve configuration files
     */
    protected String getConfigFilesPrefix() {
        return "duniter4j-es-subscription-test";
    }

}
