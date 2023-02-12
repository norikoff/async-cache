package com.norikoff.asynccache.service;

import com.norikoff.asynccache.cache.ConcurrentHashMapWithCleaningCacheProvider;
import com.norikoff.asynccache.dto.SimpleDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
@Slf4j
public class StringCacheService {
    private final ConcurrentHashMapWithCleaningCacheProvider<String, String> concurrentHashMapCacheProvider;

    public Mono<SimpleDto> get(String key) {
        log.debug("Getting by key: {}", key);
        return concurrentHashMapCacheProvider.get(key).map(val -> SimpleDto.builder().key(key).value(val).build()).doOnSuccess(foo -> log.debug("Get by key result: {}", foo));
    }

    public Mono<SimpleDto> put(String key, String value) {
        log.debug("Putting by key: {} value: {}", key, value);
        return concurrentHashMapCacheProvider.put(key, value).map(val -> SimpleDto.builder().key(key).value(val).build()).doOnSuccess(foo -> log.debug("Putted successful by key: {} value: {}", key, foo));
    }

}
