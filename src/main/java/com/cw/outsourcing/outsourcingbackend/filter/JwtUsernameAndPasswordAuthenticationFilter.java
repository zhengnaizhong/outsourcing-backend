package com.cw.outsourcing.outsourcingbackend.filter;

import com.cw.outsourcing.outsourcingbackend.config.JwtConfig;
import com.cw.outsourcing.outsourcingbackend.constant.ResponseBodyCodeMessageEnum;
import com.cw.outsourcing.outsourcingbackend.pojo.vo.LoginVO;
import com.cw.outsourcing.outsourcingbackend.pojo.vo.ResponseBodyVO;
import com.cw.outsourcing.outsourcingbackend.util.RedisUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Jwts;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.crypto.SecretKey;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;

public class JwtUsernameAndPasswordAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final AuthenticationManager authenticationManager;
    private final JwtConfig jwtConfig;
    private final SecretKey secretKey;
    private final ObjectMapper objectMapper;
    private final RedisUtil redisUtil;

    public JwtUsernameAndPasswordAuthenticationFilter(AuthenticationManager authenticationManager,
                                                      JwtConfig jwtConfig,
                                                      SecretKey secretKey, ObjectMapper objectMapper,
                                                      RedisUtil redisUtil) {
        this.authenticationManager = authenticationManager;
        this.jwtConfig = jwtConfig;
        this.secretKey = secretKey;
        this.objectMapper = objectMapper;
        this.redisUtil = redisUtil;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request,
                                                HttpServletResponse response) throws AuthenticationException {
        try {
            LoginVO loginVO = objectMapper.readValue(request.getInputStream(), LoginVO.class);
            Authentication authentication = new UsernamePasswordAuthenticationToken(
                    loginVO.getUserName(),
                    loginVO.getPassword()
            );
            return authenticationManager.authenticate(authentication);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request,
                                            HttpServletResponse response,
                                            FilterChain chain,
                                            Authentication authResult) throws IOException, ServletException {
        String token = Jwts.builder()
                .setSubject(authResult.getName())
                .claim("authorities", authResult.getAuthorities())
                .setIssuedAt(new Date())
                .signWith(secretKey)
                .compact();
        // 覆盖原来的token
        redisUtil.setAnyMore(authResult.getName(), token);
        response.addHeader(jwtConfig.getAuthorizationHeader(), jwtConfig.getTokenPrefix() + token);
        response.addHeader(HttpHeaders.CONTENT_TYPE, "application/json");
        response.setCharacterEncoding("utf-8");
        response.getWriter().write(objectMapper.writeValueAsString(
                ResponseBodyVO.<String>builder()
                .code(ResponseBodyCodeMessageEnum.SUCCESS.getCode())
                .message(ResponseBodyCodeMessageEnum.SUCCESS.getMessage())
                .build())
        );
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response,
                                              AuthenticationException failed) throws IOException, ServletException {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.addHeader(HttpHeaders.CONTENT_TYPE, "application/json");
        response.setCharacterEncoding("utf-8");
        if (failed instanceof BadCredentialsException) {
            response.getWriter().write(objectMapper.writeValueAsString(ResponseBodyVO.<String>builder()
                    .code(ResponseBodyCodeMessageEnum.LOGIN_BAD_PASSWORD.getCode())
                    .message(ResponseBodyCodeMessageEnum.LOGIN_BAD_PASSWORD.getMessage())
                    .build())
            );
        }
        if (failed instanceof DisabledException) {
            response.getWriter().write(objectMapper.writeValueAsString(ResponseBodyVO.<String>builder()
                    .code(ResponseBodyCodeMessageEnum.LOGIN_DISABLED.getCode())
                    .message(ResponseBodyCodeMessageEnum.LOGIN_DISABLED.getMessage())
                    .build())
            );
        }
    }


}
