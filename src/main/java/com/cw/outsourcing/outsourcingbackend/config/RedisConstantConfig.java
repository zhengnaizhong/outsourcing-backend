package com.cw.outsourcing.outsourcingbackend.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "redis")
public class RedisConstantConfig {

    private String baseKeyPrefix;

    private Long expireMinutes;
}
