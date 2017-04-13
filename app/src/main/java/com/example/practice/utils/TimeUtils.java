package com.example.practice.utils;

import android.content.Context;
import android.text.TextUtils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

public class TimeUtils {

	public static int DAY_TIME_SPAN = 24 * 3600 * 1000;
	public static int HOUR_TIME_SPAN = 3600 * 1000;
	public static int MINUTES_TIME_SPAN = 60 * 1000;
	public static int MIN_TIME_SPAN = 60 * 1000 * 5;
	public static int SECOND_TIME_SPAN = 1000;

	/**
	 * 将秒个数转化为字符串 ，格式为"00:00"
	 *
	 * @param milliseconds
	 * @return
	 */
	public static String formatTimeFromMillisecondCount(int milliseconds) {
		int seconds = milliseconds / 1000;
		String str = "";
		if (seconds < 60) {
			String s;
			if (seconds < 10) {
				s = "0" + seconds;
			}
			else {
				s = seconds + "";
			}
			str = "00:" + s;
		}
		else if (seconds < 3600) {
			String m, s;
			if (seconds / 60 < 10) {
				m = "0" + seconds / 60;
			}
			else {
				m = seconds / 60 + "";
			}
			if (seconds % 60 < 10) {
				s = "0" + seconds % 60;
			}
			else {
				s = seconds % 60 + "";
			}
			str = m + ":" + s;
		}
		else {
			String h, m, s;
			if (seconds / 3600 < 10) {
				h = "0" + seconds / 3600;
			}
			else {
				h = seconds / 3600 + "";
			}
			if (((seconds % 3600) / 60) < 10) {
				m = "0" + (seconds % 3600) / 60;
			}
			else {
				m = (seconds % 3600) / 60 + "";
			}
			if (seconds % 60 < 10) {
				s = "0" + seconds % 60;
			}
			else {
				s = seconds % 60 + "";
			}
			str = m + ":" + s;
		}
		return str;
	}

	public static String getMonthInfo(Context context, Date time) {
		String strInfo = "";
		if (time != null) {
			SimpleDateFormat simpleDateFormat = null;
			if (context.getResources().getConfiguration().locale.getCountry().equals("US")) {
				simpleDateFormat = new SimpleDateFormat("yyyy/MM/");
			}
			else {
				simpleDateFormat = new SimpleDateFormat("yyyy年MM月");
			}
			strInfo = simpleDateFormat.format(time);
		}
		return strInfo;
	}

	public static String getTimeInfo(Context context, Date time) {
		String strInfo = "";
		if (time != null) {
			Date curDate = new Date();
			if (isSameDate(curDate, time)) {
				SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm");
				strInfo = strInfo + simpleDateFormat.format(time);
			}
			else if (isYesterday(curDate, time)) {
				if (context.getResources().getConfiguration().locale.getCountry().equals("US")) {
					strInfo = "Yesterday  ";
				}
				else {
					strInfo = "昨天  ";
				}

			}
			else {
				SimpleDateFormat simpleDateFormat = null;
				if (context.getResources().getConfiguration().locale.getCountry().equals("US")) {
					simpleDateFormat = new SimpleDateFormat("MM/dd/");
				}
				else {
					simpleDateFormat = new SimpleDateFormat("MM月dd日");
				}

				strInfo = simpleDateFormat.format(time) + " ";
			}
		}
		return strInfo;
	}

	/**
	 * date1 大 返回true
	 *
	 * @param date1
	 * @param date2
	 * @return
	 */
	public static boolean compareDate(String date1, String date2) {

		if (TextUtils.isEmpty(date1) || TextUtils.isEmpty(date2)) {
			return true;

		}
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		try {
			Date dt1 = df.parse(date1);
			Date dt2 = df.parse(date2);

			if (dt1.getTime() > dt2.getTime()) {
				return true;
			}
			return false;
		}
		catch (ParseException e) {
			e.printStackTrace();
		}
		return false;

	}

	/**
	 * date1大 通过
	 *
	 * @return
	 */
	public static boolean compareTime(String date1, String date2) {
		DateFormat df = new SimpleDateFormat("HH : mm");
		try {
			Date dt1 = df.parse(date1);
			Date dt2 = df.parse(date2);
			if (dt1.compareTo(dt2) > 0) {
				return true;
			}
		}
		catch (ParseException e) {
			e.printStackTrace();
		}
		return false;

	}


	public static String getDateInfo(Context context, Date time) {

		String strInfo = "";
		if (time != null) {
			Date curDate = new Date();
			if (context.getResources().getConfiguration().locale.getCountry().equals("US")) {
				if (isSameDate(curDate, time)) {
					strInfo = "Today ";
				}
				else if (isYesterday(curDate, time)) {
					strInfo = "Yesterday ";
				}
				else {
					SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM/dd/");
					strInfo = simpleDateFormat.format(time);
				}
			}
			else {
				if (isSameDate(curDate, time)) {
					strInfo = "今天 ";
				}
				else if (isYesterday(curDate, time)) {
					strInfo = "昨天 ";
				}
				else {
					SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM月dd日");
					strInfo = simpleDateFormat.format(time);
				}
			}
		}
		return strInfo;
	}

	public static String getDateInfo3(Context context, Date time) {

		String strInfo = "";
		if (time != null) {
			Date curDate = new Date();
			if (isSameDate(curDate, time)) {
				if (context.getResources().getConfiguration().locale.getCountry().equals("US")) {
					strInfo = "Today ";
				}
				else {
					strInfo = "今天 ";
				}
			}
			else {
				SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy/MM/dd/HH:mm");
				strInfo = simpleDateFormat.format(time);
			}
		}
		return strInfo;
	}

	public static String getDateInfo6(Date time) {

		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy/MM/dd/HH:mm");
		String strInfo = simpleDateFormat.format(time);
		return strInfo;
	}

	public static String getDateInfo5(Context context, Date time) {

		String strInfo = "";
		if (time != null) {
			Date curDate = new Date();
			if (isSameDate(curDate, time)) {
				SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm");
				String date = simpleDateFormat.format(time);
				if (context.getResources().getConfiguration().locale.getCountry().equals("US")) {
					strInfo = "Today " + date;
				}
				else {
					strInfo = "今天 " + date;
				}
			}
			else {
				SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
				strInfo = simpleDateFormat.format(time);
			}
		}
		return strInfo;
	}

	public static String getDateInfo4(Date time) {

		String strInfo = "";
		if (time != null) {
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy/MM/dd/HH:mm");
			strInfo = simpleDateFormat.format(time);
		}
		return strInfo;
	}

	private static long lastClickTime;

	public static boolean filter() {
		long time = System.currentTimeMillis();
		long timeD = time - lastClickTime;
		if (0 < timeD && timeD < 500) {
			return true;
		}
		lastClickTime = time;
		return false;
	}

	public static String getDateInf3(Context context, Date time) {

		String strInfo = "";
		if (time != null) {
			Date curDate = new Date();
			if (context.getResources().getConfiguration().locale.getCountry().equals("US")) {
				if (isSameDate(curDate, time)) {
					strInfo = "Today ";
				}
				else if (isYesterday(curDate, time)) {
					strInfo = "Yesterday ";
				}
				else {
					SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM/dd");
					strInfo = simpleDateFormat.format(time);
				}
			}
			else {
				if (isSameDate(curDate, time)) {
					strInfo = "今天 ";
				}
				else if (isYesterday(curDate, time)) {
					strInfo = "昨天 ";
				}
				else {
					SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM/dd");
					strInfo = simpleDateFormat.format(time);
				}
			}
		}
		return strInfo;
	}

	public static String getDateInf6(Context context, Date time) {

		String strInfo = "";
		if (time != null) {
			Date curDate = new Date();
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm");
			String date = simpleDateFormat.format(time);
			if (context.getResources().getConfiguration().locale.getCountry().equals("US")) {
				if (isSameDate(curDate, time)) {
					strInfo = "Today/" + date;
				}
				else if (isYesterday(curDate, time)) {
					strInfo = "Yesterday/" + date;
				}
				else {
					strInfo = new SimpleDateFormat("MM/dd/HH:mm").format(time);
				}
			}
			else {
				if (isSameDate(curDate, time)) {
					strInfo = "今天/" + date;
				}
				else if (isYesterday(curDate, time)) {
					strInfo = "昨天/" + date;
				}
				else {
					strInfo = new SimpleDateFormat("MM/dd/HH:mm").format(time);
				}
			}


		}
		return strInfo;
	}

	public static String getDateInfo1(Date time) {
		if (time != null && time.getTime() != 0) {
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy/MM/dd");
			return simpleDateFormat.format(time);
		}
		else {
			return "";
		}
	}

	public static String getDateInfo2(Context context, Date time) {
		if (time != null && context != null) {
			SimpleDateFormat simpleDateFormat = null;
			if (context.getResources().getConfiguration().locale.getCountry().equals("US")) {
				simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
			}
			else {
				simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
			}

			return simpleDateFormat.format(time);

		}
		else {
			return null;
		}
	}

	public static String getTimeInfo1(Date time) {
		if (time != null) {
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm");
			return simpleDateFormat.format(time);

		}
		else {
			return null;
		}
	}

	public static String getTimeInfo2(Date time) {
		if (time != null) {
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
			return simpleDateFormat.format(time);

		}
		else {
			return null;
		}
	}

	public static String getTimeInfo3(Date time) {
		if (time != null) {
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd\nHH:mm:ss");
			return simpleDateFormat.format(time);

		}
		else {
			return null;
		}
	}

	public static String getTimeInfo4(Date time) {
		if (time != null) {
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMdd");
			return simpleDateFormat.format(time);
		}
		else {
			return null;
		}
	}

	public static Date getTimeInfoLast(Date time, int month) {
		Calendar c = Calendar.getInstance();
		c.setTime(new Date());
		c.add(Calendar.MONTH, month);
		Date seleDate = c.getTime();
		return seleDate;
	}


	public static String getTimeBeforeInfo(Context context, Date createTime) {
		String strInfo = "";
		if (createTime != null) {
			if (context.getResources().getConfiguration().locale.getCountry().equals("US")) {
				Date curDate = new Date();
				if (isSameDate(curDate, createTime)) {
					long datespan = curDate.getTime() - createTime.getTime();
					if (datespan > HOUR_TIME_SPAN) {
						strInfo = String.format("%1$dHours before", datespan / HOUR_TIME_SPAN);
					}
					else if (datespan > MIN_TIME_SPAN) {
						strInfo = String.format("%1$dMinutes before", datespan / MINUTES_TIME_SPAN);
					}
					else if (datespan <= MIN_TIME_SPAN) {
						strInfo = "recently";
					}
					else {
						strInfo = String.format("%1$dSeconds before", datespan / 1000);
					}
				}
				else {
					SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
					strInfo = strInfo + simpleDateFormat.format(createTime);
				}
			}
			else {
				Date curDate = new Date();
				if (isSameDate(curDate, createTime)) {
					long datespan = curDate.getTime() - createTime.getTime();
					if (datespan > HOUR_TIME_SPAN) {
						strInfo = String.format("%1$d小时之前", datespan / HOUR_TIME_SPAN);
					}
					else if (datespan > MIN_TIME_SPAN) {
						strInfo = String.format("%1$d分钟之前", datespan / MINUTES_TIME_SPAN);
					}
					else if (datespan <= MIN_TIME_SPAN) {
						strInfo = "刚刚";
					}
					else {
						strInfo = String.format("%1$d秒之前", datespan / 1000);
					}
				}
				else {
					SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
					strInfo = strInfo + simpleDateFormat.format(createTime);
				}
			}

		}
		return strInfo;
	}

	public static String getTimeInfoBefor(Context context, Date createTime) {
		String strInfo = "";
		if (createTime != null) {

			if (context.getResources().getConfiguration().locale.getCountry().equals("US")) {
				Date curDate = new Date();
				long datespan = curDate.getTime() - createTime.getTime();
				if (datespan > DAY_TIME_SPAN * 2) {
					SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/");
					strInfo = dateFormat.format(createTime);
				}
				else if (datespan > HOUR_TIME_SPAN) {
					strInfo = String.format("%1$dHours before", datespan / HOUR_TIME_SPAN);
				}
				else if (datespan > MIN_TIME_SPAN) {
					strInfo = String.format("%1$dMinutes before", datespan / MINUTES_TIME_SPAN);
				}
				else if (datespan <= MIN_TIME_SPAN) {
					strInfo = "recently";
				}
				else {
					strInfo = String.format("%1$dSeconds before", datespan / 1000);
				}

			}
			else {
				Date curDate = new Date();
				long datespan = curDate.getTime() - createTime.getTime();
				if (datespan > DAY_TIME_SPAN * 2) {
					SimpleDateFormat dateFormat = new SimpleDateFormat("MM月dd日");
					strInfo = dateFormat.format(createTime);
				}
				else if (datespan > HOUR_TIME_SPAN) {
					strInfo = String.format("%1$d小时之前", datespan / HOUR_TIME_SPAN);
				}
				else if (datespan > MIN_TIME_SPAN) {
					strInfo = String.format("%1$d分钟之前", datespan / MINUTES_TIME_SPAN);
				}
				else if (datespan <= MIN_TIME_SPAN) {
					strInfo = "刚刚";
				}
				else {
					strInfo = String.format("%1$d秒之前", datespan / 1000);
				}
			}
		}
		return strInfo;
	}

	/**
	 * 时间n年月日转为 年月日分秒
	 *
	 * @param time
	 * @return
	 */
	public static Date getDateFromString(String time) {
		if (!TextUtils.isEmpty(time)) {
			Date selDate = getDateFromString2(time.trim());
			Calendar c = Calendar.getInstance();
			c.set(Calendar.YEAR, selDate.getYear() + 1900);
			c.set(Calendar.MONTH, selDate.getMonth());
			c.set(Calendar.DAY_OF_MONTH, selDate.getDate());
			return c.getTime();
		}
		return null;
	}

	/**获取一千年前的时间，当做初始时间使用*/
	public static Date getOneThousandYearsAgo(){
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(new Date(0));
		calendar.add(Calendar.YEAR, -1000);
		return calendar.getTime();
	}

	/**
	 * 获取当天的起始和结束时间
	 */
	public static Date getDateStartTimeFromDate(Date time) {
		if (time != null) {
			Calendar Start = Calendar.getInstance();
			Start.setTime(time);
			Start.set(Calendar.HOUR_OF_DAY, 0);
			Start.set(Calendar.MINUTE, 0);
			Start.set(Calendar.SECOND, 0);
			Start.set(Calendar.MILLISECOND, 0);
			return Start.getTime();
		}
		return null;
	}

	public static Date getDateEndTimeFromDate(Date time) {
		if (time != null) {
			Calendar End = Calendar.getInstance();
			End.setTime(time);
			End.set(Calendar.HOUR_OF_DAY, 23);
			End.set(Calendar.MINUTE, 59);
			End.set(Calendar.SECOND, 59);
			End.set(Calendar.MILLISECOND, 999);
			return End.getTime();
		}
		return null;
	}

	public static Date getDateFromString2(String time) {
		if (!TextUtils.isEmpty(time)) {
			Date dateTime = null;
			try {
				SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
				dateTime = format.parse(time);
			}
			catch (Exception e) {
				e.printStackTrace();
			}
			return dateTime;
		}
		return null;

	}

	public static Date getDateFromString3(String time) {
		if (!TextUtils.isEmpty(time)) {
			Date dateTime = null;
			try {
				SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd");
				dateTime = format.parse(time);
			}
			catch (Exception e) {
				e.printStackTrace();
			}
			return dateTime;
		}
		return null;
	}

	public static Date getWeekBefore(Date date) {
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		c.add(Calendar.DATE, -7);
		return c.getTime();
	}

	public static Date getWeekLater(Date date) {
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		c.add(Calendar.DATE, 7);
		return c.getTime();
	}

	public static Date getMonthBefore(Date date) {
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		c.add(Calendar.MONTH, -1);
		return c.getTime();
	}

	public static Date getMonthLater(Date date) {
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		c.add(Calendar.MONTH, 1);
		return c.getTime();
	}

	public static boolean isSameDate(Date date1, Date date2) {
		boolean result = false;
		Calendar c1 = Calendar.getInstance();
		c1.setTime(date1);

		Calendar c2 = Calendar.getInstance();
		c2.setTime(date2);

		if (c1.get(Calendar.YEAR) == c2.get(Calendar.YEAR) && c1.get(Calendar.MONTH) == c2.get(Calendar.MONTH) && c1.get(Calendar.DAY_OF_MONTH) == c2.get(Calendar.DAY_OF_MONTH)) {
			result = true;
		}
		return result;
	}

	public static boolean isToday(Date targetDate) {
		return isSameDate(new Date(), targetDate);
	}

	public static boolean isYesterday(Date currentDate, Date targetDate) {
		boolean result = false;
		Calendar c1 = Calendar.getInstance();
		c1.setTime(currentDate);

		Calendar c2 = Calendar.getInstance();
		c2.setTime(targetDate);

		return c1.get(Calendar.DATE) - c2.get(Calendar.DATE) == 1;
	}

	public static boolean isTheDayBeforeYesterday(Date currentDate, Date targetDate) {
		boolean result = false;
		Calendar c1 = Calendar.getInstance();
		c1.setTime(currentDate);

		Calendar c2 = Calendar.getInstance();
		c2.setTime(targetDate);

		return c1.get(Calendar.DATE) - c2.get(Calendar.DATE) == 2;
	}

	public static Date getFirstDayOfWeek(Date date) {
		Calendar c = new GregorianCalendar();
		c.setFirstDayOfWeek(Calendar.MONDAY);
		c.setTime(date);
		c.set(Calendar.DAY_OF_WEEK, c.getFirstDayOfWeek()); // Monday
		c.set(Calendar.HOUR_OF_DAY, 0);
		c.set(Calendar.MINUTE, 0);
		c.set(Calendar.SECOND, 1);
		return c.getTime();
	}

	public static Date getLastDayOfWeek(Date date) {
		Calendar c = new GregorianCalendar();
		c.setFirstDayOfWeek(Calendar.MONDAY);
		c.setTime(date);
		c.set(Calendar.DAY_OF_WEEK, c.getFirstDayOfWeek() + 6); // Sunday
		c.set(Calendar.HOUR_OF_DAY, 23);
		c.set(Calendar.MINUTE, 59);
		c.set(Calendar.SECOND, 59);
		return c.getTime();
	}

	public static Date getFirstDayOfMonth(Date date) {
		Calendar c = new GregorianCalendar();
		c.setTime(date);
		c.set(Calendar.DAY_OF_MONTH, 1);
		c.set(Calendar.HOUR_OF_DAY, 0);
		c.set(Calendar.MINUTE, 0);
		c.set(Calendar.SECOND, 1);
		return c.getTime();
	}

	public static Date getLastDayOfMonth(Date beginDate) {
		Calendar c = new GregorianCalendar();
		c.setTime(beginDate);
		c.add(Calendar.MONTH, 1);
		c.set(Calendar.HOUR_OF_DAY, 0);
		c.set(Calendar.MINUTE, 0);
		c.set(Calendar.SECOND, 0);
		return c.getTime();
	}

	public static int getAge(Date birthDate) {
//		int age = 0;
//		if (birthDate != null) {
//			Date curDate = new Date();
//			age = curDate.getYear() - birthDate.getYear();
//			if (curDate.getMonth() < birthDate.getMonth()) {
//				age = age - 1;
//			}
//			else if (curDate.getMonth() == birthDate.getMonth() && curDate.getDate() < birthDate.getDate()) {
//				age = age - 1;
//			}
//		}
//		return age > 0 ? age : 0;
		return birthDate == null ? 1 : Math.max(1, new Date().getYear() - birthDate.getYear());
	}

	public static String getBirthDay(long time) {
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
		return simpleDateFormat.format(time);
	}

	public static String getBirthDay(Date birthday) {
		if (birthday != null) {
			return getBirthDay(birthday.getTime());
		}
		return "";
	}

	public static String getBirthDay(Calendar birthday) {
		if (birthday != null) {
			return getBirthDay(birthday.getTime());
		}
		return "";
	}

	public static byte[] getEmbbeddedDate(Date date) {
		return new byte[]{(byte) date.getYear(), (byte) date.getMonth(),
				(byte) date.getDate(), (byte) date.getHours(),
				(byte) date.getMinutes(), (byte) date.getSeconds()};
	}

	public static Date getDateFromEmbbedded(Byte[] date) {
		return new Date(date[0], date[1], date[2], date[3], date[4], date[5]);
	}

	/**
	 * @return 返回年加月
	 */
	public static String getStringFromYearMonth(String time) {
		String yearMonth = new String(time);
		String yearMonthFrom[] = yearMonth.split("-");
		return yearMonthFrom[0] + "年" + yearMonthFrom[1] + "月";
	}

	/**
	 * @return 返回日历对象
	 */
	public static Calendar getCalendarFromString(String time) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Date date = null;
		try {
			date = sdf.parse(time);
		}
		catch (ParseException e) {
			e.printStackTrace();
		}
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		return calendar;
	}

	public static long getTimeByAge(int age){
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.YEAR,-age);
		//年龄的计算以1月1日为准
		calendar.set(Calendar.MONTH, Calendar.JANUARY);
		calendar.set(Calendar.DATE, 1);
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);
		return calendar.getTime().getTime();
	}
}