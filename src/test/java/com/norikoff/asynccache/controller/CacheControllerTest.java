package com.norikoff.asynccache.controller;

import com.norikoff.asynccache.config.CacheConfig;
import com.norikoff.asynccache.config.CacheProperties;
import com.norikoff.asynccache.dto.SimpleDto;
import com.norikoff.asynccache.service.StringCacheService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.BodyInserters;


@ExtendWith(SpringExtension.class)
@WebFluxTest(controllers = CacheController.class)
@Import({StringCacheService.class, CacheConfig.class, CacheProperties.class})
class CacheControllerTest {

    @Autowired
    private WebTestClient webClient;


    @Test
    void get() {
        SimpleDto simpleDto = SimpleDto.builder().key("1").value("asd").build();

        webClient.post()
                .uri("/cache/put")
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(simpleDto))
                .exchange()
                .expectStatus().is2xxSuccessful()
                .expectBody(SimpleDto.class);
    }

    @Test
    void put() {

    }
}