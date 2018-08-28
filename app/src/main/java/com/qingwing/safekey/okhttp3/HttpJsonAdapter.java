package com.qingwing.safekey.okhttp3;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;

public class HttpJsonAdapter {

    private static HttpJsonAdapter instance = null;

    private HttpJsonAdapter() {
    }

    public static synchronized HttpJsonAdapter getInstance() {
        if (instance == null) {
            instance = new HttpJsonAdapter();
        }
        return instance;
    }

    public <T> T get(String str, Class<T> clazz) throws BizException {

        Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();

        try {
            return (T) gson.fromJson(str, clazz);
        } catch (JsonSyntaxException ee) {
            ee.printStackTrace();
            throw new BizException("数据处理异常", ee);
        } catch (Exception e) {
            e.printStackTrace();
            throw new BizException("数据处理异常", e);
        }
    }

    public String getJsonString(Object object) {
        Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
        String str = gson.toJson(object);
        return str;
    }

}
