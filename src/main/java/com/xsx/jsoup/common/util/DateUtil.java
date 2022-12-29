package com.xsx.jsoup.common.util;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Timestamp;
import java.text.*;
import java.util.*;

/**
 * @Author:夏世雄
 * @Date: 2022/10/11/10:36
 * @Version: 1.0
 * @Discription:
 **/
public class DateUtil {
    public final static String DATETIME_PATTERN = "yyyyMMddHHmmss";

    public final static String TIME_STAMP_PATTERN = "yyyyMMddHHmmssSSS";

    public final static String DATE_PATTERN = "yyyyMMdd";

    public final static String TIME_PATTERN = "HHmmss";

    public final static String STANDARD_DATETIME_PATTERN = "yyyy-MM-dd HH:mm:ss";

    public final static String STANDARD_DATETIME_PATTERN_HMSS = "yyyy-MM-dd HH:mm:ss.SSS";

    public final static String STANDARD_DATETIME_PATTERN_HMSSZ = "yyyy-MM-dd'T'HH:mm:ss.SSSZ";

    public final static String STANDARD_DATETIME_PATTERN_HM = "yyyy-MM-dd HH:mm";

    public final static String STANDARD_DATETIME_PATTERN_H = "yyyy-MM-dd HH";

    public final static String STANDARD_DATE_PATTERN = "yyyy-MM-dd";

    public final static String STANDARD_DATE_PATTERN_YM = "yyyy-MM";

    public final static String DAY_M_Y_PATTERN = "dd-MM-yyyy";

    public final static String STANDARD_TIME_PATTERN = "HH:mm:ss";

    public final static String STANDARD_TIME_PATTERN_HM = "HH:mm";

    public final static String STANDARD_DATETIME_PATTERN_SOLIDUS = "yyyy/MM/dd HH:mm:ss";

    public final static String STANDARD_DATETIME_PATTERN_SOLIDUS_HM = "yyyy/MM/dd HH:mm";

    public final static String STANDARD_DATE_PATTERN_SOLIDUS = "yyyy/MM/dd";

    public final static String MM_DD_YYYY = "MM/dd/yyyy";

    public final static String M_Y_PATTERN = "MM/yyyy";

    public final static String STANDARD_DATE_PATTERN_SOLIDUS_D_M_Y = "dd/MM/yyyy";

    private DateUtil() {
        super();
    }

    public static String currentDatetime() {
        return formatDate(new Date());
    }

    public static Timestamp parseDate(String dateStr, String pattern) {
        Date d = DateUtil.parse(dateStr, pattern);
        return new Timestamp(d.getTime());
    }

    public static Timestamp parseDate(String dateStr) throws ParseException {
        return parseDate(dateStr, STANDARD_DATE_PATTERN);
    }

    public static java.sql.Date parseSQLDate(String dateStr, String pattern) throws ParseException {
        Date d = parse(dateStr, pattern);
        return new java.sql.Date(d.getTime());
    }

    public static java.sql.Date parseSQLDate(String dateStr) throws ParseException {
        Date d = parse(dateStr, STANDARD_DATE_PATTERN);
        return new java.sql.Date(d.getTime());
    }

    public static Timestamp getFutureTime(int month) {
        Calendar c = Calendar.getInstance();
        c.add(Calendar.MONTH, month);
        return new Timestamp(c.getTimeInMillis());
    }

    /**
     * 显示今天时间
     *
     * @return yyyy-MM-dd HH:mm:ss
     */
    public static String today() {
        return formatDateTime(new Date());
    }

    public static String formatDate(Timestamp t) {
        return formatDate(new Date(t.getTime()));
    }

    public static String formatDate(Timestamp t, String pattern) {
        return formatDate(new Date(t.getTime()), pattern);
    }

    public static String formatDateTime(Timestamp t, String pattern) {
        return formatDate(new Date(t.getTime()), STANDARD_DATETIME_PATTERN);
    }

    public static String formatDateTime(Date t, String pattern) {
        return formatDate(t, pattern);
    }

    /**
     * 格式化日期
     *
     * @param date
     * @return yyyy-MM-dd
     */
    public static String formatDate(Date date) {
        return formatDate(date, STANDARD_DATE_PATTERN);
    }


    public static String formatDateCoupon(Date date) {
        return formatDate(date, STANDARD_DATE_PATTERN_SOLIDUS_D_M_Y);
    }

    /**
     * 格式化日期
     *
     * @param date
     * @return yyyy-MM-dd HH:mm:ss
     */
    public static String formatDateTime(Date date) {
        return formatDate(date, STANDARD_DATETIME_PATTERN);
    }

    /**
     * 格式化日期
     *
     * @param date    日期
     * @param pattern 格式
     * @return
     */
    public static String formatDate(Date date, String pattern) {
        if (date == null)
            return null;
        DateFormat format = new SimpleDateFormat(pattern);
        return format.format(date);
    }

    /**
     * 解析日期
     *
     * @param dateStr yyyy-MM-dd
     * @return
     */
    public static Date parse(String dateStr) {
        return parse(dateStr, STANDARD_DATE_PATTERN);
    }

    /**
     * 解析日期
     *
     * @param dateStr yyyy-MM-dd HH:mm:ss
     * @return
     */
    public static Date parseTime(String dateStr) {
        return parse(dateStr, STANDARD_DATETIME_PATTERN);
    }

    public static Date parse(String dateStr, String pattern) {

        try {
            DateFormat format = new SimpleDateFormat(pattern);
            return format.parse(dateStr);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * 当月的第一天
     *
     * @return日期格式化数据
     */
    public static String firstDayOfMonth() {
        Date fristday = new Date();
        fristday.setTime(firstDayOfMonthTime());
        return DateUtil.formatDateTime(fristday);
    }

    /**
     * 当月的第一天
     *
     * @return日期格式化数据
     */
    public static String firstDayOfMonth1(Date fristday) {
        //Date fristday = new Date();
        fristday.setTime(firstDayOfMonthTime());
        return DateUtil.formatDate(fristday);
    }

    /**
     * 上个月的第一天
     *
     * @return日期格式化数据
     */
    public static String firstDayOfMonth2() {
        //上个月一号
        int maxCurrentMonthDay;
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Calendar calendar5 = Calendar.getInstance();
        maxCurrentMonthDay = calendar5.getActualMaximum(Calendar.DAY_OF_MONTH);
        calendar5.add(Calendar.DAY_OF_MONTH, -maxCurrentMonthDay);
        calendar5.set(Calendar.DAY_OF_MONTH, 1);
        return dateFormat.format(calendar5.getTime());
    }

    /**
     * 当月的第一天
     *
     * @return日期格式化数据
     */
    public static String firstDayOfMonth(int month) {
        Date fristday = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MONTH, month);
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        fristday.setTime(calendar.getTime().getTime());
        return DateUtil.formatDateTime(fristday);
    }

    /**
     * 计算给定日期的下一个月
     *
     * @return
     */
    public static String firstDayOfMonth(Date date, int month) {
        //获取前一个月第一天
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.MONTH, month);
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        Date time = calendar.getTime();
        return DateUtil.formatDateTime(time);
    }

    public static String firstDayOfMonth(Date fristday) {
        fristday.setTime(firstDayOfMonthTime());
        return DateUtil.formatDateTime(fristday);
    }

    public static long firstDayOfMonthTime() {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MONTH, 0);
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        return calendar.getTime().getTime();
    }

    /**
     * 计算给定日期的下一个月
     *
     * @return
     */
    public static String lastDayOfMonth(Date date, int month) {
        //获取前一个月第一天
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.MONTH, month);
        calendar.set(Calendar.DAY_OF_MONTH, 0);
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        Date time = calendar.getTime();
        return DateUtil.formatDateTime(time);
    }

    /**
     * 当月的最后一天
     *
     * @return日期格式化数据
     */
    public static String lastDayOfMonth() {
        Date lastday = new Date();
        lastday.setTime(lastDayOfMonthTime());
        return DateUtil.formatDateTime(lastday);
    }

    /**
     * 当月的最后一天
     *
     * @return日期格式化数据
     */
    public static String lastDayOfMonth(int month) {
        Date lastday = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MONTH, month + 1);
        calendar.set(Calendar.DAY_OF_MONTH, 0);
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        lastday.setTime(calendar.getTime().getTime());
        return DateUtil.formatDateTime(lastday);
    }

    /**
     * 前月的最后一天
     *
     * @return日期格式化数据
     */
    public static String lastDayOfMonth1() {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Calendar cale = Calendar.getInstance();
        cale.set(Calendar.DAY_OF_MONTH, 0);//设置为1号,当前日期既为本月第一天
        String format1 = format.format(cale.getTime());
        return format1;
    }

    public static String lastDayOfMonth(Date lastday) {
        lastday.setTime(lastDayOfMonthTime());
        return DateUtil.formatDateTime(lastday);
    }

    public static long lastDayOfMonthTime() {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MONTH, 1);
        calendar.set(Calendar.DAY_OF_MONTH, 0);
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        return calendar.getTime().getTime();
    }

    /**
     * @param
     * @param
     * @return
     * @throws ParseException
     */
    public static List<Date> findDates(String start, String end) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date dBegin = sdf.parse(start);
        Date dEnd = sdf.parse(end);

        List<Date> lDate = new ArrayList<Date>();
        lDate.add(dBegin);
        Calendar calBegin = Calendar.getInstance();
        // 使用给定的 Date 设置此 Calendar 的时间
        calBegin.setTime(dBegin);
        Calendar calEnd = Calendar.getInstance();
        // 使用给定的 Date 设置此 Calendar 的时间
        calEnd.setTime(dEnd);
        // 测试此日期是否在指定日期之后
        while (dEnd.after(calBegin.getTime())) {
            // 根据日历的规则，为给定的日历字段添加或减去指定的时间量
            calBegin.add(Calendar.DAY_OF_MONTH, 1);
            lDate.add(calBegin.getTime());
        }
        return lDate;
    }

    public static int differentDays(String end, String start, String pattern) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(parse(end, pattern));
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        Date date1 = calendar.getTime();
        calendar.setTime(parse(start, pattern));
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        Date date2 = calendar.getTime();
        long l = (date1.getTime() - date2.getTime()) / (1000 * 3600 * 24);
        return Math.abs((int) l);
    }

    /**
     * date1比date2多的天数
     *
     * @param date1
     * @param date2
     * @return
     */
    public static int differentDays(Date date1, Date date2) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date1);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        date1 = calendar.getTime();
        calendar.setTime(date2);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        date2 = calendar.getTime();
        long l = (date1.getTime() - date2.getTime()) / (1000 * 3600 * 24);
        return Math.abs((int) l);
    }

    /**
     * date1比date2多的天数
     *
     * @param date1
     * @param date2
     * @return
     */
    public static int differentDaysTwo(Date date1, Date date2) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date1);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        date1 = calendar.getTime();
        calendar.setTime(date2);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        date2 = calendar.getTime();
        long l = (date1.getTime() - date2.getTime()) / (1000 * 3600 * 24);
        return (int) l;
    }

    /**
     * date1比date2多的天数
     *
     * @param date1
     * @param date2
     * @return
     */
    public static int differentDaysThree(Date date1, Date date2) {
        long l = (date1.getTime() - date2.getTime()) / (1000 * 3600 * 24);
        return (int) l;
    }

    public static int differentStrDays(String end, String start) {
        Date date11 = DateUtil.parse(end, DateUtil.STANDARD_DATE_PATTERN);
        Date date22 = DateUtil.parse(start, DateUtil.STANDARD_DATE_PATTERN);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date11);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        date11 = calendar.getTime();
        calendar.setTime(date22);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        date22 = calendar.getTime();
        long l = (date11.getTime() - date22.getTime()) / (1000 * 3600 * 24);
        return (int) l;
    }

    public static int differentStrDaysYYMMDD(String end, String start) {
        Date date11 = DateUtil.parse(end, DateUtil.DATE_PATTERN);
        Date date22 = DateUtil.parse(start, DateUtil.DATE_PATTERN);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date11);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        date11 = calendar.getTime();
        calendar.setTime(date22);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        date22 = calendar.getTime();
        long l = (date11.getTime() - date22.getTime()) / (1000 * 3600 * 24);
        return (int) l;
    }

    /**
     * 计算给定日期的下一个月
     *
     * @param date
     * @param month
     * @return
     */
    public static Date nextMonth(Date date, int month) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.MONTH, month);
        return calendar.getTime();

    }

    /**
     * 计算给定日期的下一个月
     *
     * @param date
     * @param month
     * @return
     */
    public static String nextMonth(String date, int month) {
        Date date1 = DateUtil.parse(date, DateUtil.STANDARD_DATE_PATTERN);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date1);
        calendar.add(Calendar.MONTH, month);
        return DateUtil.formatDate(calendar.getTime(), DateUtil.STANDARD_DATE_PATTERN_YM);

    }

    public static Date getBeginDayOfTomorrow(Date date, int day) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.setTime(date);
        calendar.add(Calendar.DAY_OF_MONTH, day);
        return calendar.getTime();
    }

    public static int getMonthOfDay(Date date) {
        try {
            String substring = formatDate(date, DateUtil.DATE_PATTERN).substring(6, 8);
            if (substring.startsWith("0")) {
                return Integer.valueOf(substring.substring(1, 2));
            } else {
                return Integer.valueOf(substring);
            }
        } catch (Throwable throwable) {

        }
        return 0;
    }

    /**
     * 获得day天前日期
     *
     * @param date
     * @param day
     * @return
     */
    public static String getDate(Date date, int day) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DAY_OF_MONTH, (0 - day));
        date = calendar.getTime();
        return DateUtil.formatDate(date);
    }

    /**
     * 获得day天hou日期
     *
     * @param date
     * @param day
     * @return
     */
    public static String getDayDateStr(Date date, int day) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DAY_OF_MONTH, day);
        date = calendar.getTime();
        return DateUtil.formatDate(date);
    }

    /**
     * 获得day后日期
     *
     * @param date
     * @param day
     * @return
     */
    public static Date getDayDate(Date date, int day) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DAY_OF_MONTH, day);
        date = calendar.getTime();
        return date;
    }

    /**
     * 获得day后日期
     *
     * @param date
     * @param day
     * @return
     */
    public static Date getMonthDate(Date date, int day) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.MONTH, day);
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        date = calendar.getTime();
        return date;
    }

    /**
     * 获得date时间的hour小时
     *
     * @param date
     * @param hour
     * @return
     */
    public static Date getHourDate(Date date, int hour) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.HOUR, hour);
        date = calendar.getTime();
        return date;
    }

    /**
     * 获得date时间的minute分钟
     *
     * @param date
     * @param minute
     * @return
     */
    public static Date getMinuteDate(Date date, int minute) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.MINUTE, minute);
        date = calendar.getTime();
        return date;
    }

    /**
     * 获得day天hou日期
     *
     * @param date
     * @param day
     * @return
     */
    public static String getDayDate(String date, int day) {
        Date date1 = DateUtil.parse(date, DateUtil.STANDARD_DATE_PATTERN);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date1);
        calendar.add(Calendar.DAY_OF_MONTH, day);
        date1 = calendar.getTime();
        return DateUtil.formatDate(date1);
    }

    /**
     * 获取当前
     *
     * @return
     */
    public static int getWeekofday() {
        Calendar c = Calendar.getInstance();
        int i = c.get(Calendar.DAY_OF_WEEK) - 1;
        if (i == 0)
            return 7;
        return i;
    }

    /**
     * 计算两个日期相差年数
     *
     * @param startDate 小一些的日期
     * @param endDate   大一些的日期
     * @return
     */
    public static int yearDateDiff(String startDate, String endDate) {
        Calendar calBegin = Calendar.getInstance(); //获取日历实例
        Calendar calEnd = Calendar.getInstance();
        calBegin.setTime(stringTodate(startDate, "dd-MM-yyyy")); //字符串按照指定格式转化为日期
        calEnd.setTime(stringTodate(endDate, "dd-MM-yyyy"));
        int age = calEnd.get(Calendar.YEAR) - calBegin.get(Calendar.YEAR);
//        if (calEnd.get(Calendar.DAY_OF_YEAR) < calBegin.get(Calendar.DAY_OF_YEAR)) {
//            age = age - 1;
//        }
        return age;
    }

    /**
     * 计算两个日期相差年数
     *
     * @param startDate 小一些的日期
     * @param endDate   大一些的日期
     * @return
     */
    public static int yearDateDiff(Date startDate, Date endDate) {
        Calendar calBegin = Calendar.getInstance(); //获取日历实例
        Calendar calEnd = Calendar.getInstance();
        calBegin.setTime(startDate); //字符串按照指定格式转化为日期
        calEnd.setTime(endDate);
        int age = calEnd.get(Calendar.YEAR) - calBegin.get(Calendar.YEAR);
//        if (calEnd.get(Calendar.DAY_OF_YEAR) < calBegin.get(Calendar.DAY_OF_YEAR)) {
//            age = age - 1;
//        }
        return age;
    }

    /**
     * 字符串按照指定格式转化为日期
     */
    public static Date stringTodate(String dateStr, String formatStr) {
        // 如果时间为空则默认当前时间
        Date date = null;
        SimpleDateFormat format = new SimpleDateFormat(formatStr);
        if (dateStr != null && !"".equals(dateStr)) {
            String time = "";
            try {
                Date dateTwo = format.parse(dateStr);
                time = format.format(dateTwo);
                date = format.parse(time);
            } catch (ParseException e) {
                e.printStackTrace();
            }

        } else {
            String timeTwo = format.format(new Date());
            try {
                date = format.parse(timeTwo);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        return date;
    }

    /**
     * 获取某个日期的 当月 第一天 0点0分0秒，如：2018-01-01 00:00:00
     */
    public static String getMonthDate0h0m0s(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        date = calendar.getTime();
        return formatDate(date, STANDARD_DATETIME_PATTERN);
    }

    /**
     * 获取某个日期的 当月 第最后一天 23点59分59秒，如：2018-01-31 23:59:59
     */
    public static String getMonthDate23h59m59s(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.DATE, 1);
        calendar.roll(Calendar.DATE, -1);
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        date = calendar.getTime();
        return formatDate(date, STANDARD_DATETIME_PATTERN);
    }

    /**
     * 获取某个日期的 0点0分0秒，如：2018-01-02 00:00:00
     *
     * @param date
     * @return 如：2018-01-02 00:00:00
     */
    public static String getDate0h0m0s(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        date = calendar.getTime();
        return formatDate(date, STANDARD_DATETIME_PATTERN);
    }

    /**
     * 获取某个日期的 0点0分0秒，如：2018-01-02 00:00:00
     *
     * @param date
     * @return 如：2018-01-02 00:00:00
     */
    public static String getDate23h59m59s(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        date = calendar.getTime();
        return formatDate(date, STANDARD_DATETIME_PATTERN);
    }

    /**
     * 获取某个日期的 0点0分0秒，如：2018-01-02 00:00:00
     *
     * @param date
     * @return 如：2018-01-02 00:00:00
     */
    public static Date getDate23h59m59sDate(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        return calendar.getTime();
    }

    /**
     * 获取日期的小时
     *
     * @param date
     * @return
     */
    public static int getHourOfDay(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar.get(Calendar.HOUR_OF_DAY);
    }

    /**
     * 给某个日期加上固定的小时
     *
     * @param date
     * @return
     */
    public static Date getPlusDateHour(Date date, int hour) {

        return new Date(date.getTime() + hour * 60 * 60 * 1000);
    }

    /**
     * 获取当前日期是星期几<br>
     *
     * @param dt
     * @return 当前日期是星期几
     */
    public static String getWeekOfDate(Date dt) {
        String[] weekDays = {"星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六"};
        Calendar cal = Calendar.getInstance();
        cal.setTime(dt);
        int w = cal.get(Calendar.DAY_OF_WEEK) - 1;
        if (w < 0)
            w = 0;
        return weekDays[w];
    }

    /**
     * 时间间隔：单位为天，保留4位小数，计算精确到秒；
     */
    public static double getQuot(Date startDate, Date endDate, int number) {
        String retQuot = "-1";
        try {
            if (null == startDate || null == endDate) {
                return -1;
            }
            long timeout = endDate.getTime() - startDate.getTime();
            double quot = 0.0;
            quot = ((double) timeout) / 1000 / 60 / 60 / 24;

            DecimalFormat formater = new DecimalFormat();
            formater.setMaximumFractionDigits(number);
            formater.setGroupingSize(0);
            formater.setRoundingMode(RoundingMode.HALF_UP);
            retQuot = formater.format(quot);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Double.valueOf(retQuot);
    }

    /**
     * 时间间隔：单位为天，保留4位小数，计算精确到秒；
     */
    public static double getQuotCount(Date startDate, Date endDate, int number, int count) {
        String retQuot = "";
        try {
            long timeout = endDate.getTime() - startDate.getTime();
            double quot = 0.0;
            quot = ((double) timeout) / 1000 / 60 / 60 / 24;
            quot = quot / count;
            DecimalFormat formater = new DecimalFormat();
            formater.setMaximumFractionDigits(number);
            formater.setGroupingSize(0);
            formater.setRoundingMode(RoundingMode.HALF_UP);
            retQuot = formater.format(quot);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Double.valueOf(retQuot);
    }

    public static double getSubDateSeconds(Date startDate, Date endDate) {
        if (startDate == null || endDate == null)
            return -1111;
        long seconds = (endDate.getTime() - startDate.getTime()) / 1000;
        BigDecimal divide = new BigDecimal(seconds).divide(new BigDecimal(24 * 60 * 60), 4, BigDecimal.ROUND_HALF_UP);
        return divide.doubleValue();
    }

    public static void main(String[] args) {

        boolean b = DateUtil.differentDays(new Date(), DateUtil.parse("2020-05-07", DateUtil.STANDARD_DATE_PATTERN)) >= Integer.valueOf("45");
        int differentStrDays = DateUtil.differentStrDays(DateUtil.formatDate(new Date(), DateUtil.STANDARD_DATE_PATTERN), "2018-12-07");
        Date dayDate = DateUtil.getDayDate(new Date(), 1);
        boolean matches = "HhTtt h".matches("^[A-Za-z][A-Za-z\\s]*[A-Za-z]$");

        String str = DecimalFormat.getNumberInstance().format(1245600000);
        String replace = str.replace(",", ".");
        String currecy = NumberFormat.getCurrencyInstance().format(1245600000);

        String currecy1 = NumberFormat.getCurrencyInstance().format(1245600000);
    }

    public static Date localToUTC(Date localDate) {
		/*SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date localDate= null;
		try {
			localDate = sdf.parse(localTime);
		} catch (ParseException e) {
			e.printStackTrace();
		}*/
        long localTimeInMillis = localDate.getTime();
        /** long时间转换成Calendar */
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(localTimeInMillis);
        /** 取得时间偏移量 */
        int zoneOffset = calendar.get(java.util.Calendar.ZONE_OFFSET);
        /** 取得夏令时差 */
        int dstOffset = calendar.get(java.util.Calendar.DST_OFFSET);
        /** 从本地时间里扣除这些差量，即可以取得UTC时间*/
        calendar.add(java.util.Calendar.MILLISECOND, -(zoneOffset + dstOffset));
        /** 取得的时间就是UTC标准时间 */
        Date utcDate = new Date(calendar.getTimeInMillis());
        return utcDate;
    }


    public static String getISODate(Date date) {
        TimeZone tz = TimeZone.getDefault();
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        df.setTimeZone(tz);
        String nowAsISO = df.format(date);
        System.out.println(nowAsISO);
        return nowAsISO;
    }

    public static int getIntYear(Date date) {
        return date.getYear();
    }
}

