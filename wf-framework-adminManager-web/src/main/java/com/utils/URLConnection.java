package com.utils;

import java.net.HttpURLConnection;
import java.net.URL;

public class URLConnection {
	
	public boolean testURL(String url){
		boolean rt = false;
		try {
			int status = 0;
			URL urlObj = new URL(url);
			HttpURLConnection oc = (HttpURLConnection) urlObj.openConnection();
			oc.setUseCaches(false);
			oc.setConnectTimeout(1000);
			status = oc.getResponseCode();
			if(status==200){
				rt = true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return rt;
	}
}
