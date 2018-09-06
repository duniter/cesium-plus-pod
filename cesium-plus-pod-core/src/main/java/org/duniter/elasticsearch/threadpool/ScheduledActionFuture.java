package org.duniter.elasticsearch.threadpool;

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

import org.elasticsearch.ElasticsearchException;
import org.elasticsearch.ElasticsearchTimeoutException;
import org.elasticsearch.action.ActionFuture;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.common.util.concurrent.UncategorizedExecutionException;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class ScheduledActionFuture<T> implements ActionFuture<T> {

    private final ScheduledFuture<T> delegate;

    public ScheduledActionFuture(ScheduledFuture<T> delegate) {
        this.delegate = delegate;
    }

    @Override
    public T actionGet() {
        try {
            return get();
        } catch (InterruptedException e) {
            throw new IllegalStateException("Future got interrupted", e);
        } catch (ExecutionException e) {
            throw rethrowExecutionException(e);
        }
    }

    @Override
    public T actionGet(String timeout) {
        return actionGet(TimeValue.parseTimeValue(timeout, null, getClass().getSimpleName() + ".actionGet.timeout"));
    }

    @Override
    public T actionGet(long timeoutMillis) {
        return actionGet(timeoutMillis, TimeUnit.MILLISECONDS);
    }

    @Override
    public T actionGet(TimeValue timeout) {
        return actionGet(timeout.millis(), TimeUnit.MILLISECONDS);
    }

    @Override
    public T actionGet(long timeout, TimeUnit unit) {
        try {
            return get(timeout, unit);
        } catch (TimeoutException e) {
            throw new ElasticsearchTimeoutException(e.getMessage());
        } catch (InterruptedException e) {
            throw new IllegalStateException("Future got interrupted", e);
        } catch (ExecutionException e) {
            throw rethrowExecutionException(e);
        }
    }

    @Override
    public boolean cancel(boolean mayInterruptIfRunning) {
        return delegate.cancel(mayInterruptIfRunning);
    }

    @Override
    public boolean isCancelled() {
        return delegate.isCancelled();
    }

    @Override
    public boolean isDone() {
        return delegate.isDone();
    }

    @Override
    public T get() throws InterruptedException, ExecutionException {
        return delegate.get();
    }

    @Override
    public T get(long timeout, TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
        return delegate.get(timeout, unit);
    }

    static RuntimeException rethrowExecutionException(ExecutionException e) {
        if (e.getCause() instanceof ElasticsearchException) {
            ElasticsearchException esEx = (ElasticsearchException) e.getCause();
            Throwable root = esEx.unwrapCause();
            if (root instanceof ElasticsearchException) {
                return (ElasticsearchException) root;
            } else if (root instanceof RuntimeException) {
                return (RuntimeException) root;
            }
            return new UncategorizedExecutionException("Failed execution", root);
        } else if (e.getCause() instanceof RuntimeException) {
            return (RuntimeException) e.getCause();
        } else {
            return new UncategorizedExecutionException("Failed execution", e);
        }
    }
}