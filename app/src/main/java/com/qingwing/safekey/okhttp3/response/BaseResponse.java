package com.qingwing.safekey.okhttp3.response;

import java.io.Serializable;

public class BaseResponse implements Serializable {
    private static final long serialVersionUID = 1L;
    private int errCode;//code值含义:* 0表示正常* 其它表示错误
    private String errMsg;

    public int getErrCode() {
        return errCode;
    }

    public void setErrCode(int errCode) {
        this.errCode = errCode;
    }

    public String getErrMsg() {
        return errMsg;
    }

    public void setErrMsg(String errMsg) {
        this.errMsg = errMsg;
    }
}
