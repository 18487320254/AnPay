package com.anpay.sdk;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

//实现常用时间日期的操作
public class B_time_pay {
	//获取当前系统时间
	public static String Time(){
		SimpleDateFormat df=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String time=df.format(System.currentTimeMillis());
		return time;
	}
	
	//生成14位系统时间标记
	public static String Timerm(){
		SimpleDateFormat df=new SimpleDateFormat("yyyyMMddHHmmss");
		String time=df.format(System.currentTimeMillis());
		return time;
	}
	
	//比较两个时间，如果date1在date2之前则返回1，反之返回2，一模一样则返回0
    public int compareDate(String date1, String date2) {
    	int i=0;
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            Date date3 = format.parse(date1);
            Date date4 = format.parse(date2);
            if (date3.getTime() < date4.getTime()) {
                i=1;
            } else if (date3.getTime() > date4.getTime()) {
                i=2;
            } else {
                i=0;
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
		return i;
    }

	//计算时间与时间之差(time1大为正。type:时hour，分mimute，秒second，年year，月month，日day)
	public static int Times(String type,String time1,String time2) throws ParseException {
		int total=0;
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date d1 = (Date) df.parse(time1);
		Date d2 = (Date) df.parse(time2);
		long diff = d1.getTime() - d2.getTime();
    	switch (type) {
			case "day":
				total = (int) (diff / (1000 * 60 * 60 * 24));
				break;
			case "year":
				total = (int) (diff / (1000 * 60 * 60 * 24)/365);
				break;
			case "hour":
				total = (int) (diff / (1000 * 60 * 60));
				break;
			case "mimute":
				total = (int) (diff / (1000 * 60));
				break;
			case "second":
				total = (int) (diff / 1000);
				break;
		}
		return total;
	}
    
    //时间运算（type:时hour，分mimute，秒second，年year，月month，日day。x为加减值，正负数均可。time为待计算时间，格式2019-01-31 21:19:06）
    public String Time_compute(String type,int x,String time) {
		SimpleDateFormat df=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Calendar calendar = Calendar.getInstance();// 获取Calendar实例
        try {
			calendar.setTime(df.parse(time));//设置当前Calendar实例中的时间,如不设置则默认为系统当前时间
		} catch (ParseException e) {
			e.printStackTrace();
		}
    	switch (type) {
			case "day":
		        calendar.add(Calendar.DAY_OF_MONTH, x);// 在当前设置的时间基础上进行计算，field为时间计算的单位，x为计算的数值
				break;
			case "month":
		        calendar.add(Calendar.MONTH, x);
				break;
			case "year":
		        calendar.add(Calendar.YEAR, x);
				break;
			case "hour":
		        calendar.add(Calendar.HOUR, x);
				break;
			case "mimute":
		        calendar.add(Calendar.MINUTE, x);
				break;
			case "second":
		        calendar.add(Calendar.SECOND, x);
				break;
		}
        Date date1 = calendar.getTime();// 计算后Calendar实例中的时间就变成了运算后的时间，获取该事件
        return df.format(date1);
	}
}
