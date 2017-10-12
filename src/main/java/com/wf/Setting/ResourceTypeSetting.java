package com.wf.Setting;

import com.wanfangdata.setting.Setting;
import com.wf.bean.ResourceType;
import net.sf.json.JSONArray;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.dom4j.*;


import java.util.*;



/**
 * Created by syl on 2017/9/13.
 */
public class ResourceTypeSetting {

    private static final Logger log = LogManager.getLogger(ResourceTypeSetting.class);
    private static final String path = "/ResourceType";
    private static final String XPath =  "/ResourceTypes/ResourceType";

    /**
     * 解析资源类型管理的xml文件并得到所有资源类型
     */
    public  Map<String, ResourceType> getResources(){
        Map<String, ResourceType> resources = new LinkedHashMap<>();
         String xml = Setting.get(path);
        try{
            //解析商品配置
            Document document = DocumentHelper.parseText(xml);
            //获取根
            Element root = document.getRootElement();
            List<Element> list = root.elements();
            //遍历节点
            for(Element element : list){
                ResourceType rt = new ResourceType();
                rt.setId(element.attributeValue("id"));
                rt.setTypeName(element.elementText("name"));
                rt.setTypedescri(element.elementText("describe"));
                rt.setTypeCode(element.elementText("code"));
                rt.setTypeState(element.elementText("state"));
                resources.put(rt.getId(),rt);
            }
            return resources;
        }catch (DocumentException e) {
            log.error("解析商品配置出错, xml:" + xml, e);
            throw new IllegalArgumentException("解析商品配置出错");
        }
    }

    public JSONArray getResources1(){
        String xml = Setting.get(path);
        List<ResourceType> resources = new ArrayList<>();
        try {
            //解析商品配置
            Document document = DocumentHelper.parseText(xml);
            //获取根
            Element root = document.getRootElement();
            List<Element> list = root.elements();
            //遍历节点
            for(Element element : list){
                if(element.elementText("state").equals("1")){
                    ResourceType rt = new ResourceType();
                    rt.setId(element.attributeValue("id"));
                    rt.setTypeName(element.elementText("name"));
                    rt.setTypedescri(element.elementText("describe"));
                    rt.setTypeCode(element.elementText("code"));
                    rt.setTypeState(element.elementText("state"));
                    resources.add(rt);
                }
            }
            JSONArray jsonArray =JSONArray.fromObject(resources);

            return jsonArray;

        }catch (DocumentException  e){
            log.error("解析商品配置出错, xml:" + xml, e);
            throw new IllegalArgumentException("解析商品配置出错");
        }
    }

    /**
     * 添加资源类型
     */
    public  boolean addResourceType(ResourceType resourceType){
        String xml = Setting.get(path);
        try{
            Document document = DocumentHelper.parseText(xml);
            Element root =  document.getRootElement();
            //增加新节点
            Element newElem=root.addElement("ResourceType");
            //设置节点的id属性
            newElem.addAttribute("id",resourceType.getId());
            //增加子节点
            Element nameElem = newElem.addElement("name");
            Element descriElem = newElem.addElement("describe");
            Element codeElem = newElem.addElement("code");
            Element stateElem = newElem.addElement("state");
            //设置resource子节点的文本内容
            nameElem.setText(resourceType.getTypeName());
            descriElem.setText(resourceType.getTypedescri());
            codeElem.setText(resourceType.getTypeCode());

            //指定文件输出的位置
            Setting.set(path, document.asXML());
            return true;
        }catch (Exception e){
            log.error("解析商品配置出错, xml:" + xml, e);
            throw new IllegalArgumentException("解析商品配置出错");
        }

    }

    /**
     *删除资源类型
     */
    public  void deleResourceType(String ids) {
        String xml = Setting.get(path);
        String[] id = ids.split(",");
        try {
            Document document = DocumentHelper.parseText(xml);
            Element root =document.getRootElement();
            for (int i = 0;i<id.length;i++){
                Element resourceTypeEmlem =(Element) root.selectSingleNode("/ResourceTypes/ResourceType[@id='"+id[i]+"']");
                root.remove(resourceTypeEmlem);
            }
            Setting.set(path, document.asXML());
        } catch (Exception e){
            log.error("解析商品配置出错, xml:" + xml, e);
            throw new IllegalArgumentException("解析商品配置出错");
    }


    }

    /**
     * 修改资源类型
     */
    public  void updateResouceType(ResourceType resourceType) {
        String xml = Setting.get(path);
        try{
            Document document = DocumentHelper.parseText(xml);
            Element root = document.getRootElement();
            String xpath = "/ResourceTypes/ResourceType[@id='" + resourceType.getId()+ "']";
            Element resourceTypeEmlem = (Element) root.selectSingleNode(xpath);
            Element nameElem = resourceTypeEmlem.element("name");
            Element descirElem = resourceTypeEmlem.element("describe");
            Element codeElem = resourceTypeEmlem.element("code");

            nameElem.setText(resourceType.getTypeName());
            descirElem.setText(resourceType.getTypedescri());
            codeElem.setText(resourceType.getTypeCode());
            Setting.set(path, document.asXML());
        } catch (Exception e){
            log.error("解析商品配置出错, xml:" + xml, e);
            throw new IllegalArgumentException("解析商品配置出错");
        }
    }
    /**
     * 根据id查找资源类型
     */
    public  ResourceType findResourceTypeById(String id){
        String xml = Setting.get(path);
        try{
            Document document = DocumentHelper.parseText(xml);
            Element root = document.getRootElement();
            ResourceType resourceType = new ResourceType();
            Iterator it = root.elementIterator();
            while (it.hasNext()){
                Element element = (Element) it.next();
                //通过id找到节点
                if(element.attributeValue("id").equals(id)){
                    resourceType.setId(id);
                    resourceType.setTypeName(element.elementText("name"));
                    resourceType.setTypedescri(element.elementText("describe"));
                    resourceType.setTypeCode(element.elementText("code"));
                    resourceType.setTypeState(element.elementText("state"));
                }
            }
            return resourceType;

        }catch (Exception e){
            log.error("解析商品配置出错, xml:" + xml, e);
            throw new IllegalArgumentException("解析商品配置出错");
        }
    }
    /**
     *根据name查找单个资源类型
     */
    public  Map<String, ResourceType> findResourceTypeByName(String name){
        Map<String, ResourceType> resource = new LinkedHashMap<>();
        String xml = Setting.get(path);
        try{
            Document document = DocumentHelper.parseText(xml);
            Element root = document.getRootElement();
            ResourceType resourceType = new ResourceType();
            List<Element> list  =  root.selectNodes(XPath);
            for(Element element : list){
                   if(element.elementText("name").equals(name)){
                   resourceType.setId(element.attributeValue("id"));
                   resourceType.setTypeName(element.elementText("name"));
                   resourceType.setTypedescri(element.elementText("describe"));
                   resourceType.setTypeCode(element.elementText("code"));
                       resourceType.setTypeState(element.elementText("state"));
                       resource.put(resourceType.getId(),resourceType);
                       break;
               }

           }
            return resource;
        }catch (Exception e){
            log.error("解析商品配置出错, xml:" + xml, e);
            throw new IllegalArgumentException("解析商品配置出错");
        }
    }
    /**
     * 上移资源类型
     */

    public  boolean  moveUpResource(String id){
        String xml = Setting.get(path);
        try {
            Document document = DocumentHelper.parseText(xml);
            Element root =  document.getRootElement();
            List<Element> list  =  root.selectNodes(XPath);
            for(int i = 1 ; i<list.size();i++){
                Element element = list.get(i);
                if(element.attributeValue("id").equals(id)){
                    Element element1 = (Element) root.selectSingleNode("/ResourceTypes/ResourceType[@id='" +id+ "']");
                    Element element2 = list.get(i-1);
                    //找出element1的所有属性和节点
                    Attribute attr1 = element1.attribute("id");
                    String id1 = attr1.getValue();
                    String name1 = element1.elementText("name");
                    String descri1 = element1.elementText("describe");
                    String code1 = element1.elementText("code");
                    String state1 = element1.elementText("state");
                    //找出element2的所有属性和节点
                    Attribute attr2 = element2.attribute("id");
                    String id2 = attr2.getValue();
                    String name2 = element2.elementText("name");
                    String descri2 = element2.elementText("describe");
                    String code2 = element2.elementText("code");
                    String state2 = element2.elementText("state");
                    //交换属性和节点
                    attr1.setValue(id2);
                    element1.element("name").setText(name2);
                    element1.element("describe").setText(descri2);
                    element1.element("code").setText(code2);
                    element1.element("state").setText(state2);

                    attr2.setValue(id1);
                    element2.element("name").setText(name1);
                    element2.element("describe").setText(descri1);
                    element2.element("code").setText(code1);
                    element2.element("state").setText(state1);
                    break;
                }
            }
            Setting.set(path, document.asXML());
            return true;
        }catch (Exception e){
            log.error("解析商品配置出错, xml:" + xml, e);
            throw new IllegalArgumentException("解析商品配置出错");
        }
    }

    /**
     * 下移资源类型
     */

    public  boolean  moveDownResource(String id){
        String xml = Setting.get(path);
        try {
            Document document = DocumentHelper.parseText(xml);
            Element root =  document.getRootElement();
            List<Element> list  =  root.selectNodes(XPath);
            for(int i = 0 ; i<list.size()-1;i++){
                Element element = list.get(i);
                if(element.attributeValue("id").equals(id)){
                    Element element1 = (Element) root.selectSingleNode("/ResourceTypes/ResourceType[@id='" +id+ "']");
                    Element element2 = list.get(i+1);
                    //找出element1的所有属性和节点
                    Attribute attr1 = element1.attribute("id");
                    String id1 = attr1.getValue();
                    String name1 = element1.elementText("name");
                    String descri1 = element1.elementText("describe");
                    String code1 = element1.elementText("code");
                    String state1 = element1.elementText("state");
                    //找出element2的所有属性和节点
                    Attribute attr2 = element2.attribute("id");
                    String id2 = attr2.getValue();
                    String name2 = element2.elementText("name");
                    String descri2 = element2.elementText("describe");
                    String code2 = element2.elementText("code");
                    String state2 = element2.elementText("state");
                    //交换属性和节点
                    attr1.setValue(id2);
                    element1.element("name").setText(name2);
                    element1.element("describe").setText(descri2);
                    element1.element("code").setText(code2);
                    element1.element("state").setText(state2);

                    attr2.setValue(id1);
                    element2.element("name").setText(name1);
                    element2.element("describe").setText(descri1);
                    element2.element("code").setText(code1);
                    element2.element("state").setText(state1);
                    break;
                }
            }
            Setting.set(path, document.asXML());
            return true;
        }catch (Exception e){
            log.error("解析商品配置出错, xml:" + xml, e);
            throw new IllegalArgumentException("解析商品配置出错");
        }
    }

    /**
     * 检查资源code是否重复
     */

    public boolean checkTypeCode(String code){
        String xml = Setting.get(path);
        try{
            Document document = DocumentHelper.parseText(xml);
            Element root = document.getRootElement();
            List<Element> list  =  root.selectNodes(XPath);
            for(Element element : list){
                if(element.elementText("code").equals(code)){
                    return false;
                }
            }
            return true;
        }catch (Exception e){
            log.error("解析商品配置出错, xml:" + xml, e);
            throw new IllegalArgumentException("解析商品配置出错");
        }
    }

    /**
     * 检查资源name是否重复
     */
    public boolean checkTypeName(String name){
        String xml = Setting.get(path);
        try{
            Document document = DocumentHelper.parseText(xml);
            Element root = document.getRootElement();
            List<Element> list  =  root.selectNodes(XPath);
            for(Element element : list){
                if(element.elementText("name").equals(name)){
                    return false;
                }
            }
            return true;
        }catch (Exception e){
            log.error("解析商品配置出错, xml:" + xml, e);
            throw new IllegalArgumentException("解析商品配置出错");
        }

    }

    /**
     * 得到所有的资源类型（数据库配置中使用）
     */
    public List<ResourceType> getAll(){
        String xml = Setting.get(path);
        List<ResourceType> resourceType = new ArrayList<>();
        try{
            Document document = DocumentHelper.parseText(xml);
            Element root = document.getRootElement();
            List<Element> list  =  root.selectNodes(XPath);
            for(Element element : list){
                ResourceType rt = new ResourceType();
                rt.setId(element.attributeValue("id"));
                rt.setTypeName(element.elementText("name"));
                rt.setTypedescri(element.elementText("describe"));
                rt.setTypeCode(element.elementText("code"));
                resourceType.add(rt);
            }
            return resourceType;
        }catch (Exception e){
            log.error("解析商品配置出错, xml:" + xml, e);
            throw new IllegalArgumentException("解析商品配置出错");
        }
    }

    /**
     * 更新资源类型状态
     */

    public void updateResourceTypeState(int typeState,String id){
        String xml = Setting.get(path);
        try{
            Document document = DocumentHelper.parseText(xml);
            Element root = document.getRootElement();
            String xpath = "/ResourceTypes/ResourceType[@id='" +id+ "']";
            Element resourceTypeEmlem = (Element) root.selectSingleNode(xpath);
            resourceTypeEmlem.element("state").setText(String.valueOf(typeState));
            Setting.set(path, document.asXML());
        }catch (Exception e){
            log.error("解析商品配置出错, xml:" + xml, e);
            throw new IllegalArgumentException("解析商品配置出错");
        }
    }
    /**
     * 判断资源类型是否已发布
    */
    public boolean checkResourceForOne(String id){
        String xml = Setting.get(path);
        String[] ids = id.split(",");
        try{
            Document  document = DocumentHelper.parseText(xml);
            Element root = document.getRootElement();
            for (int i = 0;i<ids.length;i++){
                String xpath = "/ResourceTypes/ResourceType[@id='" +ids[i]+ "']";
                Element resourceTypeEmlem = (Element) root.selectSingleNode(xpath);
                if ("1".equals(resourceTypeEmlem.elementText("state"))){
                    return false;
                }
            }
            return true;

        }catch (Exception e){
            log.error("解析商品配置出错, xml:" + xml, e);
            throw new IllegalArgumentException("解析商品配置出错");
        }
    }

}
