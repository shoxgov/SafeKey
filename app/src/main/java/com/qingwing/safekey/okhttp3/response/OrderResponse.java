package com.qingwing.safekey.okhttp3.response;


import java.util.List;

/*
"order": {
		"finger": [{"finorder": "125362563226333","rfids": "1,2,3"}],
		"card": [{"cardorder": "125362563226333","rcids": "1,2,3"}]
	       }
  */
public class OrderResponse extends BaseResponse {
    private OrderInfo order;

    public OrderInfo getOrder() {
        return order;
    }

    public void setOrder(OrderInfo order) {
        this.order = order;
    }

    public class OrderInfo {
        private List<FingerOrder> finger;
        private List<CardOrder> card;

        public List<FingerOrder> getFinger() {
            return finger;
        }

        public void setFinger(List<FingerOrder> finger) {
            this.finger = finger;
        }

        public List<CardOrder> getCard() {
            return card;
        }

        public void setCard(List<CardOrder> card) {
            this.card = card;
        }
    }

    public class FingerOrder {
        private String finorder;
        private String rfids;

        public String getFinorder() {
            return finorder;
        }

        public void setFinorder(String finorder) {
            this.finorder = finorder;
        }

        public String getRfids() {
            return rfids;
        }

        public void setRfids(String rfids) {
            this.rfids = rfids;
        }
    }

    public class CardOrder {
        private String cardorder;
        private String rcids;

        public String getCardorder() {
            return cardorder;
        }

        public void setCardorder(String cardorder) {
            this.cardorder = cardorder;
        }

        public String getRcids() {
            return rcids;
        }

        public void setRcids(String rcids) {
            this.rcids = rcids;
        }
    }
}
