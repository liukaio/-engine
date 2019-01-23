package com.zyctd.wxlogin.util;

import java.util.Random;

/**
 * 字符串工具类
 */
public class StringUtils {

	public static boolean isBlank(CharSequence cs) {
		int strLen;
		if (cs == null || (strLen = cs.length()) == 0) {
			return true;
		}
		for (int i = 0; i < strLen; i++) {
			if (Character.isWhitespace(cs.charAt(i)) == false) {
				return false;
			}
		}
		return true;
	}

	public static boolean isNotBlank(CharSequence cs) {
		return !StringUtils.isBlank(cs);
	}

	public static boolean isEmpty(CharSequence cs) {
		return cs == null || cs.length() == 0;
	}

	public static boolean isNotEmpty(CharSequence cs) {
		return !StringUtils.isEmpty(cs);
	}

	/**
	 * 生成指定长度的随机字符串
	 * @param length 随机字符串的长度
	 * @return
	 */
	public static String randomStr(int length) {
	    String base = "abcdefghijklmnopqrstuvwxyz0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ";
	    Random random = new Random();     
	    StringBuffer sb = new StringBuffer();     
	    for (int i = 0; i < length; i++) {     
	        int number = random.nextInt(base.length());     
	        sb.append(base.charAt(number));     
	    }     
	    return sb.toString();     
	 } 
	
}
