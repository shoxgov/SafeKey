package com.qingwing.safekey.bluetooth;

import android.text.TextUtils;

import com.qingwing.safekey.bean.LockStatus;
import com.qingwing.safekey.observable.ObservableBean;
import com.qingwing.safekey.observable.ObserverManager;
import com.qingwing.safekey.utils.LogUtil;


/**
 * 解析蓝牙数据类
 */
public class BLEAnylizeManager {
    /**
     * 解析
     *
     * @param data
     */

    public static void anylize(String data) {
        if (TextUtils.isEmpty(data)) {
            return;
        }
        LogUtil.d(" anylize >>>" + data);
        String interceptData = data.substring(20, 24);
        LogUtil.d("QWBLE analyzeBT   interceptData：" + interceptData);
        // 保管箱状态查询
        /*AA AA|				数据包头
        xx|					包长，若为小无线通讯数据，则包长不含前3字节
        00 01 02 01 04		     网关地址（5字节），0001学校，2栋1层04号网关
        03 10|    			门锁ID（头2字节）（2栋310房间）
        01 A0|				命令（2字节）
        13 14 09 |				时间因子，取时间的时分秒（3字节）
        xx|	高4bit代表门锁生命周期状态：【0代表状态0】【1代表状态1】
        bit0为1代表门锁处于打开状态，为0代表处于关门状态
        bit1为1代表门未反锁，为0代表门已经反锁
        bit2为1代表bit1无意义，本锁体没有反锁检测功能
        bit3为1代表bit0位无意义，本锁体没有门磁检测功能功能
        xx|					电量（1字节）
        xx xx|				学生开门卡数量（2字节）
        xx xx|				管理开门卡数量（2字节）
        xx xx|				特殊开门卡数量（2字节）
        xx |					小无线发射功率（1字节），A0-A3代表最低到最高
        xx 			     	授权卡数量（1字节）
        00 00 |				记录条数信息（2字节）
        01 00 08 25			版本号V1.0 0825 16进制，版本号4字节
        XX XX  				小无线ID，高在前，低在后
        XX					通讯信道
        Xx xx xx xx xx			5字节门锁唯一ID号
        XX	开门工作模式：后半字节01代表开门后立即关门(自锁模式)，02代表刷卡开门刷卡关门（常开模式）
        联网模式：前半字节10代表仅工作在联网模式，20代表门锁允许离线授权模式（脱机授权）

        XX					门锁类型
        后半字节01代表无线联网锁 02代表有线联网锁 03代表NB联网锁
        前半字节01代表支持刷卡功能 02代表支持密码 04代表支持指纹 08代表支持扫描枪

        Xx					已经下发的指纹数量
        Xx xx xx xx xx		    5字节
        XX XX				校验（2字节）*/
        if (interceptData.equals("01A0")) {
            LogUtil.d("在BlueService： 收到状态查询01A0返回指令");
            String addr = data.substring(6, 16);//网关地址
            String lockId = data.substring(16, 20);//门锁ID
            String time = data.substring(24, 30);//
            String lockStatus = data.substring(30, 32);//
            String electricValue = data.substring(32, 34);//电量值  16进制
            String studentOpenCardNum = data.substring(34, 38);//
            String managerOpenCardNum = data.substring(38, 42);//
            String otherOpenCardNum = data.substring(42, 46);//
            String authoryCardNum = data.substring(48, 50);//
            String recordNum = data.substring(50, 54);//记录条数信息
            String versonCode = data.substring(54, 62);//版本号4字节
            String lockSN = data.substring(68, 78);//版本号4字节
            LockStatus ls = new LockStatus();
            if(lockStatus.equals("00")){
                ls.setLockStatus(0);
            } else if(lockStatus.equals("01")){
                ls.setLockStatus(1);
            } else if(lockStatus.equals("10")){
                ls.setLockStatus(2);
            } else if(lockStatus.equals("11")){
                ls.setLockStatus(3);
            }
            ls.setLockId(lockId);
            ls.setTime(time);
            ls.setElectricValue(BlueDeviceUtils.hexStringToInteger(electricValue));
            ls.setStudentOpenCardNum(BlueDeviceUtils.hexStringToInteger(studentOpenCardNum));
            ls.setManagerOpenCardNum(BlueDeviceUtils.hexStringToInteger(managerOpenCardNum));
            ls.setOtherOpenCardNum(BlueDeviceUtils.hexStringToInteger(otherOpenCardNum));
            ls.setAuthoryCardNum(BlueDeviceUtils.hexStringToInteger(authoryCardNum));
            ls.setRecordNum(BlueDeviceUtils.hexStringToInteger(recordNum));
            ls.setVersonCode(versonCode);
            ls.setLockSN(lockSN);
            ObservableBean obj = new ObservableBean();
            obj.setWhat(BleObserverConstance.RECEIVER_BT_SATUS);
            obj.setObject(data);
            ObserverManager.getObserver().setMessage(obj);
        } // 接收保管箱读取记录信息
        else if (interceptData.equals("0117")) {
        } else if (interceptData.equals("00A2")) { // 接收到插入充电线主动上传APP
//            ObservableBean obj = new ObservableBean();
//            obj.setWhat(BleObserverConstance.RECEIVER_BOX_DATA_RECHARGE);
//            obj.setObject(data);
//            ObserverManager.getObserver().setMessage(obj);
        } else {
//            ObservableBean obj = new ObservableBean();
//            obj.setWhat(BleObserverConstance.BOX_RECEIVER_INFO_UNKNOW);
//            ObserverManager.getObserver().setMessage(obj);
        }
    }

    public static boolean checkCommand(String comd) {
        String recevier = BLECommandManager.getSendBlueId(comd.substring(0, comd.length() - 4), "", "");
        if (!recevier.equals(comd)) {
            LogUtil.d("校验错误，数据传输异常请重发！");
            return false;
        }
        return true;
    }
}