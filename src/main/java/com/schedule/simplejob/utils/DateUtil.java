package com.schedule.simplejob.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @description: 日期工具类
 * @author: zhangbing
 * @create: 2019-12-03 14:14
 **/
public class DateUtil {

    public static final String DEFAULT_DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";
    public static final String DATE_FORMAT_DATE_MINUTE = "yyyy-MM-dd HH:mm";
    public static final String DATE_FORMAT_DATE = "yyyy-MM-dd";
    public static final String NEW_DATE_FORMAT = "yyyyMMddHHmmss";
    public static final String MONTH_DAY = "MM/dd";
    public static final String YEAR = "yy";

    private DateUtil() {
    }

    //使用ThreadLocal代替原来的new SimpleDateFormat 以防并发访问带来的数据转化问题
    private static final ThreadLocal<SimpleDateFormat> dateFormatter = ThreadLocal.withInitial(() -> new SimpleDateFormat(DEFAULT_DATE_FORMAT));

    /**
     * 获取DEFAULT_DATE_FORMAT格式(yyyy-MM-dd HH:mm:ss)的SimpleDateFormat对象
     *
     * @return
     */
    public static SimpleDateFormat getDefaultDateFormat() {
        return getDateFormatByPattern(DEFAULT_DATE_FORMAT);
    }

    /**
     * 获取MINUTE格式(yyyy-MM-dd HH:mm)的的SimpleDateFormat对象
     *
     * @return
     */
    public static SimpleDateFormat getMinuteDateFormat() {
        return getDateFormatByPattern(DATE_FORMAT_DATE_MINUTE);
    }

    /**
     * 获取Simple格式(yyyy-MM-dd)的的SimpleDateFormat对象
     *
     * @return
     */
    public static SimpleDateFormat getSimpleDateFormat() {
        return getDateFormatByPattern(DATE_FORMAT_DATE);
    }

    /**
     * 获取NEW_DATE_FORMAT格式(yyyyMMddHHmmss)的的SimpleDateFormat对象
     *
     * @return
     */
    public static SimpleDateFormat getNewDateFormat() {
        return getDateFormatByPattern(NEW_DATE_FORMAT);
    }

    /**
     * 设置SimpleDateFormat处理的日期格式
     *
     * @param pattern
     * @return
     */
    public static SimpleDateFormat getDateFormatByPattern(String pattern) {
        SimpleDateFormat dateFormat = dateFormatter.get();
        if (pattern != null && !"".equals(pattern)) dateFormat.applyPattern(pattern);
        return dateFormat;
    }

    public static Date parseDate(String dateStr) {
        return parseDate(dateStr, DEFAULT_DATE_FORMAT);
    }

    /**
     * 把字符串格式的日期转化为Date类型日期
     *
     * @param dateStr 字符串格式的日期
     * @param pattern 字符串日期格式
     * @return Date
     */
    public static Date parseDate(String dateStr, String pattern) {
        try {
            return getDateFormatByPattern(pattern).parse(dateStr);
        } catch (ParseException e) {
//            log.error("异常信息{}", e);
            return null;
        }
    }

    public static Long parseLong(String dateStr) {
        return parseLong(dateStr, DEFAULT_DATE_FORMAT);
    }

    /**
     * String 转 long
     *
     * @param dateStr 字符串日期
     * @param pattern 字符串日期的格式
     * @return
     */
    public static Long parseLong(String dateStr, String pattern) {
        Date date = parseDate(dateStr, pattern);
        if (Objects.nonNull(date)) {
            return date.getTime();
        }
        return null;
    }

    /**
     * 获取当前日期
     *
     * @param pattern 日期格式
     * @return
     */
    public static String getNewTime(String pattern) {
        return getTime(getCurrentTimeInLong(), pattern);
    }

    public static Date longToDate(long timeInMillis) {
        return longToDate(timeInMillis, DEFAULT_DATE_FORMAT);
    }

    public static Date longToDate(long timeInMillis, String pattern) {
        return parseDate(getTime(timeInMillis), pattern);
    }

    /**
     * 把时间戳转成字符串
     *
     * @param timeInMillis
     * @param pattern      日期格式
     * @return
     */
    public static String getTime(long timeInMillis, String pattern) {
        return getDateFormatByPattern(pattern).format(new Date(timeInMillis));
    }

    /**
     * 把日期转成字符串
     *
     * @param date
     * @param pattern 日期格式
     * @return
     */
    public static String getTime(Date date, String pattern) {
        return getDateFormatByPattern(pattern).format(date);
    }

    /**
     * 把时间戳转成字符串，用默认的格式
     *
     * @param timeInMillis
     * @return
     */
    public static String getTime(long timeInMillis) {
        return getTime(timeInMillis, DEFAULT_DATE_FORMAT);
    }

    /**
     * 把日期转成字符串，用默认的格式
     *
     * @param date
     * @return
     */
    public static String getTime(Date date) {
        return getTime(date, DEFAULT_DATE_FORMAT);
    }

    /**
     * 获取当前时间戳
     *
     * @return
     */
    public static long getCurrentTimeInLong() {
        return System.currentTimeMillis();
    }

    /**
     * 获取当前时间，字符串格式，默认的格式
     *
     * @return
     */
    public static String getCurrentTimeInString() {
        return getTime(getCurrentTimeInLong());
    }


    /**
     * 获取当前的时间截
     *
     * @return
     */
    public static String getTimestamp() {
        String timeStamp = "";
        long mils = System.currentTimeMillis() / 1000;
        timeStamp = timeStamp + mils;
        return timeStamp;
    }

    /**
     * 获取某天的开始时间
     *
     * @param date 日期
     * @return 某天的开始时间
     */
    public static Date getBeginTimeOfDay(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTime();
    }

    /**
     * 获取某天的结束时间
     *
     * @param date 日期
     * @return 某天的结束时间
     */
    public static Date getEndTimeOfDay(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        calendar.set(Calendar.MILLISECOND, 999);
        return calendar.getTime();
    }

    /**
     * 获取指定日期的年开始时间
     *
     * @param date
     * @return
     */
    public static Date getStartTimeOfYear(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setFirstDayOfWeek(Calendar.MONDAY);
        cal.setTime(date);
        cal.set(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH),
                cal.get(Calendar.DAY_OF_MONTH), 0, 0, 0);
        cal.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
        return cal.getTime();
    }

    /**
     * 获取指定日期的一周结束时间
     *
     * @param date
     * @return
     */
    public static Date getEndTimeOfWeek(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setFirstDayOfWeek(Calendar.MONDAY);
        cal.setTime(date);
        cal.set(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH),
                cal.get(Calendar.DAY_OF_MONTH), 0, 0, 0);
        cal.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
        return cal.getTime();
    }

    /**
     * 获取当前时间所在周的第一天  也就是周一
     *
     * @param time
     */
    public static Date getWeekByOneDate(Date time) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(time);
        // 判断要计算的日期是否是周日，如果是则减一天计算周六的，否则会出问题，计算到下一周去了
        int dayWeek = cal.get(Calendar.DAY_OF_WEEK);// 获得当前日期是一个星期的第几天
        if (1 == dayWeek) {
            cal.add(Calendar.DAY_OF_MONTH, -1);
        }
        cal.setFirstDayOfWeek(Calendar.MONDAY);// 设置一个星期的第一天，按中国的习惯一个星期的第一天是星期一
        int day = cal.get(Calendar.DAY_OF_WEEK);// 获得当前日期是一个星期的第几天
        cal.add(Calendar.DATE, cal.getFirstDayOfWeek() - day);// 根据日历的规则，给当前日期减去星期几与一个星期第一天的差值
        return cal.getTime();
    }

    /**
     * 比较时间大小
     *
     * @param dt1
     * @param dt2 DATE1>DATE2 return 1
     *            DATE1<DATE2 return -1
     *            DATE1=DATE2 return 0
     * @return
     */
    public static int compareDate(Date dt1, Date dt2) {

        try {
            if (dt1.getTime() > dt2.getTime()) {
                //dt1 在dt2前
                return 1;
            } else if (dt1.getTime() < dt2.getTime()) {
                //dt1在dt2后
                return -1;
            } else {
                return 0;
            }
        } catch (Exception exception) {
//            log.error("日期比较错误:{}", exception);
        }
        return 0;
    }


    /**
     * 偏移分钟
     *
     * @param date    日期
     * @param offsite 偏移的分钟数，正数向未来偏移，负数向历史偏移
     * @return
     */
    public static Date offsiteMin(Date date, int offsite) {
        return offsiteDate(date, Calendar.MINUTE, offsite);
    }

    /**
     * 偏移小时
     *
     * @param date    日期
     * @param offsite 偏移的小时数，正数向未来偏移，负数向历史偏移
     * @return
     */
    public static Date offsiteHour(Date date, int offsite) {
        return offsiteDate(date, Calendar.HOUR, offsite);
    }

    /**
     * 偏移天
     *
     * @param date    日期
     * @param offsite 偏移天数，正数向未来偏移，负数向历史偏移
     * @return 偏移后的日期
     */
    public static Date offsiteDay(Date date, int offsite) {
        return offsiteDate(date, Calendar.DAY_OF_YEAR, offsite);
    }

    /**
     * 偏移周
     *
     * @param date    日期
     * @param offsite 偏移周数，正数向未来偏移，负数向历史偏移
     * @return 偏移后的日期
     */
    public static Date offsiteWeek(Date date, int offsite) {
        return offsiteDate(date, Calendar.WEEK_OF_YEAR, offsite);
    }

    /**
     * 偏移月
     *
     * @param date    日期
     * @param offsite 偏移月数，正数向未来偏移，负数向历史偏移
     * @return 偏移后的日期
     */
    public static Date offsiteMonth(Date date, int offsite) {
        return offsiteDate(date, Calendar.MONTH, offsite);
    }

    /**
     * 获取指定日期偏移指定时间后的时间
     *
     * @param date          基准日期
     * @param calendarField 偏移的粒度大小（小时、天、月等）使用Calendar中的常数
     * @param offsite       偏移量，正数为向后偏移，负数为向前偏移
     * @return 偏移后的日期
     */
    public static Date offsiteDate(Date date, int calendarField, int offsite) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(calendarField, offsite);
        return cal.getTime();
    }

    /**
     * 判断日期是否在24H当天内
     *
     * @param inputJudgeDate
     * @return
     */
    public static boolean isToday(Date inputJudgeDate, Date CurrentDate) {
        boolean flag = false;
        if (Objects.isNull(inputJudgeDate)) {
            return flag;
        }
        //获取当前系统时间
        Date nowDate = CurrentDate;
        SimpleDateFormat dateFormat = getDateFormatByPattern(DEFAULT_DATE_FORMAT);
        String format = dateFormat.format(nowDate);
        String subDate = format.substring(0, 10);
        //定义每天的24h时间范围
        String beginTime = subDate + " 00:00:00";
        String endTime = subDate + " 23:59:59";
        Date paseBeginTime = null;
        Date paseEndTime = null;
        try {
            paseBeginTime = dateFormat.parse(beginTime);
            paseEndTime = dateFormat.parse(endTime);

        } catch (ParseException e) {
//            log.error(e.getMessage());
        }
        if (inputJudgeDate.after(paseBeginTime) && inputJudgeDate.before(paseEndTime)) {
            flag = true;
        }
        return flag;
    }

    /**
     * 返回最晚的时间
     *
     * @param date1
     * @param date2
     * @return
     */
    public static Date compareAndSwap(Date date1, Date date2) {
        if (date1 == null || compareDate(date1, date2) == -1) {
            //最晚时间为空或者小于date 设置date为最晚时间
            date1 = date2;
        }
        return date1;
    }

    /**
     * 获取当前时间是周几
     *
     * @param pTime
     * @return
     * @throws Throwable
     */
    public static String dayForWeek(String pTime) throws Throwable {

        SimpleDateFormat format = getDateFormatByPattern(DATE_FORMAT_DATE);

        Date tmpDate = format.parse(pTime);

        Calendar cal = Calendar.getInstance();

        String[] weekDays = {"7", "1", "2", "3", "4", "5", "6"};

        try {

            cal.setTime(tmpDate);

        } catch (Exception e) {

            e.printStackTrace();

        }

        int w = cal.get(Calendar.DAY_OF_WEEK) - 1; // 指示一个星期中的某天。

        if (w < 0)

            w = 0;

        return weekDays[w];

    }

    public static Map<String, Integer> getWeekAndYear(String date) {
        Map<String, Integer> result = new HashMap<String, Integer>();
        Calendar cal = Calendar.getInstance();

        //设置一周的开始,默认是周日,这里设置成星期一
        cal.setFirstDayOfWeek(Calendar.MONDAY);
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat formatMon = new SimpleDateFormat("MM");
        SimpleDateFormat formatYear = new SimpleDateFormat("yyyy");
        Date d = null;
        try {
            d = format.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        cal.setTime(d);
        int month = Integer.valueOf(formatMon.format(d));
        int year = Integer.valueOf(formatYear.format(d));

        int week = cal.get(Calendar.WEEK_OF_YEAR);
        result.put("week", week);
        if (week == 1 && month == 12) {
            result.put("year", year + 1);
        } else {

            result.put("year", year);
        }

        return result;
    }

    /**
     * Author: wangzx
     * Description : 比较时间
     * Date: 11:46 2020/3/30
     * Param: [date, anotherDate]
     * return: java.lang.Boolean
     **/
    public static Boolean compareShortEndTime(Date date, Date anotherDate) {
        int i = date.compareTo(anotherDate);
        return i > 0;
    }
}
