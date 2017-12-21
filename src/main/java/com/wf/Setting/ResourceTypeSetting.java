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

    /**
     * 解析资源类型管理的xml文件并得到所有资源类型
     */
    public Map<String, ResourceType> getResources() {
        try {
            Map<String, com.wanfangdata.resourceupdatetools.model.ResourceType> resourceMap = com.wanfangdata.resourceupdatetools.setting.ResourceTypeSetting.getResources();
            Map<String, ResourceType> resources = new LinkedHashMap<>();
            for (Map.Entry<String, com.wanfangdata.resourceupdatetools.model.ResourceType> resource : resourceMap.entrySet()) {
                resources.put(resource.getKey(), convertToResourceType(resource.getValue()));
            }
            return resources;
        } catch (Exception e) {
            log.error("getResources出错", e);
            throw new IllegalArgumentException("getResources出错");
        }
    }

    public JSONArray getResources1() {
        try {
            List<com.wanfangdata.resourceupdatetools.model.ResourceType> resourceList = com.wanfangdata.resourceupdatetools.setting.ResourceTypeSetting.getResources1();
            List<ResourceType> resources = new ArrayList<>();
            for (com.wanfangdata.resourceupdatetools.model.ResourceType resource : resourceList) {
                resources.add(convertToResourceType(resource));
            }
            JSONArray jsonArray = JSONArray.fromObject(resources);
            return jsonArray;
        } catch (Exception e) {
            log.error("getResources1出错", e);
            throw new IllegalArgumentException("getResources1出错");
        }
    }

    /**
     * 添加资源类型
     */
    public boolean addResourceType(ResourceType resourceType) {
        try {
            return com.wanfangdata.resourceupdatetools.setting.ResourceTypeSetting.addResourceType(convertToWFResourceType(resourceType));
        } catch (Exception e) {
            log.error("addResourceType出错, resourceType:" + resourceType, e);
            throw new IllegalArgumentException("addResourceType出错出错");
        }
    }

    /**
     * 删除资源类型
     */
    public void deleResourceType(String ids) {
        try {
            com.wanfangdata.resourceupdatetools.setting.ResourceTypeSetting.deleResourceType(ids);
        } catch (Exception e) {
            log.error("deleResourceType出错, ids:" + ids, e);
            throw new IllegalArgumentException("deleResourceType出错");
        }
    }

    /**
     * 修改资源类型
     */
    public void updateResouceType(ResourceType resourceType) {
        try {
            com.wanfangdata.resourceupdatetools.setting.ResourceTypeSetting.updateResouceType(convertToWFResourceType(resourceType));
        } catch (Exception e) {
            log.error("updateResouceType出错, resourceType:" + resourceType, e);
            throw new IllegalArgumentException("updateResouceType出错");
        }
    }

    /**
     * 根据id查找资源类型
     */
    public ResourceType findResourceTypeById(String id) {
        try {
            return convertToResourceType(com.wanfangdata.resourceupdatetools.setting.ResourceTypeSetting.findResourceTypeById(id));
        } catch (Exception e) {
            log.error("findResourceTypeById出错, id:" + id, e);
            throw new IllegalArgumentException("findResourceTypeById出错");
        }
    }

    /**
     * 根据name查找单个资源类型
     */
    public Map<String, ResourceType> findResourceTypeByName(String name) {
        try {
            Map<String, com.wanfangdata.resourceupdatetools.model.ResourceType> resourceMap = com.wanfangdata.resourceupdatetools.setting.ResourceTypeSetting.findResourceTypeByName(name);
            Map<String, ResourceType> resources = new LinkedHashMap<>();
            for (Map.Entry<String, com.wanfangdata.resourceupdatetools.model.ResourceType> resource : resourceMap.entrySet()) {
                resources.put(resource.getKey(), convertToResourceType(resource.getValue()));
            }
            return resources;
        } catch (Exception e) {
            log.error("findResourceTypeByName出错, name:" + name, e);
            throw new IllegalArgumentException("findResourceTypeByName出错");
        }
    }

    /**
     * 上移资源类型
     */

    public boolean moveUpResource(String id) {
        try {
            return com.wanfangdata.resourceupdatetools.setting.ResourceTypeSetting.moveUpResource(id);
        } catch (Exception e) {
            log.error("moveUpResource出错, id:" + id, e);
            throw new IllegalArgumentException("moveUpResource出错");
        }
    }

    /**
     * 下移资源类型
     */

    public boolean moveDownResource(String id) {
        try {
            return com.wanfangdata.resourceupdatetools.setting.ResourceTypeSetting.moveDownResource(id);
        } catch (Exception e) {
            log.error("moveDownResource出错, id:" + id, e);
            throw new IllegalArgumentException("moveDownResource出错");
        }
    }

    /**
     * 检查资源code是否重复
     */

    public boolean checkTypeCode(String code) {
        try {
            return com.wanfangdata.resourceupdatetools.setting.ResourceTypeSetting.checkTypeCode(code);
        } catch (Exception e) {
            log.error("checkTypeCode出错, code:" + code, e);
            throw new IllegalArgumentException("checkTypeCode出错");
        }
    }

    /**
     * 检查资源name是否重复
     */
    public boolean checkTypeName(String name) {
        try {
            return com.wanfangdata.resourceupdatetools.setting.ResourceTypeSetting.checkTypeName(name);
        } catch (Exception e) {
            log.error("checkTypeName出错, name:" + name, e);
            throw new IllegalArgumentException("checkTypeName出错");
        }
    }

    /**
     * 得到所有的资源类型（数据库配置中使用）
     */
    public List<ResourceType> getAll() {
        try {
            List<com.wanfangdata.resourceupdatetools.model.ResourceType> resourceList = com.wanfangdata.resourceupdatetools.setting.ResourceTypeSetting.getAll();
            List<ResourceType> resourceType = new ArrayList<>();
            for (com.wanfangdata.resourceupdatetools.model.ResourceType resource : resourceList) {
                resourceType.add(convertToResourceType(resource));
            }
            return resourceType;
        } catch (Exception e) {
            log.error("getAll出错", e);
            throw new IllegalArgumentException("getAll出错");
        }
    }

    /**
     * 更新资源类型状态
     */

    public void updateResourceTypeState(int typeState, String id) {
        try {
            com.wanfangdata.resourceupdatetools.setting.ResourceTypeSetting.updateResourceTypeState(typeState, id);
        } catch (Exception e) {
            log.error("updateResourceTypeState出错, typeState:" + typeState + " id:" + id, e);
            throw new IllegalArgumentException("updateResourceTypeState出错");
        }
    }

    /**
     * 判断资源类型是否已发布
     */
    public boolean checkResourceForOne(String id) {
        try {
            return com.wanfangdata.resourceupdatetools.setting.ResourceTypeSetting.checkResourceForOne(id);
        } catch (Exception e) {
            log.error("checkResourceForOne出错, id:" + id, e);
            throw new IllegalArgumentException("checkResourceForOne出错");
        }
    }

    private static ResourceType convertToResourceType(com.wanfangdata.resourceupdatetools.model.ResourceType resourceType) {
        ResourceType newType = new ResourceType();
        newType.setId(resourceType.getId());
        newType.setTypeCode(resourceType.getCode());
        newType.setTypeName(resourceType.getName());
        newType.setTypedescri(resourceType.getDescribe());
        newType.setTypeState(resourceType.getState());
        return newType;
    }

    private static com.wanfangdata.resourceupdatetools.model.ResourceType convertToWFResourceType(ResourceType resourceType) {
        com.wanfangdata.resourceupdatetools.model.ResourceType newType = new com.wanfangdata.resourceupdatetools.model.ResourceType();
        newType.setId(resourceType.getId());
        newType.setCode(resourceType.getTypeCode());
        newType.setName(resourceType.getTypeName());
        newType.setDescribe(resourceType.getTypedescri());
        newType.setState(resourceType.getTypeState());
        return newType;
    }
}
