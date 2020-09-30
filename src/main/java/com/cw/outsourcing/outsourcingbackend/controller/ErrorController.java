package com.cw.outsourcing.outsourcingbackend.controller;

import com.cw.outsourcing.outsourcingbackend.constant.ResponseBodyCodeMessageEnum;
import com.cw.outsourcing.outsourcingbackend.pojo.vo.ResponseBodyVO;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@RestControllerAdvice
public class ErrorController {

    private final Logger logger = LoggerFactory.getLogger(ErrorController.class);

    private final ObjectMapper objectMapper;

    public ErrorController(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @ExceptionHandler
    public ResponseEntity<ResponseBodyVO<String>> errorHandler(HttpServletRequest httpServletRequest,
                                                               Exception exception) throws IOException {
        logger.warn("捕获异常，请求路径：{}，路径参数：{}，body参数：{}，异常描述：{}，堆栈信息：",
                httpServletRequest.getRequestURI(),
                objectMapper.writeValueAsString(httpServletRequest.getParameterMap()),
                httpServletRequest.getInputStream(),
                exception.getMessage(), exception);
        if (exception instanceof HttpRequestMethodNotSupportedException) {
            return ResponseEntity
                    .status(HttpStatus.METHOD_NOT_ALLOWED)
                    .body(ResponseBodyVO.<String>builder()
                            .code(ResponseBodyCodeMessageEnum.NOT_SUPPORTED_METHOD.getCode())
                            .message(ResponseBodyCodeMessageEnum.NOT_SUPPORTED_METHOD.getMessage())
                            .build());
        }
        if (exception instanceof HttpMessageNotReadableException
                || exception instanceof MissingServletRequestParameterException) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(ResponseBodyVO.<String>builder()
                            .code(ResponseBodyCodeMessageEnum.MISSING_PARAMETER.getCode())
                            .message(ResponseBodyCodeMessageEnum.MISSING_PARAMETER.getMessage())
                            .build());
        }
        if (exception instanceof AccessDeniedException) {
            return ResponseEntity
                    .status(HttpStatus.FORBIDDEN)
                    .body(ResponseBodyVO.<String>builder()
                            .code(ResponseBodyCodeMessageEnum.ACCESS_DENIED.getCode())
                            .message(ResponseBodyCodeMessageEnum.ACCESS_DENIED.getMessage())
                            .build());
        }
        if (exception instanceof NoHandlerFoundException) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(ResponseBodyVO.<String>builder()
                            .code(ResponseBodyCodeMessageEnum.NOT_FOUND.getCode())
                            .message(ResponseBodyCodeMessageEnum.NOT_FOUND.getMessage())
                            .build());
        }
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ResponseBodyVO.<String>builder()
                        .code(ResponseBodyCodeMessageEnum.INTERNAL_SERVER.getCode())
                        .message(ResponseBodyCodeMessageEnum.INTERNAL_SERVER.getMessage())
                        .build());
    }
}
