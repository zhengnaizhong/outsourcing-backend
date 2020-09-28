package com.cw.outsourcing.outsourcingbackend.pojo.vo;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class ResponseBodyVO<T> {

    private final String code;

    private final String message;

    private final T data;
}
