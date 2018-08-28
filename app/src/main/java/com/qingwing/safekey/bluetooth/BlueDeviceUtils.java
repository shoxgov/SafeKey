package com.qingwing.safekey.bluetooth;

import java.util.HashMap;

public class BlueDeviceUtils {

    // 将字节数组转换为字符串
    public static String binaryToHexString(byte[] bytes) {
        String hexStr = "0123456789ABCDEF";
        String result = "";
        String hex = "";
        for (int i = 0; i < bytes.length; i++) {
            // 字节高4位
            hex = String.valueOf(hexStr.charAt((bytes[i] & 0xF0) >> 4));
            // 字节 低四位
            hex += String.valueOf(hexStr.charAt(bytes[i] & 0x0F));
            result += hex;
        }
        return result;
    }

    //十六进制字符串转换为字节数组
    public static byte[] hexStringToBinary(String hexString) {
        String hexStr = "0123456789ABCDEF";
        // hexString的长度对2取整，作为bytes的长度
        int len = hexString.length() / 2;
        byte[] bytes = new byte[len];
        byte high = 0;// 字节高四位
        byte low = 0;// 字节低四位
        for (int i = 0; i < len; i++) {
            // 左移四位得到高位
            high = (byte) ((hexStr.indexOf(hexString.charAt(2 * i))) << 4);
            low = (byte) hexStr.indexOf(hexString.charAt(2 * i + 1));
            bytes[i] = (byte) (high | low);// 高地位做或运算
        }
        return bytes;
    }

    public static int hexStringToInteger(String hexString) {
        String hexStr = "0123456789ABCDEF";
        // hexString的长度对2取整，作为bytes的长度
        int len = hexString.length() / 2;
        int count = 0;
        int high = 0;// 字节高四位
        int low = 0;// 字节低四位
        for (int i = 0; i < len; i++) {
            // 左移四位得到高位
            high = ((hexStr.indexOf(hexString.charAt(2 * i))) << 4);
            low = hexStr.indexOf(hexString.charAt(2 * i + 1));
            count = (high | low);// 高地位做或运算
        }
        return count;
    }

    //加密
    public static  byte[] Encryption(byte[] BlueSendBuf) {
        int length = BlueSendBuf.length;
        byte y;
        if (length < 16) {//这个16这个参数不能改
            y = BlueSendBuf[10];
            for (byte i = 0; i < length; i++) {
                BlueSendBuf[i] = (byte) (BlueSendBuf[i] ^ i ^ y);
            }
            BlueSendBuf[10] = y;
        } else {
            y = BlueSendBuf[13];
            for (byte i = 0; i < length; i++) {
                BlueSendBuf[i] = (byte) (BlueSendBuf[i] ^ i ^ y);
            }
            BlueSendBuf[13] = y;
        }
        return BlueSendBuf;
    }

    //解密
    public static byte[] Decode(byte[] BlueRecevBuf) {
        int length = BlueRecevBuf.length;
        byte y;
        if (length <= 10) {
            y = BlueRecevBuf[7];
            for (int i = 0; i < BlueRecevBuf.length; i++) {
                BlueRecevBuf[i] = (byte) (BlueRecevBuf[i] ^ i ^ y);
            }
            BlueRecevBuf[7] = y;
        } else {
            y = BlueRecevBuf[10];
            for (int i = 0; i < BlueRecevBuf.length; i++) {
                BlueRecevBuf[i] = (byte) (BlueRecevBuf[i] ^ i ^ y);
            }
            BlueRecevBuf[10] = y;
        }
        return BlueRecevBuf;
    }

    public static String interceptInitString(String result) {
        if (!result.contains("FF3101")) {
            return "";
        }
        String[] split = result.split("FF3101");
        String BlueId = split[1].substring(0, 16);
        System.out.println("SAFEKEY 截取的ID:" + BlueId);
        return BlueId;
    }

    public static String findAddress(HashMap<String, String> blueIdAddress, String blueId) {
        if(blueIdAddress.isEmpty()){
            return "";
        }
        for(String id : blueIdAddress.keySet()){
            if(id.contains(blueId)){
                return blueIdAddress.get(id);
            }
        }
        return "";
    }
}
