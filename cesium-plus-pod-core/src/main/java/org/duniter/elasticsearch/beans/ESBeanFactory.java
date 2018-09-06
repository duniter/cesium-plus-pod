package org.duniter.elasticsearch.beans;

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

import org.duniter.core.beans.Bean;
import org.duniter.core.beans.BeanCreationException;
import org.duniter.core.beans.BeanFactory;
import org.elasticsearch.common.inject.Inject;
import org.elasticsearch.common.inject.Injector;

/**
 * Created by blavenie on 31/03/17.
 */
public class ESBeanFactory extends BeanFactory {

    private Injector injector = null;

    @Inject
    public void setInjector(Injector injector) {
        this.injector = injector;
    }

    @Override
    protected <S extends Bean> void initBean(S bean) {
        super.initBean(bean);
        if (injector != null) {
            injector.injectMembers(bean);
        }
    }

    @Override
    protected <S extends Bean> S newBean(Class<S> clazz) {
        try {
            return super.newBean(clazz);
        }
        catch(BeanCreationException e) {
            // try using injector, if exists
            if (injector != null) {
                return injector.getBinding(clazz).getProvider().get();
            }
            throw e;
        }
    }
}
