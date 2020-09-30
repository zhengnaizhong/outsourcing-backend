package com.cw.outsourcing.outsourcingbackend.controller;

import com.cw.outsourcing.outsourcingbackend.constant.ResponseBodyCodeMessageEnum;
import com.cw.outsourcing.outsourcingbackend.pojo.vo.LoginVO;
import com.cw.outsourcing.outsourcingbackend.pojo.vo.ResponseBodyVO;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

    @PostMapping("test")
    //@PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    public ResponseEntity<ResponseBodyVO<String>> test(@RequestBody LoginVO loginVO) {
        return ResponseEntity.ok(ResponseBodyVO.<String>builder()
                .code(ResponseBodyCodeMessageEnum.SUCCESS.getCode())
                .message(ResponseBodyCodeMessageEnum.SUCCESS.getMessage())
                .build());
    }

}
