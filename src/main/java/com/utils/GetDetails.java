package com.utils;

import java.io.File;
import java.net.URL;
import java.util.Iterator;

import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

public class GetDetails {
	public static String CLC_DIC;
	public static String GAZETTEER_TYPE_DIC;
	public static String REGION;
	public static String PATENT_IPC;
	public static String PERIOINFO_DIC;
	static{
		CLC_DIC=getCLCDic();
		GAZETTEER_TYPE_DIC=getGazetteerTypeDic();
		REGION=getRegion();
		PATENT_IPC=getPatentIpc();
		PERIOINFO_DIC=getPerioInfoDic();
	}
	public static String getCLCDic(){
		String clcdic=null;
		long lasting = System.currentTimeMillis();   
	    try {   
	     URL url = GetDetails.class.getClassLoader().getResource("sourceDBDetails/CLCDic.xml");
	     File f = new File(url.getFile());   
	     SAXReader reader = new SAXReader();   
	     Document doc = reader.read(f);   
	     Element root = doc.getRootElement(); 
	     clcdic=root.elementText("VALUE");
	    } catch (Exception e) {   
	     e.printStackTrace();   
    }   
	    return clcdic;
	}
	
	public static String getGazetteerTypeDic(){
		String gazetteerTypeDic=null;
		long lasting = System.currentTimeMillis();   
	    try {   
	     URL url = GetDetails.class.getClassLoader().getResource("sourceDBDetails/GazetteerTypeDic.xml");
	     File f = new File(url.getFile());   
	     SAXReader reader = new SAXReader();   
	     Document doc = reader.read(f);   
	     Element root = doc.getRootElement(); 
	     gazetteerTypeDic=root.elementText("VALUE");
	    } catch (Exception e) {   
	     e.printStackTrace();   
    }   
	    return gazetteerTypeDic;
	}
	
	public static String getRegion(){
		String region=null;
		long lasting = System.currentTimeMillis();   
	    try {   
	     URL url = GetDetails.class.getClassLoader().getResource("sourceDBDetails/Region.xml");
	     File f = new File(url.getFile());   
	     SAXReader reader = new SAXReader();   
	     Document doc = reader.read(f);   
	     Element root = doc.getRootElement(); 
	     region=root.elementText("VALUE");
	    } catch (Exception e) {   
	     e.printStackTrace();   
    }   
	    return region;
	}
	
	public static String getPatentIpc(){
		String patentIpc=null;
		long lasting = System.currentTimeMillis();   
	    try {   
	     URL url = GetDetails.class.getClassLoader().getResource("sourceDBDetails/PatentIpc.xml");
	     File f = new File(url.getFile());   
	     SAXReader reader = new SAXReader();   
	     Document doc = reader.read(f);   
	     Element root = doc.getRootElement(); 
	     patentIpc=root.elementText("VALUE");
	    } catch (Exception e) {   
	     e.printStackTrace();   
    }   
	    return patentIpc;
	}
	
	public static String getPerioInfoDic(){
		String perioInfoDic=null;
		long lasting = System.currentTimeMillis();   
	    try {   
	     URL url = GetDetails.class.getClassLoader().getResource("sourceDBDetails/PerioInfoDic.xml");
	     File f = new File(url.getFile());   
	     SAXReader reader = new SAXReader();   
	     Document doc = reader.read(f);   
	     Element root = doc.getRootElement(); 
	     perioInfoDic=root.elementText("VALUE"); 
	    } catch (Exception e) {   
	     e.printStackTrace();   
    }   
	    return perioInfoDic;
	}
}
