package com.norikoff.asynccache.controller;

import com.norikoff.asynccache.dto.SimpleDto;
import com.norikoff.asynccache.service.StringCacheService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping(path = "cache")
@RequiredArgsConstructor
public class CacheController {

    final StringCacheService cacheService;

    @GetMapping(value = "/get/{key}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<SimpleDto> get(@PathVariable String key) {
        return cacheService.get(key);
    }

    @PostMapping(value = "/put", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<SimpleDto> put(@RequestBody SimpleDto simpleDto) {
        return cacheService.put(simpleDto.getKey(), simpleDto.getValue());
    }
}