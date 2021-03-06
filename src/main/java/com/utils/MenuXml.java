package com.utils;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import com.wf.bean.Menu;


public class MenuXml {

	public static Map<String,String> MENU_NAME;
	
	public static List<String> MENU_ALL_URL;
	
	public static List<String> MENU_PURVIEW_URL;
	
	public static List<Menu> LIST_MENU;
	
	public static int NODE_NUM;
	
	static{
		if(MENU_ALL_URL==null){
			//获取所有权限url
			MENU_ALL_URL=new ArrayList<String>();
			try {
				URL url = MenuXml.class.getClassLoader().getResource("menu.xml");
				File f = new File(url.getFile());   
				SAXReader reader = new SAXReader();   
				Document doc = reader.read(f);   
				Element root = doc.getRootElement();
				List<Element> listElements=root.elements();//所有一级子节点的list
				for (Element element : listElements) {
					getNodesUrl(element);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		if(LIST_MENU==null){
			try {
				LIST_MENU=new ArrayList<Menu>();
				URL url = MenuXml.class.getClassLoader().getResource("menu.xml");
				File f = new File(url.getFile());   
				SAXReader reader = new SAXReader();   
				Document doc = reader.read(f);   
				Element root = doc.getRootElement();
				List<Element> listElements=root.elements();//所有一级子节点的list
				for (Element element : listElements) {
					LIST_MENU.add(getNodes(element));
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		if(MENU_NAME==null){
			MENU_NAME=new HashMap<String, String>();
			try {
				URL url = MenuXml.class.getClassLoader().getResource("menu.xml");
				File f = new File(url.getFile());   
				SAXReader reader = new SAXReader();   
				Document doc = reader.read(f);   
				Element root = doc.getRootElement();
				getNodesName(root);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	public static List<Menu> getPurviewsListMenu(List<String> list){
		List<Menu> listMenu=new ArrayList<Menu>();
		try {
			URL url = MenuXml.class.getClassLoader().getResource("menu.xml");
			File f = new File(url.getFile());   
			SAXReader reader = new SAXReader();   
			Document doc = reader.read(f);   
			Element root = doc.getRootElement();
			List<Element> listElements=root.elements();//所有一级子节点的list
			for (Element element : listElements) {
				String s=element.attributeValue("Id");
				if(list.contains(s)){
					listMenu.add(getPurviewsNodes(element,list));
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return listMenu;
	}
	private static Menu getPurviewsNodes(Element node,List<String> list){
		Menu m=new Menu();
		//当前节点的名称、文本内容和属性
		List<Attribute> listAttr=node.attributes();//当前节点的所有属性的list
		for(Attribute attr:listAttr){//遍历当前节点的所有属性
			String name=attr.getName();//属性名称
			String value=attr.getValue();//属性的值
			if(name.equals("Id")){
				m.setId(value);
			}
			if(name.equals("Name")){
				m.setName(value);
			}
			if(name.equals("HasChild")){
				m.setHaschild(value.equals("true"));
			}
			if(name.equals("Url")&&value!=""){
				m.setUrl(value);
			}
			if(m.getHaschild()){
				List<Menu> listMenu=new ArrayList();
				//递归遍历当前节点所有的子节点
				List<Element> listElement=node.elements();//所有一级子节点的list
				for(Element e:listElement){//遍历所有一级子节点
					if(list.contains(e.attributeValue("Id"))){
						listMenu.add(getPurviewsNodes(e,list));//递归
					}
				}
				m.setChildren(listMenu);
			}
		}
		return m;
	}
	
	//获取所有权限url
	private static void getNodesUrl(Element node){
		List<Attribute> listAttr=node.attributes();//当前节点的所有属性的list
		for(Attribute attr:listAttr){//遍历当前节点的所有属性
			String name=attr.getName();//属性名称
			String value=attr.getValue();//属性的值
			if(name.equals("Url")&&value!=""){
				MENU_ALL_URL.add(value);
			}
		}
		//递归遍历当前节点所有的子节点
		List<Element> listElement=node.elements();//所有一级子节点的list
		for(Element e:listElement){//遍历所有一级子节点
			getNodesUrl(e);//递归
		}
	}

	//根据id查询url
	public static List<String> getPurviewsListUrl(List<String> list){
		MENU_PURVIEW_URL=new ArrayList<String>();
		try {
			URL url = MenuXml.class.getClassLoader().getResource("menu.xml");
			File f = new File(url.getFile());   
			SAXReader reader = new SAXReader();   
			Document doc = reader.read(f);   
			Element root = doc.getRootElement();
			List<Element> listElements=root.elements();//所有一级子节点的list
			for (Element element : listElements) {
				String s=element.attributeValue("Id");
				if(list.contains(s)){
					getPurviewsListUrlById(element,list);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return MENU_PURVIEW_URL;
	}

	private static void getPurviewsListUrlById(Element element, List<String> list) {
		List<Attribute> listAttr=element.attributes();//当前节点的所有属性的list
		for(Attribute attr:listAttr){//遍历当前节点的所有属性
			String name=attr.getName();//属性名称
			String value=attr.getValue();//属性的值
			if(name.equals("Url")&&value!=""){
				MENU_PURVIEW_URL.add(value);
			}
		}
		List<Element> listElement=element.elements();//所有一级子节点的list
		for(Element e:listElement){//遍历所有一级子节点
			if(list.contains(e.attributeValue("Id"))){
				getPurviewsListUrlById(e,list);//递归
			}
		}
	}

	
	private static void getNodesName(Element node){
		//当前节点的名称、文本内容和属性
		List<Attribute> listAttr=node.attributes();//当前节点的所有属性的list
		String id=null;
		String na=null;
		for(Attribute attr:listAttr){//遍历当前节点的所有属性
			String name=attr.getName();//属性名称
			String value=attr.getValue();//属性的值
			if(name.equals("Id")){
				id=value;
			}
			if(name.equals("Name")){
				na=value;
			}
		}
		if(id!=null&&na!=null){
			MENU_NAME.put(id, na);
		}
		//递归遍历当前节点所有的子节点
		List<Element> listElement=node.elements();//所有一级子节点的list
		for(Element e:listElement){//遍历所有一级子节点
			getNodesName(e);//递归
		}
	}
	private static Menu getNodes(Element node){
		Menu m=new Menu();
		//当前节点的名称、文本内容和属性
		List<Attribute> listAttr=node.attributes();//当前节点的所有属性的list
		for(Attribute attr:listAttr){//遍历当前节点的所有属性
			String name=attr.getName();//属性名称
			String value=attr.getValue();//属性的值
			if(name.equals("Id")){
				m.setId(value);
			}
			if(name.equals("Name")){
				m.setName(value);
			}
			if(name.equals("HasChild")){
				m.setHaschild(value.equals("true"));
			}
			if(name.equals("Url")&&value!=""){
				m.setUrl(value);
			}
			if(m.getHaschild()){
				List<Menu> listMenu=new ArrayList();
				//递归遍历当前节点所有的子节点
				List<Element> listElement=node.elements();//所有一级子节点的list
				for(Element e:listElement){//遍历所有一级子节点
					listMenu.add(getNodes(e));//递归
				}
				m.setChildren(listMenu);
			}
		}
		return m;
	}
}
