package com.wf.Setting;



import com.wanfangdata.setting.Setting;
import com.wf.bean.Datamanager;
import net.sf.json.JSONArray;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.dom4j.*;

import java.util.LinkedHashMap;
import java.util.*;

/**
 * Created by syl on 2017/9/19.
 */
public class DatabaseConfigureSetting {
    private static final Logger log = LogManager.getLogger(DatabaseConfigureSetting.class);
    private static final String path = "/databaseConfigure";
    private static final String XPath =  "/databaseConfigures/databaseConfigure";

    /**
     * 解析资源类型管理的xml文件并得到所有数据库配置
     */
    public  List  getDatabase(){
        String xml = Setting.get(path);
        List<Datamanager> database = new ArrayList<>();
        try{
            //解析商品配置
            Document document = DocumentHelper.parseText(xml);
            //获取根
            Element root = document.getRootElement();
            List<Element> list = root.elements();
            //遍历节点
            for(Element element : list){
                Datamanager db = new Datamanager();
                db.setId(element.attributeValue("id"));
                db.setTableName(element.elementText("name"));
                db.setTableDescribe(element.elementText("describe"));
                db.setDbtype(element.elementText("dbType"));
                db.setSourceDb(element.elementText("dbSource"));
                db.setResType(element.elementText("resourceType"));
                db.setLanguage(element.elementText("language"));
                db.setCustomPolicy(element.elementText("customPolicy"));
                if(element.elementText("state").equals("")){
                    db.setStatus(null);
                }else {
                    db.setStatus(Integer.valueOf(element.elementText("state")));
                }
                database.add(db);
            }
            return database;
        }catch (DocumentException e) {
            log.error("解析商品配置出错, xml:" + xml, e);
            throw new IllegalArgumentException("解析商品配置出错");
        }
    }

    /**
     * 通过name查找数据库配置
     */
    public List findDatabaseByName(String dataName){
        String xml = Setting.get(path);
        List<Datamanager> database = new ArrayList<>();
        try{
            Document document = DocumentHelper.parseText(xml);
            Element root = document.getRootElement();
            Datamanager db = new Datamanager();
            List<Element> list  =  root.selectNodes(XPath);
            for(Element element : list){
                if(element.elementText("name").equals(dataName)){
                    db.setId(element.attributeValue("id"));
                    db.setTableName(element.elementText("name"));
                    db.setTableDescribe(element.elementText("describe"));
                    db.setDbtype(element.elementText("dbType"));
                    db.setSourceDb(element.elementText("dbSource"));
                    db.setResType(element.elementText("resourceType"));
                    db.setLanguage(element.elementText("language"));
                    db.setCustomPolicy(element.elementText("customPolicy"));
                    if(element.elementText("state").equals("")){
                        db.setStatus(null);
                    }else {
                        db.setStatus(Integer.valueOf(element.elementText("state")));
                    }
                    database.add(db);
                    break;
                }

            }
            return database;
        }catch (Exception e){
            log.error("加载setting配置出错，path:" + path, e);
            throw new IllegalArgumentException("加载setting配置出错，path:" + path);
        }
    }
    /**
     * 添加数据库配置
     */
    public void addDatabase(Datamanager data) {
        String xml = Setting.get(path);
        try{
            //解析商品配置
            Document document = DocumentHelper.parseText(xml);
            //获取根
            Element root = document.getRootElement();
            //增加新节点
            Element newElem=root.addElement("databaseConfigure");
            //设置节点的id属性
            newElem.addAttribute("id",data.getId());
            //增加子节点
            Element nameElem = newElem.addElement("name");
            Element abbElem = newElem.addElement("abbreviation");
            Element descriElem = newElem.addElement("describe");
            Element dbtypeElem = newElem.addElement("dbType");
            Element dbSourceElem = newElem.addElement("dbSource");
            Element resourceTypeElem = newElem.addElement("resourceType");
            Element languageElem = newElem.addElement("language");
            Element customPolicyElem = newElem.addElement("customPolicy");
            Element codeElem = newElem.addElement("code");
            Element stateElem = newElem.addElement("state");
            //设置子节点的文本内容
            nameElem.setText(data.getTableName());
            abbElem.setText(data.getAbbreviation());
            descriElem.setText(data.getTableDescribe());
            dbtypeElem.setText(data.getDbtype());
            dbSourceElem.setText(data.getSourceDb());
            resourceTypeElem.setText(data.getResType());
            languageElem.setText(data.getLanguage());
            codeElem.setText(data.getProductSourceCode());
            //指定文件输出的位置
            Setting.set(path, document.asXML());

        }catch (Exception e){
            log.error("解析商品配置出错, xml:" + xml, e);
            throw new IllegalArgumentException("解析商品配置出错");
        }
    }

    /**
     * 删除数据库配置
     */

    public void deleteDatabase(String id){
        String xml = Setting.get(path);
        try{
            Document document = DocumentHelper.parseText(xml);
            Element root =document.getRootElement();
            Element databaseEmlem =(Element) root.selectSingleNode("/databaseConfigures/databaseConfigure[@id='"+id+"']");
            root.remove(databaseEmlem);
            Setting.set(path, document.asXML());
        }catch (Exception e){
            log.error("解析商品配置出错, xml:" + xml, e);
            throw new IllegalArgumentException("解析商品配置出错");
        }
    }
    /**
     * 修改数据库配置
     */

    public void updateDatabase(Datamanager data){
        String xml = Setting.get(path);
        try{
            Document document = DocumentHelper.parseText(xml);
            Element root = document.getRootElement();
            String xpath = "/databaseConfigures/databaseConfigure[@id='" + data.getId()+ "']";
            Element databaseEmlem = (Element) root.selectSingleNode(xpath);
            Element nameElem = databaseEmlem.element("name");
            Element abbElem = databaseEmlem.element("abbreviation");
            Element descirElem = databaseEmlem.element("describe");
            Element dbtypeElem = databaseEmlem.element("dbType");
            Element dbSourceElem = databaseEmlem.element("dbSource");
            Element resourceTypeElem = databaseEmlem.element("resourceType");
            Element languageElem = databaseEmlem.element("language");
            Element customPolicyElem = databaseEmlem.element("customPolicy");
            Element codeElem = databaseEmlem.element("code");

            nameElem.setText(data.getTableName());
            abbElem.setText(data.getAbbreviation());
            descirElem.setText(data.getTableDescribe());
            dbtypeElem.setText(data.getDbtype());
            dbSourceElem.setText(data.getSourceDb());
            resourceTypeElem.setText(data.getResType());
            languageElem.setText(data.getLanguage());
            codeElem.setText(data.getProductSourceCode());
            Setting.set(path, document.asXML());
        } catch (Exception e){
            log.error("加载setting配置出错，path:" + path, e);
            throw new IllegalArgumentException("加载setting配置出错，path:" + path);
        }
    }
    /**
     * 上移资源类型
     */
    public  boolean  moveUpDatabase(String id){
        String xml = Setting.get(path);
        try {
            Document document = DocumentHelper.parseText(xml);
            Element root =  document.getRootElement();
            List<Element> list  =  root.selectNodes(XPath);
            for(int i = 1 ; i<list.size();i++){
                Element element = list.get(i);
                if(element.attributeValue("id").equals(id)){
                    Element element1 = (Element) root.selectSingleNode("/databaseConfigures/databaseConfigure[@id='" +id+ "']");
                    Element element2 = list.get(i-1);
                    //找出element1的所有属性和节点
                    Attribute attr1 = element1.attribute("id");
                    String id1 = attr1.getValue();
                    String name1 = element1.elementText("name");
                    String abb1 = element1.elementText("abbreviation");
                    String descri1 = element1.elementText("describe");
                    String dbType1 = element1.elementText("dbType");
                    String dbSource1 = element1.elementText("dbSource");
                    String resourceType1 = element1.elementText("resourceType");
                    String language1 = element1.elementText("language");
                    String customPolicy1 = element1.elementText("customPolicy");
                    String code1 = element1.elementText("code");
                    String state1 = element1.elementText("state");
                    //找出element2的所有属性和节点
                    Attribute attr2 = element2.attribute("id");
                    String id2 = attr2.getValue();
                    String name2 = element2.elementText("name");
                    String abb2 = element2.elementText("abbreviation");
                    String descri2 = element2.elementText("describe");
                    String dbType2 = element2.elementText("dbType");
                    String dbSource2 = element2.elementText("dbSource");
                    String resourceType2 = element2.elementText("resourceType");
                    String language2 = element2.elementText("language");
                    String customPolicy2 = element2.elementText("customPolicy");
                    String code2 = element2.elementText("code");
                    String state2 = element2.elementText("state");
                    //交换属性和节点
                    attr1.setValue(id2);
                    element1.element("name").setText(name2);
                    element1.element("abbreviation").setText(abb2);
                    element1.element("describe").setText(descri2);
                    element1.element("dbType").setText(dbType2);
                    element1.element("dbSource").setText(dbSource2);
                    element1.element("resourceType").setText(resourceType2);
                    element1.element("language").setText(language2);
                    element1.element("customPolicy").setText(customPolicy2);
                    element1.element("code").setText(code2);
                    element1.element("state").setText(state2);

                    attr2.setValue(id1);
                    element2.element("name").setText(name1);
                    element2.element("abbreviation").setText(abb1);
                    element2.element("describe").setText(descri1);
                    element2.element("dbType").setText(dbType1);
                    element2.element("dbSource").setText(dbSource1);
                    element2.element("resourceType").setText(resourceType1);
                    element2.element("language").setText(language1);
                    element2.element("customPolicy").setText(customPolicy1);
                    element2.element("code").setText(code1);
                    element2.element("state").setText(state1);
                    break;
                }
            }
            Setting.set(path, document.asXML());
            return true;
        }catch (Exception e){
            log.error("加载setting配置出错，path:" + path, e);
            throw new IllegalArgumentException("加载setting配置出错，path:" + path);
        }
    }

    /**
     * 下移资源类型
     */
    public  boolean  moveDownDatabase(String id){
        String xml = Setting.get(path);
        try {
            Document document = DocumentHelper.parseText(xml);
            Element root =  document.getRootElement();
            List<Element> list  =  root.selectNodes(XPath);
            for(int i = 0 ; i<list.size()-1;i++){
                Element element = list.get(i);
                if(element.attributeValue("id").equals(id)){
                    Element element1 = (Element) root.selectSingleNode("/databaseConfigures/databaseConfigure[@id='" +id+ "']");
                    Element element2 = list.get(i+1);
                    //找出element1的所有属性和节点
                    Attribute attr1 = element1.attribute("id");
                    String id1 = attr1.getValue();
                    String name1 = element1.elementText("name");
                    String abb1 = element1.elementText("abbreviation");
                    String descri1 = element1.elementText("describe");
                    String dbType1 = element1.elementText("dbType");
                    String dbSource1 = element1.elementText("dbSource");
                    String resourceType1 = element1.elementText("resourceType");
                    String language1 = element1.elementText("language");
                    String customPolicy1 = element1.elementText("customPolicy");
                    String code1 = element1.elementText("code");
                    String state1 = element1.elementText("state");
                    //找出element2的所有属性和节点
                    Attribute attr2 = element2.attribute("id");
                    String id2 = attr2.getValue();
                    String name2 = element2.elementText("name");
                    String abb2 = element2.elementText("abbreviation");
                    String descri2 = element2.elementText("describe");
                    String dbType2 = element2.elementText("dbType");
                    String dbSource2 = element2.elementText("dbSource");
                    String resourceType2 = element2.elementText("resourceType");
                    String language2 = element2.elementText("language");
                    String customPolicy2 = element2.elementText("customPolicy");
                    String code2 = element2.elementText("code");
                    String state2 = element2.elementText("state");
                    //交换属性和节点
                    attr1.setValue(id2);
                    element1.element("name").setText(name2);
                    element1.element("abbreviation").setText(abb2);
                    element1.element("describe").setText(descri2);
                    element1.element("dbType").setText(dbType2);
                    element1.element("dbSource").setText(dbSource2);
                    element1.element("resourceType").setText(resourceType2);
                    element1.element("language").setText(language2);
                    element1.element("customPolicy").setText(customPolicy2);
                    element1.element("code").setText(code2);
                    element1.element("state").setText(state2);

                    attr2.setValue(id1);
                    element2.element("name").setText(name1);
                    element2.element("abbreviation").setText(abb1);
                    element2.element("describe").setText(descri1);
                    element2.element("dbType").setText(dbType1);
                    element2.element("dbSource").setText(dbSource1);
                    element2.element("resourceType").setText(resourceType1);
                    element2.element("language").setText(language1);
                    element2.element("customPolicy").setText(customPolicy1);
                    element2.element("code").setText(code1);
                    element2.element("state").setText(state1);
                    break;
                }
            }
            Setting.set(path, document.asXML());
            return true;
        }catch (Exception e){
            log.error("加载setting配置出错，path:" + path, e);
            throw new IllegalArgumentException("加载setting配置出错，path:" + path);
        }
    }

    /**
     * 检查name是否重复
     */
    public boolean checkName(String name){
        String xml = Setting.get(path);
        try{
            Document document = DocumentHelper.parseText(xml);
            Element root =  document.getRootElement();
            List<Element> list  =  root.selectNodes(XPath);
            for(Element element : list){
                if(element.elementText("name").equals(name)){
                    return false;
                }
            }
            return true;

        }catch (Exception e){
            log.error("加载setting配置出错，path:" + path, e);
            throw new IllegalArgumentException("加载setting配置出错，path:" + path);
        }
    }
    /**
     * 更新资源类型状态
     */

    public void updateDatabaseState(int typeState,String id){
        String xml = Setting.get(path);
        try{
            Document document = DocumentHelper.parseText(xml);
            Element root = document.getRootElement();
            String xpath = "/databaseConfigures/databaseConfigure[@id='" +id+ "']";
            Element databaseEmlem = (Element) root.selectSingleNode(xpath);
            databaseEmlem.element("state").setText(String.valueOf(typeState));
            Setting.set(path, document.asXML());
        }catch (Exception e){
            log.error("加载setting配置出错，path:" + path, e);
            throw new IllegalArgumentException("加载setting配置出错，path:" + path);
        }
    }

    public JSONArray selectSitateFoOne(){
        String xml = Setting.get(path);
        List<Datamanager> database = new ArrayList<>();
        try{
            //解析商品配置
            Document document = DocumentHelper.parseText(xml);
            //获取根
            Element root = document.getRootElement();
            List<Element> list = root.elements();
            //遍历节点
            for(Element element : list){
                if(element.elementText("state").equals("1")){
                    Datamanager db = new Datamanager();
                    db.setId(element.attributeValue("id"));
                    db.setTableName(element.elementText("name"));
                    db.setTableDescribe(element.elementText("describe"));
                    db.setDbtype(element.elementText("dbType"));
                    db.setSourceDb(element.elementText("dbSource"));
                    db.setResType(element.elementText("resourceType"));
                    db.setLanguage(element.elementText("language"));
                    db.setCustomPolicy(element.elementText("customPolicy"));
                    if(element.elementText("state").equals("")){
                        db.setStatus(null);
                    }else {
                        db.setStatus(Integer.valueOf(element.elementText("state")));
                    }
                    database.add(db);
                }
            }
            JSONArray jsonArray =JSONArray.fromObject(database);
            return jsonArray;
        }catch (DocumentException e) {
            log.error("解析商品配置出错, xml:" + xml, e);
            throw new IllegalArgumentException("解析商品配置出错");
        }
    }

    /**
     * 判断资源类型是否已发布
     */
    public boolean checkResourceForOne(String id){
        String xml = Setting.get(path);
        try{
            Document  document = DocumentHelper.parseText(xml);
            Element root = document.getRootElement();
            String xpath = "/databaseConfigures/databaseConfigure[@id='" +id+ "']";
            Element databaseEmlem = (Element) root.selectSingleNode(xpath);
            if ("1".equals(databaseEmlem.elementText("state"))){
                return false;
            }else {
                return true;
            }

        }catch (Exception e){
            log.error("解析商品配置出错, xml:" + xml, e);
            throw new IllegalArgumentException("解析商品配置出错");
        }
    }

}
