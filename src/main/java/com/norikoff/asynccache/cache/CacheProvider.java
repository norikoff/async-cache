package com.norikoff.asynccache.cache;

import reactor.core.publisher.Mono;

public interface CacheProvider<K, V> {


    public Mono<V> put(K key, V value);

    public Mono<V> putIfAbsent(K key, V value);

    public Mono<V> get(K key);

    public Mono<Boolean> containsKey(K key);

    public Mono<V> remove(K key);


}
