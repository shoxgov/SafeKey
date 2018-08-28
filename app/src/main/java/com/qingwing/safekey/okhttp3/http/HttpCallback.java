package com.qingwing.safekey.okhttp3.http;

import android.graphics.Bitmap;

import com.qingwing.safekey.okhttp3.response.BaseResponse;


/**
 */

public class HttpCallback {

    /**
     * @param br 返回数据
     * @Description 请求成功时回调
     */
    public void onSuccess(BaseResponse br) {
    }

    /**
     * @param code    状态码
     * @param message 状态消息
     * @Description 请求失败时回调
     */
    public void onFailure(int code, String message) {
        System.out.println("BENBEN HttpCallback onFailure  code=" + code + ";message=" + message);
    }

    /**
     * @param currentTotalLen 进度
     * @param totalLen        总量
     * @Description 上传或下载时进度回调
     */
    public void onProgress(long currentTotalLen, long totalLen) {
        System.out.println("BENBEN HttpCallback onProgress  currentTotalLen=" + currentTotalLen + ";totalLen=" + totalLen);
    }

    /**
     * @param bitmap 图片bitmap
     * @Description 显示图片成功回调
     */
    public void onBitmapSuccess(Bitmap bitmap) {
    }
}
