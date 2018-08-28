package com.qingwing.safekey.bean;

/**
 roombacktype	是	取消授权类型（1（密码）,2（指纹）,3（卡片））
 roombackid	否	授权id
 */

public class OfflineDelAuthoryRoomBack {
    private int roombacktype;
    private String roombackid;

    public int getRoombacktype() {
        return roombacktype;
    }

    public void setRoombacktype(int roombacktype) {
        this.roombacktype = roombacktype;
    }

    public String getRoombackid() {
        return roombackid;
    }

    public void setRoombackid(String roombackid) {
        this.roombackid = roombackid;
    }
}
