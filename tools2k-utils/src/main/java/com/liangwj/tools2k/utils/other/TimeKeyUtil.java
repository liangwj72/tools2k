package com.liangwj.tools2k.utils.other;

import java.util.Calendar;
import java.util.Date;

/**
 * <pre>
 * 时间键值的工具
 * </pre>
 *
 * @author rock
 */
public class TimeKeyUtil {

	/** 获取当前的天的key */
	public static int getDayKey() {
		Calendar c = Calendar.getInstance();
		int year = c.get(Calendar.YEAR);
		int mon = c.get(Calendar.MONTH) + 1;
		int day = c.get(Calendar.DAY_OF_MONTH);

		return year * 10000 + mon * 100 + day;
	}

	/** 获取昨天的key **/
	public static int getYesterdayKey() {
		return getBeforeDayKey(1);
	}

	public static int getBeforeDayKey(int before) {
		Calendar c = Calendar.getInstance();
		c.add(Calendar.DAY_OF_MONTH, -before);

		int year = c.get(Calendar.YEAR);
		int mon = c.get(Calendar.MONTH) + 1;
		int day = c.get(Calendar.DAY_OF_MONTH);

		return year * 10000 + mon * 100 + day;
	}

	/** 获取当前的小时的key */
	public static int getHourKey() {
		Calendar c = Calendar.getInstance();
		int year = c.get(Calendar.YEAR);
		int mon = c.get(Calendar.MONTH) + 1;
		int day = c.get(Calendar.DAY_OF_MONTH);
		int hour = c.get(Calendar.HOUR_OF_DAY);

		return (year * 100 + mon) * 100 + day + hour;
	}

	/** 获取当前的月的key */
	public static int getMonthKey() {
		Calendar c = Calendar.getInstance();
		int year = c.get(Calendar.YEAR);
		int mon = c.get(Calendar.MONTH) + 1;

		return year * 100 + mon;
	}

	/** 获取当前小时 */
	public static int getHour() {
		return getHour(System.currentTimeMillis());
	}

	/** 获取当前小时 */
	public static int getHour(long millis) {
		Calendar c = Calendar.getInstance();
		c.setTimeInMillis(millis);
		int hour = c.get(Calendar.HOUR_OF_DAY);
		return hour;
	}

	/** 获取当前小时 */
	public static int getHour(Date date) {
		if (date != null) {
			return getHour(date.getTime());
		} else {
			return getHour();
		}
	}

}
