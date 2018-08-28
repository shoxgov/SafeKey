package com.qingwing.safekey.okhttp3.response;


/*
order	指令信息
itid	远程开门原始id（用于返回指令执行结果）
  */
public class OfflineUnlockResponse extends BaseResponse {
    private String order;
    private String itid;

    public String getOrder() {
        return order;
    }

    public void setOrder(String order) {
        this.order = order;
    }

    public String getItid() {
        return itid;
    }

    public void setItid(String itid) {
        this.itid = itid;
    }
}
