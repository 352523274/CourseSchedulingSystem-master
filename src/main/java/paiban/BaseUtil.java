package paiban;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.pagehelper.Page;

import java.io.*;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.security.SecureRandom;
import java.sql.*;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.*;

/**
 * @author:		 lewe
 * @date:		 2017年2月24日 下午07:03:28
 * @description: TODO(基础工具类)
 */
@SuppressWarnings("restriction")
public class BaseUtil {

	/** 初始编号 */
	private static long startVaue = 0;
	
	/** Json转换操作对象 */
	private static ObjectMapper objectMapper = new ObjectMapper();
	
	/** 路径字符黑名单 */
//	private static HashMap<String, String> charMap = new HashMap<String, String>();
	
	//加载类时初始化数据
	/*static {
		charMap.put("..", "");
	}*/
	/**
	 * @author:		 lewe
	 * @date:		 2017年2月24日 下午07:43:14
	 * @description: TODO(对指定字符串判空)
	 */
	public static boolean isBlank(String str) {
		if (str == null || str.trim().equals("")) {
			return true;
		}
		return false;
	}


//	/**
//	 *传入一个
//	 */
//	public static String getOneRandom(String date, int n) throws ParseException {
//		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
//		Date parse = sdf.parse(date);
//		Calendar calendar = Calendar.getInstance();
//		calendar.setTime(parse);
//		calendar.add(Calendar.DATE, n);
//		parse = calendar.getTime();
//		return sdf.format(parse);
//	}
//





	/**
	 * 传入日期String：2021-03-02 * 获取前n天的日期，正数加    负数减
	 */


	public static String getBeforeDate(String date, int n) throws ParseException {
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
		Date parse = sdf.parse(date);
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(parse);
		calendar.add(Calendar.DATE, n);
		parse = calendar.getTime();
		return sdf.format(parse);
	}



	/**
	 * 随机获取n个不同的数字  包头不包尾
	 * @param min
	 * @param max
	 * @param n
	 * @return
	 */
	public static Integer[] randomCommon(int min, int max, int n){
    if (n > (max - min + 1) || max < min) {  
           return null;  
       }
		Integer[] result = new Integer[n];
    int count = 0;  
    while(count < n) {  
        int num = (int) (Math.random() * (max - min)) + min;
		boolean flag = true;
        for (int j = 0; j < n; j++) {  
            if(result[j]!=null&&num == result[j]){
                flag = false;  
                break;  
            }  
        }  
        if(flag){  
            result[count] = num;  
            count++;  
        }  
    }  
    return result;
	}  
	



	/**
	 * @author:		 常保安
	 * @date:
	 * @description: TODO(将1转成01)
	 */
	public static String str2gene1to01(String str) {
		if (str == null || str.trim().equals("")) {
			return "";
		}
		int i=0;
		try {
			i = Integer.parseInt(str);
		} catch (NumberFormatException e) {
			return "";
		}

		if (str.length()==2){
			return str;
		}
		return "0"+str;
	}

	/**
	 * @author:		 常保安
	 * @date:
	 * @description: TODO(将1转成0001)
	 */
	public static String str2gene1to0001(String str) {
		if (str == null || str.trim().equals("")) {
			return "";
		}
		int i=0;
		try {
			i = Integer.parseInt(str);
		} catch (NumberFormatException e) {
			return "";
		}

		if (str.length()==4){
			return str;
		}else if (str.length()==3){
			return "0"+str;
		}else if (str.length()==2){
			return "00"+str;
		}else if (str.length()==1){
			return "000"+str;
		}
		return "";

	}




	/**
	 * 转换空值，为null时返回空字符串，其他情况原值去空格返回
	 * 
	 * @author:	lewe
	 * @date:	2017年12月7日 下午3:34:37
	 */
	public static String convetNullValue(String str) {
		if (isBlank(str)) {
			return "";
		}
		return str.trim();
	}
	
	/**
	 * @author:		 lewe
	 * @date:		 2017年2月24日 下午07:43:25
	 * @description: TODO(按逗号分割字符串str，给每一项前后加上英文单引号，再返回用逗号拼接后的字符串)
	 */
	public static String addSingleQuote(String str) {
		if (isBlank(str)) {
			return "''";
		}
		String[] arr = str.split("\\,");
		String result = "'" + arr[0] + "'";
		for (int i=1; i<arr.length; i++) {
			result = result + ",'" + arr[i] + "'";
		}
		return result;
	}
	
	/**
	 * @author:		 lewe
	 * @date:		 2017年2月24日 下午07:43:40
	 * @description: TODO(去除字符串str中的所有英文单引号)
	 */
	public static String deleteSingleQuote(String str) {
		return str.replaceAll("'", "");
	}
	
	/**
	 * @author:		 lewe
	 * @date:		 2017年2月24日 下午07:45:58
	 * @description: TODO(获取rs中指定名称的值，并非Null化（即如果获得的结果为Null，返回“”空字符串）)
	 */
	public static String getStringByName(ResultSet rs, String name) throws SQLException {
		String str = rs.getString(name);
		return str == null ? "" : str.trim();
	}
	
	/**
	 * @author:		 lewe
	 * @date:		 2017年2月24日 下午07:46:18
	 * @description: TODO(获取rs中指定索引的值，并非Null化（即如果获得的结果为Null，返回“”空字符串）)
	 */
	public static String getStringByIndex(ResultSet rs, int index) throws SQLException {
		String str = rs.getString(index);
		return str == null ? "" : str.trim();
	}
	
	/**
	 * @author:		 lewe
	 * @date:		 2017年2月24日 下午07:46:29
	 * @description: TODO(判断是否是单列查询)
	 */
	public static boolean isSingleColumnSelecte(String sql) {
		int fromIndex = sql.toLowerCase().indexOf("from");
		String temp = sql.substring(0, fromIndex);
		if (temp.indexOf(",") > 0) { // 有点片面
			return false;
		}
		return true;
	}
	
	/**
	 * @author:		 lewe
	 * @date:		 2017年2月24日 下午07:46:49
	 * @description: TODO(根据不同的类型返回不同的对象)
	 */
	@SuppressWarnings("deprecation")
	public static Object getValue(String type, String value) {
		if (type.equalsIgnoreCase("string")) {
			return value;
		} else if (type.equalsIgnoreCase("int") || type.equalsIgnoreCase("integer")) {
			return Integer.parseInt(value);
		} else if (type.equalsIgnoreCase("float")) {
			return Float.parseFloat(value);
		} else if (type.equalsIgnoreCase("double")) {
			return Double.parseDouble(value);
		} else if (type.equalsIgnoreCase("boolean")) {
			return value.equalsIgnoreCase("true") ? true : false;
		} else if (type.equalsIgnoreCase("short")) {
			return Short.parseShort(value);
		} else if (type.equalsIgnoreCase("long")) {
			return Long.parseLong(value);
		} else if (type.equalsIgnoreCase("byte")) {
			return Byte.parseByte(value);
		} else if (type.equalsIgnoreCase("date")) {
			return Date.parse(value);
		} else if (type.equalsIgnoreCase("clob")) {
			return (Clob)((Object)value);
		} else if (type.equalsIgnoreCase("timestamp")) {
			return Timestamp.parse(value);
		}
		return value;
	}

	/**
	 * @author:		 lewe
	 * @date:		 2017年2月24日 下午07:51:57
	 * @description: TODO(转换Clob为字符串)
	 */
	public static String clob2String(Object cbObj) {
		String retStr = "";
		Clob cb = (Clob)cbObj;
		Reader in = null;
		BufferedReader br = null;
		try {
			in = cb.getCharacterStream();
			br = new BufferedReader(in);
			String s = br.readLine();
			StringBuffer sb = new StringBuffer();
			while (s != null) {
				sb.append(s);
				s = br.readLine();
			}
			retStr = sb.toString();
		} catch (Exception e) {
		} finally {
			try {
				if (in != null) {
					in.close();
				}
				if (br != null) {
					br.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return retStr;
	}
	
	/**
	 * @author:		 lewe
	 * @date:		 2017年2月24日 下午07:50:57
	 * @description: TODO(获得指定的年月份包含的天数（区分闰年），如"201210"返回的天数为31)
	 */
	public static int getDayNumOfMonth(String yearMonth) {
		int daysNum = 30;
		if (yearMonth == null || yearMonth.equals("") || yearMonth.length() != 6) {
			return daysNum;
		}
		String month = Integer.valueOf(yearMonth.substring(4)).toString();
		if (",1,3,5,7,8,10,12,".indexOf("," + month + ",") != -1) {
			daysNum = 31;
			
		} else if ("2".equals(month)) {
			int year = Integer.valueOf(yearMonth.substring(0,4));
			if ((year%4 == 0 && year%100 != 0) || year%400 == 0) {
				daysNum = 29;
			} else {
				daysNum = 28;
			}
		}
		return daysNum;
	}
	
	/**
	 * @author:		 lewe
	 * @date:		 2017年2月24日 下午07:51:09
	 * @description: TODO(获取一个流水号)
	 */
	public static synchronized String getSerialNumber() {
		if (startVaue >= 999) {
			startVaue = 0;
		}
		startVaue ++;
		startVaue = startVaue % 1000;
		DecimalFormat df = new DecimalFormat("000");
		String sStartVaue = df.format(startVaue);
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmssSSS");
		Date today = new Date();
		String sDate = sdf.format(today);
		
		return sDate + sStartVaue;
	}
	

	

	/**
	 * @author:		 lewe
	 * @date:		 2017年2月24日 下午07:52:35
	 * @description: TODO(转换指定对象为Json字符串)
	 */
	public static String transObj2Json(Object object) {
		String retJson = "";
		try {
			retJson = objectMapper.writeValueAsString(object);
		} catch (JsonProcessingException e) {

		}
		return retJson;
	}
	
	/**
	 * @author:		 lewe
	 * @date:		 2017年2月24日 下午07:52:35
	 * @description: TODO(转换Json字符串为指定对象)
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static Object transJson2Obj(String str, Class objClass) {
		Object retObj = new Object();
		try {
			retObj = objectMapper.readValue(str, objClass);
		} catch (IOException e) {

		}
		return retObj;
	}
	
	/**
	 * @author:		 lewe
	 * @date:		 2017年6月18日 上午10:29:02
	 * @description: TODO(格式化json，便于输出日志查看)
	 */
	public static String formatJson(String jsonStr) {
		if (isBlank(jsonStr)) {
			return "";
		}
		StringBuilder sb = new StringBuilder();
		char last = '\0';
		char current = '\0';
		int indent = 0;
		boolean isInQuotationMarks = false;
		for (int i = 0; i < jsonStr.length(); i++) {
			last = current;
			current = jsonStr.charAt(i);
			switch (current) {
				case '"':
	                if (last != '\\') {
	                	isInQuotationMarks = !isInQuotationMarks;
	                }
	                sb.append(current);
	                break;
				case '{':
				case '[':
					sb.append(current);
					if (!isInQuotationMarks) {
						sb.append('\n');
						indent++;
						addIndentBlank(sb, indent);
					}
					break;
				case '}':
				case ']':
					if (!isInQuotationMarks) {
						sb.append('\n');
						indent--;
						addIndentBlank(sb, indent);
					}
					sb.append(current);
					break;
				case ',':
					sb.append(current);
					if (last != '\\' && !isInQuotationMarks) {
						sb.append('\n');
						addIndentBlank(sb, indent);
					}
					break;
				default:
					sb.append(current);
			}
		}
		return sb.toString();
	}

	/**
	 * @author:		 lewe
	 * @date:		 2017年6月18日 上午10:32:27
	 * @description: TODO(添加缩进)
	 */
	private static void addIndentBlank(StringBuilder sb, int indent) {
		for (int i = 0; i < indent; i++) {
			sb.append('\t');
		}
	}
	
	/**
	 * @author:		zheng.jw
	 * @date:		2017年8月31日 上午9:31:19
	 * @Description:传入文件路径，返回文件名
	 */
	public static String getAttachmentName(String path) {
		int index = path.lastIndexOf("\\");
		if (index == -1) {
			index = path.lastIndexOf("/");
		}
		String name = path.substring(index + 1);
		index = name.indexOf("_");
		name = name.substring(index + 1);
		return name;
	}
	
	/**
	 * 获取本地服务器IP
	 * 
	 * @author:	lewe
	 * @date:	2017年12月6日 下午4:35:49
	 */
	public static String getHostAddress() throws Exception {
		return InetAddress.getLocalHost().getHostAddress();
	}
	
	/**
	 * 获取本地服务器的所有IP
	 * 
	 * @author:	lewe
	 * @date:	2017年12月6日 下午4:35:38
	 */
	public static List<String> getHostAllAddress() throws Exception {
		List<String> ipList = new ArrayList<String>();
		Enumeration<NetworkInterface> netInterfaces = null;
        netInterfaces = NetworkInterface.getNetworkInterfaces();
        while (netInterfaces.hasMoreElements()) {
            NetworkInterface ni = netInterfaces.nextElement();
            Enumeration<InetAddress> ips = ni.getInetAddresses();
            while (ips.hasMoreElements()) {
                ipList.add(ips.nextElement().getHostAddress());     
            }
        }
        return ipList;
	}
	
	/** 
     * 获取指定时间范围内的所有日期 
     *  
     * @param startDate 
     *            (format="20160601") 
     * @param endDate 
     *            (format="20160712") 
     * @return 
     * @throws ParseException
     */  
	public static List<String> getDaysBetween(String startDate, String endDate) {
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
        List<String> result = new ArrayList<String>();
        result.add(startDate);
        try {
            Calendar calBegin = Calendar.getInstance();
            // 使用给定的 Date 设置此 Calendar 的时间 
            Date begin = format.parse(startDate);
            calBegin.setTime(begin);
            Calendar calEnd = Calendar.getInstance();
            // 使用给定的 Date 设置此 Calendar 的时间 
            Date end = format.parse(endDate);
            calEnd.setTime(end);
            // 测试此日期是否在指定日期之后 
            while (end.after(calBegin.getTime())) {
                // 根据日历的规则，为给定的日历字段添加或减去指定的时间量 
                calBegin.add(Calendar.DAY_OF_MONTH, 1);
                result.add(format.format(calBegin.getTime()));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }
	

	
	/**
	 * 获取当前日期的指定间隔（年、月、日）字符窜
	 * 
	 * @author:	lewe
	 * @date:	2018年8月16日 上午10:10:45
	 * 
	 * @param	fStr	日期格式，yyyy 表示 年，yyyyMM 表示 月，yyyyMMdd 表示 日
	 * @param	num		间隔数量，正数为向后，负数为向前，比如 前几年/月/日、后几年/月/日
	 */
	public static String getInternalDateByFormat(String fStr, int num) {
		Date dNow = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(dNow);
        if ("yyyy".equals(fStr)) {
        	calendar.add(Calendar.YEAR, num);
        } else if ("yyyyMM".equals(fStr)) {
        	calendar.add(Calendar.MONTH, num);
        } else if ("yyyyMMdd".equals(fStr)) {
        	calendar.add(Calendar.DATE, num);
        }
        dNow = calendar.getTime();
        SimpleDateFormat sdf = new SimpleDateFormat(fStr);
        return sdf.format(dNow);
	}
	
	/**
	 * @author:		 wwx
	 * @date:		 2018年5月2日 下午3:43:44
	 * @description: TODO(获取当前日期后'afterNum'个月的日期或前'afterNum'个月的日期，正数为后几个月，负数获取前几个月)
	 */
	public static String getCurrentDateAfterStr(int afterNum) {
		Date dNow = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(dNow);
        calendar.add(Calendar.MONTH, afterNum);
        dNow = calendar.getTime();
        SimpleDateFormat sdf=new SimpleDateFormat("yyyyMMdd");
        String defaultDate = sdf.format(dNow);
        return defaultDate;
	}
	
	/**
	 * @author:		 wwx
	 * @date:		 2018年7月13日 下午3:45:54
	 * @description: TODO(获取当前日期后'afterNum'日后的日期或'afterNum'日前的日期，正数为n日后日期，负数获取n日之前)
	 */
	public static String getCurrentDateAfterStr2(int afterNum) {
		Date dNow = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(dNow);
        calendar.add(Calendar.DATE, afterNum);
        dNow = calendar.getTime();
        SimpleDateFormat sdf=new SimpleDateFormat("yyyyMMdd");
        String defaultDate = sdf.format(dNow);
        return defaultDate;
	}
	
	/**
	 * 获取两个日期的间隔天数
	 * 
	 * @author:	lewe
	 * @date:	2017年12月29日 下午5:11:43
	 */
	public static int getDiffDays(Date date1, Date date2) {
		int days = (int) ((date2.getTime() - date1.getTime()) / (1000*3600*24));
		return Math.abs(days);
	}
	
	/**
     *  获取两个日期相差的月数
     *  
     * @author:	xy
	 * @date:	2019年4月16日16:05:05
     * @param d1    较大的日期
     * @param d2    较小的日期
     * @return  如果d1>d2返回 月数差 否则返回0
     */
    public static int getMonthDiff(Date d1, Date d2) {
        Calendar c1 = Calendar.getInstance();
        Calendar c2 = Calendar.getInstance();
        c1.setTime(d1);
        c2.setTime(d2);
        if(c1.getTimeInMillis() < c2.getTimeInMillis()) return 0;
        int year1 = c1.get(Calendar.YEAR);
        int year2 = c2.get(Calendar.YEAR);
        int month1 = c1.get(Calendar.MONTH);
        int month2 = c2.get(Calendar.MONTH);
        int day1 = c1.get(Calendar.DAY_OF_MONTH);
        int day2 = c2.get(Calendar.DAY_OF_MONTH);
        // 获取年的差值 假设 d1 = 2015-8-16  d2 = 2011-9-30
        int yearInterval = year1 - year2;
        // 如果 d1的 月-日 小于 d2的 月-日 那么 yearInterval-- 这样就得到了相差的年数
        if(month1 < month2 || month1 == month2 && day1 < day2) yearInterval --;
        // 获取月数差值
        int monthInterval =  (month1 + 12) - month2  ;
        if(day1 < day2) monthInterval --;
        monthInterval %= 12;
        return yearInterval * 12 + monthInterval;
    }
	
	/**
     * 字符串转日期
     * 
     * @author:	xy
	 * @date:	2019年4月16日16:05:05
     * @param date 字符串
     * @return 日期
     * @throws ParseException
     */
    public static Date String2Date(String date, String format) throws ParseException {
        SimpleDateFormat dateFormat = new SimpleDateFormat(format);
        return dateFormat.parse(date);
    }
	

    

	
	/**
	 * 批量转换list中map键值，默认key为小写，指定类型value赋予默认值
	 * 
	 * @author:	lewe
	 * @date:	2018年1月3日 下午6:32:03
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static List convertListMapKeyValue(List list) {
		for (int i = 0; i < list.size(); i++) {
			list.set(i, convertMapKeyValue((Map) list.get(i)));
		}
		return list;
	}
	
	/**
	 * 转换map键值，默认key为小写，指定类型value赋予默认值
	 * 
	 * @author:	lewe
	 * @date:	2018年1月3日 下午6:32:15
	 */
	@SuppressWarnings("rawtypes")
	public static Map<String, Object> convertMapKeyValue(Map map) {
		Map<String, Object> retMap = new HashMap<String, Object>();
		if (map == null || map.isEmpty()) {
			return retMap;
		}
		for (Object key : map.keySet()) {
			String newKey = (key + "").toLowerCase();
			Object newValue = map.get(key);
			if (newValue == null) {
				newValue = "";
			} else if (newValue instanceof Clob) {
				newValue = clob2String(newValue);
			}
			retMap.put(newKey, newValue);
		}
		return retMap;
	}
	

    
    /**
     * 获取指定位数的业务验证码（数字、大写字母组合，不能出现纯字母或纯数字）
     * 
     * @author:	lewe
     * @date:	2018年4月10日 下午3:25:18
     */
    public static String getBusinessCode(int num) {
    	String busiCode = "";
    	char[] charArr = {
			'0','1','2','3','4','5','6','7','8','9',
			'A','B','C','D','E','F','G','H','J','K','L','M',
			'N','P','Q','R','S','T','U','V','W','X','Y','Z'
    	};
    	Random rand = new SecureRandom();
    	// 中间数
    	int mNum = num / 2;
    	// 前半段随机一位为数字
    	int fNum = Math.abs(rand.nextInt(mNum));
    	// 后半段随机一位为字母
    	int bNum = Math.abs(mNum + rand.nextInt(mNum));
    	// 随机获取指定位数字符
    	for (int i = 0; i < num; i++) {
    		char rChar;
    		if (i == fNum) { // 随机数字
    			rChar = charArr[Math.abs(rand.nextInt(10))];
    		} else if (i == bNum) { // 随机字母
    			rChar = charArr[Math.abs(10 + rand.nextInt(24))];
    		} else { // 随机整体
    			rChar = charArr[Math.abs(rand.nextInt(34))];
    		}
    		busiCode += rChar;
    	}
    	return busiCode;
    }
    
    /**
     * 获取指定位数的业务验证码（数字、大写字母、小写字母组合，不能出现纯字母或纯数字）
     * 
     * @author:	lewe
     * @date:	2018年4月10日 下午3:25:18
     */
    public static String getBusinessCode4Aa0(int num) {
    	String busiCode = "";
    	char[] charArr = {
			'A','B','C','D','E','F','G','H','J','K','L','M',
			'N','P','Q','R','S','T','U','V','W','X','Y','Z',
			'a','b','c','d','e','f','g','h','j','k','l','m',
			'n','p','q','r','s','t','u','v','w','x','y','z',
			'0','1','2','3','4','5','6','7','8','9'
    	};
    	Random rand = new SecureRandom();
    	// 中间数，随机一位为数字
    	int mNum = num / 2;
    	// 前半段，随机一位为大写字母
    	int fNum = Math.abs(rand.nextInt(mNum));
    	// 后半段，随机一位为小写字母
    	int bNum = Math.abs(mNum + rand.nextInt(mNum));
    	// 随机获取指定位数字符
    	for (int i = 0; i < num; i++) {
    		char rChar;
    		if (i == fNum) { // 随机大写字母
    			rChar = charArr[Math.abs(rand.nextInt(24))];
    		} else if (i == mNum) { // 随机数字
    			rChar = charArr[Math.abs(48 + rand.nextInt(10))];
    		} else if (i == bNum) { // 随机小写字母
    			rChar = charArr[Math.abs(24 + rand.nextInt(24))];
    		} else { // 随机整体
    			rChar = charArr[Math.abs(rand.nextInt(58))];
    		}
    		busiCode += rChar;
    	}
    	return busiCode;
    }
    


	/**
	 * 获取重定向响应字符串
	 * 
	 * @author:	lewe
	 * @date:	2018年8月3日 下午5:53:05
	 */
	public static String getRedirectStr(String urlStr) {
		StringBuilder builder = new StringBuilder();
		builder.append("<script type=\"text/javascript\">");
		builder.append("window.location.href='");
		builder.append(urlStr);
		builder.append("';");
		builder.append("</script>");
		return builder.toString();
	}
	

	

	

	/**
	 * 获取异常详细（堆栈）信息
	 * 
	 * @author:	lewe
	 * @date:	2018年9月6日 下午4:37:54
	 */
	public static String getStackTrace(Throwable throwable) {
		StringWriter sw = new StringWriter();
		PrintWriter pw = new PrintWriter(sw);
		try {
			throwable.printStackTrace(pw);
			return sw.toString();
		} finally {
			pw.close();
		}
	}
	
	/**
	 * 转换数据查询列表，添加行号 等
	 * 
	 * @author:	lewe
	 * @date:	2017年12月8日 下午2:21:24
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static List getList(List list, Page page) {
		if (list == null || list.size() == 0) {
			return new ArrayList();
		}
		// 起始行号
		int startRow = 0;
		if (page != null) {
			startRow = page.getStartRow();
		}
		for (int i = 0; i < list.size(); i++) {
			Map map = convertMapKeyValue((Map) list.get(i));
			map.put("rn", ++startRow);
			list.set(i, map);
		}
		return list;
	}
	
	/**
	 * @author: wj
	 * @date: 2018年10月9日 上午9:19:28
	 * @description: TODO(格式化日期字符串)
	 * @param	dateStr 日期字符串
	 * @throws ParseException
	 */
	public static String setFormatDate(String dateStr) throws ParseException {
		if (isBlank(dateStr)) {
			return "";
		}
		Date date = new Date();
		if (dateStr.length() == 8) {
			SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
			date = format.parse(dateStr.toString());
			dateStr = new SimpleDateFormat("yyyy-MM-dd").format(date);

		} else if (dateStr.length() == 14) {
			SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
			date = format.parse(dateStr.toString());
			dateStr = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date);
		}
		return dateStr;
	}
	
	/**
	 * @author:		wj
	 * @date:		2018年10月31日 下午5:08:19
	 * @Description:TODO（按逗号分割字符串str,依次放入进list中，并返回该list）
	 */
	@SuppressWarnings({ "rawtypes" })
	public static List addStrToList(String str) {
		List<String> list = new ArrayList<>();
		if (isBlank(str)) {
			return list;
		}
		String[] arr = str.split("\\,");
		for (int i=0; i<arr.length; i++) {
			list.add(arr[i]);
		}
		return list;
	}
	
	/**
     * 判断字符串是否为数字
     * 
     * @author:	lewe
     * @date:	2018年12月8日 下午2:29:19
     */
    public static boolean isNumeric(String str) {
    	if (isBlank(str)) {
    		return false;
    	}
    	str = str.trim().replaceFirst("\\.", ""); // 剔除小数点判断
    	for (int i = 0; i < str.length(); i++) {
    		if (!Character.isDigit(str.charAt(i))) {
    			return false;
    		}
    	}
    	return true;
    }

    /**
     * 递归修改Map中的键值为小写
     * @author: Lch
     * @date: 2019年6月4日上午10:34:12
     */
    @SuppressWarnings("rawtypes")
	public static Map<String, Object> convertMapKeyValueDeep(Map map) {
		Map<String, Object> retMap = new LinkedHashMap<String, Object>();//有序Map
		if (map == null || map.isEmpty()) {
			return retMap;
		}
		for (Object key : map.keySet()) {
			String newKey = (key + "").toLowerCase();
			Object newValue = map.get(key);
			if (newValue == null) {
				newValue = "";
			} else if (newValue instanceof Clob) {
				newValue = clob2String(newValue);
			} else if (newValue instanceof Map) {
				newValue = convertMapKeyValueDeep((Map)newValue);
			} else if (newValue instanceof List) {
				newValue = convertListMapKeyValueDeep((List)newValue);
			}
			retMap.put(newKey, newValue);
		}
		return retMap;
	}
    /**
     * 递归修改List中的Map键值为小写
     * @author: Lch
     * @date: 2019年6月4日上午10:34:12
     */
    @SuppressWarnings({ "unchecked", "rawtypes" })
	public static List convertListMapKeyValueDeep(List list) {
		for (int i = 0; i < list.size(); i++) {
			list.set(i, convertMapKeyValueDeep((Map) list.get(i)));
		}
		return list;
	}
    
   //获取指定年的周末日期数组(参数:2019;类型：int;返回:List<String>)
  	public static List<String> currentYearWeekDateList(int currentYear){
  		List<String> weekList = new ArrayList<String>();
  		try{

  			/*//获取当前年日历实例
  			Calendar currYear = Calendar.getInstance();
  			int currentYear = currYear.get(Calendar.YEAR);*/
  			Calendar calendar = Calendar.getInstance();
  			calendar.clear();
  			//获取年的第一天
  			calendar.set(Calendar.YEAR, currentYear);
  			Date yearFirstDate = calendar.getTime();
  			//获取年的最后一天
  			calendar.roll(Calendar.DAY_OF_YEAR, -1);
  			Date yearLastDate = calendar.getTime();
  			DateFormat format = new SimpleDateFormat("yyyyMMdd");
  			//获取第一天日期日历实例
  			Calendar firstDate = Calendar.getInstance();
  			//获取最后一天天日期日历实例
  			Calendar lastDate = Calendar.getInstance();
  			/*firstDate.setTime(format.parse("20191001"));
  			lastDate.setTime(format.parse("20191231"));*/
  			firstDate.setTime(yearFirstDate);
  			lastDate.setTime(yearLastDate);
  			Calendar currentDay = firstDate;
  			while(true){
  				int calWeek = 0;
  				if(currentDay.compareTo(lastDate) <= 0){
  					//获取当前日期是星期几
  					calWeek = currentDay.get(Calendar.DAY_OF_WEEK);
  					if(calWeek == 1){
  						calWeek = 7;
  					}else{
  						calWeek = calWeek - 1;
  					}
  					//如果是星期六或者星期天则获取日期
  					if(calWeek == 6 || calWeek == 7){
  						weekList.add(format.format(currentDay.getTime()));
  					}
  					//当前日期加一天
  					currentDay.add(Calendar.DATE, 1);
  				}else{
  					break;
  				}
  			}

  		}catch(Exception e){

  		}
  		return weekList;
  	} 
    
    
}
