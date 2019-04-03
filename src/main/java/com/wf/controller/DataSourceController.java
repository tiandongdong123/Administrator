package com.wf.controller;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.redis.RedisUtil;
import com.utils.CookieUtil;
import com.utils.DateTools;
import com.utils.Getproperties;
import com.wf.bean.Authorize;
import com.wf.bean.AuthorizeRelation;
import com.wf.bean.Log;
import com.wf.bean.MatrixLiterature;
import com.wf.bean.PSubjectCategory;
import com.wf.bean.PageList;
import com.wf.bean.Person;
import com.wf.bean.ProResourceType;
import com.wf.bean.Provider;
import com.wf.bean.Setting;
import com.wf.dao.AuthorizeMapper;
import com.wf.dao.AuthorizeRelationMapper;
import com.wf.dao.MatrixLiteratureMapper;
import com.wf.dao.PSubjectCategoryMapper;
import com.wf.dao.PersonMapper;
import com.wf.dao.ProResourceTypeMapper;
import com.wf.dao.ProviderMapper;
import com.wf.dao.SettingMapper;
import com.wf.service.AuthorizeRelationService;
import com.wf.service.AuthorizeService;
import com.wf.service.LogService;
import com.wf.service.MatrixLiteratureService;
import com.wf.service.PSubjectCategoryService;
import com.wf.service.PersonService;
import com.wf.service.ProResourceTypeService;
import com.wf.service.ProviderService;
import com.wf.service.SettingService;
import com.xxl.conf.core.XxlConfClient;

@Controller
@RequestMapping("dataSource")
public class DataSourceController {

	private static final HttpServletRequest request = null;
	@Autowired
	private ProviderService providerService; // 资源提供商 Service
	@Autowired
	private ProviderMapper providerMapper; // 资源提供商 dao
	@Autowired
	private PSubjectCategoryService pSubjectCategory; // 资源提供商 的 学科分类 Service
	@Autowired
	private PSubjectCategoryMapper pSubjectCategoryMapper; // 资源提供商 的 学科分类 Dao
	@Autowired
	private AuthorizeRelationMapper authorizeRelationMapper; // 授权-资源类型-学科-母体 关联表Dao
	@Autowired
	private AuthorizeRelationService authorizeRelation; // 授权-资源类型-学科-母体 关联表Service
	@Autowired
	private MatrixLiteratureService matrixLiterature; // 母体文献 Service
	@Autowired
	private MatrixLiteratureMapper MatrixLiteratureDao; // 母体文献Dao层
	@Autowired
	private AuthorizeService authorizeServie; // 授权表service
	@Autowired
	private AuthorizeMapper authorizeDao; // 授权表Dao
	@Autowired
	private PersonService personService; // 机构Service
	@Autowired
	private PersonMapper persondao; // 机构Dao
	@Autowired
	private ProResourceTypeService proResourceType; // 资源类型 service
	@Autowired
	private ProResourceTypeMapper proResourceTypeMapper;// 资源Dao层
	@Autowired
	private LogService logService;
	
	private static Connection con = null;
	private static PreparedStatement pst = null;
	static{
		String driver = "com.mysql.jdbc.Driver";
		try {
			Class.forName(driver);
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	/*********************/


	// 系统配置 添加更新 页面
	@RequestMapping("setting-addOrEdit-Manager")
	public ModelAndView settingAddOrEditManager() {
		ModelAndView view = new ModelAndView();
		view.setViewName("/page/systemmanager/setting_addOrEdit_Manager");
		return view;
	}

	@Autowired
	private SettingService settingService;
	@Autowired
	private SettingMapper settingDao;

	/** 删除Setting 
	 * @param request 
	 * @throws Exception */
	@RequestMapping("deleteSetting")
	@ResponseBody
	public int deleteSetting(String id, HttpServletRequest request) throws Exception {
		int num = settingDao.deleteSetting(id);
		
		//记录日志
		Log log=new Log();
		log.setUsername(CookieUtil.getWfadmin(request).getUser_realname());
		log.setBehavior("增加");
		log.setUrl(request.getRequestURL().toString());
		log.setTime(DateTools.getSysTime());
		log.setIp(InetAddress.getLocalHost().getHostAddress().toString());
		log.setModule("监控管理");
		log.setOperation_content("删除的settingID:"+id);
		logService.addLog(log);
		
		return num;
	}
	//查询授权机构详细信息
		@RequestMapping("getgetAuthorizeRelationInfo")
		@ResponseBody
		public HashMap<String,Object> getgetAuthorizeRelationInfo(String user_id){
			HashMap<String,Object> map =new HashMap<String,Object>();
			map = authorizeRelation.getgetAuthorizeRelationInfo(user_id);
			 return map;
		}
		
	/** 更新 Setting 
	 * @throws Exception */
	@RequestMapping("addOrUpdateSetting")
	@ResponseBody
	public int addOrUpdateSetting(String id, String settingName,
			String settingKey, String settingValue, String remark) throws Exception {
		Setting setting = null;
		int num = 0;
		if (!"".equals(id) && id != null) {
			setting = new Setting();
			setting.setId(id);
			setting.setSettingName(settingName);
			setting.setSettingKey(settingKey);
			setting.setSettingValue(settingValue);
			setting.setRemark(remark);
			num = settingDao.updateSetting(setting);
		} else {
			setting = new Setting();
			setting = settingService.addSetting(settingName, settingKey,
					settingValue, remark);
			if (setting != null) {
				num = 1;
			}
		}
		
		//记录日志
		Log log=new Log();
		log.setUsername(CookieUtil.getWfadmin(request).getUser_realname());
		log.setBehavior("修改");
		log.setUrl(request.getRequestURL().toString());
		log.setTime(DateTools.getSysTime());
		log.setIp(InetAddress.getLocalHost().getHostAddress().toString());
		log.setModule("监控管理");
		log.setOperation_content("修改后的Setting信息:"+setting.toString());
		logService.addLog(log);

		return num;
	}

	/** 查询当前id Setting */
	@RequestMapping("getSettingById")
	@ResponseBody
	public Setting getSettingById(String id) {
		Setting setting = new Setting();
		setting.setId(id);
		/*
		 * setting.setSettingName(settingName);
		 * setting.setSettingKey(settingKey);
		 * setting.setSettingValue(settingValue); setting.setRemark(remark);
		 */
		setting = settingDao.getSettingById(setting);
		return setting;
	}

	/** 分页查询 Setting 系统配置 */
	@RequestMapping("findSettingPage")
	@ResponseBody
	public PageList findSettingPage(String textKey, String textValue,
			Integer pagenum, Integer pagesize) {
		Setting setting = new Setting();
		if ("settingName".equals(textKey)) {
			setting.setSettingName(textValue);
		} else if ("settingKey".equals(textKey)) {
			setting.setSettingKey(textValue);
		} else if ("remark".equals(textKey)) {
			setting.setRemark(textValue);
		}
		PageList plist = settingService.getSetting(setting, pagenum, pagesize);
		return plist;
	}

	/*********************/

	// 资源提供商 查询
	@RequestMapping("findAllProviders")
	@ResponseBody
	public List<HashMap<String, Object>> findAllProvider() {
		List<HashMap<String, Object>> plist = providerService.getProviders();
		return plist;
	}

	// 增加 ---修改资源提供商方法
	@RequestMapping("addProvider")
	@ResponseBody
	public int addProvider(String nameZh,String providerName, Integer id) {
		if (id != null && !"".equals(id)) {
			boolean b = providerMapper.updateProvider(nameZh,providerName, id);
			if (b == true) {
				return 1;
			} else {
				return -1;
			}
		} else {
			Provider provider = new Provider();
			provider.setNameZh(nameZh);
			provider.setProviderName(providerName);
			int i = providerService.addProvider(provider);
			return i;
		}
	}

	// 删除供应商方法
	@RequestMapping("deleteProvider")
	@ResponseBody
	public boolean deleteProvider(Integer id) {
		boolean b = providerService.deleteProvider(id);
		return b;
	}

	// 查询供单体应商通过Id
	@RequestMapping("getProviderById")
	@ResponseBody
	public Provider getProviderById(Integer gysId) {
		Provider provider = providerMapper.getProvider(gysId);
		return provider;
	}

	// 提供商 学科分类 查询
	@RequestMapping("findAllPSubjectCategory")
	@ResponseBody
	public List<HashMap<String, Object>> findAllPSubjectCategory(
			Integer providerId, Integer proResourceId) {
		List<HashMap<String, Object>> psclist = pSubjectCategory
				.getPSubjectCategory(providerId, proResourceId);
		return psclist;
	}

	/** 添加学科分类 ***/
	@RequestMapping("addPSubjectCategory")
	@ResponseBody
	public int addPSubjectCategory(Integer providerId, String pCategoryCodes,
			String pCategoryName, Integer parentId, Integer id,
			Integer proResourceId) {
		if (id != null && !"".equals(id)) {
			boolean b = pSubjectCategoryMapper.updatePSubjectCategory(
					providerId, pCategoryCodes, pCategoryName, parentId, id,
					proResourceId);
			if (b == true) {
				return 1;
			} else {
				return -1;
			}
		} else {
			int i = pSubjectCategory.addPSubjectCategory(providerId,
					pCategoryCodes, pCategoryName, parentId, proResourceId);
			return i;
		}
	}

	// 查询学科分类
	@RequestMapping("getPSubjectCategoryById")
	@ResponseBody
	public PSubjectCategory getPSubjectCategoryById(Integer classid) {
		PSubjectCategory ps = null;
		if (classid != null && !"".equals(classid)) {
			ps = pSubjectCategory.getPSubjectCategoryParent(classid);
		}
		return ps;
	}

	// 删除分类学科
	@RequestMapping("deletePSubjectCategory")
	@ResponseBody
	public boolean deletePSubjectCategory(Integer id) {
		return pSubjectCategory.deletePSubjectCategory(id);
	}

	// 授权 关联查询
	@RequestMapping("getAuthorizeRelationList")
	@ResponseBody
	public List<HashMap<String, Object>> getAuthorizeRelationList(
			String institutionId, Integer providerId, Integer proResourceId,
			Integer pagenum, Integer pagesize, String startDate,String endDate) {
		AuthorizeRelation ar = new AuthorizeRelation();
		ar.setInstitutionId(institutionId);
		ar.setProviderId(providerId);
		ar.setProResourceId(proResourceId);
		List<HashMap<String, Object>> r = this.authorizeRelation.getAuthorizeRelationlist(ar, pagenum, pagesize, startDate,endDate);
		return r;
	}

	// 授权列表分页查询
	@RequestMapping("getAuthorizelist")
	@ResponseBody
	public List<HashMap<String, Object>> getAuthorizelist(String institutionId,Integer pagenum, Integer pagesize) {
//		if(StringUtils.isBlank(userId)) userId = null;
//		List<String> institutionIds = new ArrayList<>();
////		if (institutionname != null && !"".equals(institutionname)) {
//			Person person = new Person();
//			person.setUsertype(2);
//			person.setInstitution(institutionname);
//			Map<String,Object> map = new HashMap<String,Object>();
//			map.put("userId", userId);
//			map.put("institution", institutionname);
//			int num = this.persondao.findListCount(map);
//			PageList plist = personService.QueryPersion(person, 1, num, null);
//			List<Object> pObject = plist.getPageRow();
//			for (int i = 0; i < pObject.size(); i++) {
//				Person p = (Person) pObject.get(i);
//				institutionIds.add(p.getUserId());
//			}
//			if (pObject.size() <= 0) {
//				institutionIds.add("-1");
//			}
//			
////		}
//		Authorize a = new Authorize();
		List<HashMap<String, Object>> r = this.authorizeServie.getAuthorizelist(institutionId, 1, 10);
		return r;
	}

	/** 授权列表 删除 */
	@RequestMapping("deleteAuthorize")
	@ResponseBody
	public int deleteAuthorize(Integer id) {
		int num = 0;
		List<Object> list = authorizeRelationMapper.getAuReByPeriodical(id); // 查询
																				// 授权表是否有关联
		if (list.size() <= 0) {
			num = authorizeDao.deleteAuthorize(id);
			num = 1;
			//发布原文发现数据到redis
			userRolePower();
		}
		return num;
	}

	/** 编辑 授权列表 */
	@RequestMapping("updateAuthorize")
	@ResponseBody
	public int updateAuthorize(Integer id, String institutionId,
			Integer providerId, String username, String password) {
		int num = authorizeDao.updateAuthorize(institutionId, providerId,
				username, password, id);
		if(num > 0){
			//发布原文发现数据到redis
			userRolePower();
		}
		return num;
	}

	/** 编辑 授权列表 查询单条语句 */
	@RequestMapping("getAuthorizeOne")
	@ResponseBody
	public Authorize getAuthorizeOne(Integer id) {
		Authorize a = authorizeDao.getAuthorize2(id);
		return a;
	}

	/** 删除授权关联表 */
	@RequestMapping("deleteAuthorizeRelation")
	@ResponseBody
	public int deleteAuthorizeRelation(String ids) {
		int num = 0;
		ids = ids.substring(0, ids.length() - 1);
		String id[] = ids.split(",");
		for (int i = 0; i < id.length; i++) {
			Integer idr = Integer.parseInt(id[i]);
			authorizeRelationMapper.deleteAuthorizeRelation(idr);
			num = num + 1;
		}
		return num;
	}

	// 修改授权关联表URL
	@RequestMapping("updateAuthorizeRelation")
	@ResponseBody
	public int updateAuthorizeRelation(Integer id, String detailsURL,
			String downloadURL) {
		AuthorizeRelation ar = authorizeRelationMapper
				.getAuthorizeRelation2(id);
		ar.setDetailsURL(detailsURL);
		ar.setDownloadURL(downloadURL);
		int i = authorizeRelationMapper.updateAuthorizeRelation(ar);
		return i;
	}

	// 查询 授权关联表 单条语句
	@RequestMapping("getAuthorizeRelationOne")
	@ResponseBody
	public AuthorizeRelation getAuthorizeRelationOne(Integer id) {
		AuthorizeRelation ar = authorizeRelationMapper
				.getAuthorizeRelation2(id);
		return ar;
	}

	// 母体查询 筛选已授权过的母体
	@RequestMapping("getMatrixLiteratureList")
	@ResponseBody
	public List<HashMap<String, Object>> getMatrixLiteratureList(
			String institutionId, Integer providerId, Integer psubjectId,
			Integer proResourceId, Integer pagenum, Integer pagesize,
			String startDate,String endDate) {
		// institutionId ="bjdx";
		AuthorizeRelation ar = new AuthorizeRelation();
		List<String> unEqualId = null;
		if (institutionId != null && !"".equals(institutionId)) {
			ar.setProviderId(providerId);
			ar.setInstitutionId(institutionId);
			List<Object> list = this.authorizeRelation
					.getAuthorizeRelationfind(ar);
			if (list.size() > 0) {
				unEqualId = new ArrayList<String>();
			}
			for (int i = 0; i < list.size(); i++) {
				AuthorizeRelation authR = (AuthorizeRelation) list.get(i);
				unEqualId.add(authR.getPeriodicalId());
			}
		}
		List<HashMap<String, Object>> r = this.matrixLiterature
				.getMatrixLiteratureList(providerId, psubjectId, proResourceId,
						pagenum, pagesize, startDate, endDate, unEqualId);
		return r;
	}
	// 学科类型 查询母体 全选
	@RequestMapping("getMatrixLiteratureListIds")
	@ResponseBody
	public List<Object> getMatrixLiteratureListIds(Integer subjId,Integer providerId,String maId,String checkedTree,String startDate,String endDate) {
		List<String> unEqualId=null;
		if(maId !=null && !"".equals(maId)){
			maId=maId.substring(0,maId.length()-1);
			if("true".equals(checkedTree)){
				unEqualId=new ArrayList<String>();
				String maIds[] =maId.split(",");
				for(int i=0;i<maIds.length;i++){
					unEqualId.add(maIds[i]);
				}
			}
		}
		List<Object> list = this.MatrixLiteratureDao.getMatrixLiterature(providerId, subjId, null, null, null,startDate, endDate, unEqualId);
		return list;
	}
	// 母体文献 查询 所有的 资源配置页面
	@RequestMapping("getMatrixLiteratureLists")
	@ResponseBody
	public List<HashMap<String, Object>> getMatrixLiteratureLists(
			Integer providerId, Integer psubjectId, Integer pagenum,
			Integer pagesize, String startDate,String endDate,
			Integer proResourceId) {
		List<HashMap<String, Object>> r = this.matrixLiterature
				.getMatrixLiteratureList(providerId, psubjectId, proResourceId,
						pagenum, pagesize, startDate, endDate, null);
		return r;
	}

	// 添加母体文献
	@RequestMapping("addMatrixLiterature")
	@ResponseBody
	public int addMatrixLiterature(Integer providerId, Integer psubjectId,
			Integer proResourceId, String title, String nameen, String author,
			String abstracts, String datePeriod, String cover, String id) {
		if (id != null && !"".equals(id)) {
			boolean b = matrixLiterature.updateMatrixLiterature(providerId,
					psubjectId, proResourceId, title, nameen, author,
					abstracts, datePeriod, cover, id);
			if (b == true) {
				return 1;
			} else {
				return -1;
			}
		} else {
			int i = matrixLiterature.addMatrixLiterature(providerId,
					psubjectId, proResourceId, title, nameen, author,
					abstracts, datePeriod, cover);
			return i;
		}
	}

	// 查询母体文献
	@RequestMapping("getMatrixLiteratureById")
	@ResponseBody
	public HashMap<String, Object> getMatrixLiteratureById(String idss) {
		HashMap<String, Object> hm = matrixLiterature
				.getMatrixLiteratureById(idss);
		return hm;

	}

	// 删除母体
	@RequestMapping("deleteMatrixLiterature")
	@ResponseBody
	public boolean deleteMatrixLiterature(String id) {
		boolean b = matrixLiterature.deleteMatrixLiterature(id);
		return b;

	}

	// 授权用户名密码 是否存在
	@RequestMapping("findAuthorize")
	@ResponseBody
	public List<HashMap<String, Object>> findAuthorize(String institutionId,
			Integer providerId) {
		Authorize a = new Authorize();
		a.setInstitutionId(institutionId);
		a.setProviderId(providerId);
		List<HashMap<String, Object>> listmap = authorizeServie.getAuthorize(a);
		return listmap;
	}

	// 授权添加
	@RequestMapping("addAuthorizeAndRelation")
	@ResponseBody
	public int addAuthorizeAndRelation(String institutionId,
			Integer providerId, String username, String password,
			Integer proResourceId, Integer subjectId, String periodicalIds,
			String detailsURL, String downloadURL) {
		String periodicalIdss[] = periodicalIds.split(",");
		for (int i = 0; i < periodicalIdss.length; i++) {
			String periodicalId = periodicalIdss[i];
			Authorize a = new Authorize();
			a.setInstitutionId(institutionId);
			a.setProviderId(providerId);
			List<HashMap<String, Object>> listmap = authorizeServie.getAuthorize(a);
			if (listmap.size() <= 0) {
				a = authorizeServie.addAuthorize(institutionId, providerId,username, password);
			} else {
				a.setId((Integer) listmap.get(0).get("id"));
				int num = authorizeDao.updateAuthorize(a.getInstitutionId(),a.getProviderId(), a.getUsername(), password,a.getId());
			}
			AuthorizeRelation arn = new AuthorizeRelation();
			arn.setPeriodicalId(periodicalId);
			arn.setInstitutionId(institutionId);
			int num2 = authorizeRelationMapper.getAuthorizeRelationNum(arn,null);
			if (num2 <= 0 && StringUtils.isNotBlank(periodicalId)) {
				MatrixLiterature m = MatrixLiteratureDao.getMatrixLiteratureById(periodicalId);
				AuthorizeRelation ar = authorizeRelation.addAuthorizeRelation(a.getId(), institutionId, providerId,m.getProResourceId(), m.getPsubjectId(), periodicalId,
							detailsURL, downloadURL);
			}
		}
		//发布原文发现数据到redis
		userRolePower();
		return periodicalIdss.length;
	}
	public static void userRolePower(){
		RedisUtil redis = new RedisUtil();
		/*String url =Getproperties.getPros("jdbc.properties").getProperty("jdbc.url");
		String userName = Getproperties.getPros("jdbc.properties").getProperty("jdbc.username");
		String passWord = Getproperties.getPros("jdbc.properties").getProperty("jdbc.password");*/
		
		String url =XxlConfClient.get("jdbc.adminManager.url", null);
		String userName = XxlConfClient.get("jdbc.adminManager.username", null);
		String passWord = XxlConfClient.get("jdbc.adminManager.password", null);
		try {
//			con = DriverManager.getConnection(url,userName,passWord);
//			pst = con.prepareStatement("select * from UserRole");
			Connection con_authorize = DriverManager.getConnection(url,userName,passWord);
			PreparedStatement pst_authorize = con_authorize.prepareStatement("select institution_id from authorize GROUP BY institution_id");
			ResultSet rs_authorize = pst_authorize.executeQuery();
			while(rs_authorize.next()){
				JSONArray relations = new JSONArray();
				String institution_id = rs_authorize.getString("institution_id");
				Connection con = DriverManager.getConnection(url,userName,passWord);
				PreparedStatement pst = con.prepareStatement("select provider_id,username,password from authorize where institution_id='"+institution_id+"'");
				ResultSet rs = pst.executeQuery();
				while(rs.next()){
				JSONObject relation = new JSONObject();
				String provider_id =  rs.getString("provider_id");
				String username =  rs.getString("username");
				String password =  rs.getString("password");
				String details_url = "";
				String download_url = "";
				
				Connection con_perio = DriverManager.getConnection(url,userName,passWord);
				String sql = "select details_url,download_url from auth_perio_relation where provider_id="+provider_id+" and institution_id='"+institution_id+"' limit 1 ";
				PreparedStatement pst_perio = con_perio.prepareStatement(sql);
				ResultSet rs_perio = pst_perio.executeQuery();
				while(rs_perio.next()){
					details_url = rs_perio.getString("details_url");
					download_url = rs_perio.getString("download_url");
				}
				relation.put("provider_id", provider_id);
				relation.put("username", username);
				relation.put("password", password);
				relation.put("details_url", details_url);
				relation.put("download_url", download_url);
				
				Connection con_provider = DriverManager.getConnection(url,userName,passWord);
				PreparedStatement pst_provider = con_provider.prepareStatement("select name_zh,provider_name from provider where id=" + provider_id);
				ResultSet rs_provider = pst_provider.executeQuery();
				while(rs_provider.next()){
					String name_zh = rs_provider.getString("name_zh");
					String provider_name =  rs_provider.getString("provider_name");
					relation.put("provider", provider_name);
					relation.put("name_zh", name_zh);
				}
				relations.add(relation);
				JSONObject userPower = new JSONObject();
				userPower.put("original_discovery",relations);
				if(relations!=null && relations.size()>0){
					redis.set(institution_id, userPower.toString(),2);
				}
			}
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	
	public static void userRolePower_temp(){
		RedisUtil r = new RedisUtil();
		String url = Getproperties.getPros("jdbc.properties").getProperty("jdbc.url");
		String userName = Getproperties.getPros("jdbc.properties").getProperty("jdbc.username");
		String passWord = Getproperties.getPros("jdbc.properties").getProperty("jdbc.password");
		try {
			con = DriverManager.getConnection(url,userName,passWord);
			pst = con.prepareStatement("select * from UserRole");
			Connection con1 = DriverManager.getConnection(url,userName,passWord);
			PreparedStatement pst1 = con1.prepareStatement("select * from auth_perio_relation apr left join pro_resource_type prt on prt.id=apr.pro_resource_id left join  psubject_category pc on apr.subject_id=pc.id");
			Connection con2 = DriverManager.getConnection(url,userName,passWord);
			PreparedStatement pst2 = con2.prepareStatement("select * from authorize");
			Connection con3 = DriverManager.getConnection(url,userName,passWord);
			ResultSet rs3 = pst2.executeQuery();
//			PreparedStatement pst3 = con3.prepareStatement("select user_id from user where userType=2");
//			ResultSet rs3 = pst3.executeQuery();
			Connection con4 = DriverManager.getConnection(url,userName,passWord);
			PreparedStatement pst4 = con4.prepareStatement("select * from RolePower");
			Connection con5 = DriverManager.getConnection(url,userName,passWord);
			PreparedStatement pst5 = con5.prepareStatement("select * from provider");
			Connection con6 = DriverManager.getConnection(url,userName,passWord);
			PreparedStatement pst6 = con6.prepareStatement("select * from wfks_pay_channel_resources");
			
			while (rs3.next()) {
				String name = rs3.getString("institution_id");
				JSONArray roles = new JSONArray();
				ResultSet rs = pst.executeQuery();
				/*while (rs.next()) {
					JSONObject role = new JSONObject();
					String user_rolename = rs.getString(2);
					String role_id = rs.getString(3);
					if(user_rolename.equals(name)){
						role.put("role_id", role_id);
						ResultSet rs4 = pst4.executeQuery();
						while (rs4.next()) {
							String role_id02 = rs4.getString(1);
							String role_name = rs4.getString(2);
							String role_en_name = rs4.getString(3);
							String role_power = rs4.getString(4);
							if(role_id.equals(role_id02)){
								role.put("role_name", role_name);
								role.put("role_en_name", role_en_name);
								role.put("role_power", role_power);
							}
						}
						roles.add(role);
					}
				}*/
				
				JSONArray relations = new JSONArray();
				ResultSet rs1 = pst1.executeQuery();
				while (rs1.next()) {
					JSONObject relation = new JSONObject();
					String id = rs1.getString("id");
					String authorize_id = rs1.getString("authorize_id");
					String pro_resource_id = rs1.getString("pro_resource_id");
					String resource_name = rs1.getString("resource_name");
					String subject_id = rs1.getString("subject_id");
					String pcategory_name = rs1.getString("pcategory_name");
					String pcategory_codes = rs1.getString("pcategory_codes");
					String periodical_id = rs1.getString("periodical_id");
					String details_url = rs1.getString("details_url");
					String download_url = rs1.getString("download_url");
					String institution_id = rs1.getString("institution_id");
					String provider_id = rs1.getString("provider_id");
					if(institution_id.equals(name)){
						relation.put("details_url", details_url);
						relation.put("download_url", download_url);
						relation.put("provider_id", provider_id);
						ResultSet rs2 = pst2.executeQuery();
						while (rs2.next()) {
							String institution_id_02 = rs2.getString(2);
							if(institution_id.equals(institution_id_02)){
								String username = rs2.getString(3);
								String password = rs2.getString(4);
								relation.put("username", username);
								relation.put("password", password);
							}
						}
						ResultSet rs5 = pst5.executeQuery();
						while (rs5.next()) {
							String provider = rs5.getString(1);
 							if(provider_id.equals(provider)){
								String provider_name = rs5.getString("provider_name");
								String name_zh = rs5.getString("name_zh");
								relation.put("provider", provider_name);
								relation.put("name_zh", name_zh);
							}
						}
						relations.add(relation);
					}
				}
				
				JSONObject userPower = new JSONObject();
				
				/*ResultSet rs6 = pst6.executeQuery();
				while (rs6.next()) {
					String user_id = rs6.getString(2);
						if(name.equals(user_id)){
						String contract = rs6.getString(5);
						userPower.put("Terms", JSONObject.fromObject(contract).get("Terms"));
					}
				}*/
				
				//userPower.put("user_roles",roles);
				userPower.put("original_discovery",relations);
				if(relations!=null && relations.size()>0){
					r.set(name, userPower.toString(),2);
				}
				
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	// 查询机构
	@RequestMapping("findQueryPersion")
	@ResponseBody
	public Map<String,Object> findQueryPersion(@RequestParam("userId")String userId) {
		if(StringUtils.isBlank(userId)) userId = null;
		Person person = personService.findById(userId);
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("person", person);
		return map;
	}

	// 查询资源类型
	@RequestMapping("findResourceNamesById")
	@ResponseBody
	public List<ProResourceType> findResourceNamesById(Integer providerId) {
		List<ProResourceType> list = proResourceType
				.findResourceNamesById(providerId);
		return list;
	}

	// 查询个体资源类型
	@RequestMapping("getProResourceType")
	@ResponseBody
	public ProResourceType getProResourceType(Integer proid) {
		return proResourceTypeMapper.getProResourceType(proid);
	}

	// 修改 增加资源类型
	@RequestMapping("addProResourceType")
	@ResponseBody
	public int addProResourceType(Integer id, Integer providerId,
			String resourceName) {
		if (id != null && !"".equals(id)) {
			// 更新
			boolean b = proResourceType.updateProResourceType(providerId,
					resourceName, id);
			if (b == true) {
				return 1;
			} else {
				return 0;
			}

		} else {
			int i = proResourceType
					.addProResourceType(providerId, resourceName);
			return i;
		}
	}

	// 删除资源类型
	@RequestMapping("deleteProResourceType")
	@ResponseBody
	public boolean deleteProResourceType(Integer id) {
		return proResourceType.deleteProResourceType(id);
	}

	// 期刊资源配置页面
	@RequestMapping("qikan-ziyuanpeizhi")
	public ModelAndView qikanZiyuanpeizhi(HttpServletRequest request) {
		ModelAndView view = new ModelAndView();
		view.setViewName("/page/qikan/qikan_ziyuanpeizhi");
		String purview=CookieUtil.getCookiePurviews(request);
		if(purview.indexOf("G2")!=-1){
			return view;
		}else{
			return null;
		}
	}

	// 资源授权配置页面
	@RequestMapping("qikan-shouquanpeizhi")
	public ModelAndView qikanShouquanpeizhi(HttpServletRequest request) {
		ModelAndView view = new ModelAndView();
		view.setViewName("/page/qikan/qikan_shouquanpeizhi");
		String purview=CookieUtil.getCookiePurviews(request);
		if(purview.indexOf("G3")!=-1){
			return view;
		}else{
			return null;
		}
	}

	// 资源授权列表页面
	@RequestMapping("qikan-jigouList")
	public ModelAndView qikanjigouList(HttpServletRequest request) {
		ModelAndView view = new ModelAndView();
		view.setViewName("/page/qikan/qikan_jigouList");
		String purview=CookieUtil.getCookiePurviews(request);
		if(purview.indexOf("G1")!=-1){
			return view;
		}else{
			return null;
		}
	}

	// 资源授权列表页面
	@RequestMapping("qikan-edit-Authorize")
	public ModelAndView qikanEditAuthorize() {
		ModelAndView view = new ModelAndView();
		view.setViewName("/page/qikan/qikan_edit_Authorize");
		return view;
	}

	// 授权列表 详情页
	@RequestMapping("qikan-jigouList-xiangqing")
	public ModelAndView qikanJigouListXiangqing() {
		ModelAndView view = new ModelAndView();
		view.setViewName("/page/qikan/qikan_jigouList_xiangqing");
		return view;
	}

	// 授权关联表 编辑页
	@RequestMapping("qikan-edit-AuthorizeRelation")
	public ModelAndView qikanEditAuthorizeRelation() {
		ModelAndView view = new ModelAndView();
		view.setViewName("/page/qikan/qikan_edit_AuthorizeRelation");
		return view;
	}

	// 搜索机构页面
	@RequestMapping("qikan-findPerson")
	public ModelAndView qikanFindPerson() {
		ModelAndView view = new ModelAndView();
		view.setViewName("/page/qikan/qikan_findPerson");
		return view;
	}

	// 添加资源提供商页面
	@RequestMapping("qikan-addProvider")
	public ModelAndView qikanaddProvider() {
		ModelAndView view = new ModelAndView();
		view.setViewName("/page/qikan/qikan_addProvider");
		return view;
	}

	// 添加资源类型页面
	@RequestMapping("qikan-addProResourceType")
	public ModelAndView qikanaddProResourceType() {
		ModelAndView view = new ModelAndView();
		view.setViewName("/page/qikan/qikan_addProResourceType");
		return view;
	}

	// 添加学科分类页面
	@RequestMapping("qikan-addPSubjectCategory")
	public ModelAndView qikanaddPSubjectCategory() {
		ModelAndView view = new ModelAndView();
		view.setViewName("/page/qikan/qikan_addPSubjectCategory");
		return view;
	}

	// 添加母体文献页面
	@RequestMapping("qikan-addMatrixLiterature")
	public ModelAndView qikanaddMatrixLiterature() {
		ModelAndView view = new ModelAndView();
		view.setViewName("/page/qikan/qikan_addMatrixLiterature");
		return view;
	}

	// 选择父类学科
	@RequestMapping("qikan-fuleixueke")
	public ModelAndView qikanfuleixueke() {
		ModelAndView view = new ModelAndView();
		view.setViewName("/page/qikan/qikan_fuleixueke");
		return view;
	}

}
