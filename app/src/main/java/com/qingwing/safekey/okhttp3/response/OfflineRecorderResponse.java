package com.qingwing.safekey.okhttp3.response;


/*
={"order":"aaaa1100b8010103010900A90954205276","itid":11530,"errmsg":"","errcode":0}
order	指令信息
itid	获取离线记录原始id（用于返回指令执行结果）
  */
public class OfflineRecorderResponse extends BaseResponse {
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
