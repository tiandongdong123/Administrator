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

	public static Map<String,String> menuName;
	public static List<Menu> getListMenu(){
		List<Menu> listMenu=new ArrayList<Menu>();
		try {
			URL url = MenuXml.class.getClassLoader().getResource("menu.xml");
			File f = new File(url.getFile());   
			SAXReader reader = new SAXReader();   
			Document doc = reader.read(f);   
			Element root = doc.getRootElement();
			List<Element> listElements=root.elements();//所有一级子节点的list
			for (Element element : listElements) {
				listMenu.add(getNodes(element));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return listMenu;
	}
	public static Map<String,String> getMenuName(){
		menuName=new HashMap<String, String>();
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
       return menuName;
	}
	public static void getNodesName(Element node){
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
			menuName.put(id, na);
		}
		//递归遍历当前节点所有的子节点
		List<Element> listElement=node.elements();//所有一级子节点的list
		for(Element e:listElement){//遍历所有一级子节点
			getNodesName(e);//递归
		}
	}
	public static Menu getNodes(Element node){
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
