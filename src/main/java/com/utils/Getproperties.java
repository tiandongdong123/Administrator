package com.utils;

import java.io.IOException;
import java.util.Properties;

public class Getproperties {
	private static Properties pros = new Properties();

	public static Properties getPros(String param) {
		try {
			pros.load(Thread.currentThread().getContextClassLoader().getResourceAsStream(param));
		} catch (IOException e) {
			e.printStackTrace();
		}
		return pros;
	}
}
