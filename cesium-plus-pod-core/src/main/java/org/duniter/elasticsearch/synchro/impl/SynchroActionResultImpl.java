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

import org.duniter.core.util.PrimitiveIterators;
import org.duniter.elasticsearch.synchro.SynchroActionResult;

public class SynchroActionResultImpl implements SynchroActionResult {

    private final PrimitiveIterators.OfLong insertHits = PrimitiveIterators.newLongSequence();
    private final PrimitiveIterators.OfLong updateHits = PrimitiveIterators.newLongSequence();
    private final PrimitiveIterators.OfLong deleteHits = PrimitiveIterators.newLongSequence();
    private final PrimitiveIterators.OfLong invalidSignatureHits = PrimitiveIterators.newLongSequence();
    private final PrimitiveIterators.OfLong invalidTimesHits = PrimitiveIterators.newLongSequence();

    @Override
    public void addInsert() {
        insertHits.nextLong();
    }

    @Override
    public void addUpdate() {
        updateHits.nextLong();
    }

    @Override
    public void addDelete() {
        deleteHits.nextLong();
    }

    @Override
    public void addInvalidSignature() {
        invalidSignatureHits.nextLong();
    }
    @Override
    public void addInvalidTime() {
        invalidTimesHits.nextLong();
    }

    @Override
    public long getInserts() {
        return insertHits.current();
    }
    @Override
    public long getUpdates() {
        return updateHits.current();
    }
    @Override
    public long getDeletes() {
        return deleteHits.current();
    }
    @Override
    public long getInvalidSignatures() {
        return invalidSignatureHits.current();
    }
    @Override
    public long getInvalidTimes() {
        return invalidTimesHits.current();
    }
}
