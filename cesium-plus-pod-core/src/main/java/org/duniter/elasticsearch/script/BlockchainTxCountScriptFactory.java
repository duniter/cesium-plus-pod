package org.duniter.elasticsearch.script;

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

import org.elasticsearch.common.Nullable;
import org.elasticsearch.script.AbstractFloatSearchScript;
import org.elasticsearch.script.ExecutableScript;
import org.elasticsearch.script.NativeScriptFactory;

import java.util.List;
import java.util.Map;

public class BlockchainTxCountScriptFactory implements NativeScriptFactory {

    @Override
    public ExecutableScript newScript(@Nullable Map<String, Object> params) {
        return new BlockchainTxCountScript();
    }

    @Override
    public boolean needsScores() {
        return false;
    }

    public class BlockchainTxCountScript extends AbstractFloatSearchScript {

        @Override
        public float runAsFloat() {
            Object a = source().get("transactions");
            return a != null ? ((List)a).size() : 0;
        }
    }
}