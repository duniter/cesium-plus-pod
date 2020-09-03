package org.duniter.elasticsearch.subscription;

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

    protected TestResource(String configName, boolean startESNode) {
        super(configName, "cesium-plus-pod-subscription", startESNode, new TestFixtures());
    }

    public PluginSettings getPluginSettings() {
        return PluginSettings.instance();
    }

    protected void prepareEsHome(File esHomeDir) throws Throwable {
        super.prepareEsHome(esHomeDir);

        // Copy dependencies plugins
        FileUtils.copyDirectory(new File("../cesium-plus-pod-core/target/classes"), new File(esHomeDir, "plugins/cesium-plus-pod-core"));
        FileUtils.copyDirectory(new File("../cesium-plus-pod-user/target/classes"), new File(esHomeDir, "plugins/cesium-plus-pod-user"));

    }


}
