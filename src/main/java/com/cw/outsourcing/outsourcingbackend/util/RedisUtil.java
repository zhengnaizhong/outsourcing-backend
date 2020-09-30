package com.cw.outsourcing.outsourcingbackend.util;

import com.cw.outsourcing.outsourcingbackend.config.RedisConstantConfig;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.time.Duration;

@Component
public class RedisUtil {

    private final static String TOKEN_KEY_PREFIX = "TOKEN:";

    private final RedisConstantConfig redisConstantConfig;

    private final RedisTemplate<String, Object> redisTemplate;

    public RedisUtil(RedisTemplate<String, Object> redisTemplate, RedisConstantConfig redisConstantConfig) {
        this.redisTemplate = redisTemplate;
        this.redisConstantConfig = redisConstantConfig;
    }

    public Boolean setTimeIfExist(String userName) {
        return redisTemplate.expire(
                redisConstantConfig.getBaseKeyPrefix()+TOKEN_KEY_PREFIX+userName,
                Duration.ofMinutes(redisConstantConfig.getExpireMinutes())
        );
    }

    public String getTokenByUserName(String userName) {
        return (String) redisTemplate.opsForValue().get(redisConstantConfig.getBaseKeyPrefix()+TOKEN_KEY_PREFIX+userName);
    }

    public void setAnyMore(String userName, String token) {
        redisTemplate.opsForValue().set(
                redisConstantConfig.getBaseKeyPrefix()+TOKEN_KEY_PREFIX+userName,
                token,
                Duration.ofMinutes(redisConstantConfig.getExpireMinutes())
        );
    }

}
