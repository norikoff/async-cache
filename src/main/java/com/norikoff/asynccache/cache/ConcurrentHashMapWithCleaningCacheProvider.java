package com.norikoff.asynccache.cache;

import lombok.Getter;
import reactor.core.publisher.Mono;

import java.util.Iterator;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

// Пример простого кэша с отчисткой устаревших или неиспользуемых данных
public class ConcurrentHashMapWithCleaningCacheProvider<K, V> implements CacheProvider<K, V> {

    private final Map<K, Wrapper<V>> cache;

    private final ScheduledExecutorService scheduledExecutorService;

    private final long cleanInterval;
    private final long ttl;

    private void cleanDemon(Runnable cleanTask) {
        scheduledExecutorService.scheduleAtFixedRate(cleanTask, cleanInterval, cleanInterval, TimeUnit.MILLISECONDS);
    }

    private Runnable initCleanTask() {
        return () -> {
            long now = System.currentTimeMillis();
            if (!cache.isEmpty()) {
                Iterator<Map.Entry<K, Wrapper<V>>> mIterator = cache.entrySet().iterator();
                while (mIterator.hasNext()) {
                    long expiry = ttl + mIterator.next().getValue().getLastAccessMillis();
                    if (now > expiry) {
                        mIterator.remove();
                    }
                }
            }
        };
    }


    public ConcurrentHashMapWithCleaningCacheProvider(int cacheSize, long cleanInterval, long ttl, ScheduledExecutorService scheduledExecutorService) {
        this.cache = new ConcurrentHashMap<>(cacheSize);
        this.cleanInterval = cleanInterval;
        this.ttl = ttl;
        this.scheduledExecutorService = scheduledExecutorService;
        Runnable cleanTask = this.initCleanTask();
        this.cleanDemon(cleanTask);
    }

    public Mono<V> put(K key, V value) {
        return Mono.justOrEmpty(Optional.ofNullable(cache.put(key, new Wrapper<>(value))).map(Wrapper::getValue).orElse(null));
    }

    public Mono<V> putIfAbsent(K key, V value) {
        return Mono.justOrEmpty(Optional.ofNullable(cache.putIfAbsent(key, new Wrapper<>(value))).map(Wrapper::getValue).orElse(null));
    }

    public Mono<V> get(K key) {
        return Mono.justOrEmpty(Optional.ofNullable(cache.get(key)).map(Wrapper::getValue).orElse(null));
    }

    public Mono<Boolean> containsKey(K key) {
        return Mono.just(cache.containsKey(key));
    }

    public Mono<V> remove(K key) {
        return Mono.just(cache.remove(key).getValue());
    }

    @Getter
    static private class Wrapper<T> {

        private final T value;
        long lastAccessMillis;

        Wrapper(T value) {
            lastAccessMillis = System.currentTimeMillis();
            this.value = value;
        }

        T getValue() {
            lastAccessMillis = System.currentTimeMillis();
            return this.value;
        }

    }


}
