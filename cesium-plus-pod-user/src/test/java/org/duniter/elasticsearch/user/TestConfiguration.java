package org.duniter.elasticsearch.user;

/*-
 * #%L
 * Duniter4j :: ElasticSearch User plugin
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

import org.duniter.core.exception.TechnicalException;
import org.nuiton.config.ApplicationConfig;
import org.nuiton.config.ArgumentsParserException;

import static org.nuiton.i18n.I18n.t;

/**
 * Created by blavenie on 13/09/17.
 */
public class TestConfiguration {

    private ApplicationConfig applicationConfig;

    public TestConfiguration(String configFileName) {
        applicationConfig = new ApplicationConfig();
        applicationConfig.setConfigFileName(configFileName);

        try {
            applicationConfig.parse(new String[]{});

        } catch (ArgumentsParserException e) {
            throw new TechnicalException(t("duniter4j.config.parse.error"), e);
        }
    }

    public String getDataSyncHost() {
        return applicationConfig.getOption("duniter4j.data.sync.host");
    }

    public int getDataSyncPort() {
        return applicationConfig.getOptionAsInt("duniter4j.data.sync.port");
    }
}
