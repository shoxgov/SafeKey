package com.qingwing.safekey.utils;

import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;

public class DateUtils {

    /**
     * @param lo 毫秒数
     * @return String yyyy-MM-dd HH:mm:ss
     * @Description: String类型毫秒数转换成日期
     */
    public static String stringToDate(String lo) {
        long time = Long.parseLong(lo);
        Date date = new Date(time);
        SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sd.format(date);
    }

    /**
     * @param lo 毫秒数
     * @return String yyyy-MM-dd HH:mm:ss
     * @Description: long类型转换成日期
     */
    public static String longToDate(long lo) {
        return longToDate(lo, "yyyy-MM-dd HH:mm:ss");
    }
    public static String longToDate(long lo, String format) {
        Date date = new Date(lo);
        SimpleDateFormat sd = new SimpleDateFormat(format);
        return sd.format(date);
    }

    /**
     * @param lo 日期毫秒数
     * @return String yyyyMMddHHmmss
     * @Description: long类型生成没有符号的日期格式
     */
    public static String getLongToDate(long lo) {
        Date date = new Date(lo);
        SimpleDateFormat sd = new SimpleDateFormat("yyyyMMddHHmmss");
        return sd.format(date);
    }

    /**
     * @param lo 日期毫秒数（字符串形式）
     * @return String yyyyMMddHHmmss
     * @Description: String类型生成没有符号的日期格式
     */
    public static String getStringToDate(String lo) {
        long time = Long.parseLong(lo);
        Date date = new Date(time);
        SimpleDateFormat sd = new SimpleDateFormat("yyyyMMddHHmmss");
        return sd.format(date);
    }

    /**
     * @param lo 日期毫秒数
     * @return String yyyy.MM.dd
     * @Description: long类型转换成点形式的日期格式
     */
    public static String getLongPointDate(long lo) {
        Date date = new Date(lo);
        SimpleDateFormat sd = new SimpleDateFormat("yyyy.MM.dd");
        return sd.format(date);
    }

    /**
     * @param lo String类型日期毫秒数
     * @return String yyyy.MM.dd
     * @Description: String类型转换成点形式的日期格式
     */
    public static String getStringPointDate(String lo) {
        long time = Long.parseLong(lo);
        Date date = new Date(time);
        SimpleDateFormat sd = new SimpleDateFormat("yyyy.MM.dd");
        return sd.format(date);
    }

    /**
     * @param lo long类型日期好藐视
     * @return String yyyyMMdd
     * @Description: long类型转成日期格式
     */
    public static String getloToDate(long lo) {
        Date date = new Date(lo);
        SimpleDateFormat sd = new SimpleDateFormat("yyyyMMdd");
        return sd.format(date);
    }

    /**
     * @param lo String类型日期好藐视
     * @return String yyyyMMdd
     * @Description: String类型转成日期格式
     */
    public static String getStrToDate(String lo) {
        long time = Long.parseLong(lo);
        Date date = new Date(time);
        SimpleDateFormat sd = new SimpleDateFormat("yyyyMMdd");
        return sd.format(date);
    }

    /**
     * @param lo 日期毫秒数
     * @return String yyyy.MM.dd  HH:mm:ss
     * @Description: long类型转换成点形式的日期格式
     */
    public static String longPointDate(long lo) {
        Date date = new Date(lo);
        SimpleDateFormat sd = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss");
        return sd.format(date);
    }

    /**
     * @param lo String类型日期毫秒数
     * @return String yyyy.MM.dd HH:mm:ss
     * @Description: String类型转换成点形式的日期格式
     */
    public static String stringPointDate(String lo) {
        long time = Long.parseLong(lo);
        Date date = new Date(time);
        SimpleDateFormat sd = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss");
        return sd.format(date);
    }

    /**
     * 计算两个日期间相差的天数
     *
     * @param startTime
     * @param endTime
     * @param format
     * @return
     */
    public static long dateDiff(String startTime, String endTime, String format) {
        // 按照传入的格式生成一个simpledateformate对象
        SimpleDateFormat sd = new SimpleDateFormat(format);
        long nd = 1000 * 24 * 60 * 60;// 一天的毫秒数
        long nh = 1000 * 60 * 60;// 一小时的毫秒数
        long nm = 1000 * 60;// 一分钟的毫秒数
        long ns = 1000;// 一秒钟的毫秒数
        long diff;
        long day = 0;
        try {
            // 获得两个时间的毫秒时间差异
            diff = sd.parse(endTime).getTime() - sd.parse(startTime).getTime();
            day = diff / nd;// 计算差多少天
            long hour = diff % nd / nh;// 计算差多少小时
            long min = diff % nd % nh / nm;// 计算差多少分钟
            long sec = diff % nd % nh % nm / ns;// 计算差多少秒
            // 输出结果
            System.out.println("BENBEN 时间相差：" + day + "天" + hour + "小时" + min + "分钟" + sec + "秒。");
            if (day >= 1) {
                return day;
            } else {
                if (day == 0) {
                    return 1;
                } else {
                    return 0;
                }
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return 0;
    }

    /**
     * 计算日期与今天相差的天数
     *
     * @param endTime
     * @return
     */
    public static long dateDiffToday(String endTime) {
        // 按照传入的格式生成一个simpledateformate对象
        return dateDiffToday(endTime, "yyyy-MM-dd");
    }


    public static long dateDiffToday(String endTime, String format) {
        SimpleDateFormat sd = new SimpleDateFormat(format);
        long nd = 1000 * 24 * 60 * 60;// 一天的毫秒数
        long nh = 1000 * 60 * 60;// 一小时的毫秒数
        long nm = 1000 * 60;// 一分钟的毫秒数
        long ns = 1000;// 一秒钟的毫秒数
        long diff;
        long day = 0;
        try {
            // 获得两个时间的毫秒时间差异
            diff = sd.parse(endTime).getTime() - System.currentTimeMillis();
            if (diff < 0) {
                return 0;
            }
            day = diff / nd;// 计算差多少天
            long hour = diff % nd / nh;// 计算差多少小时
            long min = diff % nd % nh / nm;// 计算差多少分钟
            long sec = diff % nd % nh % nm / ns;// 计算差多少秒
            // 输出结果
            System.out.println("BENBEN 时间相差：" + day + "天" + hour + "小时" + min + "分钟" + sec + "秒。");
            if (day >= 1) {
                return day;
            } else {
                if (day == 0) {
                    return 1;
                } else {
                    return 0;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }
}