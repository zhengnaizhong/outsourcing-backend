package com.cw.outsourcing.outsourcingbackend.pojo.vo;

import lombok.Builder;

@Builder
public class BodyVO<T> {

    private final Integer code;

    private final T data;
}
