package org.duniter.elasticsearch.synchro.impl;

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

import org.duniter.elasticsearch.synchro.SynchroActionResult;

public class NullSynchroActionResult implements SynchroActionResult {

    @Override
    public void addInsert(){
    }

    @Override
    public void addUpdate() {
    }

    @Override
    public void addDelete() {
    }

    @Override
    public void addInvalidSignature() {
    }

    @Override
    public void addInvalidTime() {

    }

    @Override
    public long getInserts() {
        return 0;
    }

    @Override
    public long getUpdates() {
        return 0;
    }

    @Override
    public long getDeletes() {
        return 0;
    }

    @Override
    public long getInvalidSignatures() {
        return 0;
    }

    @Override
    public long getInvalidTimes() {
        return 0;
    }
}
