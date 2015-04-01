package com.xy.tickets.util;

import android.util.Log;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateUtils {

	public final static SimpleDateFormat formatter = new SimpleDateFormat(
			"yyyy-MM-dd");
	public final static SimpleDateFormat sdf = new SimpleDateFormat("MM月dd日HH:mm");
	public final static SimpleDateFormat CHINAYYMMDDHHMMSS = new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss");

	public final static SimpleDateFormat serverFormatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	public final static SimpleDateFormat YYYMMDD = new SimpleDateFormat("yyyy-MM-dd");
	public final static SimpleDateFormat DD = new SimpleDateFormat("dd");
	public final static SimpleDateFormat HH = new SimpleDateFormat("HH");
	public final static SimpleDateFormat HHMM = new SimpleDateFormat("HHmm");
	public final static SimpleDateFormat MDM = new SimpleDateFormat("M-d EEE");

	public static Date getDateOfNextWeek() {
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.WEEK_OF_YEAR, 1);
		return cal.getTime();
	}

	public static int subtractDateByYear(Date beginDate, Date endDate) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(beginDate);
		int start = cal.get(Calendar.YEAR);
		cal.setTime(endDate);
		int end = cal.get(Calendar.YEAR);
		return end - start;
	}
	
	public static Date addDateDays(Date date, int days) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.DAY_OF_YEAR, days);
		return cal.getTime();
	}

	public static String formatDate(Date date) {
		try {
			DateFormat formatter = SimpleDateFormat.getDateInstance();
			return formatter.format(date);
		} catch (Exception e) {
			// TODO: handle exception
		}
		return "";
		
	}

    public static String formatSDFDate(Date date) {
		try {
			return sdf.format(date);
		} catch (Exception e) {
			// TODO: handle exception
		}
		return "";

	}

    public static boolean afterNowHHdd(String hhmm){
        if(hhmm == null) return false;
        hhmm = hhmm.replace(":", "");
        int time = Integer.valueOf(hhmm);
        int now = Integer.valueOf(HHMM.format(new Date()));
        return time > now;
    }

	public static String  subtractTime(Date time,Date time2){
		try {
			long begin = time.getTime();
            long end = time2.getTime();
            long period = end - begin;
            if(period > 0){

            }else {
                period = - period;
            }
            if(period > 3600000){
                return (period / 3600000) + "小时";
            }else if(period > 60000){
                return (period / 60000) + "分钟";
            }else {
                return (period / 1000) + "秒";
            }
		} catch (Exception e) {
			// TODO: handle exception
		}
		return "";
		
	}

    public static int  betweenHours(Date time,Date time2){
		try {
			int h1=Integer.valueOf(HH.format(time));
			int h2=Integer.valueOf(HH.format(time2));
			return h2-h1;
		} catch (Exception e) {
			// TODO: handle exception
		}
		return 0;

	}
	public static boolean isBetweenDate(Date objDate, Date startDate,
			Date endDate) {
		long obj = Long.valueOf(formatter.format(objDate));
		long obj1 = Long.valueOf(formatter.format(startDate));
		long obj2 = Long.valueOf(formatter.format(endDate));
		return obj >= obj1 && obj <= obj2;
	}

	public static Date getDateWithoutTime(Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.set(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH),
				cal.get(Calendar.DAY_OF_MONTH), 0, 0, 0);
		return cal.getTime();
	}

	public static int getWeekday(Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		return cal.get(Calendar.DAY_OF_WEEK);
	}
	
	public static String getWeekofDay(String date) {
		if(date != null) {
			SimpleDateFormat lformatter = new SimpleDateFormat(
					"EE");
			return lformatter.format(defaultParse(date));
		}else {
			return null;
		}
	}

	public static String defaultFormat(Date date) {
		if(date == null) return "";
		return formatter.format(date);
	}

	public static Date defaultParse(String str) {
		if(str == null) return  null;
		try {
			return formatter.parse(str);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}
	public static long convertDate2Long(Date date){
		String temp=formatter.format(date);
		try {
			return formatter.parse(temp).getTime();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return 0;
	}
	
	public static boolean isYesterday(String str) {
		Date yesterday = addDateDays(new Date(), -1);
		return (str != null && str.equals(defaultFormat(yesterday)));
	}
	
	public static boolean isYesterday(Date date) {
		return isYesterday(defaultFormat(date));
	}
	
	public static boolean beforeToday(String str) {
		Date date = defaultParse(str);
		return beforeToday(date);
	}
	
	public static boolean beforeToday(Date date) {
		date = defaultParse(defaultFormat(date));
		Date now = defaultParse(defaultFormat(new Date()));
		return now.after(date);
	}
	
	public static String convertServerDate2DefaultFormat(String dateStr) {
		Date date;
		try {
			date = serverFormatter.parse(dateStr);
			return defaultFormat(date);
		} catch (ParseException e) {
			e.printStackTrace();
			return null;
		}
		
	}
	
	public static String convertDate2MMdd(String dateStr, boolean includeMonth) {
		Date date = defaultParse(dateStr);
		date = addDateDays(date, 1);
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		if (includeMonth || cal.get(Calendar.DAY_OF_MONTH) == 1) {
			return dateStr.substring(4, 6) + "-" + dateStr.substring(6);
		} else {
			return dateStr.substring(6);
		}
	}

	public static Calendar getTimeForAlarm(String alarmTime) {

		Calendar cal = Calendar.getInstance();
		cal.setTime(new Date());

		String[] time = alarmTime.split(":");

		if (time.length == 2) {

			Integer hour = Integer.valueOf(time[0]);
			Integer min = Integer.valueOf(time[1]);

			return getTimeForAlarm(hour, min);
		} else {
			return null;
		}
	}

	public static Calendar getTimeForAlarm(int hour, int min) {

		Calendar cal = Calendar.getInstance();
		if(cal.get(Calendar.HOUR_OF_DAY) > hour || (cal.get(Calendar.HOUR_OF_DAY) == hour && cal.get(Calendar.HOUR_OF_DAY) >= min)){
			return null;
		}
		

		cal.set(Calendar.HOUR_OF_DAY, hour);
		cal.set(Calendar.MINUTE, min);
		cal.set(Calendar.SECOND, 0);

		return cal;
	}
	
	public static Calendar calculateTimeForAlarm(int hour, int min) {

		Calendar cal = Calendar.getInstance();
//		if(cal.get(Calendar.HOUR_OF_DAY) > hour || (cal.get(Calendar.HOUR_OF_DAY) == hour && cal.get(Calendar.HOUR_OF_DAY) >= min)){
//			cal.setTime(addDateDays(cal.getTime(), 1));
//		}

		cal.set(Calendar.HOUR_OF_DAY, hour);
		cal.set(Calendar.MINUTE, min);
		cal.set(Calendar.SECOND, 0);

		return cal;
	}
	
	public static String formatHourMinute(Date date){
        if(date == null) return "";

		SimpleDateFormat lformatter = new SimpleDateFormat(
				"HH:mm");
		return lformatter.format(date);
	}
	public static String formatMonthDayHourMinute(Date date){
        if(date == null) return "";

		SimpleDateFormat lformatter = new SimpleDateFormat(
				"MM月dd日HH:mm");
		return lformatter.format(date);
	}
	public static String getMonthDay(String str, boolean linebreak){
        if(str == null) return "";

		Date date = defaultParse(str);
		String format = "";
		if(linebreak){
			format = "MM\n月\ndd\n日";
		}else{
			format = "MM月dd日";
		}
		SimpleDateFormat lformatter = new SimpleDateFormat(
				format);
		return lformatter.format(date);
	}
	
	public static Calendar getTodayWithoutHMS(){

		Calendar today = Calendar.getInstance();
		Calendar resultDate = (Calendar) today.clone();
		resultDate.set(Calendar.HOUR_OF_DAY, 0);
		resultDate.set(Calendar.MINUTE, 0);
		resultDate.set(Calendar.SECOND, 0);
		return resultDate;
	}
	
	public static Calendar getDayByMonth(int month){
		Calendar today = Calendar.getInstance();
		Calendar resultDate = (Calendar) today.clone();
		resultDate.set(Calendar.MONTH, month-1);
		return resultDate;
	}
	public static int getDaysToNextYear(){
		Calendar today = getTodayWithoutHMS();
		Calendar lastDayOfNextYear = getLastDayOfNextYear();
		int days = daysBetween(today,lastDayOfNextYear);
		return days;
	}
	public static boolean isThisYear(Calendar cal){
		Calendar today = Calendar.getInstance();
		boolean isThisYear = today.get(Calendar.YEAR) == cal.get(Calendar.YEAR);
		Log.i("DateUtil", "isThisYear ="+isThisYear);
	    return isThisYear;
	}
	
	public static boolean isThisMonth(int month){
		Calendar today = Calendar.getInstance();
	    return (today.MONTH+1)==month?true:false;
	}

	public static boolean isThisYear(long time){
		Date date = new Date(time);
		Calendar cal = DateToCalendar(date);
		return isThisYear(cal);
	}

	public static Calendar getLastDayOfNextYear(){

		Calendar today = Calendar.getInstance();
		Calendar lastDate = (Calendar) today.clone();
		lastDate.add(Calendar.YEAR,1);//加一个年
		lastDate.set(Calendar.DAY_OF_YEAR, 1);
		lastDate.roll(Calendar.DAY_OF_YEAR, -1);
		lastDate.set(Calendar.HOUR_OF_DAY, 0);
		lastDate.set(Calendar.MINUTE, 0);
		lastDate.set(Calendar.SECOND, 0);
		return lastDate;
	}
	public static Calendar DateToCalendar(Date date){
		Calendar cal=Calendar.getInstance();
		Calendar newCalendar = (Calendar) cal.clone();
		newCalendar.setTime(date);
		return newCalendar;

	}
	/**
	 * 获取两个日期之间的间隔天数（第二个日期必须大于第一个日期）
	 * @param startDay 开始时间
	 * @param endDay 结束时间
	 * @return int 间隔天数
	 */
	public static int daysBetween(Calendar startDay, Calendar endDay){
		Calendar c1 = (Calendar) startDay.clone();
		Calendar c2 = (Calendar) endDay.clone();
		int betweenYears = c2.get(Calendar.YEAR) - c1.get(Calendar.YEAR);
		int betweenDays = c2.get(Calendar.DAY_OF_YEAR)
				- c1.get(Calendar.DAY_OF_YEAR);
		for (int i = 0; i < betweenYears; i++) {
			c1.set(Calendar.YEAR, (c1.get(Calendar.YEAR) + 1));
			betweenDays += c1.getMaximum(Calendar.DAY_OF_YEAR);
		}
		return betweenDays;
	}
	
	public static int getDaysBetweenMonth(int startDay,int startMonth,int endMonth){
		Calendar c1 =Calendar.getInstance();
		c1.set(Calendar.MONTH, startMonth);
		Calendar c2 = Calendar.getInstance();
		c2.set(Calendar.MONTH, endMonth);
		int betweenDays = c2.get(Calendar.DAY_OF_YEAR)
				- c1.get(Calendar.DAY_OF_YEAR)-startDay;
		return betweenDays;
	}
	public static Calendar getTodayAfterDays(int day){

		Calendar today = Calendar.getInstance();
		Calendar result = (Calendar) today.clone();
		result.set(Calendar.HOUR_OF_DAY, 0);
		result.set(Calendar.MINUTE, 0);
		result.set(Calendar.SECOND, 0);
		result.add(Calendar.DAY_OF_YEAR,day);
		Log.i("DateUtil", DateUtils.CHINAYYMMDDHHMMSS.format(DateUtils.CalendarToDate(result)));
		return result;

	}
	public static Date CalendarToDate(Calendar cal){
		Date date = cal.getTime();
		return date;
	}
	// 获得明年
		public static String getNextYearString() {
			String str = "";
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy");
			Calendar today = Calendar.getInstance();
			Calendar lastDate = (Calendar)today.clone();
			lastDate.add(Calendar.YEAR, 1);// 加一个年
			str = sdf.format(lastDate.getTime());
			return str;
		}
		/**
		 * 获取这是哪一年
		 * @return
		 */
		public static String getThisYearString() {
			String str = "";
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy");
			Calendar today = Calendar.getInstance();
			str = sdf.format(today.getTime());
			return str;
		}

		/**
	     * 获取当前日期是星期几<br>
	     *
	     * @param dt
	     * @return 当前日期是星期几
	     */
	    public static String getWeekOfDate(Date dt) {
	        String[] weekDays = {"周日", "周一", "周二", "周三", "周四", "周五", "周六"};
	        Calendar cal = Calendar.getInstance();
	        cal.setTime(dt);

	        int w = cal.get(Calendar.DAY_OF_WEEK) - 1;
	        if (w < 0) {
				w = 0;
			}

	        return weekDays[w];
	    }
	    public static Calendar getFirstDayOfNextYear(){

			Calendar today = Calendar.getInstance();
			Calendar lastDate = (Calendar) today.clone();
			lastDate.add(Calendar.YEAR,1);//加一个年
			lastDate.set(Calendar.DAY_OF_YEAR, 1);
			lastDate.set(Calendar.HOUR_OF_DAY, 0);
			lastDate.set(Calendar.MINUTE, 0);
			lastDate.set(Calendar.SECOND, 0);
			return lastDate;
		}
	    public static int getTheMonthAllDays(){
	    	Calendar cal = Calendar.getInstance();
//	    	cal.set(Calendar.YEAR,2010); 
//	    	cal.set(Calendar.MONTH, 6);//Java月份才0开始算  6代表上一个月7月 
	    	int dateOfMonth = cal.getActualMaximum(Calendar.DATE);
	    	return dateOfMonth;
	    }
	    
	    public static int getTodyDate(){
			Calendar cal =	Calendar.getInstance();
			int day=1;
			try {
				day=Integer.valueOf(DD.format(cal.getTime()));
			} catch (Exception e) {
				// TODO: handle exception
			}
			return day;
		}
	    public static int getWeekOfDates(Calendar cal) {
	        int w = cal.get(Calendar.DAY_OF_WEEK) - 1;
	        if (w < 0) {
				w = 0;
			}

	        return w;
	    }
	    
	    public static String fomateDay(long time){
	    	String str="";
	    	try {
	    		Date date=new Date(time);
	    		str=YYYMMDD.format(date);
			} catch (Exception e) {
				// TODO: handle exception
			}
	    	return str;
	    
	    }
	    
	    public static boolean compareTwoDates(String startDay,String endDay){
	    	try {
	    		long sTime=0L;
	    		long eTime=0L;
	    		if(!Utils.isNull(startDay)&&!Utils.isNull(endDay)){
	    			sTime=formatter.parse(startDay).getTime();
	    			eTime=formatter.parse(endDay).getTime();
	    		}
	    		
	    		return (eTime-sTime)>0?true:false;
			} catch (Exception e) {
				// TODO: handle exception
			}
	    	return false;
	    }
}
