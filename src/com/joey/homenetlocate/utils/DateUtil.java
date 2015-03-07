
package com.joey.homenetlocate.utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * 文件名称 : DateUtil
 * <p>
 * 作者信息 : xusheng
 * <p>
 * 文件描述 : DateUtil - 日期类
 * <p>
 * 创建时间 : 2014-3-25 上午12:31:38
 * <p>
 */
public class DateUtil
{
    private static SimpleDateFormat simpleDateFormatYear = new SimpleDateFormat("yyyy年MM月dd日 HH:mm");
    
    private static SimpleDateFormat simpleDateFormatMonth = new SimpleDateFormat("MM月dd日 HH:mm");
    
    private static SimpleDateFormat simpleDateFormatDay = new SimpleDateFormat("HH:mm");
    
    public static String currentDate(long time)
    {
        // 时间格式，精确到毫秒
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd--HH-mm-ss-SSS");
        
        // 1、取得本地时间：
        java.util.Calendar cal = java.util.Calendar.getInstance();
        
        // 2、取得时间偏移量：
        int zoneOffset = cal.get(java.util.Calendar.ZONE_OFFSET);
        
        // 3、取得夏令时差：
        int dstOffset = cal.get(java.util.Calendar.DST_OFFSET);
        
        // 4、从本地时间里扣除这些差量，即可以取得UTC时间：
        cal.add(java.util.Calendar.MILLISECOND, -(zoneOffset + dstOffset));
        
        // 之后调用cal.get(int x)或cal.getTimeInMillis()方法所取得的时间即是UTC标准时间。
        return format.format(new Date(time));
    }
    
    /**
     * 时间显示规则：今天的显示到分钟，如13：30，今天以前的显示月日，如06-07 15：30；今年以前的显示年份，如2010-09-08
     * 13：30; <BR>
     * yyyy.MM.dd HH:mm : 2011.06.02 10:10
     * 
     * @param milliseconds
     * @return
     */
    public static String formatTimeInMillis(final long milliseconds, final Calendar calendar)
    {
        calendar.setTimeInMillis(milliseconds);
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        
        calendar.setTimeInMillis(System.currentTimeMillis());
        int currentYear = calendar.get(Calendar.YEAR);
        int currentMonth = calendar.get(Calendar.MONTH);
        int currentDay = calendar.get(Calendar.DAY_OF_MONTH);
        
        String time;
        
        if (currentYear != year)
        {
            if (null == simpleDateFormatYear)
            {
                simpleDateFormatYear = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            }
            
            time = simpleDateFormatYear.format(milliseconds);
        }
        else if ((currentMonth != month) || (currentDay != day))
        {
            if (null == simpleDateFormatMonth)
            {
                simpleDateFormatMonth = new SimpleDateFormat("MM-dd HH:mm");
            }
            
            time = simpleDateFormatMonth.format(milliseconds);
        }
        else
        {
            if (null == simpleDateFormatDay)
            {
                simpleDateFormatDay = new SimpleDateFormat("HH:mm");
            }
            
            time = simpleDateFormatDay.format(milliseconds);
        }
        
        return time;
    }
}
