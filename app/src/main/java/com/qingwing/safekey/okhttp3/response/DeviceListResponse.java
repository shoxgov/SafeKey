package com.qingwing.safekey.okhttp3.response;


import java.util.List;

/*
"total": "100",
"device":[{
"devicetypeid": "1",
"devicetypename": "门锁",
"deviceid": "12",
"devicename": "唯智园区1栋1层0101"
}]
ut: SAFEKEY result={"total":"2","device":[{"location":",189,193,198,","devicename":"张家界学院2区-3栋-3层-2331前门","devicetypeid":"1","devicetypename":"门锁","deviceid":"1678"},{"location":",189,193,198,","devicena
  */
public class DeviceListResponse extends BaseResponse {
    private String total;

    private List<DeviceInfo> device;

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public List<DeviceInfo> getDevice() {
        return device;
    }

    public void setDevice(List<DeviceInfo> device) {
        this.device = device;
    }

    public class DeviceInfo {
        private String devicetypeid;
        private String devicetypename;
        private String deviceid;
        private String wcode;
        private String gatewaycode;
        private String devicename;
        private String btAddress;
        private boolean online;
        private boolean select;
        private int rssi;

        public boolean isSelect() {
            return select;
        }

        public void setSelect(boolean select) {
            this.select = select;
        }

        public boolean isOnline() {
            return online;
        }

        public void setOnline(boolean online) {
            this.online = online;
        }

        public String getBtAddress() {
            return btAddress;
        }

        public void setBtAddress(String btAddress) {
            this.btAddress = btAddress;
        }

        public String getDevicetypeid() {
            return devicetypeid;
        }

        public void setDevicetypeid(String devicetypeid) {
            this.devicetypeid = devicetypeid;
        }

        public String getDevicetypename() {
            return devicetypename;
        }

        public void setDevicetypename(String devicetypename) {
            this.devicetypename = devicetypename;
        }

        public String getDeviceid() {
            return deviceid;
        }

        public void setDeviceid(String deviceid) {
            this.deviceid = deviceid;
        }

        public int getRssi() {
            return rssi;
        }

        public void setRssi(int rssi) {
            this.rssi = rssi;
        }

        public String getDevicename() {
            return devicename;
        }

        public void setDevicename(String devicename) {
            this.devicename = devicename;
        }

        public String getWcode() {
            return wcode;
        }

        public void setWcode(String wcode) {
            this.wcode = wcode;
        }

        public String getGatewaycode() {
            return gatewaycode;
        }

        public void setGatewaycode(String gatewaycode) {
            this.gatewaycode = gatewaycode;
        }
    }
}
