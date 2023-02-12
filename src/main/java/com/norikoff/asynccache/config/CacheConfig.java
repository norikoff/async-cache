package com.norikoff.asynccache.config;

import com.norikoff.asynccache.cache.ConcurrentHashMapWithCleaningCacheProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

@Configuration
public class CacheConfig {

    @Autowired
    CacheProperties cacheProperties;

    @Bean
    public ConcurrentHashMapWithCleaningCacheProvider<String, String> cacheProvider() {
        return new ConcurrentHashMapWithCleaningCacheProvider<>(cacheProperties.getSize(), cacheProperties.getCleanInterval(), cacheProperties.getTtl(), cacheCleanTimer());
    }

    @Bean(destroyMethod="shutdown")
    public ScheduledExecutorService cacheCleanTimer() {
        return Executors.newSingleThreadScheduledExecutor(r -> {
            Thread thread = Executors.defaultThreadFactory().newThread(r);
            thread.setDaemon(true);
            return thread;
        });
    }

}
