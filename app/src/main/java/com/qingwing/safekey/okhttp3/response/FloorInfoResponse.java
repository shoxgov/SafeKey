package com.qingwing.safekey.okhttp3.response;


import java.util.List;

/*
"lc":[{
"agid": "1",
"agname": "1"
	 }]
  */
public class FloorInfoResponse extends BaseResponse {
    private List<FloorInfo> lc;

    public List<FloorInfo> getLc() {
        return lc;
    }

    public void setLc(List<FloorInfo> lc) {
        this.lc = lc;
    }

    public class FloorInfo {
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
