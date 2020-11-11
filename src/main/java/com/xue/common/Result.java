package com.xue.common;

public enum Result {
    Success(0, "成功"),
    Fail(-1, "失败");

    private Integer code;
    private String msg;

    Result(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
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
