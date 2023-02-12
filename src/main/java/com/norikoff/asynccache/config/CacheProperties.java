package com.norikoff.asynccache.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "cache")
@Getter
@Setter
public class CacheProperties {
    private int size;
    private long cleanInterval;
    private long ttl;
}
