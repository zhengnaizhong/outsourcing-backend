package com.cw.outsourcing.outsourcingbackend.config;

import com.cw.outsourcing.outsourcingbackend.filter.JwtTokenVerifier;
import com.cw.outsourcing.outsourcingbackend.filter.JwtUsernameAndPasswordAuthenticationFilter;
import com.cw.outsourcing.outsourcingbackend.service.impl.MyUserDetailsService;
import com.cw.outsourcing.outsourcingbackend.util.RedisUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import javax.crypto.SecretKey;

@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final RedisUtil redisUtil;

    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    private final MyUserDetailsService myUserDetailsService;

    private final SecretKey secretKey;

    private final JwtConfig jwtConfig;

    private final ObjectMapper objectMapper;

    public SecurityConfig(MyUserDetailsService myUserDetailsService,
                          BCryptPasswordEncoder bCryptPasswordEncoder, SecretKey secretKey,
                          JwtConfig jwtConfig, ObjectMapper objectMapper, RedisUtil redisUtil) {
        this.myUserDetailsService = myUserDetailsService;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.secretKey = secretKey;
        this.jwtConfig = jwtConfig;
        this.objectMapper = objectMapper;
        this.redisUtil = redisUtil;
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(myUserDetailsService).passwordEncoder(bCryptPasswordEncoder);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .addFilter(new JwtUsernameAndPasswordAuthenticationFilter(authenticationManager(), jwtConfig, secretKey, objectMapper, redisUtil))
                .addFilterAfter(new JwtTokenVerifier(secretKey, jwtConfig, redisUtil),JwtUsernameAndPasswordAuthenticationFilter.class)
                .authorizeRequests()
                .anyRequest().authenticated()
                .and().sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
    }

}
