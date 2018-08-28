package com.qingwing.safekey.okhttp3.response;


import java.util.List;

/*
"total": "100",
"roomcard":[{
"sqtype": "1",
"sqid": "1",
"personname": "张三",
"personcode": "20180001",
"sqcode": "0a2d0e0f",
"sqdate": "180814 05:25:11"
}]
  */
public class OfflineDelAuthoryUserInfoResponse extends BaseResponse {
    private String total;
    private List<DelAuthoryUserInfo> roomcard;

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public List<DelAuthoryUserInfo> getRoomcard() {
        return roomcard;
    }

    public void setRoomcard(List<DelAuthoryUserInfo> roomcard) {
        this.roomcard = roomcard;
    }

    public class DelAuthoryUserInfo{
        private String personcode;
        private String personname;
        private String sqcode;
        private String sqdate;

        public String getPersoncode() {
            return personcode;
        }

        public void setPersoncode(String personcode) {
            this.personcode = personcode;
        }

        public String getPersonname() {
            return personname;
        }

        public void setPersonname(String personname) {
            this.personname = personname;
        }

        public String getSqcode() {
            return sqcode;
        }

        public void setSqcode(String sqcode) {
            this.sqcode = sqcode;
        }

        public String getSqdate() {
            return sqdate;
        }

        public void setSqdate(String sqdate) {
            this.sqdate = sqdate;
        }
    }
}
