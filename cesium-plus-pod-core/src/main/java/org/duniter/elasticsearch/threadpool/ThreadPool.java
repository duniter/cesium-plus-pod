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

import org.duniter.core.util.Preconditions;
import org.elasticsearch.action.admin.cluster.stats.ClusterStatsRequestBuilder;
import org.elasticsearch.action.admin.cluster.stats.ClusterStatsResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.cluster.ClusterService;
import org.elasticsearch.cluster.LocalNodeMasterListener;
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

import java.io.Closeable;
import java.io.IOException;
import java.util.concurrent.*;

/**
 * Manage thread pool, to execute tasks asynchronously.
 * Created by eis on 17/06/16.
 */
public class ThreadPool extends AbstractLifecycleComponent<ThreadPool> {

    private ScheduledThreadPoolExecutor scheduler;
    private final Injector injector;
    private final ESLogger logger;

    private final org.elasticsearch.threadpool.ThreadPool delegate;

    private LocalNodeMasterListener isMasterListener;
    private boolean clusterStarted = false;
    private boolean isMaster = false;

    @Inject
    public ThreadPool(Settings settings,
                      Injector injector,
                      org.elasticsearch.threadpool.ThreadPool esThreadPool
                        ) {
        super(settings);
        this.logger = Loggers.getLogger("duniter.threadpool", settings, new String[0]);
        this.injector = injector;

        this.delegate = esThreadPool;

        int availableProcessors = EsExecutors.boundedNumberOfProcessors(settings);
        this.scheduler = new LoggingScheduledThreadPoolExecutor(logger, availableProcessors,
                EsExecutors.daemonThreadFactory(settings, "cesium_plus_scheduler"),
                new RetryPolicy(1, TimeUnit.SECONDS));
        this.scheduler.setExecuteExistingDelayedTasksAfterShutdownPolicy(false);
        this.scheduler.setContinueExistingPeriodicTasksAfterShutdownPolicy(false);
        this.scheduler.setRemoveOnCancelPolicy(true);
    }

    public boolean isMasterNode() {
        return isMaster;
    }

    public void doStart(){
        if (logger.isDebugEnabled()) {
            logger.debug("Starting thread pool...");
        }

        isMasterListener = new LocalNodeMasterListener() {
            @Override
            public void onMaster() {
                if (!isMaster) {
                    isMaster = true;
                    logger.debug("Executing master on start jobs...");
                }
            }

            @Override
            public void offMaster() {
                isMaster = false;
                logger.debug("Executing master on stop jobs...");
            }

            @Override
            public String executorName() {
                return org.elasticsearch.threadpool.ThreadPool.Names.MANAGEMENT;
            }
        };
        injector.getInstance(ClusterService.class)
                .add(isMasterListener);
    }

    public void doStop(){
        if (!scheduler.isShutdown()) {
            scheduler.shutdown();
        }
        injector.getInstance(ClusterService.class)
                .remove(isMasterListener);
        isMasterListener = null;
    }

    public void doClose() {}

    /**
     * Schedules an rest when node is started (allOfToList services and modules ready)
     *
     * @param job the rest to execute when node started
     */
    public void scheduleOnStarted(Runnable job) {
        Preconditions.checkNotNull(job);
        if (clusterStarted) {
            schedule(job);
        }
        else {
            scheduleAfterServiceState(TransportService.class, Lifecycle.State.STARTED, () -> {
                clusterStarted = true;
                job.run();
            });
        }
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
     */
    public void scheduleOnClusterReady(Runnable job) {
        scheduleOnClusterHealthStatus(job, ClusterHealthStatus.YELLOW, ClusterHealthStatus.GREEN);
    }

    /**
     * Schedules when node BECOME the master for the first time (and the cluster ready)
     *
     * @param job the rest to execute
     */
    public void scheduleOnMasterFirstStart(Runnable job) {
        scheduleOnMasterStart(job, true);
    }

    /**
     * Schedules when node BECOME the master (and the cluster ready)
     *
     * @param job the rest to execute
     */
    public void scheduleOnMasterEachStart(Runnable job) {
        scheduleOnMasterStart(job, false);
    }

    /**
     * Schedules when node stop to be the master node, for the first time
     *
     * @param job the rest to execute
     */
    public void scheduleOnMasterFirstStop(Runnable job) {
        scheduleOnMasterStop(job, true);
    }

    /**
     * Schedules when node stop to be the master node, for the first time
     *
     * @param job the rest to execute
     */
    public void scheduleOnMasterFirstStop(Closeable job) {
        scheduleOnMasterStop(() -> {
            try {
                job.close();
            }
            catch(IOException e) {
                // Silent
            }
        }, true);
    }

    /**
     * Schedules each time node stop to be the master node
     *
     * @param job the rest to execute
     */
    public void scheduleOnMasterEachStop(Runnable job) {
        scheduleOnMasterStop(job, false);
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
     */
    public ScheduledActionFuture<?> scheduleAtFixedRate(Runnable command, long initialDelay, long period, TimeUnit timeUnit) {
        long initialDelayMs = new TimeValue(initialDelay, timeUnit).millis();
        long periodMs = new TimeValue(period, timeUnit).millis();
        ScheduledActionFuture<?> future = new ScheduledActionFuture<>(null);
        scheduleAtFixedRateWorkaround(command, initialDelayMs, periodMs, future);
        return future;
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
     * Schedules when node BECOME the master (and the cluster ready)
     *
     * @param job the rest to execute
     * @param onlyOnce if true, run only on first master event on this node
     */
    protected void scheduleOnMasterStart(Runnable job, boolean onlyOnce) {
        if (isMaster) {
            scheduleOnClusterReady(job);
            if (onlyOnce) return;
        }

        final ClusterService cluster = injector.getInstance(ClusterService.class);
        cluster.add(new LocalNodeMasterListener() {
            @Override
            public void onMaster() {
                scheduleOnClusterReady(job);

                // Was run once, so remove the listener
                if (onlyOnce) cluster.remove(this);
            }

            @Override
            public void offMaster() {
            }

            @Override
            public String executorName() {
                return org.elasticsearch.threadpool.ThreadPool.Names.GENERIC;
            }
        });
    }

    /**
     * Schedules when node BECOME the master (and the cluster ready)
     *
     * @param job the rest to execute
     * @param onlyOnce if true, run only on first master event on this node
     */
    protected void scheduleOnMasterStop(Runnable job, boolean onlyOnce) {
        if (clusterStarted && !isMaster && onlyOnce) {
            logger.debug("Skipping a job execution, because node is not the master node");
            return; // Skip; as node is not a master
        }

        final ClusterService cluster = injector.getInstance(ClusterService.class);
        cluster.add(new LocalNodeMasterListener() {
            @Override
            public void onMaster() {
            }

            @Override
            public void offMaster() {
                scheduleOnClusterReady(job);

                // Was run once, so remove the listener
                if (onlyOnce) cluster.remove(this);
            }

            @Override
            public String executorName() {
                return org.elasticsearch.threadpool.ThreadPool.Names.GENERIC;
            }
        });
    }

    /**
     * This method use a workaround to execution schedule at fixed time, because standard call of scheduler.scheduleAtFixedRate
     * does not worked !!
     **/
    protected <T> void scheduleAtFixedRateWorkaround(final Runnable command, final long initialDelayMs,
                                                 final long periodMs,
                                                 final ScheduledActionFuture<T> future) {
        final long expectedNextExecutionTime = System.currentTimeMillis() + initialDelayMs + periodMs;

        ScheduledFuture<?> delegate = scheduler.schedule(() -> {
            if (future.isCancelled()) return; // Was stopped

            try {
                command.run();
            } catch (Throwable t) {
                logger.error("Error while processing subscriptions", t);
            }

            if (future.isCancelled()) return; // Was stopped // Do NOT schedule the next execution

            long nextDelayMs = expectedNextExecutionTime - System.currentTimeMillis();

            // When an execution duration is too long, go to next execution time.
            while (nextDelayMs < 0) {
                nextDelayMs += periodMs;
            }

            // Loop, to schedule the next execution
            scheduleAtFixedRateWorkaround(command, nextDelayMs, periodMs, future);
        },
        initialDelayMs,
        TimeUnit.MILLISECONDS);
        future.setDelegate((ScheduledFuture<T>) delegate);
    }

    public ScheduledExecutorService scheduler() {
        return scheduler;
    }


}
