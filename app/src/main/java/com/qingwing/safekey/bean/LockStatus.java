package com.qingwing.safekey.bean;

/**
 * Created by wangshengyin on 2018-08-10.
 * email:shoxgov@126.com
 */
public class LockStatus {
    private String addr;
    private String lockId;
    private String time;
    private int lockStatus;
    private int electricValue;
    private int studentOpenCardNum;
    private int managerOpenCardNum;
    private int otherOpenCardNum;
    private int authoryCardNum;
    private int recordNum;
    private String versonCode;
    private String lockSN;

    public String getAddr() {
        return addr;
    }

    public void setAddr(String addr) {
        this.addr = addr;
    }

    public String getLockId() {
        return lockId;
    }

    public void setLockId(String lockId) {
        this.lockId = lockId;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public int getLockStatus() {
        return lockStatus;
    }

    public void setLockStatus(int lockStatus) {
        this.lockStatus = lockStatus;
    }

    public int getElectricValue() {
        return electricValue;
    }

    public void setElectricValue(int electricValue) {
        this.electricValue = electricValue;
    }

    public int getStudentOpenCardNum() {
        return studentOpenCardNum;
    }

    public void setStudentOpenCardNum(int studentOpenCardNum) {
        this.studentOpenCardNum = studentOpenCardNum;
    }

    public int getManagerOpenCardNum() {
        return managerOpenCardNum;
    }

    public void setManagerOpenCardNum(int managerOpenCardNum) {
        this.managerOpenCardNum = managerOpenCardNum;
    }

    public int getOtherOpenCardNum() {
        return otherOpenCardNum;
    }

    public void setOtherOpenCardNum(int otherOpenCardNum) {
        this.otherOpenCardNum = otherOpenCardNum;
    }

    public int getAuthoryCardNum() {
        return authoryCardNum;
    }

    public void setAuthoryCardNum(int authoryCardNum) {
        this.authoryCardNum = authoryCardNum;
    }

    public int getRecordNum() {
        return recordNum;
    }

    public void setRecordNum(int recordNum) {
        this.recordNum = recordNum;
    }

    public String getVersonCode() {
        return versonCode;
    }

    public void setVersonCode(String versonCode) {
        this.versonCode = versonCode;
    }

    public String getLockSN() {
        return lockSN;
    }

    public void setLockSN(String lockSN) {
        this.lockSN = lockSN;
    }
}
