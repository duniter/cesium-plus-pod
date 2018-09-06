package org.duniter.elasticsearch.rest.security;

/*
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
import org.elasticsearch.common.bytes.BytesReference;
import org.elasticsearch.common.unit.ByteSizeValue;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.rest.RestRequest;

import java.net.SocketAddress;
import java.util.Map;

/**
 * Created by blavenie on 30/12/16.
 */
public class RedirectionRestRequest extends RestRequest {

    private final RestRequest delegate;
    private String path;

    public RedirectionRestRequest(RestRequest delegate, String path) {
        super();
        this.delegate = delegate;
        this.path = path;
    }

    @Override
    public Method method() {
        return delegate.method();
    }

    @Override
    public String uri() {
        return delegate.uri();
    }

    @Override
    public String rawPath() {
        return delegate.rawPath();
    }

    @Override
    public boolean hasContent() {
        return delegate.hasContent();
    }

    @Override
    public BytesReference content() {
        return delegate.content();
    }

    @Override
    public String header(String name) {
        return delegate.header(name);
    }

    @Override
    public Iterable<Map.Entry<String, String>> headers() {
        return delegate.headers();
    }

    @Override
    @Nullable
    public SocketAddress getRemoteAddress() {
        return delegate.getRemoteAddress();
    }

    @Override
    @Nullable
    public SocketAddress getLocalAddress() {
        return delegate.getLocalAddress();
    }

    @Override
    public boolean hasParam(String key) {
        return delegate.hasParam(key);
    }

    @Override
    public String param(String key) {
        return delegate.param(key);
    }

    @Override
    public Map<String, String> params() {
        return delegate.params();
    }

    @Override
    public float paramAsFloat(String key, float defaultValue) {
        return delegate.paramAsFloat(key, defaultValue);
    }

    @Override
    public int paramAsInt(String key, int defaultValue) {
        return delegate.paramAsInt(key, defaultValue);
    }

    @Override
    public long paramAsLong(String key, long defaultValue) {
        return delegate.paramAsLong(key, defaultValue);
    }

    @Override
    public boolean paramAsBoolean(String key, boolean defaultValue) {
        return delegate.paramAsBoolean(key, defaultValue);
    }

    @Override
    public Boolean paramAsBoolean(String key, Boolean defaultValue) {
        return delegate.paramAsBoolean(key, defaultValue);
    }

    @Override
    public TimeValue paramAsTime(String key, TimeValue defaultValue) {
        return delegate.paramAsTime(key, defaultValue);
    }

    @Override
    public ByteSizeValue paramAsSize(String key, ByteSizeValue defaultValue) {
        return delegate.paramAsSize(key, defaultValue);
    }

    @Override
    public String[] paramAsStringArray(String key, String[] defaultValue) {
        return delegate.paramAsStringArray(key, defaultValue);
    }

    @Override
    public String[] paramAsStringArrayOrEmptyIfAll(String key) {
        return delegate.paramAsStringArrayOrEmptyIfAll(key);
    }

    @Override
    public String param(String key, String defaultValue) {
        return delegate.param(key, defaultValue);
    }
}
