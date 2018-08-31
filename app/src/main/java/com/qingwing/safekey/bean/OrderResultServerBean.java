package com.qingwing.safekey.bean;


import java.util.List;

/*
{
"token": "5d2sf1s2df25ds52f1g2d",
"roomid": 1,
"orderresult": {
		"fingerresult": [{"finorderresult": "125362563226333","rfids": "1,2,3"}],
		"cardresult": [{"cardorderresult": "125362563226333","rcids": "1,2,3"}]
	      }
}
  */

public class OrderResultServerBean {
    private String fingerresult;
    private String cardresult;

    public String getFingerresult() {
        return fingerresult;
    }

    public void setFingerresult(String fingerresult) {
        this.fingerresult = fingerresult;
    }

    public String getCardresult() {
        return cardresult;
    }

    public void setCardresult(String cardresult) {
        this.cardresult = cardresult;
    }

    //    private List<FingerOrderResult> fingerresult;
//    private List<CardOrderResult> cardresult;
//
//    public List<FingerOrderResult> getFingerresult() {
//        return fingerresult;
//    }
//
//    public void setFingerresult(List<FingerOrderResult> fingerresult) {
//        this.fingerresult = fingerresult;
//    }
//
//    public List<CardOrderResult> getCardresult() {
//        return cardresult;
//    }
//
//    public void setCardresult(List<CardOrderResult> cardresult) {
//        this.cardresult = cardresult;
//    }

}
