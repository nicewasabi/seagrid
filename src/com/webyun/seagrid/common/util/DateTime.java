package com.webyun.seagrid.common.util;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;


public class DateTime {
	public static final String YYYY_MM_DD="yyyy-MM-dd";
	public static final String YYYY_MM="yyyy-MM";
	public static final String YYYYMM="yyyyMM";
	public static final String YYYYMMdd="yyyyMMdd";
	public static final String MMDD="MM-dd";
	/**
	 * 给当前时间匹配格式
	 * @param pattern
	 * @return
	 */
	public static String getDate(String pattern){
		SimpleDateFormat sdf = new SimpleDateFormat(pattern);
		return sdf.format(new Date());
	}
	/**
	 * 给指定时间匹配格式
	 * @param pattern
	 * @return
	 */
	public static String getDate(Date date,String pattern){
		SimpleDateFormat sdf = new SimpleDateFormat(pattern);
		return sdf.format(date);
	}
	
	/**
	 * 在给定时间基础上加旬 如传入201109 01（最后两位表示旬）表示9月第一旬 比如加一旬就是返回20110902
	 * @param current 	当前YYYYMM
	 * @param currentDate 1 2 3
	 * @param addNum 只能是1 2 3 -1 -2 3
	 * @return
	 */
	public static  Date getAddTenDay(Date currentDate,int current,int addNum){
		int result = 1;
		String date = "";
		switch (addNum) {
		case 1:
			date = current==3?getDate(addMonths(currentDate, 1),YYYYMM ):getDate(currentDate,YYYYMM );
			result = current==3?1:current+1;
			break;
		case 2:
			date = current==1?getDate(currentDate,YYYYMM ):getDate(addMonths(currentDate, 1),YYYYMM );
			result = current==1?3:current==2?1:2;
			break;
		case 3:
			date = getDate(addMonths(currentDate, 1),YYYYMM );
			result = current;
			break;
		case -1:
			date = current==1?getDate(addMonths(currentDate, -1),YYYYMM ):getDate(currentDate,YYYYMM );
			result = current==1?3:current-1;
			break;
		case -2:
			date = current==3?getDate(currentDate,YYYYMM ):getDate(addMonths(currentDate, -1),YYYYMM );
			result = current==3?1:current==2?3:2;
			break;
		case -3:
			date = getDate(addMonths(currentDate, -1),YYYYMM );
			result = current;
			break;
		}
		String yyyymmday =  date + "0" + result;
		return parseStringToDate(YYYYMMdd, yyyymmday);
	}
	
	
	/**
	 * 在给定时间基础上加时间 addNum单位为分钟
	 * @param currentDate
	 * @param addNum
	 * @return
	 */
	public static  Date getAddTime(Date currentDate,int addNum){
		Calendar cal = Calendar.getInstance();
		cal.setTime(currentDate);
		cal.add(Calendar.MINUTE, addNum);
		return cal.getTime();
	}
	
	/**
	 * 给指定日期加上对应的天数
	 * @param date
	 * @param i
	 * @return
	 */
	public static Date addDays(Date date,int i){
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.DATE,i);
		return cal.getTime();
		
	}
	/**
	 * 给指定日期加上对应的天数
	 * @param date
	 * @param i
	 * @return
	 */
	public static Date addMonths(Date date,int i){
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.MONTH,i);
		return cal.getTime();
		
	}
	/**
	 * 给指定日期加上对应的小时
	 * @param date
	 * @param i
	 * @return
	 */
	public static Date addHours(Date date,int i){
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.HOUR,i);
		return cal.getTime();
		
	}
	
	/**
	 * 得到当前是星期几
	 * @return
	 */
	public static String getWeek(){
		String[] weekDays = {"日", "一", "二", "三", "四", "五", "六"};
		return weekDays[Calendar.getInstance().get(Calendar.DAY_OF_WEEK)-1];
	}
	
	/**
	 * 给当前时间匹配格式
	 * @param pattern
	 * @return
	 */
	public static Date parseStringToDate(String pattern,String stringDate){
		SimpleDateFormat sdf = new SimpleDateFormat(pattern);
		Date date = null;
		try {
			date = sdf.parse(stringDate);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return date;
	}
	
	/**
	 * 根据传入的字符串时间和格式与当前时间做比较 大于当前时间返回ture
	 * @param time
	 * @param pattern
	 * @return
	 */
	public static boolean isMoreThanCurrentTime(String time,String pattern){
		boolean flag = false;
		Date date = parseStringToDate(pattern,time);
		Calendar cal = Calendar.getInstance();
		cal.clear();
		cal.setTime(date);
		long d1 = cal.getTimeInMillis();
		long d2 = System.currentTimeMillis();
		flag = ((d1-d2)>=0)?true:flag;
		return flag;
	}
	
	 /**
	  * 年份
	  * @param date
	  * @return
	  */
	 public static int getYear(Date date){
	  Calendar cal = Calendar.getInstance();
	  cal.clear();
	  cal.setTime(date);
	  return cal.get(Calendar.YEAR);
	 }
	 /**
	  * 年份
	  * @param date
	  * @return
	  */
	 public static String getYearString(Date date){
		 return String.valueOf(getYear(date));
	 }
	 
	 /**
	  * 月份
	  * @param date
	  * @return
	  */
	 public static int getMonth(Date date){
	  Calendar cal = Calendar.getInstance();
	  cal.clear();
	  cal.setTime(date);
	  return cal.get(Calendar.MONTH)+1;//Calendar对象默认一月为0
	  
	 }
	 
	 /**
	  * 月份
	  * @param date
	  * @return
	  */
	 public static String getMonthString(Date date){
		 Calendar cal = Calendar.getInstance();
		 cal.clear();
		 cal.setTime(date);
		 int month = cal.get(Calendar.MONTH)+1;//Calendar对象默认一月为0
		 if(month<10){
			 return "0"+String.valueOf(month);
		 }
		 return  String.valueOf(month);
	 }
	 
	 
	 /**
	   * 根据日期获取季度
	   * @param cal
	   * @return
	   */
	  public static int getSeason(Date date) {
		Calendar cal = Calendar.getInstance();
		cal.clear();
		cal.setTime(date);
		int month = cal.get(Calendar.MONTH);
		return (month + 3) / 3;
	}

	 
	 /**
	  * 多少号
	  * @param date
	  * @return
	  */
	 public static String getDayString(Date date){
	  Calendar cal = Calendar.getInstance();
	  cal.clear();
	  cal.setTime(date);
	  int day = cal.get(Calendar.DAY_OF_MONTH);
	  if (day < 10) {
			return "0" + String.valueOf(day);
		}
		return String.valueOf(day);
	 }
	 
	 
	 /**
	  * 多少小时
	  * @param date
	  * @return
	  */
	 public static String getHourString(Date date){
		 Calendar cal = Calendar.getInstance();
		 cal.clear();
		 cal.setTime(date);
		 int hour = cal.get(Calendar.HOUR_OF_DAY);
		 if (hour < 10) {
			 return "0" + String.valueOf(hour);
		 }
		 return String.valueOf(hour);
	 }
	 
	 
	 /**
	  * 多少分
	  * @param date
	  * @return
	  */
	 public static String getMinString(Date date){
		 Calendar cal = Calendar.getInstance();
		 cal.clear();
		 cal.setTime(date);
		 int min = cal.get(Calendar.MINUTE);
		 if (min < 10) {
			 return "0" + String.valueOf(min);
		 }
		 return String.valueOf(min);
	 }
	 
	 
	//需要注意的是：月份是从0开始的，比如说如果输入5的话，实际上显示的是4月份的最后一天
     public static String getLastDayOfMonth(String year, String month,String pattern) {     
         Calendar cal = Calendar.getInstance();     
         cal.set(Calendar.YEAR, Integer.parseInt(year));     
         cal.set(Calendar.MONTH, Integer.parseInt(month));     
    	 cal.set(Calendar.DAY_OF_MONTH,1);
    	 cal.add(Calendar.DAY_OF_MONTH, -1); 
        return  new   SimpleDateFormat(pattern).format(cal.getTime());  
     }   
     public static String getFirstDayOfMonth(String year, String month,String pattern) {     
         Calendar cal = Calendar.getInstance();     
         cal.set(Calendar.YEAR, Integer.parseInt(year));     
         cal.set(Calendar.MONTH, Integer.parseInt(month)-1);  
         cal.set(Calendar.DAY_OF_MONTH,cal.getMinimum(Calendar.DATE));  
        return   new   SimpleDateFormat(pattern).format(cal.getTime());  
     } 
     public static String getLastDayOfMonth(int year, int month,String pattern) {     
    	 Calendar cal = Calendar.getInstance();     
    	 cal.set(Calendar.YEAR, year);     
    	 cal.set(Calendar.MONTH, (month)); 
    	 cal.set(Calendar.DAY_OF_MONTH,1);
    	 cal.add(Calendar.DAY_OF_MONTH, -1);
    	 return  new   SimpleDateFormat(pattern).format(cal.getTime());  
     }   
     public static String getFirstDayOfMonth(int year, int month,String pattern) {     
    	 Calendar cal = Calendar.getInstance();     
    	 cal.set(Calendar.YEAR, year);     
    	 cal.set(Calendar.MONTH, month-1);  
    	 cal.set(Calendar.DAY_OF_MONTH,cal.getMinimum(Calendar.DATE));  
    	 return   new   SimpleDateFormat(pattern).format(cal.getTime());  
     } 
     
     
     // 获得某年某月第一天的日期
	public static Date getFirstDayOfMonth(int year, int month) {

		Calendar calendar = Calendar.getInstance();

		calendar.set(Calendar.YEAR, year);

		calendar.set(Calendar.MONTH, month - 1);

		calendar.set(Calendar.DATE, 1);
		
		return calendar.getTime();

	}

	// 获得某年某月最后一天的日期
	public static Date getLastDayOfMonth(int year, int month) {

		Calendar calendar = Calendar.getInstance();

		calendar.set(Calendar.YEAR, year);

		calendar.set(Calendar.MONTH, month);

		calendar.set(Calendar.DATE, 1);

		return getPreviousDate(calendar.getTime());

	}

	// 获得某一日期的前一天
	public static Date getPreviousDate(Date date) {

		Calendar calendar = Calendar.getInstance();

		calendar.setTime(date);

		int day = calendar.get(Calendar.DATE);

		calendar.set(Calendar.DATE, day - 1);

		return calendar.getTime();

	}  

	
	public static String getYYYYMM() {
		Calendar calendar = Calendar.getInstance();
		return Integer.toString(calendar.get(Calendar.YEAR));
	}

	public static String getMonth() {
		Calendar cal = Calendar.getInstance();
		return cal.get(Calendar.MONTH) + 1 > 9 ? Integer.toString((cal
				.get(Calendar.MONTH) + 1)) : "0"
				+ (cal.get(Calendar.MONTH) + 1);
	}
	
	/**
	 * 根据开始时间和结束时间返回范围内的每月开始日期和结束日期 如2011-07-01～2011-09-06，那么返回{"2011-07-01,2011-07-31","2011-08-01,2011-08-31","2011-09-01,2011-09-06"}
	 * @param start  YYYY-MM-DD格式
	 * @param end  	 YYYY-MM-DD格式
	 * @return
	 */
	public static List<String> getMonths(String start,String end){
		Date date_s = parseStringToDate(YYYY_MM_DD, start); 
		Date date_e = parseStringToDate(YYYY_MM_DD, end);
		int year_s = getYear(date_s);
		int year_e = getYear(date_e);
		int month_s = getMonth(date_s);
		int month_e = getMonth(date_e);
		Date temp = date_s;
		int temp_year = year_s;
		int temp_month = month_s;
		List<String> days = null;
		if(date_s.compareTo(date_e)<=0){
			days = new ArrayList<String>();
			if(year_s==year_e && month_s == month_e){
				days.add(start+","+end);
				return days;
			}
			while(!getDate(temp, YYYY_MM).equals(getDate(date_e, YYYY_MM))){
				if(temp_year == year_e && temp_month == month_s){
					days.add(start+","+getLastDayOfMonth(temp_year, temp_month, YYYY_MM_DD));
				}else{
					days.add(getFirstDayOfMonth(temp_year, temp_month, YYYY_MM_DD)+","+getLastDayOfMonth(temp_year, temp_month, YYYY_MM_DD));
				}
				temp = addMonths(temp, 1);
				temp_year = getYear(temp);
				temp_month = getMonth(temp);
			}
			if(temp_year == year_e && temp_month == month_e){
				days.add(getFirstDayOfMonth(temp_year, temp_month, YYYY_MM_DD)+","+end);
				return days;
			}
		}
		return days;
	}
	/**
	 * 根据年和季度获得季度的开始日期和结束日期
	 * @param yyyy
	 * @param season
	 * @param pattern
	 * @return
	 */
	public static String getSeasonDate(String yyyy,int season,String pattern){
		String seasonDate = "";
		switch (season) {
		case 1:
			seasonDate = getFirstDayOfMonth(yyyy, "1", pattern)+","+getLastDayOfMonth(yyyy, "3", pattern);
			break;
		case 2:
			seasonDate = getFirstDayOfMonth(yyyy, "4", pattern)+","+getLastDayOfMonth(yyyy, "6", pattern);
			break;
		case 3:
			seasonDate = getFirstDayOfMonth(yyyy, "7", pattern)+","+getLastDayOfMonth(yyyy, "9", pattern);
			break;
		case 4:
			seasonDate = getFirstDayOfMonth(yyyy, "10", pattern)+","+getLastDayOfMonth(yyyy, "12", pattern);
			break;
		}
		return seasonDate;
	}
	/**
	 * 根据年和季度获得季度的月份
	 * @param yyyy
	 * @param season
	 * @param pattern
	 * @return
	 */
	public static String[] getSeasonMonth(String yyyy,int season,String pattern){
		String seasonMonth[] = new String[3];
		switch (season) {
		case 1:
			seasonMonth[0]=getFirstDayOfMonth(yyyy, "1", pattern)+","+getLastDayOfMonth(yyyy, "1", pattern);
			seasonMonth[1]=getFirstDayOfMonth(yyyy, "2", pattern)+","+getLastDayOfMonth(yyyy, "2", pattern);
			seasonMonth[2]=getFirstDayOfMonth(yyyy, "3", pattern)+","+getLastDayOfMonth(yyyy, "3", pattern);
			break;
		case 2:
			seasonMonth[0]=getFirstDayOfMonth(yyyy, "4", pattern)+","+getLastDayOfMonth(yyyy, "4", pattern);
			seasonMonth[1]=getFirstDayOfMonth(yyyy, "5", pattern)+","+getLastDayOfMonth(yyyy, "5", pattern);
			seasonMonth[2]=getFirstDayOfMonth(yyyy, "6", pattern)+","+getLastDayOfMonth(yyyy, "6", pattern);
			break;
		case 3:
			seasonMonth[0]=getFirstDayOfMonth(yyyy, "7", pattern)+","+getLastDayOfMonth(yyyy, "7", pattern);
			seasonMonth[1]=getFirstDayOfMonth(yyyy, "8", pattern)+","+getLastDayOfMonth(yyyy, "8", pattern);
			seasonMonth[2]=getFirstDayOfMonth(yyyy, "9", pattern)+","+getLastDayOfMonth(yyyy, "9", pattern);
			break;
		case 4:
			seasonMonth[0]=getFirstDayOfMonth(yyyy, "10", pattern)+","+getLastDayOfMonth(yyyy, "10", pattern);
			seasonMonth[1]=getFirstDayOfMonth(yyyy, "11", pattern)+","+getLastDayOfMonth(yyyy, "11", pattern);
			seasonMonth[2]=getFirstDayOfMonth(yyyy, "12", pattern)+","+getLastDayOfMonth(yyyy, "12", pattern);
			break;
		}
		return seasonMonth;
	}
	
	/**
	 * 返回指定日期的年对应天数
	 * @param date
	 * @return
	 */
	public static int getYearDays(Date date){
		int days = 0; 
		Calendar calendar = Calendar.getInstance();
		calendar.clear();
		calendar.setTime(date);
		days = calendar.getActualMaximum(Calendar.DAY_OF_YEAR);
		return days;
	}
	
	/**
	 * 返回指定日期的年对月天数
	 * @param date
	 * @return
	 */
	public static int getMonthDays(Date date){
		int days = 0; 
		Calendar calendar = Calendar.getInstance();
		calendar.clear();
		calendar.setTime(date);
		days = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
		return days;
	}
	
	/**
	 * 返回对应日期下的指定旬对应的天数
	 * @param date
	 * @param ten
	 * @return
	 */
	public static int getTendays(Date date, int ten) {
		int days = 10;
		if(ten==1 || ten ==2){return days;}
		days = getMonthDays(date)-20;
		return days;
	}
	
	/**
	 * 返回对应日期下的对应季度下的天数
	 * @param date
	 * @return
	 */
	public static int getSeasonDays(Date date) {
		int days = 0;
		int season = getSeason(date);
		String dateString[] = getSeasonDate(getYearString(date), season, YYYY_MM_DD).split(",");
		if(dateString!=null && dateString.length!=0){
			days = getMonthDays(parseStringToDate(YYYY_MM_DD, dateString[0]))
					+ getMonthDays(addMonths(parseStringToDate(YYYY_MM_DD, dateString[0]), 1))
					+ getMonthDays(parseStringToDate(YYYY_MM_DD, dateString[1]));
		}
		return days;
	}
	
	public static Date parseDate(String dateStr) {
		return parseDate(dateStr, "yyyy-MM-dd HH:mm:ss");
	}
	
	public static Date parseDate(String dateStr, String format) {
		try {
			if ((dateStr == null) || dateStr.equals("")) {
				return null;
			}
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
			return simpleDateFormat.parse(dateStr);
		} catch (Exception e) {
			return new Date();
		}
	}
	
	/**
	 * 获得之前多少天的日期集合
	 * @param date
	 * @param days
	 * @return
	 */
	public static List<Date> getPrevDaysList(Date date,int days){
		List<Date> list = new ArrayList<Date>();
		for(int i=0;i<days;i++){
			list.add(addDays(date,-(29-i)));
		}
		return list;
	}
	/**
	 * 获得之前多少月的日期集合
	 * @param date
	 * @param days
	 * @return
	 */
	public static List<Date> getPrevMonthsList(Date date,int days){
		List<Date> list = new ArrayList<Date>();
		for(int i=0;i<days;i++){
			list.add(addMonths(date,-i));
		}
		return list;
	}
	/**
	 * 获得当前日期的前两天
	 * @param date
	 * @param days
	 * @return
	 */
	 public static String getDateTwo() { 
	        Calendar c = Calendar.getInstance(); 
	        // 前两天 
	        c = test(c, -2); 
			return getDate(c.getTime(), YYYYMMdd); 
	    } 
		/**
		 * 获得当前日期的前两天
		 * @param date
		 * @param days
		 * @return
		 */
		 public static String getDateThree() { 
		        Calendar c = Calendar.getInstance(); 
		        // 前两天 
		        c = test(c, -3); 
				return getDate(c.getTime(), YYYYMMdd); 
		    } 
	 
		/**
		 * 获得当前日期的前两天
		 * @param date
		 * @param days
		 * @return
		 */
		 public static String getMonthOne(int n) { 
		        Calendar c = Calendar.getInstance(); 
		        c.add(Calendar.MONTH, -n);    //得到前一个月
				return getDate(c.getTime(), YYYYMMdd); 
		    } 
		/**
		 * 获得当前日期的前N天
		 * @param date
		 * @param days
		 * @return
		 */
		 public static String getDate(int day) { 
		        Calendar c = Calendar.getInstance(); 
		        c = test(c, -day); 
				return getDate(c.getTime(), YYYYMMdd); 
		    } 
		 
	    private static Calendar test(Calendar c, int day) { 

	        c.add(Calendar.DATE, day); 

	        return c; 

	    }
		/**yangec 20151211 
		 * 获得指定日期内的天数
		 * @param date
		 * @param days
		 * @return
		 */ 
    public static int daysBetween(Date date1,Date date2){  
       
    	Calendar cal = Calendar.getInstance();  
        cal.setTime(date1);  
        long time1 = cal.getTimeInMillis();               
        cal.setTime(date2);  
        long time2 = cal.getTimeInMillis();       
        long between_days=(time2-time1)/(1000*3600*24);  
       return Integer.parseInt(String.valueOf(between_days))+1;         

    }
    /** 
     * 根据开始时间和结束时间返回时间段内的时间集合 
     *  
     * @param beginDate 
     * @param endDate 
     * @return List 
     */  
    public static List<Date> getDatesBetweenTwoDate(Date beginDate, Date endDate) {  
        List<Date> lDate = new ArrayList<Date>();  
       lDate.add(beginDate);// 把开始时间加入集合  
        Calendar cal = Calendar.getInstance();  
        // 使用给定的 Date 设置此 Calendar 的时间  
        cal.setTime(beginDate);  
        boolean bContinue = true;  
        while (bContinue) {  
            // 根据日历的规则，为给定的日历字段添加或减去指定的时间量  
            cal.add(Calendar.DAY_OF_MONTH, 1);  
            // 测试此日期是否在指定日期之后  
            if (endDate.after(cal.getTime())) {  
                lDate.add(cal.getTime());  
            } else {  
                break;  
            }  
       }  
        lDate.add(endDate);// 把结束时间加入集合  
        return lDate;  
    } 


	public static void main(String args[]) throws ParseException{
		// SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHH");
		//System.out.println(sdf.format(getAddTime(new Date(),30)));
		////System.out.println(Calendar.getInstance().get(Calendar.DAY_OF_WEEK));
		////System.out.println(DateTime.getDate(CONSTANT.TimePatternTwo));
		//String str = "advESLDFJ塑料袋警方";
/*		System.out.println(DateTime.getDate(DateTime.addHours(sdf.parse("2011042112"),8),"yyyyMMddHHss"));
		System.out.println(DateTime.getDate(DateTime.addDays(new Date(), 1),"yyyy-MM-dd"));
		System.out.println(DateTime.getDate(DateTime.addDays(new Date(), -1),"yyyy-MM-dd"));
		System.out.println(DateTime.getDate(DateTime.getAddTime(new Date(), -1),SystemConstant.DateyyyyMMdd));
		System.out.println(DateTime.getDate(DateTime.getAddTime(new Date(), -10),"yyyy-MM-dd HH:mm:ss"));
		System.out.println(DateTime.parseStringToDate("yyyy-MM-dd HH:mm","2011-04-14 17:14"));
		System.out.println(DateTime.isMoreThanCurrentTime("2011-04-04 17:14","yyyy-MM-dd HH:mm"));
		System.out.println(DateTime.getDate(DateTime.addHours(new Date(), -8),SystemConstant.DateTimePatterntwo));
		String fileNamePattern= "Z_SEVP_C_*_*_P_RFFC_SPCC_*_16812.TXT";
		String filePattern[] = fileNamePattern.split("\\*");
		System.out.println(filePattern[0]+"xxx"+filePattern[1]+"yyy"+filePattern[2]+"zzz"+filePattern[3]);
		System.out.println(DateTime.getYear(new Date()));
		System.out.println(DateTime.getMonthString(new Date()));
		System.out.println(DateTime.getDayString(new Date()));
		System.out.println(DateTime.getHourString(new Date()));
		System.out.println(DateTime.getMinString(new Date()));
		List<String> ms = DateTime.getMonths("2011-08-01", "2011-08-12");
		for(String m:ms){
			System.out.println(m);
		}*/
		/*System.out.println(DateTime.getSeasonDate("2011", 4, "yyyyMM"));
		System.out.println(DateTime.getTendays(new Date(), 2));
		System.out.println(DateTime.getSeasonDays(new Date()));
		System.out.println(DateTime.getAddTenDay(DateTime.parseStringToDate("yyyyMM", "201101"), 2, -2));*/
		//System.out.println(DateTime.getDate(new Date(), "yyyyMMddHHmmsss"));
//		System.out.println(DateTime.getLastDayOfMonth(2012, 2, DateTime.YYYY_MM_DD));
//		System.out.println(DateTime.getFirstDayOfMonth(2012, 8, DateTime.YYYY_MM_DD));
	//	System.out.println(getDate("20151130","20151201"));	
//	System.out.println(daysBetween(parseStringToDate(YYYYMMdd, "20151201"),parseStringToDate(YYYYMMdd, "20151203")));	
		//System.out.println(	getMonthOne());
		  
	}
}