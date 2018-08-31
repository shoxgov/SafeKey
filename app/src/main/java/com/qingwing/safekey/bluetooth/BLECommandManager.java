package com.qingwing.safekey.bluetooth;

import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;

import com.qingwing.safekey.utils.LogUtil;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 发送蓝牙指令方法
 */
public class BLECommandManager {
    public static boolean isSupportBLE(Context context) {
        // 检查当前手机是否支持ble,蓝牙，如果不支持就退出
        if (context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE)) {
            return true;
        }
        return false;
    }

    public static boolean isEnable(Context context) {
        // 初始化 Bluetooth adapter, 通过蓝牙管理器得到一个参考蓝牙适配器(API必须在以上android4.3或以上和版本)
        BluetoothManager bluetoothManager = (BluetoothManager) context.getSystemService(Context.BLUETOOTH_SERVICE);
        // 检查设备上是否支持蓝牙
        if (bluetoothManager.getAdapter() == null) {
            return false;
        }
        return true;
    }

    public static String getSystemTime() {
        Date date = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyMMddHHmmss");
        return dateFormat.format(date);
    }

    private static String getThreeSystemTime() {
        Date date = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("HHmmss");
        return dateFormat.format(date);
    }


    /**
     * AA AA|			数据包头
     * xx|			     包长，若为小无线通讯数据，则包长不含前3字节
     * 00 01 02 01 04		网关地址（5字节），0001学校，2栋1层04号网关
     * 03 10|    		门锁ID（头2字节）（2栋310房间）
     * 00 A0|			命令（2字节） 00代表仅查询，A0-A3代表配置小无线功率为最低到最高
     * B0代表将门锁设置为自锁模式
     * B1代表将门锁设置为常开模式
     * C0代表允许门锁工作在离线授权模式
     * C1代表关闭门锁的离线授权模式
     * 13 14 09 |  		     服务器当前时间（6字节，先时分秒作为时间因子，后增加年月日，保持格式统一）
     * 17 05 08
     * xx xx				校验（2字节）
     **/
    public static void queryStatus(Context context, String gatewayCode, String roomid) {
        try {
            LogUtil.d("查询状态");
            Intent intent = new Intent(BluetoothService.ACTION_GATT_WRITE_COMMAND);
            String str = "AAAA" + "11" + gatewayCode + roomid + "00A0" + getThreeSystemTime();
            intent.putExtra(BluetoothService.WRITE_COMMAND_VALUE, getSendBlueId(str, "", ""));
            context.sendBroadcast(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * 解析时也会用到
     *
     * @param blueInfo
     * @param code
     * @param time
     * @return
     */
    public static String getSendBlueId(String blueInfo, String code, String time) {
        String hexStr = "0123456789ABCDEF";
        //将小写字母编写成大写字母。
        String content;
        if (blueInfo.startsWith("AAAA") || blueInfo.startsWith("BBBB")) {
            content = blueInfo.toUpperCase();
        } else {
            content = "AAAA" + blueInfo.toUpperCase() + code.toUpperCase() + time.toUpperCase();
        }
        int postion = content.length() / 2;
        byte top = (byte) (hexStr.indexOf(content.charAt(0)) << 4 | (byte) (hexStr.indexOf(content.charAt(1))));
        byte bottom = 0;
        //校验位
        for (int i = 1; i < postion; i++) {
            top = (byte) (top ^ ((byte) (hexStr.indexOf(content.charAt(2 * i))) << 4 | (byte) (hexStr.indexOf(content.charAt(2 * i + 1)))));
        }
        for (int i = 0; i < postion; i++) {
            bottom += ((byte) (hexStr.indexOf(content.charAt(2 * i))) << 4 | (byte) (hexStr.indexOf(content.charAt(2 * i + 1))));
        }
        byte[] bts = {bottom, top};
        String str = BlueDeviceUtils.binaryToHexString(bts);
        LogUtil.d("执行getSendBlueId>>>>>>>>>>> content" + content + ",  校验码：" + str);
        return content + str.toUpperCase();
    }

}
