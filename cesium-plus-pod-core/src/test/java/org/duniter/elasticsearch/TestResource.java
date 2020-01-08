package org.duniter.elasticsearch;

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


import com.google.common.collect.Lists;
import org.apache.commons.io.FileUtils;
import org.duniter.core.client.config.Configuration;
import org.duniter.core.client.config.ConfigurationOption;
import org.duniter.core.client.service.ServiceLocator;
import org.elasticsearch.bootstrap.Elasticsearch;
import org.junit.runner.Description;
import org.nuiton.config.ApplicationConfig;
import org.nuiton.i18n.I18n;
import org.nuiton.i18n.init.DefaultI18nInitializer;
import org.nuiton.i18n.init.UserI18nInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class TestResource extends org.duniter.core.test.TestResource {

    private static final Logger log = LoggerFactory.getLogger(TestResource.class);

    public static TestResource create() {
        return new TestResource(null, true);
    }
    
    public static TestResource create(String configName) {
        return new TestResource(configName, true);
    }

    public static TestResource stopped() {
        return new TestResource(null, false);
    }

    private TestFixtures fixtures = new TestFixtures();
    private boolean startNode;

    protected TestResource(String configName, boolean startNode) {
        super(configName);
        this.startNode = startNode;
    }
    
    public TestFixtures getFixtures() {
        return fixtures;
    }

    protected void before(Description description) throws Throwable {
        super.before(description);

        // Prepare ES home
        File esHomeDir = getResourceDirectory("es-home");

        System.setProperty("es.path.home", esHomeDir.getCanonicalPath());

        FileUtils.copyDirectory(new File("src/test/es-home"), esHomeDir);
        FileUtils.copyDirectory(new File("target/classes"), new File(esHomeDir, "plugins/cesium-plus-pod-core"));

        if (startNode) {
            Elasticsearch.main(new String[]{"start"});
        }
        else {
            Configuration clientConfig = new org.duniter.core.client.config.Configuration(getConfigFilesPrefix() + ".properties");
            Configuration.setInstance(clientConfig);
        }
    }

    /**
     * Return configuration files prefix (i.e. 'allegro-test')
     * Could be override by external project
     * 
     * @return the prefix to use to retrieve configuration files
     */
    protected String getConfigFilesPrefix() {
        return "cesium-plus-pod-core-test";
    }

}
