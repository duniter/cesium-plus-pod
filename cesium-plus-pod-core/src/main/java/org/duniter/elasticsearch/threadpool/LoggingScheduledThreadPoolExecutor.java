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

import java.util.concurrent.*;

public class LoggingScheduledThreadPoolExecutor extends ScheduledThreadPoolExecutor {

    private final ESLogger logger;

    public LoggingScheduledThreadPoolExecutor(ESLogger logger,
                                              int corePoolSize,
                                              ThreadFactory threadFactory,
                                              RejectedExecutionHandler handler) {
        super(corePoolSize, threadFactory, handler);
        this.logger =logger;
    }

   protected <V> RunnableScheduledFuture<V> decorateTask(
                Runnable r, RunnableScheduledFuture<V> task) {
       return new LoggingTask<V>(logger, task);
   }

   protected <V> RunnableScheduledFuture<V> decorateTask(
           Callable<V> c, RunnableScheduledFuture<V> task) {
       return new LoggingTask<V>(logger, task);
   }


    static class LoggingTask<V> implements RunnableScheduledFuture<V> {
        private final RunnableScheduledFuture<V> task;
        private final ESLogger logger;

        public LoggingTask(ESLogger logger, RunnableScheduledFuture<V> task) {
            this.task = task;
            this.logger = logger;
        }

        @Override
        public void run() {
            try {
                task.run();
            } catch (Throwable e) {
                logger.warn(String.format("Failed to execute a task: %s", e.getMessage()), e);
            }
        }

        @Override
        public boolean isPeriodic() {
            return task.isPeriodic();
        }

        @Override
        public int compareTo(Delayed o) {
            return task.compareTo(o);
        }

        @Override
        public boolean cancel(boolean mayInterruptIfRunning) {
            return task.cancel(mayInterruptIfRunning);
        }

        @Override
        public boolean isCancelled() {
            return task.isCancelled();
        }

        @Override
        public boolean isDone() {
            return task.isDone();
        }

        @Override
        public V get() throws InterruptedException, ExecutionException {
           return task.get();
        }

        @Override
        public V get(long timeout, TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
            return task.get(timeout, unit);
        }

        @Override
        public long getDelay(TimeUnit unit) {
            return task.getDelay(unit);
        }
    }

}