package com.norikoff.asynccache.dto;

import lombok.Builder;
import lombok.Data;
import org.springframework.context.annotation.Bean;

@Data
@Builder
public class SimpleDto {
    private final String key;
    private final String value;
}
