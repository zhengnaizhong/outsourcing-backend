package com.cw.outsourcing.outsourcingbackend.filter;

import com.cw.outsourcing.outsourcingbackend.config.JwtConfig;
import com.cw.outsourcing.outsourcingbackend.constant.ResponseBodyCodeMessageEnum;
import com.cw.outsourcing.outsourcingbackend.pojo.vo.ResponseBodyVO;
import com.cw.outsourcing.outsourcingbackend.util.RedisUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.crypto.SecretKey;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class JwtTokenVerifier extends OncePerRequestFilter {

    private final SecretKey secretKey;
    private final JwtConfig jwtConfig;
    private final RedisUtil redisUtil;
    private final ObjectMapper objectMapper;

    public JwtTokenVerifier(SecretKey secretKey,
                            JwtConfig jwtConfig, RedisUtil redisUtil,
                            ObjectMapper objectMapper) {
        this.secretKey = secretKey;
        this.jwtConfig = jwtConfig;
        this.redisUtil = redisUtil;
        this.objectMapper = objectMapper;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        String authorizationHeader = request.getHeader(jwtConfig.getAuthorizationHeader());
        if (StringUtils.isEmpty(authorizationHeader) || !authorizationHeader.startsWith(jwtConfig.getTokenPrefix())) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.addHeader(HttpHeaders.CONTENT_TYPE, "application/json");
            response.setCharacterEncoding("utf-8");
            response.getWriter().write(objectMapper.writeValueAsString(ResponseBodyVO.<String>builder()
                    .code(ResponseBodyCodeMessageEnum.LOGIN_BAD_PASSWORD.getCode())
                    .message(ResponseBodyCodeMessageEnum.LOGIN_BAD_PASSWORD.getMessage())
                    .build())
            );
            return;
        }
        String token = authorizationHeader.replace(jwtConfig.getTokenPrefix(), "");
        try {
            Jws<Claims> claimsJws = Jwts.parserBuilder()
                    .setSigningKey(secretKey)
                    .build()
                    .parseClaimsJws(token);
            Claims body = claimsJws.getBody();
            String userName = body.getSubject();
            // token校验成功，判断redis是否存在，存在续期，不存在直接抛出异常失败处理
            if (redisUtil.setTimeIfExist(userName) && token.equals(redisUtil.getTokenByUserName(userName))) {
                List<Map<String, String>> authorities = (List<Map<String, String>>) body.get("authorities");
                Set<SimpleGrantedAuthority> simpleGrantedAuthorities = authorities.stream()
                        .map(m -> new SimpleGrantedAuthority(m.get("authority")))
                        .collect(Collectors.toSet());
                Authentication authentication = new UsernamePasswordAuthenticationToken(
                        userName,
                        null,
                        simpleGrantedAuthorities
                );
                SecurityContextHolder.getContext().setAuthentication(authentication);
            } else {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.addHeader(HttpHeaders.CONTENT_TYPE, "application/json");
                response.setCharacterEncoding("utf-8");
                response.getWriter().write(objectMapper.writeValueAsString(ResponseBodyVO.<String>builder()
                        .code(ResponseBodyCodeMessageEnum.LOGIN_BAD_PASSWORD.getCode())
                        .message(ResponseBodyCodeMessageEnum.LOGIN_BAD_PASSWORD.getMessage())
                        .build())
                );
                return;
            }
        } catch (JwtException jwtException) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.addHeader(HttpHeaders.CONTENT_TYPE, "application/json");
            response.setCharacterEncoding("utf-8");
            response.getWriter().write(objectMapper.writeValueAsString(ResponseBodyVO.<String>builder()
                    .code(ResponseBodyCodeMessageEnum.LOGIN_BAD_PASSWORD.getCode())
                    .message(ResponseBodyCodeMessageEnum.LOGIN_BAD_PASSWORD.getMessage())
                    .build())
            );
            return;
        }
        filterChain.doFilter(request, response);
    }
}
