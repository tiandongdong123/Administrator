package com.utils;

public class IPConvertHelper {

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
