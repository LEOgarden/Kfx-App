package com.kfx.android.util.time;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;

public class TimeUtil {
	//毫秒的字符串转成时间格式的字符串
	public static String formatMillisToTime(String time){
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		sdf.setTimeZone(TimeZone.getTimeZone("GMT+08:00"));
		Calendar calendar = Calendar.getInstance();		
		calendar.setTimeInMillis(Long.parseLong(time));
		return sdf.format(calendar.getTime());
	}
	public static String getCurrentTime(String format) {
		Date date = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat(format, Locale.getDefault());
		String currentTime = sdf.format(date);
		return currentTime;
	}

	public static String getCurrentTime() {
		return getCurrentTime("yyyy-MM-dd  hh:mm:ss");
	}
	public static String getLatestTime(List<Map<String, Object>> newsList){
		Date date1=new Date();
		if (newsList.size() > 0) {			
			date1=TimeToDate(newsList.get(0).get("createTime").toString());
			for (Map<String, Object> map : newsList) {
				Date date2=TimeToDate(map.get("createTime").toString()) ;
				date1=getNewDate(date1,date2);
			}			
		}
		return  DateToStr(date1);	
	} 
	public static Date getNewDate(Date date1,Date date2){
		if(date1.before(date2)){
			date1=date2;
		}
		return date1;
	}
	/**
	* 字符串转换成日期
	* @param str
	* @return date
	*/
	public static Date StrToDate(String str) {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date date = null;
		try {
			date=format.parse(str);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return date;
	}
	/**
	* 日期转换成字符串
	* @param date 
	* @return str
	*/
	public static String DateToStr(Date date) {	  
	   SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	   format.setTimeZone(TimeZone.getTimeZone("GMT+08:00"));
	   String str = format.format(date);
	   return str;
	} 
	public static Date TimeToDate(String str){
		Date date=new Date(Long.parseLong(str));
		return date;
	}

}
