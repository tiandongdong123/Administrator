package com.wf.Setting;


import com.wanfangdata.resourceupdatetools.model.Database;
import com.wanfangdata.setting.Setting;
import com.wf.bean.Datamanager;
import com.wf.bean.ResourceType;
import net.sf.json.JSONArray;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.dom4j.*;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.*;

/**
 * Created by syl on 2017/9/19.
 */
public class DatabaseConfigureSetting {
    private static final Logger log = LogManager.getLogger(DatabaseConfigureSetting.class);

    /**
     * 解析资源类型管理的xml文件并得到所有数据库配置
     */
    public List getDatabase() {
        try {
            List<Database> dbList = com.wanfangdata.resourceupdatetools.setting.DatabaseConfigureSetting.getDatabase();
            List<Datamanager> database = new ArrayList<>();
            for (Database db : dbList) {
                database.add(convertToDatamanager(db));
            }
            return database;
        } catch (Exception e) {
            log.error("getDatabase出错", e);
            throw new IllegalArgumentException("getDatabase出错");
        }
    }

    /**
     * 通过name查找数据库配置
     */
    public List findDatabaseByName(String dataName) {
        try {
            List<Database> dbList = com.wanfangdata.resourceupdatetools.setting.DatabaseConfigureSetting.findDatabaseByName(dataName);
            List<Datamanager> database = new ArrayList<>();
            for (Database db : dbList) {
                database.add(convertToDatamanager(db));
            }
            return database;
        } catch (Exception e) {
            log.error("findDatabaseByName出错，dataName:" + dataName, e);
            throw new IllegalArgumentException("findDatabaseByName出错，dataName:" + dataName);
        }
    }

    /**
     * 添加数据库配置
     */
    public void addDatabase(Datamanager data) {
        try {
            com.wanfangdata.resourceupdatetools.setting.DatabaseConfigureSetting.addDatabase(convertToDatabase(data));
        } catch (Exception e) {
            log.error("addDatabase出错, data:" + data, e);
            throw new IllegalArgumentException("addDatabase出错");
        }
    }

    /**
     * 删除数据库配置
     */

    public void deleteDatabase(String id) {
        try {
            com.wanfangdata.resourceupdatetools.setting.DatabaseConfigureSetting.deleteDatabase(id);
        } catch (Exception e) {
            log.error("deleteDatabase出错, id:" + id, e);
            throw new IllegalArgumentException("deleteDatabase出错");
        }
    }

    /**
     * 修改数据库配置
     */

    public void updateDatabase(Datamanager data) {
        try {
            com.wanfangdata.resourceupdatetools.setting.DatabaseConfigureSetting.updateDatabase(convertToDatabase(data));
        } catch (Exception e) {
            log.error("updateDatabase出错，data:" + data, e);
            throw new IllegalArgumentException("updateDatabase出错，data:" + data);
        }
    }

    /**
     * 上移资源类型
     */
    public boolean moveUpDatabase(String id) {
        try {
            return com.wanfangdata.resourceupdatetools.setting.DatabaseConfigureSetting.moveUpDatabase(id);
        } catch (Exception e) {
            log.error("moveUpDatabase出错，id:" + id, e);
            throw new IllegalArgumentException("moveUpDatabase出错，id:" + id);
        }
    }

    /**
     * 下移资源类型
     */
    public boolean moveDownDatabase(String id) {
        try {
            return com.wanfangdata.resourceupdatetools.setting.DatabaseConfigureSetting.moveDownDatabase(id);
        } catch (Exception e) {
            log.error("moveDownDatabase出错，id:" + id, e);
            throw new IllegalArgumentException("moveDownDatabase出错，id:" + id);
        }
    }

    /**
     * 检查name是否重复
     */
    public boolean checkName(String name) {
        try {
            return com.wanfangdata.resourceupdatetools.setting.DatabaseConfigureSetting.checkName(name);
        } catch (Exception e) {
            log.error("checkName出错，name:" + name, e);
            throw new IllegalArgumentException("checkName出错，name:" + name);
        }
    }

    /**
     * 更新数据库状态
     */

    public void updateDatabaseState(int state, String id) {
        try {
            com.wanfangdata.resourceupdatetools.setting.DatabaseConfigureSetting.updateDatabaseState(state, id);
        } catch (Exception e) {
            log.error("updateDatabaseState出错，state:" + state + " id:" + id, e);
            throw new IllegalArgumentException("updateDatabaseState出错，state:" + state + " id:" + id);
        }
    }

    /**
     * 更新数据库权限
     */

    public void updateDatabaseStatus(int status, String id) {
        try {
            com.wanfangdata.resourceupdatetools.setting.DatabaseConfigureSetting.updateDatabaseStatus(status, id);
        } catch (Exception e) {
            log.error("updateDatabaseStatus出错，status:" + status + " id:" + id, e);
            throw new IllegalArgumentException("updateDatabaseStatus出错，status:" + status + " id:" + id);
        }
    }

    public JSONArray selectSitateFoOne() {
        try {
            List<Database> dbList = com.wanfangdata.resourceupdatetools.setting.DatabaseConfigureSetting.selectSitateFoOne();
            List<Datamanager> database = new ArrayList<>();
            for (Database db : dbList) {
                database.add(convertToDatamanager(db));
            }
            JSONArray jsonArray = JSONArray.fromObject(database);
            return jsonArray;
        } catch (Exception e) {
            log.error("selectSitateFoOne出错", e);
            throw new IllegalArgumentException("selectSitateFoOne出错");
        }
    }

    /**
     * 判断数据库state
     */
    public boolean checkResourceForOne(String id) {
        try {
            return com.wanfangdata.resourceupdatetools.setting.DatabaseConfigureSetting.checkResourceForOne(id);
        } catch (Exception e) {
            log.error("checkResourceForOne出错, id:" + id, e);
            throw new IllegalArgumentException("checkResourceForOne出错");
        }
    }

    /**
     * 判断数据库status
     */
    public boolean checkStatus(String id) {
        try {
            return com.wanfangdata.resourceupdatetools.setting.DatabaseConfigureSetting.checkStatus(id);
        } catch (Exception e) {
            log.error("checkStatus出错, id:" + id, e);
            throw new IllegalArgumentException("checkStatus出错");
        }
    }

    private static Datamanager convertToDatamanager(Database database) {
        Datamanager datamanager = new Datamanager();
        datamanager.setId(database.getId());
        datamanager.setAbbreviation(database.getAbbreviation());
        datamanager.setCustomPolicy(database.getCustomPolicy());
        datamanager.setDbtype(database.getDbType());
        datamanager.setLanguage(database.getLanguage());
        datamanager.setProductSourceCode(database.getProductSourceCode());
        datamanager.setResType(database.getResourceType());
        datamanager.setSourceDb(database.getDbSource());
        datamanager.setState(database.getState());
        datamanager.setStatus(database.getStatus());
        datamanager.setTableName(database.getName());
        datamanager.setTableDescribe(database.getDescribe());
        datamanager.setImgLogoSrc(database.getImgLogoSrc());
        datamanager.setLink(database.getLink());
        return datamanager;
    }

    private static Database convertToDatabase(Datamanager datamanager) {
        Database database = new Database();
        database.setId(datamanager.getId());
        database.setAbbreviation(datamanager.getAbbreviation());
        database.setCustomPolicy(datamanager.getCustomPolicy());
        database.setDbType(datamanager.getDbtype());
        database.setLanguage(datamanager.getLanguage());
        database.setProductSourceCode(datamanager.getProductSourceCode());
        database.setResourceType(datamanager.getResType());
        database.setDbSource(datamanager.getSourceDb());
        database.setState(datamanager.getState());
        database.setStatus(datamanager.getStatus());
        database.setName(datamanager.getTableName());
        database.setDescribe(datamanager.getTableDescribe());
        database.setImgLogoSrc(datamanager.getImgLogoSrc());
        database.setLink(datamanager.getLink());
        return database;
    }

    /**
     * 通过id查找数据库配置
     */
    public Datamanager findDatabaseById(String id) {
        try {
            Database db = com.wanfangdata.resourceupdatetools.setting.DatabaseConfigureSetting.findDatabaseById(id);
            Datamanager database = null;
            database=convertToDatamanager(db);
            return database;
        } catch (Exception e) {
            log.error("findDatabaseById，id:" + id, e);
            throw new IllegalArgumentException("findDatabaseById，id:" + id);
        }
    }
}
