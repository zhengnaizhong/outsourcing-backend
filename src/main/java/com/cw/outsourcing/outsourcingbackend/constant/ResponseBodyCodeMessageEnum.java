package com.cw.outsourcing.outsourcingbackend.constant;

public enum ResponseBodyCodeMessageEnum {

    SUCCESS("00000", "成功"),
    LOGIN_BAD_PASSWORD("A0210", "认证信息错误"),
    LOGIN_DISABLED("A0202", "用户账户被冻结"),
    NOT_SUPPORTED_METHOD("A0405", "不支持的请求方式"),
    INTERNAL_SERVER("B0001", "系统执行出错"),
    MISSING_PARAMETER("A0400", "用户请求参数错误 "),
    ACCESS_DENIED("A0301", "访问未授权"),
    NOT_FOUND("A0404", "不存在的资源");

    private String code;

    private String message;

    ResponseBodyCodeMessageEnum(String code, String message) {
        this.code = code;
        this.message = message;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
