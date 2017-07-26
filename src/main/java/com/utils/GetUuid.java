package com.utils;

import java.util.UUID;

public class GetUuid {
	public static String getId(){
		UUID  uuid = UUID.randomUUID();
		String id = uuid.toString().replaceAll("-", "");
		return id;
	}
	
}
