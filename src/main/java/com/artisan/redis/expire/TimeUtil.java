package com.artisan.redis.expire;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import org.springframework.util.StringUtils;

public class TimeUtil {

	/**
	 * Java将Unix时间戳转换成指定格式日期字符串
	 * 
	 * @param timestampString
	 *            时间戳 如："1473048265";
	 * @param formats
	 *            要格式化的格式 默认："yyyy-MM-dd HH:mm:ss";
	 *
	 * @return 返回结果 如："2016-09-05 16:06:42";
	 */
	public static String TimeStamp2Date(String timestampString, String formats) {
		if (StringUtils.isEmpty(formats))
			formats = "yyyy-MM-dd HH:mm:ss";
		Long timestamp = Long.parseLong(timestampString) * 1000;
		String date = new SimpleDateFormat(formats, Locale.CHINA).format(new Date(timestamp));
		return date;
	}

	/**
	 * 日期格式字符串转换成时间戳
	 *
	 * @param dateStr
	 *            字符串日期
	 * @param format
	 *            如：yyyy-MM-dd HH:mm:ss
	 *
	 * @return
	 */
	public static String Date2TimeStamp(String dateStr, String format) {
		try {
			SimpleDateFormat sdf = new SimpleDateFormat(format);
			return String.valueOf(sdf.parse(dateStr).getTime() / 1000);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "";
	}

	public static void main(String[] args) {
		String result = TimeUtil.Date2TimeStamp("2018-09-28 14:00:00", "yyyy-MM-dd HH:mm:ss");
		System.out.println(result);
		
		
		String result2 = TimeUtil.TimeStamp2Date("1538114400", "yyyy-MM-dd HH:mm:ss");
		System.out.println(result2);
	}
}
