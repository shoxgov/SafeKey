package com.qingwing.safekey.okhttp3.response;


import java.util.List;

/*
"total": "100",
"record":[{
"recordtype": "刷卡开门",
"recorddate": "180813 14:38:22",
"username": "张三"
}]
  */
public class DeviceHistoryListResponse extends BaseResponse {
    private String total;

    private List<DeviceHistoryInfo> record;

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public List<DeviceHistoryInfo> getRecord() {
        return record;
    }

    public void setRecord(List<DeviceHistoryInfo> record) {
        this.record = record;
    }

    public class DeviceHistoryInfo {
        private String recordtype;
        private String recorddate;
        private String username;

        public String getRecordtype() {
            return recordtype;
        }

        public void setRecordtype(String recordtype) {
            this.recordtype = recordtype;
        }

        public String getRecorddate() {
            return recorddate;
        }

        public void setRecorddate(String recorddate) {
            this.recorddate = recorddate;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }
    }
}
