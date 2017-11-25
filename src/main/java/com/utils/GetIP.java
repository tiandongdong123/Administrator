package com.utils;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class GetIP {

	public static String getIP(){
		try {
			return InetAddress.getLocalHost().getHostAddress().toString();
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}
}
