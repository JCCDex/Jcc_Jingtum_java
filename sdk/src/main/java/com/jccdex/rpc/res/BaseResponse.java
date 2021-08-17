package com.jccdex.rpc.res;

public class BaseResponse {

    private String code;
    private String msg;

    public void setCode(String code) {
        this.code = code;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }

    public boolean isSuccess() {
        String successCode = "0";
        return successCode.equals(code);
    }

}
