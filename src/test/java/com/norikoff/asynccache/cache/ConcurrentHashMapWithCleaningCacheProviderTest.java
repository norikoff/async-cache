package com.norikoff.asynccache.cache;

import com.norikoff.asynccache.config.CacheConfig;
import com.norikoff.asynccache.dto.SimpleDto;
import com.norikoff.asynccache.service.StringCacheService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.reactivestreams.Publisher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
class ConcurrentHashMapWithCleaningCacheProviderTest {

    @Autowired
    private ConcurrentHashMapWithCleaningCacheProvider<String, String> cacheProvider;


    @Test
    public void putAndGetTest() {
        SimpleDto dto = SimpleDto.builder().key("1").value("asd").build();
        Mono<String> put = cacheProvider.put(dto.getKey(), dto.getValue());
        Mono<String> get = cacheProvider.get(dto.getKey());
        Publisher<String> composite = Mono
                .from(put)
                .then(get);
        StepVerifier
                .create(composite)
                .consumeNextWith(s -> {
                    assertNotNull(s);
                    assertEquals("asd", s);
                })
                .verifyComplete();
    }
}