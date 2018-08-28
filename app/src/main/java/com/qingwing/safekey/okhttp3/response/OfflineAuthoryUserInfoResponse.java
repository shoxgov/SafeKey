package com.qingwing.safekey.okhttp3.response;


import java.util.List;

/*
"total": "100",
"person":[{
"personcode": "20180001",
"personname": "张三"
}]
  */
public class OfflineAuthoryUserInfoResponse extends BaseResponse {
    private String total;
    private List<AuthoryUserInfo> person;

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public List<AuthoryUserInfo> getPerson() {
        return person;
    }

    public void setPerson(List<AuthoryUserInfo> person) {
        this.person = person;
    }

    public class AuthoryUserInfo{
        private String personcode;
        private String personname;

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
    }
}
