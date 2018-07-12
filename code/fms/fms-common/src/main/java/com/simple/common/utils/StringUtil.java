package com.simple.common.utils;

import org.apache.commons.lang3.StringUtils;

/**
 * 字符串操作类(使用apache工具类)
 *
 * @author Panda.HuangWei.
 * @version 1.0 .
 * @since 2016.12.19 11:09.
 */
public class StringUtil {
    /**
     * 下划线分隔符
     */
    private static final String UNDERLINE_SIGN = "_";

    private static final String R_N = "\r|\n";
    /**
     * 逗号分隔符
     */
    public static final String COMMA_SIGN = ",";
    public static final String LINE_SIGN = "-";

    /**
     * 构建字符串
     *
     * @param keys 字符串，多参数
     * @return key
     */
    public static String genKey(String... keys) {
        return genKey(UNDERLINE_SIGN, keys);
    }

    /**
     * 构建字符串.
     * <p>
     * Examples:
     * <p>
     * <pre>
     * splitChar="_",key="A","B","C" returns "A_B_C"
     * </pre>
     *
     * @param splitChar 分隔符
     * @param keys      字符串，多参数
     * @return key
     */
    private static String genKey(String splitChar, String... keys) {
        StringBuilder sb = new StringBuilder();

        for (int i = 0, len = keys.length; i < len; i++) {
            sb.append(keys[i]);
            if (i != len - 1) {
                sb.append(splitChar);
            }
        }
        return sb.toString();
    }

    /**
     * 判断字符串 "" 或 null
     *
     * @param value 字符串
     * @return 如果为""或null返回true
     */
    public static boolean isEmpty(String value) {
        return StringUtils.isEmpty(value);
    }

    /**
     * 判断字符串 不是"" 或 null
     *
     * @param value 字符串
     * @return 如果为""或null返回true
     */
    public static boolean isNotEmpty(String value) {
        return StringUtils.isNotEmpty(value);
    }

    /**
     * 判断字符串为 ""," "空白占位符或者null
     *
     * @param value 字符串
     * @return 如果为""," "空白占位符或者null,返回true
     */
    public static boolean isBlank(String value) {
        return StringUtils.isBlank(value);
    }

    /**
     * 替换\r\n占位符为""
     *
     * @param value 字符串
     * @return 去除\r\n后的字符串
     */
    public static String replaceRN(String value) {
        if (isEmpty(value)) {
            return null;
        }
        return value.replaceAll(R_N, "");
    }


    public static String trimEnd(String src, String trim) {
        if (StringUtils.isNotBlank(src) && src.endsWith(trim)) {
            return src.substring(0, src.lastIndexOf(trim));
        }
        return src;
    }

    /**
     * 去掉数字前面的0
     *
     * @param value
     * @return
     */
    public static String trimstart(String value) {
        //字符串是否全部是数字
        if (value.matches("[0-9]{1,}")) {
            //全部是0
            if (value.matches("[0]*")) {
                return "0";
            } else {
                return value.replaceFirst("^0*", "");
            }
        }
        return value;
    }

    /**
     * 花费的时间字符串如：spent 4h,30m,33s
     *
     * @param startTm 开始时间，以纳秒为单位
     * @return 花费的时间字符串如：spent 4h,30m,33s
     */
    public static String getSpentTm(long startTm) {
        long spentTm = System.nanoTime() - startTm;
        return changeNanoTm2String(spentTm);
    }

    /**
     * 把纳秒时间转换为英文字符串
     *
     * @param nanoTm 纳秒
     * @return 字符串
     */
    public static String changeNanoTm2String(long nanoTm) {
        if (nanoTm == 0) {
            return " spent 0 millSec.";
        }
        long temp = nanoTm;
        long nanoSec = temp % 1000;
        temp = temp / 1000;
        long microSec = temp % 1000;
        temp = temp / 1000;
        long millSec = temp % 1000;
        temp = temp / 1000;
        long sec = temp % 60;
        temp = temp / 60;
        long min = temp % 60;
        temp = temp / 60;
        long hrs = temp % 12;
        temp = temp / 12;
        long days = temp % 365;
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(" spent ");
        if (days != 0) {
            stringBuilder.append(days + " days ");
        }
        if (hrs != 0) {
            stringBuilder.append(hrs + " hrs ");
        }
        if (min != 0) {
            stringBuilder.append(min + " min ");
        }
        if (sec != 0) {
            stringBuilder.append(sec + " sec ");
        }
        if (millSec != 0) {
            stringBuilder.append(millSec + " millSec ");
        }
        if (microSec != 0) {
            stringBuilder.append(microSec + " microSec ");
        }
        if (nanoSec != 0) {
            stringBuilder.append(nanoSec + " nanoSec");
        }
        stringBuilder.append(".");
        return stringBuilder.toString();
    }

    public static String getMsgType(String msgType) {
        if (!msgType.contains(LINE_SIGN)) {
            return msgType;
        }
        String[] arr = StringUtils.split(msgType,LINE_SIGN);
        return arr[1];
    }
}
