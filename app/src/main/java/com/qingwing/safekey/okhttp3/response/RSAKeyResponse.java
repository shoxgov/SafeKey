package com.qingwing.safekey.okhttp3.response;


/*
publicKeyExponent	公钥模
publicKeyModulus	公钥模
privateKeyid	私钥id
  */
public class RSAKeyResponse extends BaseResponse {
    private String publicKeyExponent;
    private String publicKeyModulus;
    private String privateKeyid;

    public String getPublicKeyExponent() {
        return publicKeyExponent;
    }

    public void setPublicKeyExponent(String publicKeyExponent) {
        this.publicKeyExponent = publicKeyExponent;
    }

    public String getPublicKeyModulus() {
        return publicKeyModulus;
    }

    public void setPublicKeyModulus(String publicKeyModulus) {
        this.publicKeyModulus = publicKeyModulus;
    }

    public String getPrivateKeyid() {
        return privateKeyid;
    }

    public void setPrivateKeyid(String privateKeyid) {
        this.privateKeyid = privateKeyid;
    }
}
