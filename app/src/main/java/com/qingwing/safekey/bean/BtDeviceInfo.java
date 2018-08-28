package com.qingwing.safekey.bean;

/**
 * Created by wangshengyin on 2018-08-10.
 * email:shoxgov@126.com
 */

public class BtDeviceInfo {
    private int rssi;
    private String address;
    private String name;

    public int getRssi() {
        return rssi;
    }

    public void setRssi(int rssi) {
        this.rssi = rssi;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
