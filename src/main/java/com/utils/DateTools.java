package com.utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateTools {

	public static String format1 = "yyyy-MM-dd";
	public static String format2 = "yyyy/MM/dd";
	public static String format3 = "yyyy年M月d日";
	public static String format4 = "yyyy/M/d";
	public static String format5 = "yyyy-MM-dd HH:mm:ss";
	public static String format6 = "yyyy-MM-dd'T'HH:mm:ss'Z'";

	/**
	 * 得到当前系统时间【yyyy-MM-dd hh:mm:ss】字符串
	 * 
	 * @return
	 */
	public static String getSysTime() {
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String date = df.format(java.util.Calendar.getInstance().getTime());
		return date;
	}

	/**
	 * 得到当前系统时间【yyyy-MM-dd hh:mm:ss SSS】字符串
	 * 
	 * @return
	 */
	public static String getSysTimeSSS() {
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss SSS");
		String date = df.format(java.util.Calendar.getInstance().getTime());
		return date;
	}
	
	/**
	 * 得到当前系统时间【yyyy-MM-dd】字符串
	 * 
	 * @return
	 */
	public static String getSysDate() {
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		String date = df.format(java.util.Calendar.getInstance().getTime());
		return date;
	}
	
	/**
	 * 计算当前日期前几天或是后几天
	 * 日期格式 【yyyy-MM-dd HH:mm:ss】
	 */
	public static String beforeOrAfterNDay(int n) {
		Calendar c = Calendar.getInstance();
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		c.setTime(new Date());
		c.add(Calendar.DATE, n);
		Date d2 = c.getTime();
		String s = df.format(d2);
		return s;
	}

	/**
	 * 得到当前系统时间【yyyyMMdd】字符串
	 * 
	 * @return
	 */
	public static String getDate() {
		DateFormat df = new SimpleDateFormat("yyyyMMdd");
		String date = df.format(java.util.Calendar.getInstance().getTime());
		return date;
	}

	/**
	 * 得到当前系统时间【HH:mm:ss.SSS】字符串
	 * 
	 * @return
	 */
	public static String getNum() {
		DateFormat df = new SimpleDateFormat("HH:mm:ss.SSS");
		String date = df.format(java.util.Calendar.getInstance().getTime());
		return date;
	}
	

	/**
	  * getYYYYMMDD 方法
	  * <p>
	  * 方法说明:将给定的date转换为yyyy/MM/dd格式的字符串
	  * </p>
	  * @param date 待转换的date
	  * @param format 返回的格式，请使用常量。
	  * @return String
	  * @author 孟令宏
	  * @date 2013-3-18
	*/ 
	public static String formatDate(Date date,String format) {
		DateFormat df = new SimpleDateFormat(format);
		return df.format(date);
	}
	public static Date parseDate(String dateStr,String format){
		try{
			return new SimpleDateFormat(format).parse(dateStr);
		}catch(Exception e){
			return null;
		}
	}
	public static Date formatString(String string,String format) throws ParseException{
		SimpleDateFormat simple = new SimpleDateFormat(format,java.util.Locale.ENGLISH);
		return simple.parse(string);
	}
	
	public static long getLongTime(){
		return java.util.Calendar.getInstance().getTime().getTime();
	}
	
	public static void main(String[] args) {
			DateTools.formatDate(DateTools.parseDate("2014/01/01", DateTools.format1), DateTools.format1);
	}
}
