package org.duniter.elasticsearch.rest.security;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import org.duniter.core.util.Preconditions;
import org.duniter.core.util.StringUtils;
import org.elasticsearch.common.logging.ESLogger;

import java.util.concurrent.TimeUnit;

public class QuotaMapByIp {

    private final String name;
    private final int maxCallCount;
    private Cache<String, Integer> cacheCounterByIp;

    public QuotaMapByIp(String name, int max, int duration, TimeUnit unit) {
        this(name, max, duration, unit, null);
    }

    public QuotaMapByIp(
            String name,
            int max, int duration, TimeUnit unit, ESLogger log) {
        this.name = name;
        this.maxCallCount = max;
        CacheBuilder builder = CacheBuilder.newBuilder()
                .expireAfterWrite(duration, unit)
                .concurrencyLevel(4);
        //if (log != null && log.isTraceEnabled()) {
            builder.removalListener((removalNotification) -> {
                // TODO trace
                log.debug(String.format("Forget IP {%s} from quota {name: '%s', callCount: %s, cause: '%s'}",
                        removalNotification.getKey(),
                        name,
                        removalNotification.getValue(),
                        removalNotification.getCause().name()));
            });
        //}

        cacheCounterByIp = builder.build();
    }

    boolean increment(String ip) {
        Preconditions.checkArgument(StringUtils.isNotEmpty(ip));

        Integer counter = cacheCounterByIp.getIfPresent(ip);
        if (counter == null) {
            counter = 0;
        }

        // Max reached: cannot increment
        if (counter >= maxCallCount) {
            return false;
        }

        // Increment
        cacheCounterByIp.put(ip, counter + 1);
        return true;
    }
}
