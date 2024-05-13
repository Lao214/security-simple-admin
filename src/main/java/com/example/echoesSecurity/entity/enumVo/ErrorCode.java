package com.example.echoesSecurity.entity.enumVo;

public enum ErrorCode {
    SYSTEM_ERROR(10000, "系统错误!"),
    UN_AUTH(10001, "用户未认证，请先登录！"),
    AUTH_FAILURE(10002, "认证失败，用户名或密码错误！"),
    UN_ACCESS(10003, "该用户没有此操作权限！"),
    METHOD_ARGS_VALID(10004, "方法参数验证失败！"),
    TOKEN_VALID(10005, "token鉴权失败！"),
    TOKEN_NOT_EXIST(10006, "token不存在！"),
    ;


    private Integer code;
    private String msg;

    ErrorCode(Integer code, String message) {
        this.code = code;
        this.msg = message;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}