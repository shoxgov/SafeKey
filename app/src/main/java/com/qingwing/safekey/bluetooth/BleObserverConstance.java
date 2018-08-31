package com.qingwing.safekey.bluetooth;

public class BleObserverConstance {
    /**
     * 登录互斥 强制退出登录
     */
    public static final int LOGIN_SINGLE_STOP_ACTION = 20001;
    /**
     * 手动关闭蓝牙事件
     */
    public static final int BT_OFF_HAND_ACTION = 20002;
    /**
     * 手动关闭网络事件
     */
    public static final int NETWORK_OFF_HAND_ACTION = 20003;
    /**
     * BOX_CONNTEC_BLE_ON: 连接保管箱BLE连接状态
     */
    public static final int BOX_CONNTEC_BLE_STATUS = 10100;
    /**
     * 正在连接蓝牙
     */
    public static final int BOX_CONNTECING_BLE = 10101;
    /**
     * 绑定时，连接BT没有找到设备
     */
    public static final int BOX_BIND_CONNECT_NODEVICE = 10129;
    /**
     * BOX_RECEIVER_READINFO:  接收保管箱读取记录信息
     */
    public static final int BOX_RECEIVER_READINFO = 10117;
    /**
     * RECEIVER_BT_SATUS:  接收状态查询回复
     */
    public static final int RECEIVER_BT_SATUS = 10118;
    /**
     * RECEIVER_OFFLINE_UPLOAD_RECORD:  接收离线上传记录
     */
    public static final int RECEIVER_OFFLINE_UPLOAD_RECORD = 10119;

    /**
     * GATT创建并连接成功，此时可以发指令了
     */
    public static final int ACTION_GATT_CONNECT_SUCCESS = 32222;
    /**
     * 离线授权用户指令下发蓝牙回复结果
     */
    public static final int LOCK_OFFLINE_AUTHORY_COMMAND_RESULT = 32223;
    /**
     * 离线删除授权用户指令下发蓝牙回复结果
     */
    public static final int LOCK_OFFLINE_DEL_AUTHORY_COMMAND_RESULT = 32224;
}
