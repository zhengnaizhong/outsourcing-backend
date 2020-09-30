package com.cw.outsourcing.outsourcingbackend.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "jwt")
public class JwtConfig {

    private String secretKey;
    private String tokenPrefix;

    public String getAuthorizationHeader() {
        return HttpHeaders.AUTHORIZATION;
    }
}
