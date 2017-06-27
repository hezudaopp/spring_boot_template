package com.youmayon.tutorial.util;

import org.springframework.util.Assert;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * 时间工具类，
 * 本类时间戳单位是s
 * Created by Jawinton on 16/12/19.
 */
public class TimeUtils {
    private TimeUtils() {}

    private static final SimpleDateFormat shortSdf = new SimpleDateFormat("yyyy-MM-dd");
    private static final SimpleDateFormat longSdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    /**
     * 两个时间戳相差的天数
     * @param firstTimestamp
     * @param secondTimestamp
     * @return
     */
    public static final long daysBetween(long firstTimestamp, long secondTimestamp, int timeZone) {
        Assert.isTrue(timeZone >= -12, "Time zone error.");
        Assert.isTrue(timeZone <= 12, "Time zone error.");
        long firstDays = (firstTimestamp + timeZone * 3600) / 86400;
        long secondDays = (secondTimestamp + timeZone * 3600)  / 86400;
        return secondDays - firstDays;
    }

    /**
     * 两个时间戳相差的天数，东8区
     * @param firstTimestamp
     * @param secondTimestamp
     * @return
     */
    public static final long daysBetween(long firstTimestamp, long secondTimestamp) {
        return daysBetween(firstTimestamp, secondTimestamp, 8);
    }

    /**
     * 获取天开始时间戳
     * @return
     */
    public static final long dayBeginTimestamp() {
        return dayBeginTimestamp(System.currentTimeMillis() / 1000, 8);
    }

    /**
     * 获取天开始时间戳
     * @param timestamp
     * @return
     */
    public static final long dayBeginTimestamp(long timestamp) {
        return dayBeginTimestamp(timestamp, 8);
    }

    /**
     * 获取天结束时间戳
     * @return
     */
    public static final long dayEndTimestamp() {
        return dayBeginTimestamp(System.currentTimeMillis() / 1000, 8) + 86400;
    }

    /**
     * 获取天结束时间戳
     * @param timestamp
     * @return
     */
    public static final long dayEndTimestamp(long timestamp) {
        return dayBeginTimestamp(timestamp, 8) + 86400;
    }

    /**
     * 获取天开始时间戳
     * @param timestamp
     * @param timeZone
     * @return
     */
    public static final long dayBeginTimestamp(long timestamp, int timeZone) {
        timestamp += timeZone * 3600;
        return (timestamp - timestamp % 86400) - timeZone * 3600;
    }

    /**
     * 月开始时间戳
     * @param months 月偏移量
     * @return
     */
    public static long monthBeginTimestamp(int months) {
        Calendar c = Calendar.getInstance();
        Date now = null;
        try {
            c.set(Calendar.DATE, 1);
            c.add(Calendar.MONTH, months);
            now = longSdf.parse(shortSdf.format(c.getTime()) + " 00:00:00");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return now.getTime() / 1000;
    }

}
