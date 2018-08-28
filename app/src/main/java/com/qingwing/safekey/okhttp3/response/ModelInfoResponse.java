package com.qingwing.safekey.okhttp3.response;


import java.util.List;

/*
"devicetype":[{
"deviceid": "1",
"devicename": "门锁"
	 }]
  */
public class ModelInfoResponse extends BaseResponse {
    private List<DevoceInfo> devicetype;

    public List<DevoceInfo> getDevicetype() {
        return devicetype;
    }

    public void setDevicetype(List<DevoceInfo> devicetype) {
        this.devicetype = devicetype;
    }

    public class DevoceInfo {
        private String deviceid;
        private String devicename;

        public String getDeviceid() {
            return deviceid;
        }

        public void setDeviceid(String deviceid) {
            this.deviceid = deviceid;
        }

        public String getDevicename() {
            return devicename;
        }

        public void setDevicename(String devicename) {
            this.devicename = devicename;
        }
    }
}
