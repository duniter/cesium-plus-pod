package org.duniter.elasticsearch;

/*
 * #%L
 * duniter4j-elasticsearch-plugin
 * %%
 * Copyright (C) 2014 - 2016 EIS
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
import org.duniter.elasticsearch.dao.DaoModule;
import org.duniter.elasticsearch.rest.RestModule;
import org.duniter.elasticsearch.script.BlockchainTxCountScriptFactory;
import org.duniter.elasticsearch.security.SecurityModule;
import org.duniter.elasticsearch.service.ServiceModule;
import org.duniter.elasticsearch.threadpool.ThreadPool;
import org.duniter.elasticsearch.websocket.WebSocketModule;
import org.duniter.elasticsearch.http.netty.NettyHttpServerTransport;
import org.elasticsearch.common.component.LifecycleComponent;
import org.elasticsearch.common.inject.Inject;
import org.elasticsearch.common.inject.Module;
import org.elasticsearch.common.logging.ESLogger;
import org.elasticsearch.common.logging.Loggers;
import org.elasticsearch.common.settings.Settings;
import org.duniter.elasticsearch.http.WebSocketServerModule;
import org.elasticsearch.http.HttpServerModule;
import org.elasticsearch.script.ScriptModule;

import java.util.Collection;

public class Plugin extends org.elasticsearch.plugins.Plugin {

    private ESLogger logger;

    private boolean enable;
    private boolean enableWs;

    @Inject public Plugin(Settings settings) {
        this.logger = Loggers.getLogger("duniter.core", settings, new String[0]);

        this.enable = settings.getAsBoolean("duniter.enable", true);
        this.enableWs = settings.getAsBoolean("duniter.ws.enable", this.enable);
    }

    @Override
    public String name() {
        return "cesium-plus-pod-core";
    }

    @Override
    public String description() {
        return "Duniter Core Plugin";
    }

    public void onModule(ScriptModule scriptModule) {
        // TODO: in ES v5+, see example here :
        // https://github.com/imotov/elasticsearch-native-script-example/blob/60a390f77f2fb25cb89d76de5071c52207a57b5f/src/main/java/org/elasticsearch/examples/nativescript/plugin/NativeScriptExamplesPlugin.java
        scriptModule.registerScript("txcount", BlockchainTxCountScriptFactory.class);

    }

    public void onModule(HttpServerModule httpServerModule) {
        if (this.enableWs) httpServerModule.setHttpServerTransport(NettyHttpServerTransport.class, "cesium-plus-core");
    }

    @Override
    public Collection<Module> nodeModules() {
        if (!enable) {
            logger.warn(description() + " has been disabled.");
            return Lists.newArrayList();
        }

        Collection<Module> modules = Lists.newArrayList();
        modules.add(new SecurityModule());
        modules.add(new RestModule());

        // Websocket
        if (this.enableWs) {
            modules.add(new WebSocketServerModule());
            modules.add(new WebSocketModule());
        }


        modules.add(new DaoModule());
        modules.add(new ServiceModule());
        return modules;
    }

    @Override
    public Collection<Class<? extends LifecycleComponent>> nodeServices() {
        if (!enable) return Lists.newArrayList();
        Collection<Class<? extends LifecycleComponent>> components = Lists.newArrayList();
        components.add(PluginSettings.class);
        components.add(ThreadPool.class);
        components.add(PluginInit.class);
        return components;
    }

    /* -- protected methods -- */


}