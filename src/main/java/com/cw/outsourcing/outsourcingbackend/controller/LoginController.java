package com.cw.outsourcing.outsourcingbackend.controller;

import com.cw.outsourcing.outsourcingbackend.pojo.vo.LoginVO;
import com.cw.outsourcing.outsourcingbackend.pojo.vo.ResponseBodyVO;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LoginController {

    private final AuthenticationManager authenticationManager;

    public LoginController(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }

    @PostMapping("login")
    public ResponseEntity<ResponseBodyVO<String>> login(@RequestBody LoginVO loginVO) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginVO.getUserName(), loginVO.getPassword()));
        return ResponseEntity.ok(ResponseBodyVO.<String>builder().code("00000").message("成功").data("dasdasda").build());
    }
}
