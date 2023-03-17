package com.liangwj.tools2k.utils.other;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.TimeUnit;

/**
 * 和时间相关的一些工具
 * 
 * @author rock
 * 
 */
public class DateUtil {

	private static final ConcurrentMap<String, DateTimeFormatter> FORMATTER_CACHE = new ConcurrentHashMap<>();

	private static final int PATTERN_CACHE_SIZE = 500;

	/**
	 * 将时间变为当天的00:00:00 或者 23:59:59，主要用于sql查询时的条件
	 * 
	 * @param date
	 * @param begin
	 *            为true时变成00:00:00，否则为23:59:59
	 * @return
	 */
	public static Date ceilDateToDay(Date date, boolean begin) {
		long time = ceilTimeToDay(date.getTime(), begin);
		return new Date(time);
	}

	/**
	 * 将时间变为当天的00:00:00 或者 23:59:59，主要用于sql查询时的条件
	 * 
	 * @param millis
	 *            unix型时间
	 * @param begin
	 *            为true时变成00:00:00，否则为23:59:59
	 * @return
	 */
	public static long ceilTimeToDay(long millis, boolean begin) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(millis);
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);

		if (!begin) {
			calendar.set(Calendar.HOUR_OF_DAY, 23);
			calendar.set(Calendar.MINUTE, 59);
			calendar.set(Calendar.SECOND, 59);
			calendar.set(Calendar.MILLISECOND, 999);
		}

		return calendar.getTimeInMillis();
	}

	/**
	 * 将日期装成为 yyyy-MM-dd HH:mm:ss 格式的字符串
	 * 
	 * @param date
	 * @return
	 */
	public static String dateFormat(Date date) {
		return dateFormat(date, "yyyy-MM-dd HH:mm:ss");
	}

	/**
	 * Date转换为格式化时间
	 * 
	 * @param date
	 *            date
	 * @param pattern
	 *            格式
	 * @return
	 */
	public static String dateFormat(Date date, String pattern) {
		if (date != null && pattern != null) {
			return format(LocalDateTime.ofInstant(date.toInstant(), ZoneId.systemDefault()), pattern);
		} else {
			return null;
		}
	}

	/**
	 * localDateTime转换为格式化时间
	 * 
	 * @param localDateTime
	 *            localDateTime
	 * @param pattern
	 *            格式
	 * @return
	 */
	public static String format(LocalDateTime localDateTime, String pattern) {
		try {
			DateTimeFormatter formatter = createCacheFormatter(pattern);
			return localDateTime.format(formatter);
		} catch (Throwable e) {
			return null;
		}
	}

	/**
	 * 获取当前日期+100年
	 * 
	 * @return
	 */
	public static Date get100YearDay() {
		Calendar c = Calendar.getInstance();
		int year = c.get(Calendar.YEAR) + 100;
		c.set(Calendar.YEAR, year);
		return c.getTime();
	}

	/**
	 * 计算从date开始n天以前（以后）的日期
	 * 
	 * @param date
	 * @param dateCnt
	 * @return
	 */
	public static Date getDateRelateToDate(Date date, int dateCnt) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(Calendar.DATE, dateCnt);
		return calendar.getTime();
	}

	/**
	 * 计算从date开始n时间以前（以后）的日期
	 * 
	 * @param date
	 * @param dateCnt
	 *            时间
	 * @param unit
	 *            单位
	 * @return
	 */
	public static Date getDateRelateToDate(Date date, int dateCnt, int unit) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(unit, dateCnt);
		return calendar.getTime();
	}

	/**
	 * 获取两个日期之间的天数间隔
	 * 
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	public static Long getDaysBetween(Date startDate, Date endDate) {
		Calendar fromCalendar = Calendar.getInstance();
		fromCalendar.setTime(startDate);
		fromCalendar.set(Calendar.HOUR_OF_DAY, 0);
		fromCalendar.set(Calendar.MINUTE, 0);
		fromCalendar.set(Calendar.SECOND, 0);
		fromCalendar.set(Calendar.MILLISECOND, 0);

		Calendar toCalendar = Calendar.getInstance();
		toCalendar.setTime(endDate);
		toCalendar.set(Calendar.HOUR_OF_DAY, 0);
		toCalendar.set(Calendar.MINUTE, 0);
		toCalendar.set(Calendar.SECOND, 0);
		toCalendar.set(Calendar.MILLISECOND, 0);

		return (toCalendar.getTime().getTime() - fromCalendar.getTime().getTime()) / (1000 * 60 * 60 * 24);
	}

	/**
	 * 将一个时间长度（单位：秒）。 变成 1天3小时3分40秒 的表示方法，
	 * 
	 * @param min
	 * @return
	 */
	public static String getDisplayTimeFromMin(int min) {
		return getDisplayTimeStr(min, TimeUnit.MINUTES, new String[] {
				"秒", "分", "小时"
		});
	}

	/**
	 * 将一个时间长度（单位：秒）。 变成 1天3小时3分40秒 的表示方法，
	 * 
	 * @param second
	 * @return
	 */
	public static String getDisplayTimeFromSecond(int second) {
		return getDisplayTimeStr(second, TimeUnit.SECONDS, new String[] {
				"秒", "分钟", "小时"
		});
	}

	/**
	 * 将一个时间长度（单位：秒）。 变成 1天3小时3分40秒 的表示方法，
	 * 
	 * @param duration
	 * @param timeUnit
	 * @param timeUnitStr
	 *            时间单位的数组，长度为4
	 * @return
	 */
	public static String getDisplayTimeStr(int duration, TimeUnit timeUnit, String[] timeUnitStr) {
		if (timeUnitStr.length != 3) {
			return null;
		}

		StringBuffer sb = new StringBuffer();
		long second = timeUnit.toSeconds(duration);
		long s = second % 60;
		if (s > 0) {
			sb.append(s).append(timeUnitStr[0]);
		}

		long remain = second / 60;
		if (remain > 0) {
			long m = remain % 60;
			if (m > 0) {
				sb.insert(0, timeUnitStr[1]);
				sb.insert(0, m);
			}
			remain = remain / 60;
			if (remain > 0) {
				sb.insert(0, timeUnitStr[2]);
				sb.insert(0, remain);
			}
		}
		return sb.toString();
	}

	/**
	 * 是否是闰年
	 * 
	 * @param date
	 * @return
	 */
	public static boolean isLeapYear(Date date) {
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		int year = c.get(Calendar.YEAR);
		GregorianCalendar gc = new GregorianCalendar();
		return gc.isLeapYear(year);
	}

	/**
	 * 格式化字符串转为Date
	 * 
	 * @param time
	 *            格式化时间
	 * @param pattern
	 *            格式
	 * @return
	 */
	public static Date parseDate(String time, String pattern) {
		return Date.from(parseLocalDateTime(time, pattern).atZone(ZoneId.systemDefault()).toInstant());

	}

	/**
	 * 格式化字符串转为LocalDateTime
	 * 
	 * @param time
	 *            格式化时间
	 * @param pattern
	 *            格式
	 * @return
	 */
	public static LocalDateTime parseLocalDateTime(String time, String pattern) {
		DateTimeFormatter formatter = createCacheFormatter(pattern);
		return LocalDateTime.parse(time, formatter);
	}

	/**
	 * 将时间字符串类型转换成Timestamp时间类型
	 * 
	 * @param str
	 * @param isStartDate
	 *            如果为false，自动添加23:59:59
	 * @return
	 */
	public static Timestamp parserTimestap(String str, boolean isStartDate) {
		Timestamp date = Timestamp.valueOf(dataAddStr(str, isStartDate));
		return date;
	}

	/**
	 * 将Date类型转换成Timestamp时间类型
	 * 
	 * @param date
	 * @param isStartDate
	 *            是否为开始时间-如果为false，自动添加23:59:59
	 * @return
	 */
	public static Timestamp parserTimestapForDate(Date date, boolean isStartDate) {
		Timestamp timestamp = Timestamp.valueOf(dataAddStr(dateFormat(date, "yyyy-MM-dd"), isStartDate));
		return timestamp;
	}

	/**
	 * 字符串日期转换成Date型日期, 应该用 parseDate
	 * 
	 * @param date
	 * @param format
	 * @return
	 */
	@Deprecated
	public static Date strTimeToDate(String date, String format) {
		return parseDate(date, format);
	}

	/**
	 * 在缓存中创建DateTimeFormatter
	 * 
	 * @param pattern
	 *            格式
	 * @return
	 */
	private static DateTimeFormatter createCacheFormatter(String pattern) {
		if (pattern == null || pattern.length() == 0) {
			throw new IllegalArgumentException("Invalid pattern specification");
		}
		DateTimeFormatter formatter = FORMATTER_CACHE.get(pattern);
		if (formatter == null) {
			if (FORMATTER_CACHE.size() < PATTERN_CACHE_SIZE) {
				formatter = DateTimeFormatter.ofPattern(pattern);
				DateTimeFormatter oldFormatter = FORMATTER_CACHE.putIfAbsent(pattern, formatter);
				if (oldFormatter != null) {
					formatter = oldFormatter;
				}
			}
		}

		return formatter;
	}

	/**
	 * 拼接日期后缀
	 * 
	 * @return
	 */
	private static String dataAddStr(String str, boolean isStartDate) {
		if (str.length() == 10) {
			if (isStartDate) {
				return str + " 00:00:00";
			} else {
				return str + " 23:59:59";
			}
		}
		return str;
	}

	/** 将unix时间显示为时间 */
	public static String timeToDateStr(long time) {
		if (time > 0) {
			Date date = new Date(time);
			return dateFormat(date);
		}
		return null;
	}

}
