package com.qingwing.safekey.okhttp3.http;

import android.graphics.Bitmap;
import android.text.TextUtils;

import com.qingwing.safekey.NetWorkConfig;
import com.qingwing.safekey.R;
import com.qingwing.safekey.okhttp3.response.BaseResponse;
import com.qingwing.safekey.utils.LogUtil;
import com.qingwing.safekey.utils.ToastUtil;
import com.qingwing.safekey.utils.UIUtils;
import com.qingwing.safekey.okhttp3.http.OkHttpRequest.HttpMethodType;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;

/**
 * @Description OkHttp3工具类
 * @Author 一花一世界
 */
public class OkHttpUtils {

    private static OkHttpUtils mInstance;
    private static OkHttpClient mOkHttpClient;
    private static Platform mPlatform;

    private OkHttpUtils(OkHttpClient okHttpClient) {
        if (okHttpClient == null) {
            mOkHttpClient = new OkHttpClient();
        } else {
            mOkHttpClient = okHttpClient;
        }
        mPlatform = Platform.get();
    }

    public static OkHttpUtils initClient(OkHttpClient okHttpClient) {
        if (mInstance == null) {
            synchronized (OkHttpUtils.class) {
                if (mInstance == null) {
                    mInstance = new OkHttpUtils(okHttpClient);
                }
            }
        }
        return mInstance;
    }

    public static OkHttpUtils getInstance() {
        return initClient(null);
    }

    public OkHttpClient getOkHttpClient() {
        return mOkHttpClient;
    }

    public static void sendFailResultCallback(final int code, final String message, final HttpCallback callback) {
        if (callback == null) {
            return;
        }
        mPlatform.execute(new Runnable() {
            @Override
            public void run() {
                callback.onFailure(code, message);
            }
        });
    }

    public static void sendSuccessResultCallback(final BaseResponse result, final HttpCallback callback) {
        if (callback == null) {
            return;
        }
        mPlatform.execute(new Runnable() {
            @Override
            public void run() {
                callback.onSuccess(result);
            }
        });
    }

    public static void sendProgressResultCallback(final long currentTotalLen, final long totalLen, final HttpCallback callback) {
        if (callback == null) {
            return;
        }
        mPlatform.execute(new Runnable() {
            @Override
            public void run() {
                callback.onProgress(currentTotalLen, totalLen);
            }
        });
    }

    public static void sendBitmapSuccessResultCallback(final Bitmap bitmap, final HttpCallback callback) {
        if (callback == null) {
            return;
        }
        mPlatform.execute(new Runnable() {
            @Override
            public void run() {
                callback.onBitmapSuccess(bitmap);
            }
        });
    }

    /**--------------------    同步数据请求    --------------------**/

    /**
     * @param url      请求地址
     * @param callback 请求回调
     * @Description GET请求
     */
    public static void getSync(String url, HttpCallback callback) {
        LogUtil.d("getSync url=" + url);
        Request request = OkHttpRequest.builderRequest(HttpMethodType.GET, NetWorkConfig.HTTPS + url, null, null);
        OkHttpRequest.doExecute(request, callback);
    }

    /**
     * @param url      请求地址
     * @param params   请求参数
     * @param callback 请求回调
     * @Description GET请求
     */
    public static void getSync(String url, Map<String, String> params, Class baseResponseClass, HttpCallback callback) {
        if (params != null && !params.isEmpty()) {
            url = OkHttpRequest.appendGetParams(NetWorkConfig.HTTPS + url, params);
        }
        LogUtil.d("getSync url=" + url);
        Request request = OkHttpRequest.builderRequest(HttpMethodType.GET, url, null, null);
        OkHttpRequest.doExecute(request, baseResponseClass, callback);
    }

    /**
     * @param url      请求地址
     * @param params   请求参数
     * @param callback 请求回调
     * @Description POST请求
     */
    public static void postSync(String url, Map<String, String> params, HttpCallback callback) {
        LogUtil.d("postSync url=" + url);
        Request request = OkHttpRequest.builderRequest(OkHttpRequest.HttpMethodType.POST, NetWorkConfig.HTTPS + url, params, null);
        OkHttpRequest.doExecute(request, callback);
    }

    /**--------------------    异步数据请求    --------------------**/

    /**
     * @param url      请求地址
     * @param callback 请求回调
     * @Description GET请求
     */
    public static void getAsyn(String url, HttpCallback callback) {
        Map<String, String> params = new HashMap<>();
        LogUtil.d("getAsyn url=" + url);
        Request request = OkHttpRequest.builderRequest(HttpMethodType.GET, NetWorkConfig.HTTPS + url, null, null);
        OkHttpRequest.doEnqueue(request, callback);
    }

    public static void getAsyn(String url, Class baseResponseClass, HttpCallback callback) {
        Map<String, String> params = new HashMap<>();
        LogUtil.d("getAsyn url=" + url);
        Request request = OkHttpRequest.builderRequest(HttpMethodType.GET, NetWorkConfig.HTTPS + url, null, null);
        OkHttpRequest.doEnqueue(request, baseResponseClass, callback);
    }

    /**
     * @param url      请求地址
     * @param params   请求参数
     * @param callback 请求回调
     * @Description GET请求
     */
    public static void getAsyn(String url, Map<String, String> params, Class baseResponseClass, HttpCallback callback) {
        if (params == null) {
            params = new HashMap<>();
        }
        url = NetWorkConfig.HTTPS + url;
        url = OkHttpRequest.appendGetParams(url, params);
        LogUtil.d("getAsyn url=" + url);
        Request request = OkHttpRequest.builderRequest(HttpMethodType.GET, url, null, null);
        OkHttpRequest.doEnqueue(request, baseResponseClass, callback);
    }

    public static void getAsyn(String url, Map<String, String> params, HttpCallback callback) {
        if (params == null) {
            params = new HashMap<>();
        }
        url = OkHttpRequest.appendGetParams(NetWorkConfig.HTTPS + url, params);
        LogUtil.d("getAsyn url=" + url);
        Request request = OkHttpRequest.builderRequest(HttpMethodType.GET, url, null, null);
        OkHttpRequest.doEnqueue(request, callback);
    }

    /**
     * @param url      请求地址
     * @param params   请求参数
     * @param callback 请求回调
     * @Description POST请求
     */
    public static void postAsyn(String url, Map<String, String> params, HttpCallback callback) {
        postAsyn(url, params, BaseResponse.class, callback);
    }

    public static void postAsyn(String url, Map<String, String> params, Class baseResponseClass, HttpCallback callback) {
        if (params == null) {
            params = new HashMap<>();
        }
        url = NetWorkConfig.HTTPS + url;
        LogUtil.d("postAsyn url=" + url);
        Request request = OkHttpRequest.builderRequest(HttpMethodType.POST, url, params, null);
        OkHttpRequest.doEnqueue(request, baseResponseClass, callback);
    }

    /**
     * @param url      请求地址
     * @param json     json数据格式
     * @param callback 请求回调
     * @Description POST提交JSON数据
     */
    public static void postAync(String url, String json, HttpCallback callback) {
        LogUtil.d("postAync url=" + NetWorkConfig.HTTPS + url);
        Request request = OkHttpRequest.builderRequest(OkHttpRequest.HttpMethodType.POST, NetWorkConfig.HTTPS + url, null, json);
        OkHttpRequest.doEnqueue(request, callback);
    }

    /**--------------------    文件上传    --------------------**/

    /**
     * @param url      请求地址
     * @param file     上传文件
     * @param callback 请求回调
     * @Description 单文件上传
     */
    public static void postAsynFile(String url, File file, HttpCallback callback) {
        if (!file.exists()) {
            ToastUtil.showText(UIUtils.getString(R.string.file_does_not_exist));
            return;
        }
        LogUtil.d("postAsynFile url=" + NetWorkConfig.HTTPS + url);
        Request request = OkHttpRequest.builderFileRequest(NetWorkConfig.HTTPS + url, file, null, null, null, callback);
        OkHttpRequest.doEnqueue(request, callback);
    }

    /**
     * @param url      请求地址
     * @param pic_key  上传图片关键字（约定pic_key如“upload”作为后台接受多张图片的key）
     * @param file     上传文件集合
     * @param params   请求参数
     * @param callback 请求回调
     * @Description 多文件上传
     */
    public static void postAsynFile(String url, String pic_key, File file, Map<String, String> params, Class baseResponseClass, HttpCallback callback) {
        if (params == null) {
            params = new HashMap<>();
        }
        LogUtil.d("postAsynFile url=" + NetWorkConfig.HTTPS + url);
        Request request = OkHttpRequest.builderFileRequest(NetWorkConfig.HTTPS + url, file, pic_key, null, params, callback);
        OkHttpRequest.doEnqueue(request, baseResponseClass, callback);
    }

    public static void postAsynFiles(String url, String pic_key, List<File> files, Map<String, String> params, Class baseResponseClass, HttpCallback callback) {
        if (params == null) {
            params = new HashMap<>();
        }
        LogUtil.d("postAsynFiles url=" + NetWorkConfig.HTTPS + url);
        Request request = OkHttpRequest.builderFileRequest(NetWorkConfig.HTTPS + url, null, pic_key, files, params, callback);
        OkHttpRequest.doEnqueue(request, baseResponseClass, callback);
    }

    public static void postAsynFiles(String url, String pic_key, List<File> files, Map<String, String> params, HttpCallback callback) {
        if (params == null) {
            params = new HashMap<>();
        }
        LogUtil.d("postAsynFiles url=" + NetWorkConfig.HTTPS + url);
        Request request = OkHttpRequest.builderFileRequest(NetWorkConfig.HTTPS + url, null, pic_key, files, params, callback);
        OkHttpRequest.doEnqueue(request, callback);
    }

    /**--------------------    文件下载    --------------------**/

    /**
     * @param url          请求地址
     * @param destFileDir  目标文件存储的文件夹路径，如：Environment.getExternalStorageDirectory().getAbsolutePath()
     * @param destFileName 目标文件存储的文件名，如：gson-2.7.jar
     * @param callback     请求回调
     * @Description 文件下载
     */
    public void downloadAsynFile(String url, String destFileDir, String destFileName, HttpCallback callback) {
        LogUtil.d("downloadAsynFile url=" + NetWorkConfig.HTTPS + url);
        Request request = OkHttpRequest.builderRequest(OkHttpRequest.HttpMethodType.GET, NetWorkConfig.HTTPS + url, null, null);
        OkHttpRequest.doDownloadEnqueue(request, destFileDir, destFileName, callback);
    }

    /**
     * @param url          请求地址
     * @param destFileDir  目标文件存储的文件夹路径
     * @param destFileName 目标文件存储的文件名
     * @param params       请求参数
     * @param callback     请求回调
     * @Description 文件下载
     */
    public void downloadAsynFile(String url, String destFileDir, String destFileName, Map<String, String> params, HttpCallback callback) {
        LogUtil.d("downloadAsynFile url=" + NetWorkConfig.HTTPS + url);
        Request request = OkHttpRequest.builderRequest(HttpMethodType.POST, NetWorkConfig.HTTPS + url, params, null);
        OkHttpRequest.doDownloadEnqueue(request, destFileDir, destFileName, callback);
    }

    /**--------------------    图片显示    --------------------**/

    /**
     * @param url      请求地址
     * @param callback 请求回调
     * @Description 图片显示
     */
    public static void displayAsynImage(String url, HttpCallback callback) {
        LogUtil.d("displayAsynImage url=" + url);
        Request request = OkHttpRequest.builderRequest(OkHttpRequest.HttpMethodType.GET, NetWorkConfig.HTTPS + url, null, null);
        OkHttpRequest.doDisplayEnqueue(request, callback);
    }

    /**--------------------    流式提交    --------------------**/

    /**
     * @param url      请求地址
     * @param content  提交内容
     * @param callback 请求回调
     * @Description 使用流的方式提交POST请求
     */
    public static void postAsynStream(String url, String content, HttpCallback callback) {
        LogUtil.d("postAsynStream url=" + url);
        Request request = OkHttpRequest.builderStreamRequest(NetWorkConfig.HTTPS + url, content);
        OkHttpRequest.doEnqueue(request, callback);
    }

    /**--------------------    Websocket    --------------------**/

    /**
     * @param url 请求地址
     * @Description WebSocket协议首先会发起http请求，握手成功后，转换协议保持长连接，类似心跳
     */
    public static void websocket(String url) {
        LogUtil.d("websocket url=" + url);
        Request request = OkHttpRequest.builderRequest(OkHttpRequest.HttpMethodType.GET, NetWorkConfig.HTTPS + url, null, null);
        OkHttpRequest.doNewWebSocket(request);
    }

    /**
     * @param tag 请求标签
     * @Description 取消请求
     */
    public static void cancelTag(Object tag) {
        if (tag == null) {
            return;
        }
        synchronized (mOkHttpClient.dispatcher().getClass()) {
            for (Call call : mOkHttpClient.dispatcher().queuedCalls()) {
                if (tag.equals(call.request().tag())) {
                    call.cancel();
                }
            }
            for (Call call : mOkHttpClient.dispatcher().runningCalls()) {
                if (tag.equals(call.request().tag())) {
                    call.cancel();
                }
            }
        }
    }
}
