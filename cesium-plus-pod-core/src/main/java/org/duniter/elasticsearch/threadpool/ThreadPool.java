package org.duniter.elasticsearch.threadpool;

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

import com.google.common.collect.Lists;
import org.duniter.core.util.Preconditions;
import org.elasticsearch.action.admin.cluster.stats.ClusterStatsRequestBuilder;
import org.elasticsearch.action.admin.cluster.stats.ClusterStatsResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.cluster.health.ClusterHealthStatus;
import org.elasticsearch.common.component.AbstractLifecycleComponent;
import org.elasticsearch.common.component.Lifecycle;
import org.elasticsearch.common.component.LifecycleComponent;
import org.elasticsearch.common.inject.Inject;
import org.elasticsearch.common.inject.Injector;
import org.elasticsearch.common.logging.ESLogger;
import org.elasticsearch.common.logging.Loggers;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.common.util.concurrent.EsExecutors;
import org.elasticsearch.transport.TransportService;
import org.nuiton.i18n.I18n;

import java.util.List;
import java.util.concurrent.*;

/**
 * Manage thread pool, to execute tasks asynchronously.
 * Created by eis on 17/06/16.
 */
public class ThreadPool extends AbstractLifecycleComponent<ThreadPool> {

    private ScheduledThreadPoolExecutor scheduler = null;
    private final Injector injector;
    private final ESLogger logger = Loggers.getLogger("duniter.threadpool");

    private final org.elasticsearch.threadpool.ThreadPool delegate;

    private final List<Runnable> afterStartedCommands;

    @Inject
    public ThreadPool(Settings settings,
                      Injector injector,
                      org.elasticsearch.threadpool.ThreadPool esThreadPool
                        ) {
        super(settings);
        this.injector = injector;
        this.afterStartedCommands = Lists.newArrayList();

        this.delegate = esThreadPool;

        int availableProcessors = EsExecutors.boundedNumberOfProcessors(settings);
        this.scheduler = new LoggingScheduledThreadPoolExecutor(logger, availableProcessors,
                EsExecutors.daemonThreadFactory(settings, "duniter-scheduler"),
                new RetryPolicy(1, TimeUnit.SECONDS));
        this.scheduler.setExecuteExistingDelayedTasksAfterShutdownPolicy(false);
        this.scheduler.setContinueExistingPeriodicTasksAfterShutdownPolicy(false);
        this.scheduler.setRemoveOnCancelPolicy(true);
    }

    public void doStart(){
        if (logger.isDebugEnabled()) {
            logger.debug("Starting Duniter4j ThreadPool...");
        }

        if (!afterStartedCommands.isEmpty()) {
            scheduleOnStarted(() -> {
                afterStartedCommands.forEach(command -> command.run());
                this.afterStartedCommands.clear();
            });
        }
    }

    public void doStop(){
        scheduler.shutdown();
    }

    public void doClose() {}

    /**
     * Schedules an rest when node is started (allOfToList services and modules ready)
     *
     * @param job the rest to execute when node started
     * @return a ScheduledFuture who's get will return when the task is complete and throw an exception if it is canceled
     */
    public void scheduleOnStarted(Runnable job) {
        Preconditions.checkNotNull(job);
        scheduleAfterServiceState(TransportService.class, Lifecycle.State.STARTED, job);
    }

    /**
     * Schedules an rest when cluster is ready AND has one of the expected health status
     *
     * @param job the rest to execute
     * @param expectedStatus expected health status, to run the job
     * @return a ScheduledFuture who's get will return when the task is complete and throw an exception if it is canceled
     */
    public void scheduleOnClusterHealthStatus(Runnable job, ClusterHealthStatus... expectedStatus) {
        Preconditions.checkNotNull(job);

        Preconditions.checkArgument(expectedStatus.length > 0);

        scheduleOnStarted(() -> {
            if (waitClusterHealthStatus(expectedStatus)) {
                // continue
                job.run();
            }
        });
    }

    /**
     * Schedules an rest when cluster is ready
     *
     * @param job the rest to execute
     * @param expectedStatus expected health status, to run the job
     * @return a ScheduledFuture who's get will return when the task is complete and throw an exception if it is canceled
     */
    public void scheduleOnClusterReady(Runnable job) {
        scheduleOnClusterHealthStatus(job, ClusterHealthStatus.YELLOW, ClusterHealthStatus.GREEN);
    }

    /**
     * Schedules an rest that runs on the scheduler thread, when possible (0 delay).
     *
     * @param command the rest to take
     * @return a ScheduledFuture who's get will return when the task is complete and throw an exception if it is canceled
     */
    public ScheduledActionFuture<?> schedule(Runnable command) {
        return schedule(command, new TimeValue(0));
    }

    /**
     * Schedules an rest that runs on the scheduler thread, after a delay.
     *
     * @param command the rest to take
     * @param name @see {@link org.elasticsearch.threadpool.ThreadPool.Names}
     * @param delay the delay interval
     * @return a ScheduledFuture who's get will return when the task is complete and throw an exception if it is canceled
     */
    public ScheduledActionFuture<?> schedule(Runnable command, String name, TimeValue delay) {
        if (name == null) {
            return new ScheduledActionFuture<>(scheduler.schedule(command, delay.millis(), TimeUnit.MILLISECONDS));
        }
        return new ScheduledActionFuture<>(delegate.schedule(delay,
                name,
                command));
    }


    public ScheduledActionFuture<?> schedule(Runnable command,
                                       long delay, TimeUnit unit) {

        return new ScheduledActionFuture<>(scheduler.schedule(command, delay, unit));
    }

    /**
     * Schedules an rest that runs on the scheduler thread, after a delay.
     *
     * @param command the rest to take
     * @param delay the delay interval
     * @return a ScheduledFuture who's get will return when the task is complete and throw an exception if it is canceled
     */
    public ScheduledActionFuture<?> schedule(Runnable command, TimeValue delay) {
        return schedule(command, null, delay);
    }

    /**
     * Schedules a periodic rest that always runs on the scheduler thread.
     *
     * @param command the rest to take
     * @param initialDelay the initial delay
     * @param period the period
     * @param timeUnit the time unit
     * @return a ScheduledFuture who's get will return when the task is complete and throw an exception if it is canceled
     */
    public ScheduledActionFuture<?> scheduleAtFixedRate(Runnable command, long initialDelay, long period, TimeUnit timeUnit) {
        long initialDelayMs = new TimeValue(initialDelay, timeUnit).millis();
        long periodMs = new TimeValue(period, timeUnit).millis();
        return new ScheduledActionFuture<>(scheduleAtFixedRateWorkaround(command, initialDelayMs, periodMs));
    }

    /* -- protected methods  -- */

    protected <T extends LifecycleComponent<T>> ScheduledActionFuture<?> scheduleAfterServiceState(Class<T> waitingServiceClass,
                                                                                             final Lifecycle.State waitingState,
                                                                                             final Runnable job) {
        Preconditions.checkNotNull(waitingServiceClass);
        Preconditions.checkNotNull(waitingState);
        Preconditions.checkNotNull(job);

        final T service = injector.getInstance(waitingServiceClass);
        return schedule(() -> {
            while(service.lifecycleState() != waitingState) {
                try {
                    Thread.sleep(100); // wait 100 ms
                }
                catch(InterruptedException e) {
                }
            }

            // continue
            job.run();
        }, TimeValue.timeValueSeconds(10));
    }

    public boolean waitClusterHealthStatus(ClusterHealthStatus... expectedStatus) {
        Preconditions.checkNotNull(expectedStatus);
        Preconditions.checkArgument(expectedStatus.length > 0);

        Client client = injector.getInstance(Client.class);
        ClusterStatsRequestBuilder statsRequest = client.admin().cluster().prepareClusterStats();
        ClusterStatsResponse stats = null;
        boolean canContinue = false;
        boolean firstTry = true;
        while (!canContinue) {
            try {
                if (stats != null) Thread.sleep(100); // wait 100 ms
                stats = statsRequest.execute().get();
                for (ClusterHealthStatus status: expectedStatus) {
                    if (stats.getStatus() == status) {
                        if (!firstTry && logger.isDebugEnabled()) {
                            logger.debug(I18n.t("duniter4j.threadPool.clusterHealthStatus.changed", status.name()));
                        }
                        canContinue = true;
                        break;
                    }
                }
                firstTry = false;
            } catch (ExecutionException e) {
                // Continue
            } catch (InterruptedException e) {
                return false; // stop
            }
        }

        return canContinue;
    }

    /**
     * This method use a workaround to execution schedule at fixed time, because standard call of scheduler.scheduleAtFixedRate
     * does not worked !!
     **/
    protected ScheduledFuture<?> scheduleAtFixedRateWorkaround(final Runnable command, final long initialDelayMs, final long periodMs) {
        final long expectedNextExecutionTime = System.currentTimeMillis() + initialDelayMs + periodMs;

        return scheduler.schedule(
                () -> {
                    try {
                        command.run();
                    } catch (Throwable t) {
                        logger.error("Error while processing subscriptions", t);
                    }

                    long nextDelayMs = expectedNextExecutionTime - System.currentTimeMillis();

                    // When an execution duration is too long, go to next execution time.
                    while (nextDelayMs < 0) {
                        nextDelayMs += periodMs;
                    }

                    // Schedule the next execution
                    scheduleAtFixedRateWorkaround(command, nextDelayMs, periodMs);
                },
                initialDelayMs,
                TimeUnit.MILLISECONDS)
                ;
    }

    public ScheduledExecutorService scheduler() {
        return scheduler;
    }


}
