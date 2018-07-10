package com.utils;

import java.text.DateFormat;
import java.text.ParseException;  
import java.text.SimpleDateFormat;  
import java.util.ArrayList;
import java.util.Calendar;  
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONArray;

public class DateUtil {

	static final String formatPattern = "yyyy-MM-dd HH:mm:ss";  
    
    static final String formatPattern_Short = "yyyy-MM-dd";  
    
    /**
	 * 得到当前系统时间【yyyy-MM-dd】字符串
	 * 
	 * @return
	 */
	public static String getSysDate() {
		DateFormat df = new SimpleDateFormat(formatPattern_Short);
		String date = df.format(java.util.Calendar.getInstance().getTime());
		return date;
	}
	
	/**
	 * 计算当前日期前几天或是后几天
	 * 日期格式 【yyyy-MM-dd HH:mm:ss】
	 */
	public static String beforeOrAfterNDay(int n) {
		Calendar c = Calendar.getInstance();
		DateFormat df = new SimpleDateFormat(formatPattern_Short);
		c.setTime(new Date());
		c.add(Calendar.DATE, n);
		Date d2 = c.getTime();
		String s = df.format(d2);
		return s;
	} 
    
    /** 
     * 获取当前日期 
     * @return String 
     */  
    public static String getStringDate(){  
        SimpleDateFormat format = new SimpleDateFormat(formatPattern);        
        return format.format(new Date());  
    }  
      
    /** 
     * 获取制定毫秒数之前的日期 
     * @param timeDiff 
     * @return 
     */  
    public static String getDesignatedDate(long timeDiff){  
        SimpleDateFormat format = new SimpleDateFormat(formatPattern);  
        long nowTime = System.currentTimeMillis();  
        long designTime = nowTime - timeDiff;         
        return format.format(designTime);  
    }  
    
    /** 
     *  
     * 获取前几天的日期 
     */  
    public static String getPrefixDate(String count){  
        Calendar cal = Calendar.getInstance();  
        int day = 0-Integer.parseInt(count);  
        cal.add(Calendar.DATE,day);   // int amount   代表天数  
        Date datNew = cal.getTime();   
        SimpleDateFormat format = new SimpleDateFormat(formatPattern);  
        return format.format(datNew);  
    }  
    /** 
     * 日期转换成字符串 
     * @param date 
     * @return 
     */  
    public static String dateToString(Date date){  
        SimpleDateFormat format = new SimpleDateFormat(formatPattern);  
        return format.format(date);  
    }  
    /** 
     * 字符串转换日期 
     * @param str 
     * @return 
     */  
    public static Date stringToDate(String str){  
        //str =  " 2008-07-10 19:20:00 " 格式  
        SimpleDateFormat format = new SimpleDateFormat(formatPattern);  
        if(!str.equals("")&&str!=null){  
            try {  
                return format.parse(str);  
            } catch (ParseException e) {  
                e.printStackTrace();  
            }  
        }  
        return null;  
    }  
    
    
    /** 
     * 字符串转换日期 
     * @param str 
     * @return 
     */  
    public static Date stringToDate1(String str){  
        //str =  " 2008-07-10 19:20:00 " 格式  
        SimpleDateFormat format = new SimpleDateFormat(formatPattern_Short);  
        if(!str.equals("")&&str!=null){  
            try {  
                return format.parse(str);  
            } catch (ParseException e) {  
                // TODO Auto-generated catch block  
                e.printStackTrace();  
            }  
        }  
        return null;  
    }
    
    public static String dateToString2(Date date){  
        SimpleDateFormat format = new SimpleDateFormat(formatPattern_Short);  
        return format.format(date);  
    }
    
    public static String DateToFromatStr(Date date){
        try {  
        	SimpleDateFormat format = new SimpleDateFormat(formatPattern_Short);
        	String time=format.format(date); 
        	String [] str=time.split("-");
            return str[0]+"年"+str[1]+"月"+str[2]+"日";
        } catch (Exception e) {  
            e.printStackTrace();  
        }
        return null;
    } 
      
    //java中怎样计算两个时间如：“21:57”和“08:20”相差的分钟数、小时数 java计算两个时间差小时 分钟 秒 .  
    public void timeSubtract(){  
        SimpleDateFormat dfs = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");   
        Date begin = null;   
        Date end = null;   
        try {   
        begin = dfs.parse("2004-01-02 11:30:24");   
        end = dfs.parse("2004-03-26 13:31:40");   
        } catch (ParseException e) {   
        e.printStackTrace();   
        }   
  
        long between = (end.getTime() - begin.getTime()) / 1000;// 除以1000是为了转换成秒   
  
        long day1 = between / (24 * 3600);   
        long hour1 = between % (24 * 3600) / 3600;   
        long minute1 = between % 3600 / 60;   
        long second1 = between % 60;   
        System.out.println("" + day1 + "天" + hour1 + "小时" + minute1 + "分"   
        + second1 + "秒");   
    }
    
    /**
     * 	获取起始时间和结束时间
     */
    public static Map<String,Object> getTimeLimit(){
    	Map<String,Object> map = new HashMap<String,Object>();
    	JSONArray startHtml = new JSONArray();//起始时间数组
    	JSONArray endYearHtml = new JSONArray();//结束时间数组
    	Calendar cal = Calendar.getInstance();
    	int curYear = cal.get(Calendar.YEAR);
		int startYear = 1900;//起始年
		int yearCount = curYear-startYear;
		for(int j=0;j <= yearCount;j++){
			startHtml.add(String.valueOf(startYear+j));
			endYearHtml.add(String.valueOf(curYear-j));
		}
		map.put("startHtml", startHtml);
		map.put("endYearHtml", endYearHtml);
		return map;
    }
    
    public  static  List<Map<String,String>> getMonthMap(String startTime,String endTime) throws Exception{
		Calendar cal=Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
		cal.setTime(sdf.parse(startTime.substring(0,7)));
		Date date2=sdf.parse(endTime.substring(0,7));
		if(cal.getTime().getTime()>date2.getTime()){
			return null;
		}
		List<Map<String,String>> list=new ArrayList<Map<String,String>>();
		while(cal.getTime().getTime()<=date2.getTime()){
			Map<String,String> map=new HashMap<String,String>();
			int year=cal.get(Calendar.YEAR);
			int month=cal.get(Calendar.MONTH)+1;
			map.put("time", year+"-"+month);
			System.out.println(map.toString());
			cal.add(Calendar.MONTH, 1);
			list.add(map);
		}
		return list;
	}
    
    public static void main(String[] args) {
		System.out.println(getSysDate());
	}
    
	public static String getCurrentYear() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy");
		Date date = new Date();
		return sdf.format(date);
	}
}
