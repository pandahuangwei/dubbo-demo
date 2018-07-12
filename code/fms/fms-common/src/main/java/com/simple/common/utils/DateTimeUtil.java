package com.simple.common.utils;

import org.joda.time.DateTime;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * 时间操作工具类
 *
 * @author Panda.HuangWei.
 * @version 1.0 .
 * @since 2016.12.19 11:19.
 */
public class DateTimeUtil {

    private static final int MILLIS_OF_SECOND = 1000;
    private static final int SECOND_OF_MINUTE = 60;
    private static final int MINUTE_OF_HOUR = 60;
    private static final int HOUR_OF_DAY = 24;

    private static final long MILLIS_OF_MINUTE = MILLIS_OF_SECOND * SECOND_OF_MINUTE;
    private static final long MILLIS_OF_HOUR = MILLIS_OF_MINUTE * MINUTE_OF_HOUR;
    private static final long MILLIS_OF_DAY = MILLIS_OF_HOUR * HOUR_OF_DAY;

    private  static final SimpleDateFormat sdfTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private  static final SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy-MM-dd");

    private  static final SimpleDateFormat YYYYMMDDHH = new SimpleDateFormat("yyyyMMddHH");

    public static String toSting(Date dt) {
        try {
            return dt == null ? null : sdfTime.format(dt);
        } catch (Exception e) {
            return null;
        }
    }

    public static String toStingShort(Date dt) {
        try {
            return dt == null ? null : sdfDate.format(dt);
        } catch (Exception e) {
            return null;
        }
    }
    /**
     * 当前时间的年月日
     * @return
     */
    public static  Date  newDate() throws ParseException {
        Date date=new Date();
        String dateNowStr = sdfDate.format(date);
        return  sdfDate.parse(dateNowStr);
    }

    /**
     * 指定时间减天数
     *
     * @param srcDate 时间
     * @param days    天数
     * @return 新的时间
     */
    public static Date subDays(Date srcDate, int days) {
        return days == 0 ? srcDate : addDays(srcDate, -1 * days);
    }

    /**
     * 指定时间加天数
     *
     * @param srcDate 时间
     * @param days    天数
     * @return 新的时间
     */
    public static Date addDays(Date srcDate, int days) {
        return new Date(srcDate.getTime() + MILLIS_OF_DAY * days);
    }

    /**
     * 指定时间加小时
     *
     * @param srcDate 时间
     * @param hours   小时数
     * @return 新的时间
     */
    public static Date addHours(Date srcDate, int hours) {
        return new Date(srcDate.getTime() + hours * MILLIS_OF_HOUR);
    }

    /**
     * 指定时间加分钟数
     *
     * @param srcDate 时间
     * @param minutes 分钟数
     * @return 新的时间
     */
    public static Date addMinutes(Date srcDate, int minutes) {
        return minutes == 0 ? srcDate : new Date(srcDate.getTime() + minutes * MILLIS_OF_MINUTE);
    }

    /**
     * 指定时间减分钟数
     *
     * @param srcDate 时间
     * @param minutes 分钟数
     * @return 新的时间
     */
    public static Date subMinutes(Date srcDate, int minutes) {
        return minutes == 0 ? srcDate : addMinutes(srcDate, -1 * minutes);
    }

    /**
     * 获取当前时刻的昨天天该时刻
     *
     * @param srcDate 当前时间
     * @return 前一天
     */
    public static Date getYesterdayDt(Date srcDate) {
        return new Date(srcDate.getTime() - MILLIS_OF_DAY);
    }

    /**
     * 获取当前时刻的下一天该时刻
     *
     * @param srcDate 当前时间
     * @return 下一天
     */
    public static Date getNextDt(Date srcDate) {
        return new Date(srcDate.getTime() + MILLIS_OF_DAY);
    }


    /**
     * 判断日期是否在两个日期之间(beginTm<=date<=entTm)
     *
     * @param date    判断日期
     * @param beginTm 开始日期
     * @param entTm   结束日期
     * @return true/false
     */
    public static boolean isBetweenTwoDate(Date date, Date beginTm, Date entTm) {
        return !date.before(beginTm) && !date.after(entTm);
    }

    /**
     * 时间是否比在当前时间之前
     *
     * @param date 比较的时间
     * @return boolean
     */
    public static boolean isBeforeNow(Date date) {
        return isFirstBeforeSecond(date, new Date());
    }

    /**
     * 比较第一个时间是否在第二个时间之前
     *
     * @param first  第一个时间
     * @param second 第二个时间
     * @return boolean
     */
    public static boolean isFirstBeforeSecond(Date first, Date second) {
        return (first == null || second == null) ? false : first.before(second);
    }

    /**
     * 截取时分秒，只保留年月日
     *
     * @param date 时间
     * @return 日期
     */
    public static Date truncDate(Date date) {
        DateTime dt = new DateTime(date.getTime());
        DateTime dateTime = new DateTime(dt.getYear(), dt.getMonthOfYear(), dt.getDayOfMonth(), 0, 0, 0, 0);
        return dateTime.toDate();
    }

    /**
     * 获取当前时间
     *
     * @return 当前时间
     */
    public static Date getNowDt() {
        return new Date();
    }


    /**
     * 获取YYYY-MM-DD HH:mm:ss格式
     *
     * @return 带时分秒的时间字符串
     */
    public static String getTime() {
        return sdfTime.format(new Date());
    }

    /**
     * 将时间格式化为YYYY-MM-DD HH:mm:ss格式
     *
     * @param date 时间
     * @return 带时分秒的时间字符串
     */
    public static String formatTime(Date date) {
        return date == null ? null : sdfTime.format(date);
    }

    public static Date formatTime(String date) {
        try {
            return sdfTime.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return new Date();
    }

    /**
     * 时间格式化为YYYY-MM-DD格式
     *
     * @param date 时间
     * @return 只有年月日的时间字符串
     */
    public static String formatDate(Date date) {
        return date == null ? null : sdfDate.format(date);
    }

    public static String toString(Date date, String format) {
        if (date == null) {
            return null;
        }
        DateFormat df = new SimpleDateFormat(format);
        return df.format(date);
    }

    public static String toString(Date date, SimpleDateFormat format) {
        if (date == null) {
            return null;
        }
        return format.format(date);
    }

    public static String changeDay(String date, int offset, String formate) throws ParseException {
        SimpleDateFormat formatdater = new SimpleDateFormat(formate);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(formatdater.parse(date));
        calendar.set(Calendar.DAY_OF_YEAR, (calendar.get(Calendar.DAY_OF_YEAR) + offset));
        return formatdater.format(calendar.getTime());
    }

    public static String getCurUTCHour() {
        Calendar cal = Calendar.getInstance();
        int zoneOffset = cal.get(java.util.Calendar.ZONE_OFFSET);
        int dstOffset = cal.get(java.util.Calendar.DST_OFFSET);
        cal.add(java.util.Calendar.MILLISECOND, -(zoneOffset + dstOffset));
        return DateTimeUtil.toString(cal.getTime(), YYYYMMDDHH);
    }

    public static String getUTCTime(String format, int hour) throws ParseException {
        final java.util.Calendar cal = java.util.Calendar.getInstance();
        final int zoneOffset = cal.get(java.util.Calendar.ZONE_OFFSET);
        final int dstOffset = cal.get(java.util.Calendar.DST_OFFSET);
        cal.add(java.util.Calendar.MILLISECOND, -(zoneOffset + dstOffset));
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        cal.setTime(sdf.parse(DateTimeUtil.toString(cal.getTime(), format)));
        cal.add(Calendar.HOUR, hour);
        return DateTimeUtil.toString(cal.getTime(), format);
    }

    public static String getCurrentDateTime(String formate) {
        String result = null;
        Date currentDate = new Date();
        SimpleDateFormat formatdater = new SimpleDateFormat(formate);
        result = formatdater.format(currentDate);
        return result;
    }

    public static String formateToHour(String datetime, int offset) throws ParseException {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(YYYYMMDDHH.parse(datetime));
        calendar.add(Calendar.HOUR, offset);
        return YYYYMMDDHH.format(calendar.getTime());
    }

    public static Date strToDate(String str, String formatStr) {
        SimpleDateFormat format = new SimpleDateFormat(formatStr);
        Date date = null;
        try {
            date = format.parse(str);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    public static String getCurUTCtimeToHH(String format) {
        Calendar cal = Calendar.getInstance();
        int zoneOffset = cal.get(Calendar.ZONE_OFFSET);
        int dstOffset = cal.get(Calendar.DST_OFFSET);
        cal.add(Calendar.MILLISECOND, -(zoneOffset + dstOffset));
        return DateTimeUtil.toString(cal.getTime(), format);
    }


    public static String changeHour(String datetime, int offset)
            throws ParseException {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(sdfTime.parse(datetime));
        calendar.add(Calendar.HOUR, offset);
        return sdfTime.format(calendar.getTime());
    }

}
