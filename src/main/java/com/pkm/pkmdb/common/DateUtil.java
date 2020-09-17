package com.pkm.pkmdb.common;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * 日期工具类
 */
public class DateUtil {

    private static final Logger logger = LoggerFactory.getLogger(com.pkm.pkmdb.common.DateUtil.class);

    private static int FIRST_DATE_OF_WEEK = Calendar.SUNDAY;
    public static final int BEFORE_START_DATE = -2;// 一个日期早于一个日期区间
    public static final int EQUAL_START_DATE = -1;// 一个日期等于一个日期区间的开始日期
    public static final int BETWEEN_TOW_DATE = 0;// 一个日期在一个日期区间之内
    public static final int EQUAL_END_DATE = 1;// 一个日期等于一个日期区间的结束日期
    public static final int AFTER_END_DATE = 2;// 一个日期晚于一个日期区间
    public static final int TREE_DATE_EQUAL = 3;// 三个日期相等

    // 日期格式
    public static final String ACCOUNT_DATETIME_PATTERN = "yy-MM-dd HH:mm";
    public static final String NORMAL_DATETIME_PATTERN = "yyyy-MM-dd HH:mm:ss";
    public static final String NORMAL_DATETIME_MILLI_PATTERN = "yyyyMMddHHmmssSSS";
    public static final String LL_DATETIME_PATTERN = "yyyyMMddHHmmss";
    public static final String NORMAL_DATE_PATTERN = "yyyy-MM-dd";
    public static final String YEAR_MONTH_PATTERN = "yyyy-MM";
    public static final String MONTH_PATTERN = "MM";
    public static final String DAILY_DATE_PATTERN = "yyyyMMdd";
    public static final String CHINESE_DATE_PATTERN = "MM月dd日";
    public static final String CHINESE_YEAR_DATE_PATTERN = "yyyy年MM月dd日";
    public static final String NORMAL_YEAR_YY = "yyyy";

    /**
     * 格式化起始日期
     * @param beginDateStr
     * @return
     * @throws Exception
     */
    public static String formatBeginDate(String beginDateStr) throws Exception {
        try {
            if(beginDateStr.isEmpty()){
                return null;
            }
            return format(parse(beginDateStr, NORMAL_DATE_PATTERN), NORMAL_DATETIME_PATTERN);
        } catch (Exception e) {
            throw new Exception("日期格式转换错误");
        }
    }

    /**
     * 格式化终止日期
     * @param endDateStr 2019-12-19
     * @return 2019-12-20 00:00:00
     * @throws Exception
     */
    public static String formatEndDate(String endDateStr) throws Exception {
        try {
            if(endDateStr.isEmpty()){
                return null;
            }
            Date date = parse(endDateStr, NORMAL_DATE_PATTERN);
            return format(getBeforeDayDate(date,1), NORMAL_DATETIME_PATTERN);
        } catch (Exception e) {
            throw new Exception("日期格式转换错误");
        }
    }


    /**
     * 不是闰年
     *
     * @param year
     * @return
     */
    public static boolean isNotLeapyear(int year) {
        return (year % 4 != 0 || year % 100 == 0) && year % 400 != 0;
    }

    /**
     * 年
     *
     * @param calendar
     * @return
     */
    public static int getYear(Calendar calendar) {
        return calendar.get(Calendar.YEAR);
    }

    /**
     * 月
     *
     * @param calendar
     * @return
     */
    public static int getMonth(Calendar calendar) {
        return calendar.get(Calendar.MONTH) + 1;
    }

    /**
     * 日
     *
     * @param calendar
     * @return
     */
    public static int getDay(Calendar calendar) {
        return calendar.get(Calendar.DATE);
    }

    /**
     * 验证日期格式
     *
     * @param dateStr
     * @return
     */
    public static boolean verifyDateStrFail(String dateStr) {
        try {
            parse(dateStr, NORMAL_DATE_PATTERN);
        } catch (Exception e) {
            logger.error("日期转换有误");
            return true;
        }
        return false;
    }

    /**
     * 获取一周开始时间
     *
     * @param calendar
     * @return
     */
    public static String getMonday(Calendar calendar) {
        calendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
        return format(calendar.getTime(), NORMAL_DATE_PATTERN);
    }

    /**
     * 获取上一周开始时间
     *
     * @param calendar
     * @return
     */
    public static String getLastMonday(Calendar calendar) {
        calendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
        calendar.set(Calendar.DATE, calendar.get(Calendar.DATE) - 7);
        return format(calendar.getTime(), NORMAL_DATE_PATTERN);
    }

    /**
     * 获取一周结束时间
     *
     * @param calendar
     * @return
     */
    public static String getSunday(Calendar calendar) {
        calendar.set(Calendar.DATE, calendar.get(Calendar.DATE) + 6);
        return format(calendar.getTime(), NORMAL_DATE_PATTERN);
    }

    /**
     * 获取下一周结束时间
     *
     * @param calendar
     * @return
     */
    public static String getNextSunday(Calendar calendar) {
        calendar.set(Calendar.DATE, calendar.get(Calendar.DATE) + 20);
        return format(calendar.getTime(), NORMAL_DATE_PATTERN);
    }


    /**
     * 获取当前月的第一天
     *
     * @return
     */
    public static String getMonthFirstDay(Date date) {
        // 当前月
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
//		calendar.add(Calendar.MONTH, 0);
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        return format(calendar.getTime(), NORMAL_DATE_PATTERN);
    }

    /**
     * 获取当前月的最后天
     *
     * @return
     */
    public static String getMonthLastDay(Date date) {
        // 下个月
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.MONTH, 1);
        calendar.set(Calendar.DAY_OF_MONTH, 0);
        return format(calendar.getTime(), NORMAL_DATE_PATTERN);
    }

    /**
     * 增加月份
     *
     * @param date
     * @param num
     * @return
     */
    public static Date addMonth(Date date, int num) {
        // 下个月
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.MONTH, num);
        return calendar.getTime();
    }

    /**
     * 增加年份
     *
     * @param date
     * @param num
     * @return
     */
    public static Date addYear(Date date, int num) {
        // 下一年
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.YEAR, num);
        return calendar.getTime();
    }

    /**
     * date to string
     *
     * @param date
     * @param pattern
     * @return
     */
    public static String format(Date date, String pattern) {
        if (null == date){
            return "";
        }
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
        return sdf.format(date);
    }

    /**
     * string to date
     *
     * @param date
     * @param pattern
     * @return
     */
    public static Date parse(String date, String pattern) {
        if(date.isEmpty()){
            return null;
        }
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
        try {
            Date d = sdf.parse(date);
            return d;
        } catch (ParseException e) {
            throw new RuntimeException("日期转换错误", e);
        }

    }

    public static Date parse(String date, String ... patterns) {
        if(patterns.length == 1){
            return parse(date, patterns[0]);
        }
        if(date.isEmpty()){
            return null;
        }
        for(String pattern : patterns){
            SimpleDateFormat sdf = new SimpleDateFormat(pattern);
            try {
                Date d = sdf.parse(date);
                return d;
            } catch (ParseException e) {

            }
        }
        logger.info("时间{}转换失败，格式不匹配！:{}", date, patterns);
        return null;
    }

    /**
     * 起止日期间的天数
     *
     * @param startDate
     * @param endDate
     * @return
     */
    public static int differentDays(Date startDate, Date endDate) {
        Calendar startCal = Calendar.getInstance();
        startCal.setTime(startDate);

        Calendar endCal = Calendar.getInstance();
        endCal.setTime(endDate);
        int startDay = startCal.get(Calendar.DAY_OF_YEAR);
        int endDay = endCal.get(Calendar.DAY_OF_YEAR);

        int startYear = startCal.get(Calendar.YEAR);
        int endYear = endCal.get(Calendar.YEAR);
        if (startYear != endYear)   //不同年
        {
            int timeDistance = 0;
            for (int i = startYear; i < endYear; i++) {
                if (i % 4 == 0 && i % 100 != 0 || i % 400 == 0)    //闰年
                {
                    timeDistance += 366;
                } else    //不是闰年
                {
                    timeDistance += 365;
                }
            }

            return timeDistance + (endDay - startDay);
        } else    //同一年
        {
            return endDay - startDay;
        }
    }

    /**
     * 年份比较
     *
     * @param startDate
     * @param endDate
     * @return
     */
    public static int compareYears(Date startDate, Date endDate) {
        Calendar startCal = Calendar.getInstance();
        startCal.setTime(startDate);

        Calendar endCal = Calendar.getInstance();
        endCal.setTime(endDate);

        int startYear = startCal.get(Calendar.YEAR);
        int endYear = endCal.get(Calendar.YEAR);

        return endYear - startYear;
    }

    /**
     * 获取该日期前后几天的日期
     *
     * @param date
     * @param beforeDays 负数表示前几天，正数表示后几天
     * @return
     */
    public static Date getBeforeDayDate(Date date, int beforeDays) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
//        calendar.set(Calendar.DATE, calendar.get(Calendar.DATE) + beforeDays);
        calendar.add(Calendar.DATE, beforeDays);
        return calendar.getTime();
    }

    /**
     * 获取该日期前后几秒的日期
     *
     * @param date
     * @param beforeSeconds 负数表示前几秒，正数表示后几秒
     * @return
     */
    public static Date getBeforeSecondDate(Date date, int beforeSeconds) {
        Calendar calendar = Calendar.getInstance();

        calendar.setTime(date);

        calendar.add(Calendar.SECOND, beforeSeconds);

        return calendar.getTime();
    }

    /**
     * 日期的天数差
     *
     * @param beginDate
     * @param endDate
     * @return
     */
    public static int differentDaysByMillisecond(Date beginDate, Date endDate) {
        int days = (int) ((endDate.getTime() - beginDate.getTime()) / (1000 * 3600 * 24));
        return days;
    }

    /**
     * add(Calendar.DAY_OF_MONTH, -5)
     *
     * @param date
     * @param calendorField
     * @param amount
     * @return
     */
    public static Date add(Date date, int calendorField, int amount) {
        Calendar cal = Calendar.getInstance();

        cal.setTime(date);

        cal.add(calendorField, amount);

        return cal.getTime();
    }

    /**
     * @return Calendar.SUNDAY <br/>
     * Calendar.MONDAY <br/>
     * Calendar.TUESDAY <br/>
     * Calendar.WEDNESDAY <br/>
     * Calendar.THURSDAY <br/>
     * Calendar.FRIDAY <br/>
     * Calendar.SATURDAY <br/>
     */
    public static int getDayOfWeek(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        switch (cal.get(Calendar.DAY_OF_WEEK)) {
            case 1:
                return 7;
            case 2:
                return 1;
            case 3:
                return 2;
            case 4:
                return 3;
            case 5:
                return 4;
            case 6:
                return 5;
            default:
                return 6;
        }
    }

    /**
     * @return Calendar.SUNDAY <br/>
     * Calendar.MONDAY <br/>
     * Calendar.TUESDAY <br/>
     * Calendar.WEDNESDAY <br/>
     * Calendar.THURSDAY <br/>
     * Calendar.FRIDAY <br/>
     * Calendar.SATURDAY <br/>
     */
    public static int getDayOfMouth(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return cal.get(Calendar.DAY_OF_MONTH);
    }

    /**
     * 计算两个日期间相差的天数
     *
     * @param date
     * @param compareDate
     * @return
     * @throws ParseException
     */
    public static long compareTo(Date date, Date compareDate) {
        // 去掉时分秒
        date = parse(format(date, DAILY_DATE_PATTERN), DAILY_DATE_PATTERN);
        compareDate = parse(format(compareDate, DAILY_DATE_PATTERN), DAILY_DATE_PATTERN);

        long a = (date.getTime() - compareDate.getTime())
                / (1000 * 60 * 60 * 24);
        return a;
    }

    /**
     * 计算两个日期间相差的月数
     * @param date1 <String>
     * @param date2 <String>
     * @return int
     * @throws ParseException
     */
    public static int getMonthSpace(Date date1, Date date2) {

        if (null == date1 || null == date2){
            return 0;
        }
        Calendar c1 = Calendar.getInstance();
        Calendar c2 = Calendar.getInstance();

        c1.setTime(date1);
        c2.setTime(date2);

        int result = c2.get(Calendar.MONTH) - c1.get(Calendar.MONTH);

        return result == 0 ? 1 : Math.abs(result);

    }


    /**
     * 判断是否为一周的最后一天(目前配置的是周日为一周的第一天)
     *
     * @param date
     * @return
     */
    public static boolean isEndOfWeek(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.DAY_OF_MONTH, 1);
        int weekDay = cal.get(Calendar.DAY_OF_WEEK);
        return weekDay == FIRST_DATE_OF_WEEK;
    }

    /**
     * 判断时间是否为月末
     *
     * @param nowDate 日期（需要验证的日期）
     * @return boolean true 表示是月末 false 表示不为月末
     */
    public static boolean isMonthEnd(Date nowDate) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(nowDate);
        cal.add(Calendar.DAY_OF_MONTH, 1);
        int day = cal.get(Calendar.DAY_OF_MONTH);
        return day == 1;
    }

    /**
     * 判断时间是否为季末
     *
     * @param nowDate 日期（需要验证的日期）
     * @return boolean true 表示是季末 false 表示不是季末
     */
    public static boolean isQuarterEnd(Date nowDate) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(nowDate);
        int month = cal.get(Calendar.MONTH);
        cal.add(Calendar.DAY_OF_MONTH, 1);
        int day = cal.get(Calendar.DAY_OF_MONTH);
        return day == 1
                && (month == Calendar.MARCH || month == Calendar.JUNE
                || month == Calendar.SEPTEMBER || month == Calendar.DECEMBER);
    }

    /**
     * 判断时间是否为季出
     *
     * @param nowDate 日期（需要验证的日期）
     * @return boolean true 表示是季初 false 表示不是季初
     */
    public static boolean isQuarterBegin(Date nowDate) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(nowDate);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);
        return day == 1
                && (month == Calendar.JANUARY || month == Calendar.APRIL
                || month == Calendar.JULY || month == Calendar.OCTOBER);
    }

    /**
     * 判断时间是否为半年末
     *
     * @param nowDate 日期（需要验证的日期）
     * @return boolean true 表示是半年末 false 表示不是半年末
     */
    public static boolean isHalfYearEnd(Date nowDate) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(nowDate);
        int month = cal.get(Calendar.MONTH);
        cal.add(Calendar.DAY_OF_MONTH, 1);
        int day = cal.get(Calendar.DAY_OF_MONTH);
        return day == 1 && (month == Calendar.JUNE || month == Calendar.DECEMBER);
    }

    /**
     * 判断时间是否为半年出
     *
     * @param nowDate 日期（需要验证的日期）
     * @return boolean true 表示是半年初 false 表示不是半年初
     */
    public static boolean isHalfYearBegin(Date nowDate) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(nowDate);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);
        return day == 1 && (month == Calendar.JANUARY || month == Calendar.JULY);
    }

    /**
     * 判断时间是否为年末
     *
     * @param nowDate 日期（需要验证的日期）
     * @return boolean true 表示是年末 false 表示不为年末
     */
    public static boolean isYearEnd(Date nowDate) {
        return "1231".equals(format(nowDate, "MMdd"));
    }

    /**
     * 判断时间是否为年初
     *
     * @param nowDate 日期（需要验证的日期）
     * @return boolean true 表示是年初 false 表示不为年初
     */
    public static boolean isYearBegin(Date nowDate) {
        return "0101".equals(format(nowDate, "MMdd"));
    }

    /**
     * 获取日期的年月日
     *
     * @return
     */
    public static Calendar getYMD(Date date) {
        String dateStr = format(date, "yyyyMMdd");
        date = parse(dateStr, "yyyyMMdd");
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar;
    }

    /**
     * 判断是否为结算日期
     *
     * @param stlCycle ,stlCycleDay,tranDate
     * @return boolean
     */
    public static boolean chkStlTime(String stlCycle, String stlCycleDay,
                                     Date tranDate) {
        boolean b = false;
        switch (stlCycle.toCharArray()[0]) {
            case '1':
                // 日结
                b = true;
                break;
            case '2':
                // 周结
                String nowDate = String.valueOf(com.pkm.pkmdb.common.DateUtil.getDayOfWeek(tranDate));
                if (nowDate.equals(stlCycleDay)) {
                    b = true;
                }
                break;
            case '3':
                // 月结
                if (stlCycleDay.equals("0")) {
                    // 月末结
                    boolean result = com.pkm.pkmdb.common.DateUtil.isMonthEnd(tranDate);
                    if (result) {
                        b = true;
                    }
                } else {
                    // 非月末结
                    String nowDate1 = String.valueOf(com.pkm.pkmdb.common.DateUtil
                            .getDayOfMouth(tranDate));
                    if (nowDate1.equals(stlCycleDay)) {
                        b = true;
                    }
                }
                break;
            case '4':
                // 季结
                if ("1".equals(stlCycleDay)) {
                    // 季初
                    if (com.pkm.pkmdb.common.DateUtil.isQuarterBegin(tranDate)) {
                        b = true;
                    }
                } else if ("0".equals(stlCycleDay)) {
                    // 季末
                    if (com.pkm.pkmdb.common.DateUtil.isQuarterEnd(tranDate)) {
                        b = true;
                    }
                }
                break;
            case '5':
                // 半年结
                if ("1".equals(stlCycleDay)) {
                    // 半年初
                    if (com.pkm.pkmdb.common.DateUtil.isHalfYearBegin(tranDate)) {
                        b = true;
                    }
                } else if ("0".equals(stlCycleDay)) {
                    // 半年末
                    if (com.pkm.pkmdb.common.DateUtil.isHalfYearEnd(tranDate)) {
                        b = true;
                    }
                }
                break;
            case '6':
                // 年结
                if ("1".equals(stlCycleDay)) {
                    // 年初
                    if (com.pkm.pkmdb.common.DateUtil.isYearBegin(tranDate)) {
                        b = true;
                    }
                } else if ("0".equals(stlCycleDay)) {
                    // 年末
                    if (com.pkm.pkmdb.common.DateUtil.isYearEnd(tranDate)) {
                        b = true;
                    }
                }
                break;
            default:
                break;
        }
        return b;
    }

    // 比较频繁交易前后两笔的时间间隔与指定的某个时间对比，在这个时间段内，是频繁交易
    public static boolean monFreCompare(Date startTime, Date endTime,
                                        int interTime) {
        boolean flag = false;
        long a = (endTime.getTime() - startTime.getTime());
        // 两笔交易的时间间隔<=interTime,是频繁交易
        long interval = a / 1000;
        if (interval <= interTime && interval > 0) {
            flag = true;
        }

        return flag;
    }

    private DateUtil() {
    }

    /**
     * 描述: 判断<firstDate>时间点是否在<secondDate>时间点之前 如果此 firstDate
     * 的时间在参数<secondDate>表示的时间之前，则返回小于 0 的值
     *
     * @param firstDate
     * @param secondDate
     * @return
     * @author chenlc
     */
    public static boolean isBefore(Date firstDate, Date secondDate) {
        return compare(firstDate, secondDate) < 0;
    }

    /**
     * 描述: 判断<firstDate>时间点是否在<secondDate>时间点之后 如果此 firstDate
     * 的时间在参数<secondDate>表示的时间之后，则返回大于 0 的值
     *
     * @param firstDate
     * @param secondDate
     * @return
     * @author chenlc
     */
    public static boolean isAfter(Date firstDate, Date secondDate) {
        return compare(firstDate, secondDate) > 0;
    }

    /**
     * 描述: 比较两个时间点是否相等
     *
     * @param firstDate
     * @param secondDate
     * @return
     * @author chenlc
     */
    public static boolean isEqual(Date firstDate, Date secondDate) {
        return compare(firstDate, secondDate) == 0;
    }

    /**
     * 描述: 比较两个时间点 如果secondDate表示的时间等于此 firstDate 表示的时间，则返回 0 值； 如果此 firstDate
     * 的时间在参数<secondDate>表示的时间之前，则返回小于 0 的值； 如果此 firstDate
     * 的时间在参数<secondDate>表示的时间之后，则返回大于 0 的值
     *
     * @param firstDate
     * @param secondDate
     * @return
     * @author chenlc
     */
    public static int compare(Date firstDate, Date secondDate) {
        Calendar firstCalendar = null;
        if (firstDate != null) {
            firstCalendar = Calendar.getInstance();
            firstCalendar.setTime(firstDate);
        }
        Calendar secondCalendar = null;
        if (firstDate != null) {
            secondCalendar = Calendar.getInstance();
            secondCalendar.setTime(secondDate);
        }
        try {
            return firstCalendar.compareTo(secondCalendar);
        } catch (NullPointerException e) {
            throw new IllegalArgumentException(e);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException(e);
        }
    }

    /**
     * 描述: 时间区间判断
     *
     * @param startDate
     * @param endDate
     * @param inDate
     * @return
     * @author chenlc
     */
    public static int betweenTowDate(Date startDate, Date endDate, Date inDate) {
        if (isBefore(endDate, startDate)) {
            throw new IllegalArgumentException(
                    "endDate can not before startDate!");
        }
        int sflag = compare(inDate, startDate);
        int eflag = compare(inDate, endDate);
        int flag = 0;
        if (sflag < 0) {
            flag = com.pkm.pkmdb.common.DateUtil.BEFORE_START_DATE;
        } else if (sflag == 0) {
            if (eflag == 0) {
                flag = com.pkm.pkmdb.common.DateUtil.TREE_DATE_EQUAL;
            } else {
                flag = com.pkm.pkmdb.common.DateUtil.EQUAL_START_DATE;
            }
        } else if (sflag > 0 && eflag < 0) {
            flag = com.pkm.pkmdb.common.DateUtil.BETWEEN_TOW_DATE;
        } else if (eflag == 0) {
            flag = com.pkm.pkmdb.common.DateUtil.EQUAL_END_DATE;
        } else if (eflag > 0) {
            flag = com.pkm.pkmdb.common.DateUtil.AFTER_END_DATE;
        }
        return flag;
    }

    /**
     * 描述: 分别判断当前日期是与一个日期区间的六种情况比较 （1） 一个日期早于一个日期区间 （2）三个日期相等
     * （3）一个日期等于一个日期区间的开始日期 （4）一个日期在一个日期区间之内 （5）一个日期等于一个日期区间的结束日期
     * （6）一个日期晚于一个日期区间
     *
     * @param startDate
     * @param endDate
     * @return
     * @author chenlc
     */
    public static int betweenTowDate(Date startDate, Date endDate) {
        return betweenTowDate(startDate, endDate, new Date());
    }

    /**
     * @param startDate 日期
     * @param x         小时
     * @return
     */
    public static String addDate(String startDate, int x) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//24小时制
        Date date = null;
        try {
            date = format.parse(startDate);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        if (date == null) return "";
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.HOUR_OF_DAY, x);//24小时制
        date = cal.getTime();
        cal = null;
        return format.format(date);
    }


    /**
     * 获取上个月第一天 初
     * @return
     */
    public static Date lastMonthStart(){
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MONTH, -1);
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        return calendar.getTime();
    }

    /**
     * 获取上个月最后一天
     * @return
     */
    public static Date lastMonthEnd(){
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        calendar.add(Calendar.DATE, -1);
        return calendar.getTime();
    }

    public static String getyyyyMMdd(Date date) {
        if (date == null) {
            return null;
        }
        SimpleDateFormat sdf = new SimpleDateFormat(CHINESE_YEAR_DATE_PATTERN);//CHINESE_YEAR_DATE_PATTERN "yyyy年MM月dd日"
        return sdf.format(date);
    }

    /**
     * 获取某一日期的0点0分0秒和23点59分59秒
     * @param dateStr
     * @param flag
     * @return
     */
    public static String DateToStringBeginOrEnd(String dateStr,Boolean flag) {
        Date date = parse(dateStr, NORMAL_DATE_PATTERN);
        String time = null;
        SimpleDateFormat dateformat1 = new SimpleDateFormat(NORMAL_DATETIME_PATTERN);
        Calendar calendar1 = Calendar.getInstance();
        //获取某一天的0点0分0秒 或者 23点59分59秒
        if (flag == true) {
            calendar1.setTime(date);
            calendar1.set(calendar1.get(Calendar.YEAR), calendar1.get(Calendar.MONTH), calendar1.get(Calendar.DAY_OF_MONTH),
                    0, 0, 0);
            Date beginOfDate = calendar1.getTime();
            time = dateformat1.format(beginOfDate);
        }else{
            Calendar calendar2 = Calendar.getInstance();
            calendar2.setTime(date);
            calendar1.set(calendar2.get(Calendar.YEAR), calendar2.get(Calendar.MONTH), calendar2.get(Calendar.DAY_OF_MONTH),
                    23, 59, 59);
            Date endOfDate = calendar1.getTime();
            time = dateformat1.format(endOfDate);
        }
        return time;
    }

    /**
     * 获取某年某月的第几周的开始日期
     * @param year
     * @param week
     * @return
     */
    public static String getFirstDayOfWeek(int year, int month, int week) {
        Calendar c = new GregorianCalendar();
        c.set(Calendar.YEAR, year);
        c.set(Calendar.MONTH, month);
        c.set(Calendar.DATE, 1);

        Calendar cal = (GregorianCalendar) c.clone();
        cal.add(Calendar.DATE, week * 7);

        return format(cal.getTime(),NORMAL_DATETIME_PATTERN);
    }

    /**
     * 获取某年某月的第几周的结束日期
     *
     * @param year
     * @param week
     * @return
     */
    public static String getLastDayOfWeek(int year, int month, int week) {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.YEAR, year);
        c.set(Calendar.MONTH, month);
        c.set(Calendar.DATE, 1);

        Calendar cal = (Calendar) c.clone();
        cal.add(Calendar.DATE, week * 7);

        return format(cal.getTime(),NORMAL_DATETIME_PATTERN);
    }

    /**
     * 获取指定日期的上个月第一天或上个月最后一天
     * @return
     */
    public static String lastMonthStartAndEnd(String todayTime, Boolean flag){
        Calendar calendar = Calendar.getInstance();
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            calendar.setTime(sdf.parse(todayTime));
            //上个月第一天
            if (flag) {
                calendar.add(Calendar.MONTH, -1);
                calendar.set(Calendar.DAY_OF_MONTH, 1);
                //上个月最后一天
            } else {
                calendar.set(Calendar.DAY_OF_MONTH, 1);
                calendar.add(Calendar.DATE, -1);
                calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH), 23, 59, 59);
            }
        }catch (Exception e){
            logger.error("获取上个月第一天或上个月最后一天异常");
        }
        return format(calendar.getTime(),NORMAL_DATETIME_PATTERN);
    }

    /**
     * 获取指定月的第一天日期和最后一天日期
     * @param todayTime:"2017-03-15"
     * @return arr[0] 第一天日期 ；arr[1]最后一天日期
     * @throws ParseException
     */
    public static String[] getMonthStartAndEndDate(String todayTime) throws ParseException{
        String[] arr = new String[2];
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Calendar c = Calendar.getInstance();
        c.setTime(sdf.parse(todayTime));
        c.set(Calendar.DAY_OF_MONTH, 1);
        arr[0] =sdf.format(c.getTime());
        c.set(Calendar.DAY_OF_MONTH, c.getActualMaximum(Calendar.DAY_OF_MONTH));
        arr[1]=sdf.format(c.getTime());
        return arr;

    }

    /**
     * 获取指定日期所在周的上个周的周一 00:00:00
     *
     * @param todayTime  日期
     */
    public static String getFirstDayOfWeek(String todayTime) {
        Calendar c = Calendar.getInstance();
        try{
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            c.setTime(sdf.parse(todayTime));
            //获取上周
            c.add(Calendar.DATE, -7);
            if (c.get(Calendar.DAY_OF_WEEK) == 1) {
                c.add(Calendar.DAY_OF_MONTH, -1);
                return format(c.getTime(),NORMAL_DATETIME_PATTERN);
            }
            c.add(Calendar.DATE, c.getFirstDayOfWeek() - c.get(Calendar.DAY_OF_WEEK) + 1);
        }catch (Exception e){
            logger.error("获取指定日期所在周的上个周的周一异常");
        }
        return format(c.getTime(),NORMAL_DATETIME_PATTERN);
    }

    /**
     * 获取指定日期所在周的上个周的周日 23:59:59
     *
     * @param todayTime      日期
     */
    public static String getLastDayOfWeek(String todayTime) {
        Calendar c = Calendar.getInstance();
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            c.setTime(sdf.parse(todayTime));
            //获取上周
            c.add(Calendar.DATE, -7);
            // 如果是周日直接返回
            if (c.get(Calendar.DAY_OF_WEEK) == 1) {
                //23:59:59
                c.set(c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH), 23, 59, 59);
                return format(c.getTime(),NORMAL_DATETIME_PATTERN);
            }
            c.add(Calendar.DATE, 7 - c.get(Calendar.DAY_OF_WEEK) + 1);

        }catch (Exception e){
            logger.error("获取指定日期所在周的上个周的周日异常");
        }
        //23:59:59
        c.set(c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH), 23, 59, 59);
        return format(c.getTime(),NORMAL_DATETIME_PATTERN);
    }

    /**
     * 获取当前月的25号 下旬
     * @return
     */
    public static Date currentMonthLate(){
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_MONTH, 25);
        return calendar.getTime();
    }

    /**
     * 获取上个月的25号 下旬
     * @return
     */
    public static Date lastMonthLate(){
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MONTH, -1);
        calendar.set(Calendar.DAY_OF_MONTH, 25);
        return calendar.getTime();
    }

/*   public static void main(String[] args) throws ParseException, BusinessException {

       //System.out.println(getFirstDayOfWeek("2019-12-16"));
       System.out.println(getLastDayOfWeek("2019-12-16"));
        //System.out.println(DateToStringBeginOrEnd("2019-12-19",false));

        // String str = "2000-07-04";
        //1. 创建一个SimpleDateFormat对象，指定一个模式。
//SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日");//CHINESE_YEAR_DATE_PATTERN "yyyy年MM月dd日"
//2. 调用parse方法，将字符串转成Date对象

//3. 打印
//System.out.println(sdf.format(new Date()));

//        System.out.println(format(new Date(), NORMAL_DATETIME_MILLI_PATTERN));
//		String startTime = "2017-02-20 9:40:12";
//        String add = DateUtil.addDate(startTime,24*7);
//		System.out.println("add:{}"+add);
//		Date startDate = DateUtil.parse(add,NORMAL_DATETIME_PATTERN);
//
//		int count = DateUtil.compare(startDate,new Date());
//		System.out.println(count);
//		while (count < 0){
//			add =  DateUtil.addDate(add,24*7);
//
//			if (DateUtil.compare(DateUtil.parse(add,NORMAL_DATETIME_PATTERN),new Date()) > 0) {
//              break;
//			}
//
//		}
//		System.out.println("add2：{}"+add);
//        System.out.println(differentDays(parse("2016-04-04",NORMAL_DATE_PATTERN),new Date()));
//        System.out.println(getBeforeDayDate(new Date(),-3));
   }*/

    public static int currYear(){
        Calendar calendar = Calendar.getInstance();
        return calendar.get(Calendar.YEAR);
    }

    public static int currWeekOfYear(){
        Calendar calendar = Calendar.getInstance();
        calendar.setFirstDayOfWeek(Calendar.MONDAY);
        return calendar.get(Calendar.WEEK_OF_YEAR);
    }

}
