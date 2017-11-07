package com.utils;

import org.apache.commons.lang3.StringUtils;

public class IPConvertHelper {
	
	/**
	 * 是否是合法ip
	 * @param ip
	 * @return
	 */
	public static boolean validateOneIp(String ip) {
		boolean flag = false;
		if (StringUtils.isBlank(ip)) {
			return flag;
		}
		if (ip.matches("\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}")) {
			String s[] = ip.split("\\.");
			if (Integer.parseInt(s[0]) < 233 && Integer.parseInt(s[0])>0)
				if (Integer.parseInt(s[1]) < 255)
					if (Integer.parseInt(s[2]) < 255)
						if (Integer.parseInt(s[3]) < 255)
							flag = true;
		}
		return flag;
	}

	/**
	 * 是否是合法ip对(ip对中间"-"号分割)
	 * 
	 * @param ips
	 * @return
	 */
	public static boolean validateDoubleIp(String ips) {
		if (StringUtils.isBlank(ips)) {
			return false;
		}
		// 开始ip
		String startip = ips.substring(0, ips.indexOf("-"));
		if (!validateOneIp(startip)) {
			return false;
		}
		// 结束ip
		String endip = ips.substring(0, ips.indexOf("-"));
		if (!validateOneIp(endip)) {
			return false;
		}
		// 结束ip大于等于开始ip
		if (IPToNumber(startip) > IPToNumber(endip)) {
			return false;
		}
		return true;
	}

	/**
	 * 是否是合法ip组
	 * 
	 * @param ip
	 * @return
	 */
	public static boolean validateIp(String ips) {
		if (StringUtils.isBlank(ips)) {
			return false;
		}
		String[] arr_ip = ips.split("\r\n");
		for (String ip : arr_ip) {
			if (ip.contains("-")) {
				if (!validateDoubleIp(ip)) {
					return false;
				}
			} else {
				return false;
			}
		}
		return true;
	}

	/// <summary>
    /// 将IPv4字符串转换成数字
    /// </summary>
    /// <param name="ip">IPv4字符串</param>
    /// <returns>对应的无符号整数</returns>
    public static long IPToNumber(String ip)
    {
        long result = 0;
        String[] ipSection = ip.split("\\.");
        result += (long)(Integer.parseInt(ipSection[0]) * 256 * 256) * 256;
        result += Integer.parseInt(ipSection[1]) * 256 * 256;
        result += Integer.parseInt(ipSection[2]) * 256;
        result += Integer.parseInt(ipSection[3]);

        return result;
    }

    /// <summary>
    /// 将数字装换成IP
    /// </summary>
    /// <param name="ipNumber">IP对应的无符号整数</param>
    /// <returns></returns>
    public static String NumberToIP(long ipNumber)
    {
        // 4段
        String result = (ipNumber % 256)+"";
        ipNumber = ipNumber / 256;

        // 3段
        result = (ipNumber % 256) + "." + result;
        ipNumber = ipNumber / 256;

        // 2段
        result = (ipNumber % 256) + "." + result;
        ipNumber = ipNumber / 256;

        // 1段
        result = ipNumber + "." + result;

        return result;
    }
    
    public static boolean ipExistsInRange(String ip,String ipSection) {

        ipSection = ipSection.trim();

        ip = ip.trim();

        int idx = ipSection.indexOf('-');

        String beginIP = ipSection.substring(0, idx);

        String endIP = ipSection.substring(idx + 1);

        return IPToNumber(beginIP)<=IPToNumber(ip) &&IPToNumber(ip)<=IPToNumber(endIP);

     }
    
    public static void main(String[] args) {
    	System.out.println(IPToNumber("10.215.10.39"));
    	//System.out.println(NumberToIP(3391520004L));
	}
}
