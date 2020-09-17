package com.pkm.pkmdb.common;

import org.apache.logging.log4j.util.Strings;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public final class DateTimeUtil {

    private DateTimeUtil() {
    }

    /**
     * 一天（毫秒）
     */
    public static final long ONE_DAY_MILSEC = 24 * 60 * 60 * 1000;

    public static final long FIRST_DAY_OFFSET_MILSEC = 8 * 60 * 60 * 1000;

    public static final long ONE_DAY_SEC = 24 * 60 * 60;

    public static final long ONE_HOUR_MIL_SEC = 60 * 60 * 1000;

    public static final long ONE_SECOND_MIL_SEC = 1000;

    private static final String BLANK = " ";
    private static final String DATE_FORMAT_STRING = "yyyy-MM-dd HH:mm:ss";
    private static final int ONE_YEAR_MONTH_TOTAL=12;
    private static final int ONE_MONTH_MAX_DAY=31;

    /**
     * 获取当前年份
     *
     * @return
     */
    public static int getYear() {
        return Calendar.getInstance().get(Calendar.YEAR);
    }

    /**
     * 计算2个日期相差多少小时 (d1-d2)
     *
     * @param d1
     * @param d2
     * @return
     */
    public static double dateTimeDiff(Date d1, Date d2) {
        return (double) dateMilliSecondDiff(d1, d2) / (double) ONE_HOUR_MIL_SEC;
    }

    /**
     * 死算2个日期相差多少天(d1-d2)
     *
     * @param d1
     * @param d2
     * @return
     */
    public static double dayTimeDiff(Date d1, Date d2) {
        return (double) dateMilliSecondDiff(d1, d2) / (double) ONE_DAY_MILSEC;
    }

    /**
     * 计算2个日期相差多少秒(d1-d2)
     *
     * @param d1 开始日期
     * @param d2 结束日期
     * @return
     */
    public static double secondTimeDiff(Date d1, Date d2) {
        assertDateParamsRequired(d1, d2);
        return (double) dateMilliSecondDiff(d1, d2) / (double) ONE_SECOND_MIL_SEC;
    }

    /**
     * 判断两个日期是否相差几年
     *
     * @param d1 开始日期
     * @param d2 结束日期
     * @param years 年数
     * @return true 表示相差小于年数 ，false 表示相差大于年数,精确度为天
     */
    public static boolean isDateTimeDiff(Date d1, Date d2, int years) {
        assertDateParamsRequired(d1, d2);
        Calendar testTimeCalendar = Calendar.getInstance();
        testTimeCalendar.setTime(d1);
        testTimeCalendar.add(Calendar.YEAR, years);
        return DateTimeUtil.dayTimeDiff(d2, testTimeCalendar.getTime()) < 1;
    }
    
    /**
     * 计算的两时间相差的年数
     * @param d1
     * @param d2
     * @return
     */
    public static int yearTimeDiff(Date d1, Date d2){
        assertDateParamsRequired(d1, d2);
        Calendar c1 = Calendar.getInstance();
        c1.setTime(d1);
        Calendar c2 = Calendar.getInstance();
        c2.setTime(d2);
        return c1.get(Calendar.YEAR)-c2.get(Calendar.YEAR);
    }

    /**
     * 计算2个日期相差多少 天 (d1-d2)
     *
     * @param d1
     * @param d2
     * @return
     */
    public static long dateDayDiff(Date d1, Date d2) {
        long millis1 = d1.getTime() + FIRST_DAY_OFFSET_MILSEC;
        long millis2 = d2.getTime() + FIRST_DAY_OFFSET_MILSEC;
        long day1 = millis1 / ONE_DAY_MILSEC;
        long day2 = millis2 / ONE_DAY_MILSEC;
        return day1 - day2;
    }

    /**
     * 计算2个日期相差毫秒数 (d1-d2)
     *
     * @param d1
     * @param d2
     * @return
     */
    public static long dateMilliSecondDiff(Date d1, Date d2) {
        long millis1 = d1.getTime();
        long millis2 = d2.getTime();
        return millis1 - millis2;
    }

    /**
     * 以"天"为单位计算两个日期间的间隔(endDate - beginDate)<br>
     * <br>
     * 例如：<br>
     * 2010-10-10 14:04:00 与 2010-10-10 23:59:59的间隔为 0 <br>
     * 2010-10-10 23:59:59 与 2010-10-11 00:00:00的间隔为 1 <br>
     * 2010-10-11 00:00:00 与 2010-10-10 23:59:59的间隔为 -1 <br>
     *
     * @author michael.yangf
     * @param beginDate
     * @param endDate
     * @return 天数, 如果beginDate或者endDate为Null则会抛出运行时异常
     */
    public static long countLoanTerm(Date beginDate, Date endDate) {
        return DateTimeUtil.dateDayDiff(endDate, beginDate);
    }

    /**
     * 获取标准的时间,格式为"yyyy-mm-dd"，去除param中的时分秒和毫秒信息<br>
     * 如果输入参数为null,则返回null<br>
     * <br>
     * 输入参数不会被修改,返回的是新的对象
     *
     * @author michael.yangf
     * @param param
     * @return
     */
    public static Date getStandardDate(Date param) {
        if (null == param) {
            return null;
        }

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(param);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        return calendar.getTime();
    }

    /**
     * 返回年月日格式 yyyy-MM-DD
     *
     * @param date
     * @return
     */
    public static String getYMD(Date date) {
        SimpleDateFormat formate = new SimpleDateFormat("yyyy-MM-dd");
        return formate.format(date);
    }

    /**
     * 返回年月日格式 yyyy年MM月DD日
     *
     * @param date
     * @return
     */
    public static String getYMDZH(Date date) {
        SimpleDateFormat formate = new SimpleDateFormat("yyyy年MM月dd日");
        return formate.format(date);
    }

    public static String getYMDHMS(Date date) {
        SimpleDateFormat formate = new SimpleDateFormat(DATE_FORMAT_STRING);
        return formate.format(date);
    }

    public static String getFormatDate(Date date, String format) {
        if (Strings.isBlank(format)) {
            return new SimpleDateFormat(DATE_FORMAT_STRING).format(date);
        } else {
            return new SimpleDateFormat(format).format(date);
        }
    }

    /**
     * 解析String为Date<br>
     * String的格式要求为"yyyy-MM-dd HH:mm:ss"<br>
     *
     * @param timeDate
     * @return 解析完毕的Date数据，如果发生解析异常则返回null
     */
    public static Date parse2Date(String timeDate) {

        SimpleDateFormat formate = new SimpleDateFormat(DATE_FORMAT_STRING);
        Date date;
        try {
            date = formate.parse(timeDate);
            return date;
        } catch (ParseException e) {
            return null;
        }
    }

    /**
     * 按日偏移,计算source指定日期的days天后的日期<Br>
     *
     * @param source, 要求非空
     * @param days, 天数,可以为负
     * @return 新创建的Date对象
     * @author michael.yangf
     */
    public static Date addDays(Date source, int days) {
        return addDate(source, Calendar.DAY_OF_MONTH, days);
    }

    /**
     * 按月偏移,计算source指定日期的months月后的日期<Br>
     *
     * @param source, 要求非空
     * @param months, 月数,可以为负
     * @return 新创建的Date对象
     * @author michael.yangf
     * @since 1.3.0
     */
    public static Date addMonths(Date source, int months) {
        return addDate(source, Calendar.MONTH, months);
    }

    /**
     * 按年偏移,计算source指定日期的years年后的日期<Br>
     *
     * @param source, 要求非空
     * @param years, 年数,可以为负
     * @return 新创建的Date对象
     * @author michael.yangf
     * @since 1.3.0
     */
    public static Date addYears(Date source, int years) {
        return addDate(source, Calendar.YEAR, years);
    }

    /**
     * 计算两个日期的间隔月份
     *
     * @param beginDate
     * @param endDate
     * @return endDate - beginDate的间隔月份
     */
    public static int calMonthDiff(Date beginDate, Date endDate) {

        Calendar beginCal = Calendar.getInstance();
        beginCal.setTime(beginDate);

        Calendar endCal = Calendar.getInstance();
        endCal.setTime(endDate);

        int yearDiff = endCal.get(Calendar.YEAR) - beginCal.get(Calendar.YEAR);
        int monthDiff = ONE_YEAR_MONTH_TOTAL * yearDiff + endCal.get(Calendar.MONTH) - beginCal.get(Calendar.MONTH);

        return monthDiff;
    }

    /**
     * 计算两个日期的间隔月份
     *
     * @param beginDate
     * @param endDate
     * @return endDate - beginDate的间隔月份
     */
    public static int monthDiff(Date beginDate, Date endDate) {
        if (beginDate == null || endDate == null) {
            return 0;
        }
        Calendar beginCal = Calendar.getInstance();
        beginCal.setTime(beginDate);

        Calendar endCal = Calendar.getInstance();
        endCal.setTime(endDate);

        int yearDiff = endCal.get(Calendar.YEAR) - beginCal.get(Calendar.YEAR);
        int monthDiff = ONE_YEAR_MONTH_TOTAL * yearDiff + endCal.get(Calendar.MONTH) - beginCal.get(Calendar.MONTH);

        return monthDiff;
    }

    /**
     * 判断当前日期是否为该月的最后一天
     *
     * @param date
     * @return
     */
    public static boolean isLastDayOfMonth(Date date) {
        Calendar cald = Calendar.getInstance();
        cald.setTime(date);
        int today = cald.get(Calendar.DAY_OF_MONTH);
        int lastDayOfMonth = cald.getMaximum(Calendar.DAY_OF_MONTH);
        return today == lastDayOfMonth;

    }

    /**
     * 判断当前日期是否为该月的第一天
     *
     * @param date
     * @return
     */
    public static boolean isFirstDayOfMonth(Date date) {
        Calendar cald = Calendar.getInstance();
        cald.setTime(date);
        int today = cald.get(Calendar.DAY_OF_MONTH);
        return today == 1;

    }

    /**
     * 只比较时间而忽略日期。 如2010-01-01 12:00:00 与 2010-01-02
     * 11:00:00比较返回true，因为12点比11点晚。
     *
     * @param curDate
     * @param baseDate
     * @return
     */
    public static boolean laterThanBaseIgnoreDate(Date curDate, Date baseDate) {
        long millis1 = curDate.getTime() + FIRST_DAY_OFFSET_MILSEC;
        long millis2 = baseDate.getTime() + FIRST_DAY_OFFSET_MILSEC;
        long curTime = millis1 % ONE_DAY_MILSEC;
        long baseTime = millis2 % ONE_DAY_MILSEC;

        return curTime > baseTime;
    }

    /**
     * 根据参数指定的年月日信息生成Date对象，其时分秒、毫秒均为0<br>
     * 注意，和Calendar类型的月份数不一样，本方法的month参数的有效范围是1-12<br>
     *
     * @param year
     * @param month
     * @param date
     * @return 如果month参数不合法，返回null
     */
    public static Date getDate(int year, int month, int date) {

        if (month < 1 || month > ONE_YEAR_MONTH_TOTAL) {
            return null;
        }

        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month - 1, date, 0, 0, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        return calendar.getTime();
    }

    /**
     * @param date
     * @param format
     * @return
     */
    public static Date getDateByString(String date, String format) {
        SimpleDateFormat dateFormat;
        if (Strings.isBlank(format)) {
            dateFormat = new SimpleDateFormat("yyyyMMdd");
        } else {
            dateFormat = new SimpleDateFormat(format);
        }
        if (date == null) {
            return null;
        }
        try {
            return dateFormat.parse(date);
        } catch (ParseException ex) {
            throw new IllegalArgumentException("The date format is not correct", ex);
        }
    }

    /**
     * 获取指定日期的DAY_OF_MONTH信息<br>
     *
     * @param date
     * @return 日期的DAY_OF_MONTH信息
     * @author michael.yangf
     * @since 2.0.0
     */
    public static short getDayOfMonth(Date date) {
        assertDateParamsRequired(date);

        Calendar c = Calendar.getInstance();
        c.setTime(date);

        return (short) c.get(Calendar.DAY_OF_MONTH);
    }

    /**
     * 接受一个日期，然后返回这个日期所属的日历年的最后一天。 举例来说，如果传入"2012-06-01", 那么返回结果是"2012-12-31"
     *
     * @author xiaobing.songxb
     * @param date
     * @return
     */

    public static Date getLastDayOfYear(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date == null ? new Date() : date);
        calendar.set(Calendar.MONTH, ONE_YEAR_MONTH_TOTAL-1); // calendar's month is 0-based, so we minus it by 1
        calendar.set(Calendar.DATE, ONE_MONTH_MAX_DAY);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTime();
    }
    
    /**
     * 获取当前时间的月份中大天数
     * @param currentDate
     * @return
     */
    public static int getMaxDayNum(Date currentDate) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(currentDate);
        return calendar.getActualMaximum(Calendar.DAY_OF_MONTH);

    }  
    /**
     * 接受一个日期，然后返回这个日期所属的日历月的最后一天。 举例来说，如果传入"2012-06-01", 那么返回结果是"2012-06-31"
     *
     * @author xiaobing.songxb
     * @param date
     * @return
     */

    public static Date getLastDayOfMonth(Date date) {
        Calendar calendar = Calendar.getInstance();
        Date tempDate =(date == null ? new Date() : date);
        calendar.setTime(tempDate);
        calendar.set(Calendar.DATE, getMaxDayNum(tempDate));
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTime();
    }
    /**
     * 接受一个日期，然后返回这个日期所属的日历月的第一天。 举例来说，如果传入"2012-06-15", 那么返回结果是"2012-06-01"
     *
     * @author xiaobing.songxb
     * @param date
     * @return
     */

    public static Date getFirstDayOfMonth(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date == null ? new Date() : date);
        calendar.set(Calendar.DATE, 1);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTime();
    }
    
    public static Date getLastMonthOfCurrentMonth(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date == null ? new Date() : date);        
        calendar.set(Calendar.MONTH, calendar.get(Calendar.MONTH)-1);       
        return calendar.getTime();
    }


    // Add Date并且返回一个新的日历对象
    private static Date addDate(Date date, int calendarField, int amount) {
        assertDateParamsRequired(date);

        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.add(calendarField, amount);
        return c.getTime();
    }

    /**
     * 获取当月的天数
     *
     * @return 天数
     */
    public static int getDaysInCurrentMonth() {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.MONTH, cal.get(Calendar.MONTH) + 1);
        cal.set(Calendar.DAY_OF_MONTH, 1);
        cal.set(Calendar.DATE, cal.get(Calendar.DATE) - 1);
        return cal.get(Calendar.DAY_OF_MONTH);
    }

    /**
     * 用指定的时分秒替换指定日期的时分秒
     *
     * @param date 指定的日期
     * @param time 时分秒,格式[HH:mm:ss],例如23:59:59,09:01:01
     * @return 替换后的日期，如果转化日期异常，返回转化前的日期
     */
    public static Date appendDateAndTime(Date date, String time) {
        try {
            if (date == null || Strings.isBlank(time)) {
                return null;
            }
            String dateString = new SimpleDateFormat("yyyy-MM-dd").format(date);
            dateString = dateString.concat(BLANK).concat(time);
            return new SimpleDateFormat(DATE_FORMAT_STRING).parse(dateString);
        } catch (ParseException e) {
            return date;
        }
    }
    
    /**
     * 获取指定时间的开始时间，比如2014-02-17 00:00：00
     * @param date
     * @return
     */
    public static Date getFirstTimeOfDate(Date date){
        return appendDateAndTime(date,"00:00:00");
    }
    /**
     * 获取指定时间的最后时间，比如2014-02-17 23:59:59
     * @param date
     * @return
     */
    public static Date getLastTimeOfDate(Date date){
        return appendDateAndTime(date,"23:59:59");
    }
    
    /**
     * 将毫秒转换为年月日时分秒
     * 
     * @author GaoHuanjie
     */
    public static String getYearMonthDayHourMinuteSecond(long timeMillis) {
        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("GMT+8"));  
        calendar.setTimeInMillis(timeMillis);
        int year=calendar.get(Calendar.YEAR);
        
        int month=calendar.get(Calendar.MONTH) + 1;
        String mToMonth=null;
        if (String.valueOf(month).length()==1) {
        	mToMonth="0"+month;
        } else {
        	mToMonth=String.valueOf(month);
        }
        
        int day=calendar.get(Calendar.DAY_OF_MONTH);
        String dToDay=null;
        if (String.valueOf(day).length()==1) {
        	dToDay="0"+day;
        } else {
        	dToDay=String.valueOf(day);
        }
        
        int hour=calendar.get(Calendar.HOUR_OF_DAY);
        String hToHour=null;
        if (String.valueOf(hour).length()==1) {
        	hToHour="0"+hour;
        } else {
        	hToHour=String.valueOf(hour);
        }
        
        int minute=calendar.get(Calendar.MINUTE);
        String mToMinute=null;
        if (String.valueOf(minute).length()==1) {
        	mToMinute="0"+minute;
        } else {
        	mToMinute=String.valueOf(minute);
        }
        
        int second=calendar.get(Calendar.SECOND);
        String sToSecond=null;
        if (String.valueOf(second).length()==1) {
        	sToSecond="0"+second;
        } else {
        	sToSecond=String.valueOf(second);
        }
        return  year+ "-" +mToMonth+ "-" +dToDay+ " "+hToHour+ ":" +mToMinute+ ":" +sToSecond; 		
    }


    private static void assertDateParamsRequired(Date... dates) {
        for (Date date : dates) {
            if (date == null) {
                throw new IllegalArgumentException("The Date type parameters are required.");
            }
        }
    }
    
}
