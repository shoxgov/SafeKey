package com.qingwing.safekey.bean;

/**
 "roomcardtype": 1,
 "personcode": "20180001",
 "password": "123456",
 "sdate": "00:00",
 "edate": "23:59",
 "rccount": "10"
 */

public class OfflineAuthoryUserInfo {
    private int roomcardtype;
    private String personcode;
    private String password;
    private String sdate;
    private String edate;
    private String rccount;

    public int getRoomcardtype() {
        return roomcardtype;
    }

    public void setRoomcardtype(int roomcardtype) {
        this.roomcardtype = roomcardtype;
    }

    public String getPersoncode() {
        return personcode;
    }

    public void setPersoncode(String personcode) {
        this.personcode = personcode;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getSdate() {
        return sdate;
    }

    public void setSdate(String sdate) {
        this.sdate = sdate;
    }

    public String getEdate() {
        return edate;
    }

    public void setEdate(String edate) {
        this.edate = edate;
    }

    public String getRccount() {
        return rccount;
    }

    public void setRccount(String rccount) {
        this.rccount = rccount;
    }
}
