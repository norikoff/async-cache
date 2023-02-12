package com.norikoff.asynccache.cache;

import lombok.Getter;
import reactor.core.publisher.Mono;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public abstract class ConcurrentHashMapWithTaskCacheProvider<K, V> {

    private final Map<K, Wrapper<V>> cache;

    private final long interval;

    private final ScheduledExecutorService scheduledExecutorService;

    private void taskDemon() {
        scheduledExecutorService.scheduleAtFixedRate(this::process, interval, interval, TimeUnit.MILLISECONDS);
    }


    public ConcurrentHashMapWithTaskCacheProvider(int cacheSize, long interval, ScheduledExecutorService scheduledExecutorService) {
        this.cache = new ConcurrentHashMap<>(cacheSize);
        this.interval = interval;
        this.scheduledExecutorService = scheduledExecutorService;
        taskDemon();
    }

    protected abstract void process();

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
