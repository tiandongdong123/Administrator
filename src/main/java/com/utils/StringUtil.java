package com.utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Hashtable;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;




/**
 * String类型工具类
 * 
 * @author jinxin
 */
public class StringUtil {
	private static final String randChars = "0123456789abcdefghigklmnopqrstuvtxyzABCDEFGHIGKLMNOPQRSTUVWXYZ";
	private static Random random = new Random();
	/**
	 * 判断字符串是否为空
	 * 
	 * @param s
	 * @return
	 */
	public static boolean isEmpty(String s) {
		return null == s || s.trim().length() <= 0 || s.trim().equalsIgnoreCase("null");
	}
	
	//判断身份证
	public static boolean isIDCard(String idCard){
		Pattern pattern = Pattern.compile("([0-9]{17}[a-zA-Z])|([0-9]{15})|([0-9]{18})");
		Matcher isNum = pattern.matcher(idCard);
		return isNum.matches();
	}

	/**
	 * 判断字符串是否为非空
	 * 
	 * @param s
	 * @return
	 */
	public static boolean isNotEmpty(String s) {
		return null != s && s.trim().length() > 0;
	}

	/**
	 * 判断两个字符串是否相等
	 * 
	 * @param s1
	 * @param s2
	 * @return
	 */
	public static boolean isEqual(String s1, String s2) {
		String s11 = null == s1 ? String.valueOf("") : s1;
		String s22 = null == s2 ? String.valueOf("") : s2;
		return s11.trim().equals(s22.trim());
	}
	/**
	 * 取字符串非空值
	 * 
	 * @param s
	 * @return
	 */
	public static String toNotNullValue(String s) {
		return null == s ? new String("") : s.trim();
	}
	/**
	 * 字符串格式化
	 * 
	 * @param s
	 * @param objs
	 * @return
	 */
	public static String format(String s, Object... objs) {
		return String.format(s, objs);
	}
	
	/**
	 * get a substring of the original string
	 * the length of substring will not be longer than the parameter length
	 * @param str
	 * @param beginIndex
	 * @param length
	 * @return
	 */
	public static String substring(String str, int beginIndex, int length) {
		
		int actualLength=str.length();
		
		String substr="";
		if(actualLength<beginIndex+length){
			substr=str.substring(beginIndex);
		}else{
			substr=str.substring(beginIndex,beginIndex+length);
		}
		
		return substr;
	}
	
	/**
	 * 左填充
	 * @param c 填充的字符
	 * @param length 填充后的长度
	 * @param content 源串
	 * @return
	 */
	public static String flushLeft(String c, long length, String content) {
		String str = "";
		String cs = "";
		if (content.length() > length) {
			str = content;
		} else {
			for (int i = 0; i < length - content.length(); i++) {
				cs = cs + c;
			}
		}
		str = cs + content;
		return str;
	}
	
	/**
	 * 对数字整数化，只保留最高位，其余为填0
	 * @param str 源数字
	 * @param mixlen 该长度以下不处理
	 * @return 
	 */
	public static String heightestSafe(String str, int mixlen){
		int strLen = str.length();
		if (strLen <= mixlen){
			return str;
		}
		long c = 1;
		for (int i = 0; i < strLen - 1; i++){
			c = c * 10;
		}
		return String.valueOf((Long.valueOf(str) / c) * c);
	}
	
	//随机字符串
	public static String getRandStr(int length, boolean isOnlyNum) {
		int size = isOnlyNum ? 10 : 62;
		StringBuffer hash = new StringBuffer(length);
		for (int i = 0; i < length; i++) {
			hash.append(randChars.charAt(random.nextInt(size)));
		}
		return hash.toString();
	}
	
    //根据时间获取ID
    public static String getIdByDate() {
    	Date now = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyMMddHHmmssSSS");
		String str = sdf.format(now);
		str += getRandStr(8, true);
    	return str;
    }
    
    public static int checkPassword(String pwd) {
		int ret = 0;
		if (pwd == null || pwd.trim().length() == 0) {
			//message.put(key, "请输入密码");
			ret = 1;
		} else {
			Pattern p = Pattern
					.compile("^[A-Za-z0-9@#$%!~&\\^\\?\\.\\*]{6,22}$");
			Matcher m = p.matcher(pwd);
			boolean isok = m.matches();
			if (!isok) {
				//message.put(key, "请输入6-22位长度的密码");
				ret = 2;
			}
		}
		return ret;
	}
    
    public static int checkPassword(String pwd, String confirPwd) {
		int ret = checkPassword(pwd);
		if (ret != 0) {
			if (!pwd.equals(confirPwd)) {
				//message.put(key, "两次输入的密码不一致");
				ret = 3;
			}
		}
		return ret;
	}
    public static boolean StringIsNull(String s) {
		if (s == null || s.trim().equals("")) {
			return true;
		}
		return false;
	}

	// 非负整数
	public static boolean IsNotMinusNum(String s) {
		Pattern pattern = Pattern.compile("[0-9]*");
		Matcher isNum = pattern.matcher(s);
		return isNum.matches();
	}

	//邮箱地址
	public static boolean IsEmail(String s){
		if(s.length() >30){
			return false;
		}
		Pattern pattern = Pattern.compile("^[\\w-]+(\\.[\\w-]+)*@[\\w-]+(\\.[\\w-]+)+$");
	    Matcher isNum = pattern.matcher(s);
	    return isNum.matches();
	}
	/**
     * 验证手机号码格式是否正确
     * @param mobiles
     * @return  true 表示正确  false表示不正确
     */
    public static boolean isMobileNum(String mobiles) {
        Pattern p = Pattern.compile("^((13[0-9])|(14[0-9])|(15[0-9])|(17[0-9])|(18[0-9]))\\d{8}");
        Matcher m = p.matcher(mobiles);
        return m.matches();
    }
    
    /**
     * 验证座机电话号码
     * @param phone
     * @return
     */
    public static boolean isPhoneNumber(String phone){
    	//判断长度
    	if(phone.length() > 20){
    		return false;
    	}
    	Pattern p = Pattern.compile("^([0-9]{4}|[0-9]{3})-[0-9]{6,}");
        Matcher m = p.matcher(phone);
        return m.matches();
    }
    
    /**
     * 验证密码
     * @param password
     * @return
     */
    public static boolean isPassword(String password){
    	Pattern p = Pattern.compile("^[0-9a-zA-Z_]{6,16}$"); 
    	Matcher m = p.matcher(password);
    	return m.matches();
    }
    
    /**
     * 将整形数据转换成固定长度的字符串，左侧补0
     * @param num		要转化为字符串的整形数据
     * @param length	要转换的总长度
     * @return
     */
    public static String IntToString(int num,int length){
    	String number = String.valueOf(num);
    	if(number.length() > length){
    		return null;
    	}
    	for(int i=number.length();i<length;i++){
    		number = "0"+number;
    	}
    	return number;
    }
    
    /**
     * 字符串判断
     * @param str				目标字符串
     * @param minLength			最小长度
     * @param maxLength			最大长度
     * @param isUndeline		是否包含下划线
     * @param isStrike			是否包含中划线
     * @param isHasChinese		是否包括中文
     * @param isHasSpecialChar	是否包含特殊字符（如果不包含则只允许包含字母、数字、下划线）
     * @return	1.字符串为空
     * 			2.包含中文
     * 			3.包含字母、数字、下划线以外的字符
     * 			4.字符长度不够
     * 			5.字符长度越界
     * 			6.字符串包含下划线
     * 			7.字符串包含中划线
     */
    public static int stringValidate(String str,int minLength,
    		int maxLength,boolean isUndeline,boolean isStrike,
    		boolean isHasChinese,boolean isHasSpecialChar) {
    	
    	if(str == null || "".equals(str)){
    		return 1;
    	}
		//判断字符长度
    	if(str.length() < minLength){
    		return 4;
    	}
    	if(str.length() > maxLength){
    		return 5;
    	}

    	//判断是否包含中文
    	if(!isHasChinese && isHasChinese(str)){
    		return 2;
    	}
    	//判断特殊字符串
		if(!isHasSpecialChar && isHasSepcChar(str)){
    		return 3;
		}
		if(!isUndeline && str.contains("_")){
			return 6;
		}
		if(!isStrike && str.contains("-")){
			return 7;
		}
    	
		return 0;
	}
    
    //判断字符串是否只包含中文
    public static boolean isOnlyChinese(String strName){
    	String regex = "^[\u4E00-\u9FFF()]+$";
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(strName);
		return matcher.matches(); 
    }
 
    //判断包含特殊字符
    public static boolean isHasSepcChar(String strName){
    	String regEx="[`~!@#$%^&*()+=|{}\":;',\\[].<>/?]";  
        Pattern   p   =   Pattern.compile(regEx);     
        Matcher   m   =   p.matcher(strName); 
        if(m.replaceAll("").trim().length() < strName.length()){
        	return true;
        }else{
        	return false;
        }
    }
    // 完整的判断中文汉字和符号
    public static boolean isHasChinese(String strName) {
        char[] ch = strName.toCharArray();
        for (int i = 0; i < ch.length; i++) {
            char c = ch[i];
            
            Character.UnicodeBlock ub = Character.UnicodeBlock.of(c);
            if (ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS || ub == Character.UnicodeBlock.CJK_COMPATIBILITY_IDEOGRAPHS
                    || ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_A || ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_B
                    || ub == Character.UnicodeBlock.CJK_SYMBOLS_AND_PUNCTUATION || ub == Character.UnicodeBlock.HALFWIDTH_AND_FULLWIDTH_FORMS
                    || ub == Character.UnicodeBlock.GENERAL_PUNCTUATION) {
                return true;
            }
            
        }
        return false;
    }
    
    /**
     * 
     * 验证邮编
     * @param postCode
     * @return
     */
    public static boolean isPostCode(String postCode){
    	return postCode.matches("([0-9]-)*[0-9]{6,}");
    }
    
    /**
     * 验证传真
     * @param faxCode
     * @return
     */
    public static boolean isFaxCode(String faxCode){
    	//判断长度
    	if(faxCode.length() > 20){
    		return false;
    	}
    	Pattern p = Pattern.compile("^([0-9]{4}|[0-9]{3})-[0-9]{6,}");
        Matcher m = p.matcher(faxCode);
        return m.matches();
    }
    //判断是否是身份证号码
    public static boolean isIDCardNum(String IDStr){
    	String Ai = "";
    	String[] ValCodeArr = { "1", "0", "x", "9", "8", "7", "6", "5", "4",
                "3", "2" };
        String[] Wi = { "7", "9", "10", "5", "8", "4", "2", "1", "6", "3", "7",
                "9", "10", "5", "8", "4", "2" };
    	//长度检测
    	 if (IDStr.length() != 15 && IDStr.length() != 18) {
             //身份证号码长度应该为15位或18位。
             return false;
         }
    	 //身份证15位号码都应为数字 ; 18位号码除最后一位外，都应为数字。
    	 if (IDStr.length() == 18) {
             Ai = IDStr.substring(0, 17);
         } else if (IDStr.length() == 15) {
             Ai = IDStr.substring(0, 6) + "19" + IDStr.substring(6, 15);
         }
    	 if (isNumeric(Ai) == false) {
             return false;
         }
    	 //检测日期
    	 String strYear = Ai.substring(6, 10);// 年份
         String strMonth = Ai.substring(10, 12);// 月份
         String strDay = Ai.substring(12, 14);// 月份
         if (isDate(strYear + "-" + strMonth + "-" + strDay) == false) {
             //身份证生日无效。
             return false;
         }
         GregorianCalendar gc = new GregorianCalendar();
         SimpleDateFormat s = new SimpleDateFormat("yyyy-MM-dd");
         try {
             if ((gc.get(Calendar.YEAR) - Integer.parseInt(strYear)) > 150
                     || (gc.getTime().getTime() - s.parse(
                             strYear + "-" + strMonth + "-" + strDay).getTime()) < 0) {
                 //身份证生日不在有效范围。
                 return false;
             }
         } catch (NumberFormatException e) {
             e.printStackTrace();
         } catch (java.text.ParseException e) {
             e.printStackTrace();
         }
         if (Integer.parseInt(strMonth) > 12 || Integer.parseInt(strMonth) == 0) {
             //身份证月份无效
             return false;
         }
         if (Integer.parseInt(strDay) > 31 || Integer.parseInt(strDay) == 0) {
             //身份证日期无效
             return false;
         }
         //判断地区码
         Hashtable h = GetAreaCode();
         if (h.get(Ai.substring(0, 2)) == null) {
             //身份证地区编码错误。
             return false;
         }
         //判断最后一位的值
         int TotalmulAiWi = 0;
         for (int i = 0; i < 17; i++) {
             TotalmulAiWi = TotalmulAiWi
                     + Integer.parseInt(String.valueOf(Ai.charAt(i)))
                     * Integer.parseInt(Wi[i]);
         }
         int modValue = TotalmulAiWi % 11;
         String strVerifyCode = ValCodeArr[modValue];
         Ai = Ai + strVerifyCode;

         if (IDStr.length() == 18) {
             if (Ai.equals(IDStr) == false) {
                 //身份证无效，不是合法的身份证号码
                 return false;
             }
         } else {
             return true;
         }
    	 
    	return true;
    }
    
    //判断是否为数字
    public static boolean isNumeric(String str){
    	Pattern pattern = Pattern.compile("[0-9]*");
        Matcher isNum = pattern.matcher(str);
        if (isNum.matches()) {
            return true;
        } else {
            return false;
        }
    }
    //判断是否为日期  以空格/中划线/斜杠分隔
    public static boolean isDate(String strDate){
    	Pattern pattern = Pattern
                .compile("^((\\d{2}(([02468][048])|([13579][26]))[\\-\\/\\s]?((((0?[13578])|(1[02]))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(3[01])))|(((0?[469])|(11))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(30)))|(0?2[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])))))|(\\d{2}(([02468][1235679])|([13579][01345789]))[\\-\\/\\s]?((((0?[13578])|(1[02]))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(3[01])))|(((0?[469])|(11))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(30)))|(0?2[\\-\\/\\s]?((0?[1-9])|(1[0-9])|(2[0-8]))))))(\\s(((0?[0-9])|([1-2][0-3]))\\:([0-5]?[0-9])((\\s)|(\\:([0-5]?[0-9])))))?$");
        Matcher m = pattern.matcher(strDate);
        if (m.matches()) {
            return true;
        } else {
            return false;
        }
    }
    
    //判断是否为银行卡号
    public static boolean isBankCard(String cardId) {
        char bit = getBankCardCheckCode(cardId.substring(0, cardId.length() - 1));
        if(bit == 'N'){
            return false;
        }
        return cardId.charAt(cardId.length() - 1) == bit;
    }
    
    //银行卡辅助方法
    public static char getBankCardCheckCode(String nonCheckCodeCardId){
        if(nonCheckCodeCardId == null || nonCheckCodeCardId.trim().length() == 0
                || !nonCheckCodeCardId.matches("\\d+")) {
            //如果传的不是数据返回N
            return 'N';
        }
        char[] chs = nonCheckCodeCardId.trim().toCharArray();
        int luhmSum = 0;
        for(int i = chs.length - 1, j = 0; i >= 0; i--, j++) {
            int k = chs[i] - '0';
            if(j % 2 == 0) {
                k *= 2;
                k = k / 10 + k % 10;
            }
            luhmSum += k;           
        }
        return (luhmSum % 10 == 0) ? '0' : (char)((10 - luhmSum % 10) + '0');
    }
    
    //身份证地区编码
    @SuppressWarnings({ "rawtypes", "unchecked" })
	private static Hashtable GetAreaCode() {
        Hashtable hashtable = new Hashtable();
        hashtable.put("11", "北京");
        hashtable.put("12", "天津");
        hashtable.put("13", "河北");
        hashtable.put("14", "山西");
        hashtable.put("15", "内蒙古");
        hashtable.put("21", "辽宁");
        hashtable.put("22", "吉林");
        hashtable.put("23", "黑龙江");
        hashtable.put("31", "上海");
        hashtable.put("32", "江苏");
        hashtable.put("33", "浙江");
        hashtable.put("34", "安徽");
        hashtable.put("35", "福建");
        hashtable.put("36", "江西");
        hashtable.put("37", "山东");
        hashtable.put("41", "河南");
        hashtable.put("42", "湖北");
        hashtable.put("43", "湖南");
        hashtable.put("44", "广东");
        hashtable.put("45", "广西");
        hashtable.put("46", "海南");
        hashtable.put("50", "重庆");
        hashtable.put("51", "四川");
        hashtable.put("52", "贵州");
        hashtable.put("53", "云南");
        hashtable.put("54", "西藏");
        hashtable.put("61", "陕西");
        hashtable.put("62", "甘肃");
        hashtable.put("63", "青海");
        hashtable.put("64", "宁夏");
        hashtable.put("65", "新疆");
        hashtable.put("71", "台湾");
        hashtable.put("81", "香港");
        hashtable.put("82", "澳门");
        hashtable.put("91", "国外");
        return hashtable;
    }
    
    //将数组以分号分隔
    public static String getCommaSeparated(String...strings){
    	String str = "";
    	for(int i = 0; i < strings.length; i++){
    		if(i<strings.length-1){
    			str += strings[i]+";";
			}else{
				str += strings[i];
			}
		}
		return str;
    }
    
	public static void main(String arg[]){
		/*System.out.println(IntToString(123,4));
		System.out.println(heightestSafe("9", 1));
		System.out.println(heightestSafe("99", 1));
		System.out.println(heightestSafe("999", 1));
		System.out.println(heightestSafe("9999", 1));*/
		//ResultData result = stringValidate("23和",4,12,false,true,"手机号");
		//System.out.println("jkdf-df".contains("-"));
		//System.out.println( "454251512".matches("[1-9]\\d{5}(?!\\d)"));
		/*System.out.println( stringValidate("a",1,
	    		10,false,false,
	    		true,false) );*/
		 
		//System.out.println();
		//System.out.println(stringValidate("公司###", 2, 50,true,true, true, false));
		//String [] strings= {"期刊库"};
		//System.out.println(getCommaSeparated(strings));
		String s = "期刊库";
		System.out.println(s.split(";").toString());
	}
}