package com.qingwing.safekey.okhttp3.http;


import android.text.TextUtils;

import com.qingwing.safekey.R;
import com.qingwing.safekey.okhttp3.HttpJsonAdapter;
import com.qingwing.safekey.okhttp3.response.BaseResponse;
import com.qingwing.safekey.utils.UIUtils;

/**
 * @Description 数据解析
 * @Author 一花一世界
 */

public class DataAnalysis {

    public enum JSON_TYPE {
        /**
         * JSONObject
         */
        JSON_OBJECT,
        /**
         * JSONArray
         */
        JSON_ARRAY,
        /**
         * 不是JSON格式的字符串
         */
        JSON_ERROR
    }

    /**
     * @param result 返回数据
     * @Description 获取result数据格式
     */
    private static JSON_TYPE getJSONType(String result) {
        if (TextUtils.isEmpty(result)) {
            return JSON_TYPE.JSON_ERROR;
        }

        final char[] strChar = result.substring(0, 1).toCharArray();
        final char firstChar = strChar[0];

        if (firstChar == '{') {
            return JSON_TYPE.JSON_OBJECT;
        } else if (firstChar == '[') {
            return JSON_TYPE.JSON_ARRAY;
        } else {
            return JSON_TYPE.JSON_ERROR;
        }
    }

    /**
     * @param result 请求返回字符串 result={"code":1,"message":"工资核算详情List","data":null}
     * @Description 返回数据解析
     */
    public static BaseResponse getReturnData(String result, Class baseResponseClass) {
        BaseResponse resultDesc = null;
        System.out.println("SAFEKEY result=" + result);
        try {
            if (TextUtils.isEmpty(result)) {
                //返回数据为空
//                resultDesc = dataRestructuring(-1, UIUtils.getString(R.string.back_abnormal_results));
                String error = "{\"code\":-1,\"message\":\"数据返回为空\"}";
                resultDesc = (BaseResponse) HttpJsonAdapter.getInstance().get(error, baseResponseClass);
                return resultDesc;
            }
            resultDesc = (BaseResponse) HttpJsonAdapter.getInstance().get(result, baseResponseClass);
        } catch (Exception e) {
            e.printStackTrace();
            String error = "{\"code\":-1,\"message\":\"数据解析异常\"}";
            try {
                resultDesc = (BaseResponse) HttpJsonAdapter.getInstance().get(error, baseResponseClass);
            } catch (Exception e2) {
                resultDesc = dataRestructuring(-1, UIUtils.getString(R.string.back_parse_exception));
            }
        }
        return resultDesc;
    }

    /**
     * @param code    返回码
     * @param message 返回数据
     * @Description 数据重组
     */
    private static BaseResponse dataRestructuring(int code, String message) {
        BaseResponse resultDesc = new BaseResponse();
        resultDesc.setErrCode(code);
        resultDesc.setErrMsg(message);
        return resultDesc;
    }
}
