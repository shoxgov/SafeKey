package com.qingwing.safekey.okhttp3;

/**
 * 自定义异常类
 * 
 * @author QiFeng 2010-8-27
 */
public class BizException extends Exception {
    private static final long serialVersionUID = -7294511800719769763L;

    public BizException() {
        super();
    }

    public BizException(String detailMessage, Throwable throwable) {
        super(detailMessage, throwable);
    }

    public BizException(String detailMessage) {
        super(detailMessage);
    }

    public BizException(Throwable throwable) {
        super(throwable);
    }
}
