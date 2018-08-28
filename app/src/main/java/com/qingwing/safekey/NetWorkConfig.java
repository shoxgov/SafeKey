package com.qingwing.safekey;

public class NetWorkConfig {
    public static String IP = "s.keenzy.cn";
    public static String PORT = "";
    public static String HTTP;
    public static String HTTPS;

    public static int networkEnv = 1;// 0 现场 1 现网测试 2 内网测试环境公网

//    static {
//        switch (networkEnv) {
//            default:
//            case 0:
//                HTTP = "http://www.ltx360.cn";
//                HTTPS = "https://www.ltx360.cn";
//                break;
//            case 1:
//                HTTP = "http://" + IP + ":" + PORT;
//                HTTPS = "https://" + IP + ":" + PORT;
//                break;
//            case 2:
//                HTTP = "http://192.168.0.110";
//                HTTPS = "http://192.168.0.110";
//                break;
//        }
//    }

    /**
     * RSA非对称加密获取秘钥
     */
    public static String RSAKEY = "/SmartLock/Phone1_1!getPublicKey.action";
    /**
     * 登录
     */
    public static String LOGIN = "/SmartLock/Phone1_1!checkLogin.action";
    /**
     * 刷新
     */
    public static String FRESH_TOKEN = "/SmartLock/Phone1_1!refreshenToken.action";
    /**
     * 获取楼栋信息
     */
    public static String OBTAIN_HOUSE_NUMBER = "/SmartLock/Phone1_1!getLd.action";
    /**
     * 获取楼层信息
     */
    public static String OBTAIN_FLOOR_NUMBER = "/SmartLock/Phone1_1!getLc.action";
    /**
     * 获取设备类型
     */
    public static String OBTAIN_MODEL_NUMBER = "/SmartLock/Phone1_1!getDeviceType.action";
    /**
     * 获取设备列表
     */
    public static String OBTAIN_DEVICE_LIST = "/SmartLock/Phone1_1!getDevice.action";
    /**
     * 获取设备开门记录
     */
    public static String OBTAIN_DEVICE_HISTORY_LIST = "/SmartLock/Phone1_1!getDeviceRecord.action";
    /**
     * 离线授权获取用户信息
     * token	是	令牌
     * roomid	是	房间id
     * sqtype	是	授权类型
     * search	否	学号/姓名
     * rows	是	每页记录数
     * page	是	页数
     */
    public static String OFFLINE_AUTHORY_OBTAIN_USERINFO = "/SmartLock/Phone1_1!getUsers.action";
    /**
     * 获取离线授权信息
     * token	是	令牌
     * roomid	是	房间id
     * sqtype	是	授权类型
     * search	否	学号/姓名
     * rows	是	每页记录数
     * page	是	页数
     */
    public static String OBTAIN_OFFLINE_AUTHORY_INFO = "/SmartLock/Phone1_1!geRoomcard.action";
    /**
     * 提交离线授权结果
     */
    public static String OFFLINE_AUTHORY_SAVE_RESULT = "/SmartLock/Phone1_1!saveRoomcardResult.action";
    /**
     * 提交离线开锁
     */
    public static String OFFLINE_AUTHORY_UNLOCK = "/SmartLock/Phone1_1!saveUnlock.action";
    /**
     * 提交离线开锁结果
     */
    public static String OFFLINE_AUTHORY_UNLOCK_SAVE_RESULT = "/SmartLock/Phone1_1!saveUnlockResult.action";
    /**
     * 获取离线记录指令
     */
    public static String OFFLINE_AUTHORY_HISROTY_RECORDER = "/SmartLock/Phone1_1!saveRecordOrder.action";
    /**
     * 提交离线记录结果
     */
    public static String OFFLINE_AUTHORY_RECORDER_SAVE_RESULT = "/SmartLock/Phone1_1!saveRecord.action";
    /**
     * 提交离线删除授权
     */
    public static String OFFLINE_DEL_AUTHORY = "/SmartLock/Phone1_1!saveRoomback.action";
    /**
     * 提交离线删除授权结果
     */
    public static String OFFLINE_DEL_AUTHORY_SAVE_RESULT = "/SmartLock/Phone1_1!saveRoombackResult.action";
    /**
     * 离线删除授权用户信息
     */
    public static String OFFLINE_DEL_AUTHORY_USERINFO = "/SmartLock/Phone1_1!geRoomcard.action";
}
