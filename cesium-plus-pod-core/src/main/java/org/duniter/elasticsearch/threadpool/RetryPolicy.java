package org.duniter.elasticsearch.threadpool;

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

import org.elasticsearch.common.logging.ESLogger;
import org.elasticsearch.common.logging.Loggers;
import org.elasticsearch.common.util.concurrent.EsAbortPolicy;

import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class RetryPolicy extends EsAbortPolicy {

    private final ESLogger logger;
    private final long retryDelay;
    private final TimeUnit timeUnit;

    public RetryPolicy(long retryDelay, TimeUnit timeUnit) {
        this(Loggers.getLogger("duniter.threadpool.policy"), retryDelay, timeUnit);
    }

    public RetryPolicy(ESLogger logger, long retryDelay, TimeUnit timeUnit) {
        super();
        this.logger = logger;
        this.retryDelay = retryDelay;
        this.timeUnit = timeUnit;
    }

    @Override
    public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {

        if (executor instanceof ScheduledThreadPoolExecutor) {
            ScheduledThreadPoolExecutor scheduledExecutorService = (ScheduledThreadPoolExecutor)executor;
            scheduledExecutorService.schedule(r, retryDelay, timeUnit);
            logger.warn(String.format("Scheduler queue is full (max pool size = %s). Will retry later...",
                    executor.getMaximumPoolSize()));
        }
        else {
            logger.error("Scheduler queue is full (max pool size = %s). Operation is rejected !");
            super.rejectedExecution(r, executor);
        }
    }

}