package tool.utility;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * 用于日期的处理
 * @author Lin
 */
public class DateCount {
	static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	
	
	
	public static Date strToDate(String today){
		Date date = null;
		try {
			date = sdf.parse(today);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		return date;
		
	}
	
	/**
	 * 获得当前日期是星期几
	 * @param date
	 * @return
	 */
	public static int getWeekOfDate(String date){
		Calendar c = Calendar.getInstance();
		c.setTime(strToDate(date));
		int d = c.get(Calendar.DAY_OF_WEEK)-1;
		if(d==0){
			d+=7;
		}
		return d;
		
	}
	
	/**
	 * 将date类转化为规定格式string
	 * @param date
	 * @return
	 */
	public static String dateToStr(Date date){
		return sdf.format(date);
	}
	
	/**
	 * 计算和某个个日期相隔若干天的日期
	 * 
	 * @param endDate
	 * @param offset
	 * @return 日期
	 */
	public static String count(String endDate,int offset){
		Date date = null;
		date = strToDate(endDate);
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		c.add(Calendar.DATE, offset);
		date = c.getTime();
		String beginDate = sdf.format(date);
		return beginDate;
	}
	
	/**
	 * 获得两个日期间的所有日期
	 * @param beginDate
	 * @param endDate
	 * @return 日期的列表
	 */
	//还未考虑结束日期在开始日期前的情况
	public static List<String> splitDays(String beginDate,String endDate){
		List<String> days = new ArrayList<String>();
		if(beginDate.compareTo(endDate)<=0){
			String date = new String(beginDate);
			while(date.compareTo(endDate)<0){
				days.add(date);
				date = count(date, 1);
			}
			days.add(endDate);
			
			if(days.size()!=0){
				return days;
			}
		}
		
		return null;
		
	}

    /**
     * 获得两个时间点之间的时间的字符串,格式为HH:mm:ss
     * 第三个参数为时间间隔
     *
     * @param start HH:mm:ss
     * @param end HH:mm:ss
     * @param intervalSeconds 单位是秒
     * @return
     */
    public static List<String> getSeconds(String start,String end,int intervalSeconds){
        ArrayList<String> strings = new ArrayList<>();

        SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");
        try {
            Date startDate = format.parse(start);
            Date endDate = format.parse(end);
            Calendar c1 = Calendar.getInstance();
            Calendar c2 = Calendar.getInstance();
            c1.setTime(startDate);
            c2.setTime(endDate);

            long begin = c1.getTimeInMillis();
            long ends = c2.getTimeInMillis();

            while (begin <= ends){
                startDate.setTime(begin);
                String oneSecond = format.format(startDate);
                strings.add(oneSecond);
                begin += intervalSeconds*1000;
            }

        } catch (ParseException e) {
            e.printStackTrace();
        }

        return strings;
    }

	/**
	 * 计算两个日期间相隔的天数
	 * @param beginDate
	 * @param endDate
	 * @return
	 */
	public static int getintervalDays(String beginDate,String endDate){
		int days = (int)((DateCount.strToDate(endDate).getTime() - DateCount.strToDate(beginDate).getTime()/86400000));
		return days;
	}

	/**
	 * 获得本月最后一天日期
	 * @param date 日期
	 * @return
     */
	public static String getLastDayOfMonth(String date) throws ParseException {
		date = date.substring(0,8) + "01";
		Calendar cal = TimeConvert.covertToCalendar(date);
		cal.add(Calendar.MONTH, 1);
		cal.add(Calendar.DAY_OF_MONTH, -1);
		return TimeConvert.getDisplayDate(cal);
	}

    /**
     * 获得从昨天开始最近的一个工作日
     *
     * @return 工作日字符串
     */
    public static String getRecentWorkDay() {
        String tody = DateCount.dateToStr(new Date());

        //获取今天的星期
        int weekDayNumber = DateCount.getWeekOfDate(tody);

        //周2-6,最新的数据是昨天的
        if (weekDayNumber >= 2 && weekDayNumber <= 6) {
            return DateCount.count(tody, -1);
            //周天,最新的数据是这个周五的
        } else if (weekDayNumber == 7) {
            return DateCount.count(tody, -2);
            //周1,最新的数据是上个周五的
        } else {
            return DateCount.count(tody, -3);
        }
    }
}
