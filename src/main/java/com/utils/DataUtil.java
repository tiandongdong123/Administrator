package com.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DataUtil {
	
	public static Map<String, List<Map<String, String>>> ListToMap(
			List<Map<String, String>> listx, String patent_id) {
		Map<String, List<Map<String, String>>> map = new HashMap<String, List<Map<String, String>>>();
		for (int i = 0; i < listx.size(); i++) {
			String id = listx.get(i).get(patent_id);
			List<Map<String, String>> list = new ArrayList<Map<String, String>>();
			if (map.get(id) == null) {
				list.add(listx.get(i));
			} else {
				list = map.get(id);
				list.add(listx.get(i));
			}
			map.put(id, list);
		}
		return map;
	}
}
