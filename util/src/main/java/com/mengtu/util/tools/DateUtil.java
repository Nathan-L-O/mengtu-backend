package com.mengtu.util.tools;

import com.mengtu.util.enums.RestResultCode;
import com.mengtu.util.exception.KaiChiException;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 时间处理工具
 *
 * @author 过昊天
 * @version 1.0 @ 2022/4/24 13:53
 */
public class DateUtil {

    private static final String SHORT_TIME = "yyyyMMddHHmmss";

    private static final String YEAR_TIME = "yyyy";

    private static final String MONTH_DAY = "MMdd";

    private static final String YEAR_MONTH_DAY = "yyyy-MM-dd";

    private static final String DAY = "dd";


    /**
     * 获取短时间字符串
     *
     * @param date
     * @return
     */
    public static String getShortDatesStr(Date date) {
        return format(date, SHORT_TIME);
    }

    public static Date getDateByShortDatesStr(String str) {
        try {
            return parse(str, SHORT_TIME);
        } catch (Exception e) {
            throw new KaiChiException(RestResultCode.ILLEGAL_PARAMETERS, "日期格式非法");
        }
    }

    public static Date getDateByYearMonthDayStr(String str) {
        return parse(str, YEAR_MONTH_DAY);
    }


    /**
     * 获取年份
     *
     * @param date
     * @return
     */
    public static String getYear(Date date) {
        return format(date, YEAR_TIME);
    }

    /**
     * 获取月日
     *
     * @param date
     * @return
     */
    public static String getMonthDay(Date date) {
        return format(date, MONTH_DAY);
    }

    public static String getYearMonthDay(Date date) {
        return format(date, YEAR_MONTH_DAY);
    }

    /**
     * 获取日
     *
     * @param date
     * @return
     */
    public static String getDay(Date date) {
        return format(date, DAY);
    }

    /**
     * 格式化时间
     *
     * @param date
     * @param format
     * @return
     */
    public static String format(Date date, String format) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(format);
        return dateFormat.format(date);
    }

    /**
     * 字符串转换成日期
     *
     * @param dateStr
     * @param format
     * @return
     */
    public static Date parse(String dateStr, String format) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(format);
        try {
            return dateFormat.parse(dateStr);
        } catch (ParseException e) {
            throw new IllegalArgumentException("日期转换失败");
        }
    }

    /**
     * 判断当前是否在时间段内
     *
     * @param start
     * @param end
     * @return
     */
    public static boolean nowIsBetween(Date start, Date end) {
        return isBetween(new Date(), start, end);
    }


    /**
     * 判断日期是不是在时间段内
     *
     * @param date
     * @param start
     * @param end
     * @return
     */
    public static boolean isBetween(Date date, Date start, Date end) {
        return !date.before(start) && !date.after(end);
    }
}
