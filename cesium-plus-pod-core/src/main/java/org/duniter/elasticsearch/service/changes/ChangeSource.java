package org.duniter.elasticsearch.service.changes;

/*
 * #%L
 * Duniter4j :: ElasticSearch Plugin
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

/*
    Copyright 2015 ForgeRock AS

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

        http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
*/

import com.google.common.base.Joiner;
import com.google.common.collect.Sets;
import org.duniter.core.util.CollectionUtils;
import org.duniter.core.util.Preconditions;
import com.google.common.collect.ImmutableSet;
import org.duniter.core.util.StringUtils;

import java.util.Set;

public class ChangeSource {
    private final Set<String> indices;
    private final Set<String> types;
    private final Set<String> ids;

    public ChangeSource() {
        this.indices = Sets.newHashSet();
        this.types = Sets.newHashSet();
        this.ids = Sets.newHashSet();
    }

    public ChangeSource(String source) {
        String[] parts = source.split("/");

        indices = parts[0].equals("*") ? null : ImmutableSet.copyOf(parts[0].split(","));

        if (parts.length > 1) {
            types = parts[1].equals("*") ? null : ImmutableSet.copyOf(parts[1].split(","));
        } else {
            types = null;
        }

        if (parts.length > 2) {
            ids = parts[2].equals("*") ? null : ImmutableSet.copyOf(parts[2].split(","));
        } else {
            ids = null;
        }
    }

    public ChangeSource(String index, String type) {
        this(index, type, null);
    }

    public ChangeSource(String index, String type, String id) {
        Preconditions.checkArgument(StringUtils.isNotBlank(index));
        indices = index.equals("*") ? null : ImmutableSet.of(index);
        types = StringUtils.isBlank(type) || type.equals("*") ? null : ImmutableSet.of(type);
        ids = StringUtils.isBlank(id) || id.equals("*") ? null : ImmutableSet.of(id);
    }

    public Set<String> getIds() {
        return ids;
    }

    public Set<String> getIndices() {
        return indices;
    }

    public Set<String> getTypes() {
        return types;
    }

    public ChangeSource addIndex(String index){
        this.indices.add(index);
        return this;
    }
    public ChangeSource addType(String type){
        this.types.add(type);
        return this;
    }
    public ChangeSource addId(String id){
        this.ids.add(id);
        return this;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();

        // Add indices
        Joiner joiner = Joiner.on(',');
        if (CollectionUtils.isEmpty(indices)) {
            sb.append('*');
        }
        else {
            joiner.appendTo(sb, indices);
        }

        // Add types
        if (CollectionUtils.isEmpty(types)) {
            if (CollectionUtils.isNotEmpty(ids)) {
                sb.append("/*");
            }
        }
        else {
            sb.append('/');
            joiner.appendTo(sb, types);
        }

        // Add ids
        if (CollectionUtils.isNotEmpty(ids)) {
            sb.append('/');
            joiner.appendTo(sb, ids);
        }
        return sb.toString();
    }

    public boolean apply(String index, String type, String id) {
        if (indices != null && !indices.contains(index)) {
            return false;
        }

        if (types != null && !types.contains(type)) {
            return false;
        }

        if (ids != null && !ids.contains(id)) {
            return false;
        }

        return true;
    }

    public boolean isEmpty() {
        return indices == null && types == null && ids == null;
    }

    public void merge(ChangeSource s) {
        if (s == null) return;
        if (CollectionUtils.isNotEmpty(s.getIndices())) {
            indices.addAll(s.getIndices());
        }
        if (CollectionUtils.isNotEmpty(s.getTypes())) {
            types.addAll(s.getTypes());
        }
        if (CollectionUtils.isNotEmpty(s.getIds())) {
            ids.addAll(s.getIds());
        }
    }
}
