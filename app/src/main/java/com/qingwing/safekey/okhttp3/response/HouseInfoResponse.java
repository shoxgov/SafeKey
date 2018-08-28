package com.qingwing.safekey.okhttp3.response;


import java.util.List;

/*
"ld":[{
"agid": "1",
"agname": "1"
	 }]
  */
public class HouseInfoResponse extends BaseResponse {
    private List<HouseInfo> ld;

    public List<HouseInfo> getLd() {
        return ld;
    }

    public void setLd(List<HouseInfo> ld) {
        this.ld = ld;
    }

    public class HouseInfo {
        private String agid;
        private String agname;

        public String getAgid() {
            return agid;
        }

        public void setAgid(String agid) {
            this.agid = agid;
        }

        public String getAgname() {
            return agname;
        }

        public void setAgname(String agname) {
            this.agname = agname;
        }
    }
}
