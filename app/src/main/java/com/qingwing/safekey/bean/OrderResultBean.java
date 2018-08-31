package com.qingwing.safekey.bean;


import com.qingwing.safekey.okhttp3.response.BaseResponse;

import java.util.List;

/*
"order": {
		"finger": [{finorderresult "finorder": "125362563226333","rfids": "1,2,3"}],
		"card": [{cardorderresult "cardorder": "125362563226333","rcids": "1,2,3"}]
	       }
  */
public class OrderResultBean extends BaseResponse {
    /**
     * 0:密码；
     * 1：指纹；
     * 2：卡片;
     */
    private int type;
    private String order;
    private String orderresult;
    private String ids;

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getOrder() {
        return order;
    }

    public void setOrder(String order) {
        this.order = order;
    }

    public String getOrderresult() {
        return orderresult;
    }

    public void setOrderresult(String orderresult) {
        this.orderresult = orderresult;
    }

    public String getIds() {
        return ids;
    }

    public void setIds(String ids) {
        this.ids = ids;
    }
}
