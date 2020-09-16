package com.cw.outsourcing.outsourcingbackend.controller;

import com.cw.outsourcing.outsourcingbackend.pojo.vo.BodyVO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("hello")
public class TestController {

    @GetMapping("world")
    public ResponseEntity<BodyVO<String>> helloWorld() {
        return ResponseEntity.ok(BodyVO.<String>builder().code(40001).data("Hello World!").build());
    }
}
