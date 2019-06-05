package com.wf.service.impl;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.protobuf.util.Timestamps;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.log4j.Logger;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrQuery.ORDER;
import org.apache.solr.client.solrj.SolrQuery.SortClause;
import org.apache.solr.common.SolrDocumentList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import wfks.accounting.account.Account;
import wfks.accounting.account.AccountDao;
import wfks.accounting.setting.PayChannelModel;
import wfks.accounting.setting.SettingPayChannels;
import wfks.accounting.transaction.TransactionProcess;
import wfks.accounting.transaction.TransactionRequest;
import wfks.accounting.transaction.TransactionResponse;
import wfks.authentication.AccountId;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.PascalNameFilter;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.utils.DateUtil;
import com.utils.Des;
import com.utils.ExcelUtil;
import com.utils.GetUuid;
import com.utils.Getproperties;
import com.utils.HttpClientUtil;
import com.utils.IPConvertHelper;
import com.utils.InstitutionUtils;
import com.utils.SendMail2;
import com.utils.SettingUtil;
import com.utils.SolrService;
import com.utils.StringUtil;
import com.wanfangdata.encrypt.PasswordHelper;
import com.wanfangdata.grpcchannel.BindAccountChannel;
import com.wanfangdata.grpcchannel.BindAuthorityChannel;
import com.wanfangdata.model.BalanceLimitAccount;
import com.wanfangdata.model.CountLimitAccount;
import com.wanfangdata.model.TimeLimitAccount;
import com.wanfangdata.model.UserAccount;
import com.wanfangdata.rpc.bindauthority.AccountAuthority;
import com.wanfangdata.rpc.bindauthority.BindType;
import com.wanfangdata.rpc.bindauthority.CloseBindRequest;
import com.wanfangdata.rpc.bindauthority.EditBindRequest;
import com.wanfangdata.rpc.bindauthority.OpenBindRequest;
import com.wanfangdata.rpc.bindauthority.SearchAccountAuthorityRequest;
import com.wanfangdata.rpc.bindauthority.SearchAccountAuthorityResponse;
import com.wanfangdata.rpc.bindauthority.SearchBindDetailsRequest;
import com.wanfangdata.rpc.bindauthority.SearchBindDetailsResponse;
import com.wanfangdata.rpc.bindauthority.ServiceResponse;
import com.wanfangdata.setting.BindAuthorityMapping;
import com.webservice.WebServiceUtils;
import com.wf.bean.BindAuthorityModel;
import com.wf.bean.BindAuthorityViewModel;
import com.wf.bean.GroupInfo;
import com.wf.bean.InstitutionalUser;
import com.wf.bean.Mail;
import com.wf.bean.PageList;
import com.wf.bean.Person;
import com.wf.bean.ProjectResources;
import com.wf.bean.ResourceDetailedDTO;
import com.wf.bean.ResourceLimitsDTO;
import com.wf.bean.StandardUnit;
import com.wf.bean.UserAccountRestriction;
import com.wf.bean.UserInstitution;
import com.wf.bean.UserIp;
import com.wf.bean.WarningInfo;
import com.wf.bean.WfResourcesModel;
import com.wf.bean.WfksAccountidMapping;
import com.wf.bean.WfksPayChannelResources;
import com.wf.bean.WfksUserSetting;
import com.wf.bean.WfksUserSettingKey;
import com.wf.controller.GroupAccountUtil;
import com.wf.dao.AheadUserMapper;
import com.wf.dao.DatamanagerMapper;
import com.wf.dao.GroupInfoMapper;
import com.wf.dao.PersonMapper;
import com.wf.dao.ProjectBalanceMapper;
import com.wf.dao.ProjectResourcesMapper;
import com.wf.dao.ResourcePriceMapper;
import com.wf.dao.StandardUnitMapper;
import com.wf.dao.UserAccountRestrictionMapper;
import com.wf.dao.UserInstitutionMapper;
import com.wf.dao.UserIpMapper;
import com.wf.dao.WfksAccountidMappingMapper;
import com.wf.dao.WfksPayChannelResourcesMapper;
import com.wf.dao.WfksUserSettingMapper;
import com.wf.service.AheadUserService;
import com.xxl.conf.core.XxlConfClient;

@Service
public class AheadUserServiceImpl implements AheadUserService{

	private static Logger log = Logger.getLogger(AheadUserServiceImpl.class);
	private static String isOpen=Getproperties.getKey("validateOldUser","isOpen");
	//标准模块的参数
	private static String STANDARD ="DB_WFSD";
	private static String STANDARD_CODE="GB168Standard";
	private static String SALEAGTID=XxlConfClient.get("wf-admin.saleagtid",null);
	private static String ORGCODE=XxlConfClient.get("wf-admin.orgcode",null);
	private static String hosts=XxlConfClient.get("wf-public.solr.url", null);
    private final static String OLD_TIME = "OLD_TIME";
    private final static String OLD_BALAB = "OLD_BALAB";
    private final static String OLD_FORMAL = "OLD_FORMAL";
    private final static String OLD_TRICAL = "OLD_TRICAL";

	private SimpleDateFormat sdfSimp = new SimpleDateFormat("yyyy-MM-dd");

	@Autowired
	private AheadUserMapper aheadUserMapper;
	@Autowired
	private PersonMapper personMapper;
	@Autowired
	private AccountDao accountDao;
	@Autowired
	private ProjectBalanceMapper projectBalanceMapper;
	@Autowired
	private ProjectResourcesMapper projectResourcesMapper;
	@Autowired
	private ResourcePriceMapper resourcePriceMapper;
	@Autowired
	private UserIpMapper userIpMapper;
	@Autowired
	private UserAccountRestrictionMapper userAccountRestrictionMapper;
	@Autowired
	private DatamanagerMapper datamanagerMapper;
	@Autowired
	private WfksPayChannelResourcesMapper wfksMapper;
	@Autowired
	private WfksAccountidMappingMapper wfksAccountidMappingMapper;
	@Autowired
	private WfksUserSettingMapper wfksUserSettingMapper;
	@Autowired
	private StandardUnitMapper standardUnitMapper;
	@Autowired
	private UserInstitutionMapper userInstitutionMapper;
	@Autowired
	private GroupInfoMapper groupInfoMapper;
	@Autowired
	private GroupAccountUtil groupAccountUtil;//机构操作类
	@Autowired
	private HttpServletRequest httpRequest;//用来获取ip
	/**
	 * 个人修改接口
	 * */
	@Autowired
	private TransactionProcess accountingService;
	@Autowired
	private BindAuthorityChannel bindAuthorityChannel;
	@Autowired
	private BindAccountChannel  bindAccountChannel;
	@Autowired
	private BindAuthorityMapping bindAuthorityMapping;

	@Transactional(propagation = Propagation.REQUIRED , readOnly = false)
	@Override
	public boolean registerInfo(InstitutionalUser user) {
		// 添加机构名称
		this.addRegisterInfo(user);
		// 添加用户IP
		if (user.getLoginMode().equals("0") || user.getLoginMode().equals("2")) {
			this.addUserIp(user);
		}
		// 机构子账号限定
		this.setAccountRestriction(user,true);
		// 添加党建管理员
		this.setPartyAdmin(user);
		// 添加机构管理员
		this.addAdmin(user);
		// 统计分析权限
		this.addUserIns(user);
		// 开通用户角色
		this.addWfksAccountidMapping(user);
		// 开通用户权限
		this.addGroupInfo(user);
		return true;
	}

	@Transactional(propagation = Propagation.REQUIRED , readOnly = false)
	@Override
	public boolean updateinfo(InstitutionalUser user) {
		// 修改机构名称
		this.updateUserInfo(user);
		//修改用户IP
		if (user.getLoginMode().equals("0") || user.getLoginMode().equals("2")) {
			this.updateUserIp(user);
		} else {
			this.deleteUserIp(user.getUserId());
		}
		// 机构子账号限定
		this.setAccountRestriction(user,true);
		// 党建管理员
		this.setPartyAdmin(user);
		// 机构管理员
		this.addAdmin(user);
		// 统计分析权限
		this.addUserIns(user);
		// 用户权限
		this.addWfksAccountidMapping(user);
		// 开通用户权限
		this.addGroupInfo(user);
		//修改机构名称
		/*if(!StringUtils.equals(user.getInstitution(), user.getOldInstitution())){
			this.updateInstitution(user.getInstitution(),user.getOldInstitution());
		}*/
		return true;
	}

	@Transactional(propagation = Propagation.REQUIRED , readOnly = false)
	@Override
	public boolean batchRegisterInfo(InstitutionalUser user,Map<String,Object> map) {
		// 添加机构名称
		this.addRegisterInfo(user);
		// 添加用户IP 批量的登录方式(用户密码、用户密码+IP)
		if ("2".equals(user.getLoginMode())) {
			String ip=(String) map.get("ip");
			ip=ip.replace("\r\n", "\n").replace("\n", "\r\n");
			user.setIpSegment(ip);
			this.updateUserIp(user);
		}
		// 机构子账号限定
		this.setAccountRestriction(user,true);
		// 添加机构管理员
		this.addAdmin(user);
		//统计分线权限
		this.addUserIns(user);
		// 开通用户角色
		this.addWfksAccountidMapping(user);
		// 开通用户权限
		this.addGroupInfo(user);
		return true;
	}

	@Transactional(propagation = Propagation.REQUIRED , readOnly = false)
	@Override
	public boolean batchUpdateInfo(InstitutionalUser user,Map<String,Object> map) {
		//默认用户的各种设置
		this.setDefaultUser(user, map);
		// 更新机构名称
		this.updateUserInfo(user);
		// 添加用户IP 批量的登录方式(用户密码、用户密码+IP)
		if ("2".equals(user.getLoginMode())) {
			String ip=(String) map.get("ip");
			ip=ip.replace("\r\n", "\n").replace("\n", "\r\n");
			if(!StringUtils.isEmpty(ip)){
				user.setIpSegment(ip);
				this.updateUserIp(user);
			}
		}else{
			this.deleteUserIp(user.getUserId());
		}
		// 机构子账号限定
		if (user.getpConcurrentnumber() != null || user.getsConcurrentnumber() != null) {
			this.setAccountRestriction(user,false);
		}
		// 添加机构管理员
		if (!StringUtils.isEmpty(user.getAdminname())
				|| !StringUtils.isEmpty(user.getAdminOldName())) {
			if(!StringUtils.isEmpty(user.getAdminpassword())){
				this.addAdmin(user);
			}
		}
		// 统计分线权限
		if (!StringUtils.isEmpty(user.getTongji())) {
			this.addUserIns(user);
		}
		// 开通用户角色
		this.updateWfksAccountidMapping(user);
		// 开通用户权限
		this.setGroupInfo(user);
		return false;
	}

	// 修改开通用户权限 没有就是默认不修改
	private void setGroupInfo(InstitutionalUser user) {
		GroupInfo groupInfo = this.getGroupInfo(user.getUserId());
		if (!StringUtils.isEmpty(user.getOrganization())) {
			groupInfo.setOrganization(user.getOrganization());
		}
		if (!StringUtils.isEmpty(user.getOrderType())&&!StringUtils.isEmpty(user.getOrderContent())) {
			groupInfo.setOrderType(user.getOrderType());
			groupInfo.setOrderContent(user.getOrderContent());
		}
		if (!StringUtils.isEmpty(user.getCountryRegion())
				&& !StringUtils.isEmpty(user.getPostCode())) {
			groupInfo.setCountryRegion(user.getCountryRegion());
			groupInfo.setPostCode(user.getPostCode());
		}
		if (!StringUtils.isEmpty(user.getAdminname())) {
			groupInfo.setPid(user.getAdminname());
		} else if (!StringUtils.isEmpty(user.getAdminOldName())) {
			groupInfo.setPid(user.getAdminOldName());
		}
		this.updateGroupInfo(groupInfo);
	}

	//用户保持默认设置
	private void setDefaultUser(InstitutionalUser user,Map<String,Object> map){
		String userId=map.get("userId").toString();
		String institution=map.get("institution")==null?"":map.get("institution").toString();
		String password=map.get("password")==null?"":map.get("password").toString();
		Person ps = this.queryPersonInfo(userId);
		// Excel表格中部分账号信息
		if (!StringUtils.isEmpty(institution)) {
			user.setInstitution(institution);
		} else {
			user.setInstitution(ps.getInstitution());
		}
		if (!StringUtils.isEmpty(password)) {
			user.setPassword(password);
		} else {
			try {
				user.setPassword(PasswordHelper.decryptPassword(ps.getPassword()));
			} catch (Exception e) {
				log.error("密码转化异常", e);
			}
		}
		if (StringUtils.isEmpty(user.getAdminname())&&StringUtils.isEmpty(user.getAdminOldName())) {
			if(StringUtils.equals(institution, ps.getInstitution())){
				user.setAdminname(ps.getPid());
			}
		}
		user.setUserId(userId);
	}

	//添加机构管理员
	private void addAdmin(InstitutionalUser user){
		if (StringUtils.isEmpty(user.getManagerType())) {
			return;
		}
		if("new".equals(user.getManagerType())&&StringUtils.isEmpty(user.getAdminname())||
				"old".equals(user.getManagerType())&&StringUtils.isEmpty(user.getAdminOldName())){
			return;
		}
		String adminId = user.getManagerType().equals("new") ? user.getAdminname() : user
				.getAdminOldName();
		Person per=this.queryPersonInfo(adminId);
		if(per!=null){
			this.updateRegisterAdmin(user);
		}else{
			this.addRegisterAdmin(user);
		}
		this.deleteUserIp(adminId);
		if(StringUtils.isNotBlank(user.getAdminIP())){
			this.addUserAdminIp(user);
		}
	}

	/**
	 * 调用接口验证老平台用户是否存在 
	 */
	@Override
	public String validateOldUser(String userName) {
		if (!"true".equals(isOpen)) {
			return "true";
		}
		HttpClient httpclient = HttpClients.createDefault();
		String login=XxlConfClient.get("wf-uias.validate.register",null);
		HttpPost httpPost = new HttpPost(login);
		List<NameValuePair> nvps = new ArrayList<NameValuePair>();
		nvps.add(new BasicNameValuePair("userName", userName));
		try {
			StringBuffer buffer = new StringBuffer();
			httpPost.setEntity(new UrlEncodedFormEntity(nvps));
			HttpResponse response = httpclient.execute(httpPost);
			InputStream is = response.getEntity().getContent();
			int i;
			while ((i = is.read()) != -1) {
				buffer.append((char) i);
			}
			String msg = buffer.toString();
			if ("false".equals(msg)) {
				msg = "old";
			} else if (!"true".equals(msg)) {
				msg = "error";
			}
			return msg;
		} catch (Exception e) {
			e.printStackTrace();
			return "error";
		} finally {
			httpPost.releaseConnection();
		}
	}


	/** 
	 * 个人账号充值（修改） 
	 */
	@Override
	public int personCharge(String userId, String turnover, String reason, String authToken, HttpServletResponse httpResponse) throws Exception {
		int i = 0;
		try{
			httpResponse.setHeader("Content-type", "text/html;charset=UTF-8");
			httpResponse.setCharacterEncoding("UTF-8");

			TransactionRequest request = new TransactionRequest();
			request.setTransferIn(new AccountId(AccountId.PERSON, userId));

			//2017.6.23新增：在request中指定transferOut,指定金额为null
			Map<AccountId, BigDecimal> transferOut = new HashMap<>();
			transferOut.put(new AccountId("Operational", "Manual"), null);
			request.setTransferOut(transferOut);

			request.setUserIP(httpRequest.getRemoteAddr());
			request.setAuthToken(authToken);
			request.setTurnover(new BigDecimal(turnover));
			Map<String, String> extraData = new HashMap<String, String>();
			extraData.put("adminmanualchargereason", reason);//充值原因的key
			request.setExtraData(extraData);
			request.setProductDetail("后台充值");

			TransactionResponse response = accountingService.submit(request);
			if (response.isSuccess()){
				i = 1;
				return i;
			}
			return i;
		} catch (Exception e) {
			throw e;
		}
	}



	/**
	 *	查询相似机构管理员
	 */
	@Override
	public List<String> queryAdminName(String adminname){
		return personMapper.queryAdminName(adminname);
	}

	/**
	 *	查询相似机构名称
	 */
	@Override
	public List<String> queryInstitution(String institution){
		return personMapper.queryInstitution(institution);
	}

	/**
	 *	查询预警信息 
	 */
	@Override
	public WarningInfo findWarning(){
		return aheadUserMapper.findWarning();
	}

	/**
	 *	更新预警信息
	 */
	@Override
	public int updateWarning(Integer amountthreshold,Integer datethreshold,Integer remindtime,String remindemail,Integer countthreshold,Date exec_time) {
		WarningInfo winfo = new WarningInfo();
		winfo.setId("1");
		winfo.setAmountthreshold(amountthreshold);
		winfo.setCountthreshold(countthreshold);
		winfo.setDatethreshold(datethreshold);
		winfo.setRemindtime(remindtime);
		winfo.setRemindemail(remindemail);
		winfo.setExec_time(exec_time);
		return aheadUserMapper.updateWarning(winfo);
	}

	/**
	 *	添加预警信息 
	 */
	@Override
	public int addWarning(Integer amountthreshold,Integer datethreshold,Integer remindtime,String remindemail,Integer countthreshold){
		WarningInfo winfo = new WarningInfo();
		winfo.setId("1");
		winfo.setAmountthreshold(amountthreshold);
		winfo.setCountthreshold(countthreshold);
		winfo.setDatethreshold(datethreshold);
		winfo.setRemindtime(remindtime);
		winfo.setRemindemail(remindemail);
		return aheadUserMapper.addWarning(winfo);
	}

	@Override
	public int addRegisterInfo(InstitutionalUser com){
		//机构账号注册
		Person p = new Person();
		p.setUserId(com.getUserId());
		p.setInstitution(com.getInstitution());
		p.setLoginMode(Integer.parseInt(com.getLoginMode()));
		try{
			if(StringUtils.isNotBlank(com.getPassword())){				
				p.setPassword(PasswordHelper.encryptPassword(com.getPassword()));
			}else{
				p.setPassword(PasswordHelper.encryptPassword("666666"));
			}
		}catch (Exception e){
			e.printStackTrace();
		}
		p.setUsertype(2);
		if(StringUtils.isNotBlank(com.getAdminname())){
			p.setPid(com.getAdminname());
		}else if(StringUtils.isNotBlank(com.getAdminOldName())){
			p.setPid(com.getAdminOldName());
		}else{
			p.setPid("");
		}
		p.setIsFreeze(2);
		p.setRegistrationTime(DateUtil.getStringDate());
		return personMapper.addRegisterInfo(p);
	}

	@Override
	public int addRegisterAdmin(InstitutionalUser com){
		//机构管理员注册
		Person per = new Person();
		per.setUserId(com.getAdminname());
		try {
			per.setPassword(PasswordHelper.encryptPassword(com.getAdminpassword()));
			per.setLoginMode(1);
			per.setUsertype(1);
			per.setIsFreeze(2);
			per.setRegistrationTime(DateUtil.getStringDate());
			per.setInstitution(com.getInstitution());
			per.setAdminEmail(com.getAdminEmail());
			per.setAdminIsTrial(com.getAdminIsTrial().equals("isTrial")?"1":"0");
			SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd");
			per.setAdminBegintime(sd.parse(com.getAdminBegintime()));
			per.setAdminEndtime(sd.parse(com.getAdminEndtime()));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return personMapper.addRegisterAdmin(per);
	}

	@Override
	public int updateRegisterAdmin(InstitutionalUser com){
		//机构管理员注册
		Person per = new Person();
		if(!StringUtils.isEmpty(com.getAdminname())){
			per.setUserId(com.getAdminname());
		}else if(!StringUtils.isEmpty(com.getAdminOldName())){
			per.setUserId(com.getAdminOldName());
		}else{
			return 0;
		}
		try {
			per.setPassword(PasswordHelper.encryptPassword(com.getAdminpassword()));
			per.setInstitution(com.getInstitution());
			per.setAdminEmail(com.getAdminEmail());
			per.setAdminIsTrial(com.getAdminIsTrial().equals("isTrial")?"1":"0");
			SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd");
			per.setAdminBegintime(sd.parse(com.getAdminBegintime()));
			per.setAdminEndtime(sd.parse(com.getAdminEndtime()));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return personMapper.updateRegisterAdmin(per);
	}

	@Override
	public void addUserAdminIp(InstitutionalUser com){
		String[] arr_ip = com.getAdminIP().replace("\r\n", "\n").split("\n");
		int index=0;
		for(String ip : arr_ip){
			if(ip.contains("-")){
				UserIp userIp = new UserIp();
				userIp.setId(GetUuid.getId());
				if(!StringUtils.isEmpty(com.getAdminname())){
					userIp.setUserId(com.getAdminname());
				}else if(!StringUtils.isEmpty(com.getAdminOldName())){
					userIp.setUserId(com.getAdminOldName());
				}
				userIp.setBeginIpAddressNumber(IPConvertHelper.IPToNumber(ip.substring(0, ip.indexOf("-"))));
				userIp.setEndIpAddressNumber(IPConvertHelper.IPToNumber(ip.substring(ip.indexOf("-")+1, ip.length())));
				userIp.setSort(index++);
				userIpMapper.insert(userIp);
			}
		}
	}

	@Override
	public int deleteAccount(InstitutionalUser com, ResourceDetailedDTO dto, String adminId)
			throws Exception {

		SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd");
		UserAccount account = new UserAccount();
		account.setUserId(com.getUserId());
		account.setPayChannelId(dto.getProjectid());
		account.setOrganName(com.getInstitution());
		account.setBeginDateTime(sd.parse(dto.getValidityStarttime()));
		account.setEndDateTime(sd.parse(dto.getValidityEndtime()));

        List change = new ArrayList();
		boolean isSuccess = groupAccountUtil.deleteAccount(account, httpRequest.getRemoteAddr(),adminId,change,dto.getMode());
		int flag = 0;
		if (isSuccess) {
			flag = 1;
		} else {
			flag = 0;
		}
		return flag;
	}
	@Override
	public Map<String,Object> deleteChangeAccount(InstitutionalUser com, String adminId)throws Exception {
		//转换前的数据
		Map<String,Object> changeFront = new HashMap<>();
			String channelId=com.getChangeFront();
		Date beginDateTime=null;
		Date endDateTime=null;
		if ("GBalanceLimit".equals(channelId)) {
			wfks.accounting.handler.entity.BalanceLimitAccount infer = (wfks.accounting.handler.entity.BalanceLimitAccount)
					accountDao.get(new AccountId(channelId,com.getUserId()), new HashMap<String,String>());
			if(infer==null){
				changeFront.put("isSuccess",0);
				return changeFront;
			}
			beginDateTime=infer.getBeginDateTime();
			endDateTime=infer.getEndDateTime();
			changeFront.put("beginDateTime",beginDateTime);
			changeFront.put("endDateTime",endDateTime);
			changeFront.put("balance",infer.getBalance());
		} else if ("GTimeLimit".equals(channelId)) {
			wfks.accounting.handler.entity.TimeLimitAccount infer = (wfks.accounting.handler.entity.TimeLimitAccount)
					accountDao.get(new AccountId(channelId,com.getUserId()), new HashMap<String,String>());
			if(infer==null){
				changeFront.put("isSuccess",0);
				return changeFront;
			}
			beginDateTime=infer.getBeginDateTime();
			endDateTime=infer.getEndDateTime();
			changeFront.put("beginDateTime",beginDateTime);
			changeFront.put("endDateTime",endDateTime);
		}
		UserAccount account = new UserAccount();
		account.setUserId(com.getUserId());
		account.setPayChannelId(channelId);
		account.setOrganName(com.getInstitution());
		account.setBeginDateTime(beginDateTime);
		account.setEndDateTime(endDateTime);
		boolean isSuccess = groupAccountUtil.deleteAccount(account, httpRequest.getRemoteAddr(),adminId,new ArrayList<String>(),com.getRdlist().get(0).getMode());
		changeFront.put("isSuccess",isSuccess?1:0);
		return changeFront;
	}

	@Override
	public int addProjectDeadline(InstitutionalUser com, ResourceDetailedDTO dto, String adminId,Map<String,Object> changeFront){
		int flag = 0;
		boolean isChange = (dto.getBeforeMode()!=null && !dto.getBeforeMode().equals(dto.getMode())) ||
				(dto.getBeforeMode() == null && StringUtils.isNotEmpty(dto.getMode()));
		try{
			if (StringUtils.equals(dto.getValidityStarttime(), dto.getValidityStarttime2())
					&& StringUtils.equals(dto.getValidityEndtime(), dto.getValidityEndtime2())
					&& StringUtils.isEmpty(com.getChangeFront())
					&& !isChange) {
				return 1;
			}

			// 创建一个限时账户
			TimeLimitAccount account = new TimeLimitAccount();
			account.setUserId(com.getUserId());// 机构用户名
			account.setOrganName(com.getInstitution());// 机构名称
			account.setPayChannelId(dto.getProjectid());// 支付渠道id
			SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd");
			account.setBeginDateTime(sd.parse(dto.getValidityStarttime()));
			account.setEndDateTime(sd.parse(dto.getValidityEndtime()));// 失效时间，可以精确到秒
			// 根据token可获取管理员登录信息
			// String authToken = "Admin."+adminId;
			// 调用添加注册余额限时账户方法
			// 第二个参数起传递账户信息,userIP,auto_token,是否重置金额
            List<String> change = new ArrayList<>();

			boolean isAdd  = getAccount(account.getPayChannelId(), account.getUserId()) == null ? false : true;
			if(isChange && isAdd){
				changeFront.put("valStartTime",dto.getValidityStarttime2());
				changeFront.put("valEndTime",dto.getValidityEndtime2());
                if("trical".equals(dto.getMode())){
                    change.add(OLD_FORMAL);
                }else{
                    change.add(OLD_TRICAL);
                }
            }
            if(StringUtils.isNotEmpty(com.getChangeFront()) && ("GTimeLimit".equals(dto.getProjectid()) || "GBalanceLimit".equals(dto.getProjectid()))){
                change.add(OLD_BALAB);
            }
            //提交注册或充值请求
            boolean	isSuccess = groupAccountUtil.addTimeLimitAccount(account, httpRequest.getRemoteAddr(), adminId,change,changeFront,dto.getMode());
			if (isSuccess) {
				flag = 1;
			} else {
				flag = 0;
			}
		}catch(Exception e){
			log.error("添加时异常：",e);
		}
		return flag;
	}

	/**
	 * 为按次数计费用户充值(更新项目)
	 *
	 * @throws Exception
	 */
	@Override
	public int chargeCountLimitUser(InstitutionalUser com, ResourceDetailedDTO dto, String adminId){
		int flag = 0;
		boolean isChange = (dto.getBeforeMode()!=null && !dto.getBeforeMode().equals(dto.getMode())) ||
				(dto.getBeforeMode() == null && StringUtils.isNotEmpty(dto.getMode()));
		try{
			if(NumberUtils.toInt(dto.getPurchaseNumber())==0&&StringUtils.equals(dto.getValidityStarttime(), dto.getValidityStarttime2())
					&& StringUtils.equals(dto.getValidityEndtime(), dto.getValidityEndtime2())
					&& !isChange){
				return 1;
			}
			// 需要更新的数据
			CountLimitAccount count = new CountLimitAccount();
			count.setUserId(com.getUserId());// 机构用户名
			count.setOrganName(com.getInstitution());// 机构名称
			count.setPayChannelId(dto.getProjectid());// 支付渠道id
			SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd");
			count.setBeginDateTime(sd.parse(dto.getValidityStarttime()));
			count.setEndDateTime(sd.parse(dto.getValidityEndtime()));
			count.setBalance(NumberUtils.toInt(dto.getPurchaseNumber()));
			// 是否重置次数
			boolean resetCount = false;
			CountLimitAccount before = null;
			if (StringUtils.isNotBlank(com.getResetCount())) {
				wfks.accounting.handler.entity.CountLimitAccount oldNum = (wfks.accounting.handler.entity.CountLimitAccount)
						accountDao.get(new AccountId(dto.getProjectid(),com.getUserId()), new HashMap<String,String>());
				if(oldNum!=null){
					//更新前的数据
					before = new CountLimitAccount();
					before.setUserId(com.getUserId());//机构用户名
					before.setOrganName(com.getInstitution());//机构名称
					before.setPayChannelId(oldNum.getPayChannelId());//支付渠道id
					before.setBeginDateTime(oldNum.getBeginDateTime());//生效时间，可以精确到秒
					before.setEndDateTime(oldNum.getEndDateTime());//失效时间，可以精确到秒
					before.setBalance(oldNum.getBalance());//充值次数
					resetCount = true;
				}
			}
			Map<String,Object> changeFront = new HashMap<>();
			List<String> change = new ArrayList<>();
			boolean isAdd  = getAccount(dto.getProjectid(), com.getUserId()) == null ? false : true;
			if(isChange && isAdd){
				if(StringUtils.isNotEmpty(dto.getBeforePurchaseNumber())){
					changeFront.put("beforePurchaseNumber",dto.getBeforePurchaseNumber());
				}else {
					changeFront.put("beforePurchaseNumber",0);
				}
				changeFront.put("valStartTime",dto.getValidityStarttime2());
				changeFront.put("valEndTime",dto.getValidityEndtime2());
                if("trical".equals(dto.getMode())){
                    change.add(OLD_FORMAL);
                }else{
                    change.add(OLD_TRICAL);
                }
            }
			boolean isSuccess = groupAccountUtil.addCountLimitAccount(before, count, httpRequest.getRemoteAddr(), adminId,change, resetCount,changeFront,dto.getMode());
			if (isSuccess) {
				flag = 1;
			} else {
				flag = 0;
			}
		}catch(Exception e){
			log.error("修改限次异常：",e);
		}
		return flag;
	}

	/**
	 * 为机构余额账户充值
	 */
	@Override
	public int chargeProjectBalance(InstitutionalUser com, ResourceDetailedDTO dto, String adminId,Map<String,Object> changeFront){
		boolean isChange = (dto.getBeforeMode()!=null && !dto.getBeforeMode().equals(dto.getMode())) ||
				(dto.getBeforeMode() == null && StringUtils.isNotEmpty(dto.getMode()));
		if (NumberUtils.toDouble(dto.getTotalMoney()) == 0&&StringUtils.isEmpty(com.getChangeFront())
				&& StringUtils.equals(dto.getValidityStarttime(), dto.getValidityStarttime2())
				&& StringUtils.equals(dto.getValidityEndtime(), dto.getValidityEndtime2())
				&& !isChange) {
			return 1;
		}
		int flag = 0;
		try{
			// 需要更新的数据
			BalanceLimitAccount account = new BalanceLimitAccount();
			account.setUserId(com.getUserId());// 机构用户名
			account.setOrganName(com.getInstitution());// 机构名称
			account.setPayChannelId(dto.getProjectid());// 支付渠道id
			SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd");
			account.setBeginDateTime(sd.parse(dto.getValidityStarttime()));
			account.setEndDateTime(sd.parse(dto.getValidityEndtime()));
			account.setBalance(BigDecimal.valueOf(NumberUtils.toDouble(dto.getTotalMoney())));
			// 根据token可获取管理员登录信息
			// String authToken = "Admin."+adminId;
			// 调用注册或充值余额限时账户方法
			// 第一个参数如果是充值，就传入充值前的账户信息，如果是注册就传入null
			// 第二个参数起传递账户信息，userIP，auto_token
			// 是否重置金额
			boolean resetMoney = false;
			BalanceLimitAccount before = null;
			if (StringUtils.isNotBlank(com.getResetMoney())) {
				wfks.accounting.handler.entity.BalanceLimitAccount oldBlance = (wfks.accounting.handler.entity.BalanceLimitAccount)
						accountDao.get(new AccountId(dto.getProjectid(),com.getUserId()), new HashMap<String,String>());
				if(oldBlance!=null){
					//更新前信息
					before = new BalanceLimitAccount();
					before.setUserId(com.getUserId());//机构用户名
					before.setOrganName(com.getInstitution());//机构名称
					before.setPayChannelId(oldBlance.getPayChannelId());//支付渠道id
					before.setBeginDateTime(oldBlance.getBeginDateTime());
					before.setEndDateTime(oldBlance.getEndDateTime());
					before.setBalance(oldBlance.getBalance());
					resetMoney = true;
				}
			}
			//检测是否存在正式试用转化
            List<String> change = new ArrayList<>();
			boolean isAdd  = getAccount(account.getPayChannelId(), account.getUserId()) == null ? false : true;
            if(isChange && isAdd){
            	if(StringUtils.isNotEmpty(dto.getBeforeTotalMoney())){
					changeFront.put("beforeTotalMoney",dto.getBeforeTotalMoney());
				}else {
					changeFront.put("beforeTotalMoney",0);
				}
				changeFront.put("valStartTime",dto.getValidityStarttime2());
				changeFront.put("valEndTime",dto.getValidityEndtime2());
                if("trical".equals(dto.getMode())){
                    change.add(OLD_FORMAL);
                }else{
                    change.add(OLD_TRICAL);
                }
            }
            if(StringUtils.isNotEmpty(com.getChangeFront()) && ("GTimeLimit".equals(dto.getProjectid()) || "GBalanceLimit".equals(dto.getProjectid()))){
                change.add(OLD_TIME);
            }
            boolean isSuccess = groupAccountUtil.addBalanceLimitAccount(before, account, httpRequest.getRemoteAddr(), adminId, resetMoney,change,changeFront,dto.getMode());
			if (isSuccess) {
				flag = 1;
				log.info("id为："+com.getUserId()+" 的用户，购买项目:"+dto.getProjectname()+"  充值成功！");
			} else {
				flag = 0;
				log.info("id为："+com.getUserId()+" 的用户，购买项目:"+dto.getProjectname()+"  充值失败！");
			}
		}catch(Exception e){
			log.error("异常：",e);
		}
		return flag;
	}

	public Account getAccount(String payChannelId, String user_id) {
		try {
			AccountId id = new AccountId(payChannelId, user_id);
			Account account = accountDao.get(id, null);
			return account;
		} catch (Exception e) {
			log.error("根据user_id获取机构账户失败", e);
			throw e;
		}

	}

	@Override
	public Double checkValue(InstitutionalUser com,ResourceDetailedDTO dto) throws Exception{
		try{
			if ("balance".equals(dto.getProjectType())) {
				wfks.accounting.handler.entity.BalanceLimitAccount account = (wfks.accounting.handler.entity.BalanceLimitAccount)
						accountDao.get(new AccountId(dto.getProjectid(),com.getUserId()), new HashMap<String,String>());
				if(account==null){
					return -Double.MAX_VALUE;
				}
				return account.getBalance().doubleValue()+NumberUtils.toDouble(dto.getTotalMoney());
			} else if ("count".equals(dto.getProjectType())) {
				wfks.accounting.handler.entity.CountLimitAccount account = (wfks.accounting.handler.entity.CountLimitAccount)
						accountDao.get(new AccountId(dto.getProjectid(),com.getUserId()), new HashMap<String,String>());
				if(account==null){
					return -Double.MAX_VALUE;
				}
				return NumberUtils.toDouble(dto.getPurchaseNumber())+account.getBalance();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return -Double.MAX_VALUE;
	}


	/**
	 * 添加项目资源信息
	 * @如后期无扩展此方法可以与updateProjectResources方法合并优化
	 * */
	@Override
	public void addProjectResources(InstitutionalUser com,ResourceDetailedDTO dto){
		List<ResourceLimitsDTO> list = dto.getRldto();
		if(list!=null){
			delUserSetting(dto,com);
			for(ResourceLimitsDTO rlDTO : list){
				if(rlDTO!=null && StringUtils.isNotBlank(rlDTO.getResourceid())){
					if(rlDTO.getResourceid().equals("DB_PEDB")){
						ProjectResources pr1 = new ProjectResources();
						pr1.setId(GetUuid.getId());
						pr1.setUserId(com.getUserId());
						pr1.setProjectId(dto.getProjectid());
						pr1.setResourceId("DB_CSPD");
						if(rlDTO.getProductid()!=null&&rlDTO.getProductid().length>0){					
							pr1.setProductid(Arrays.toString(new String[]{"Income.PeriodicalFulltext"}));//rlDTO.getProductid()
						}
						projectResourcesMapper.insert(pr1);
						addUserSetting(dto,rlDTO,com);
						
						ProjectResources pr2 = new ProjectResources();
						pr2.setId(GetUuid.getId());
						pr2.setUserId(com.getUserId());
						pr2.setProjectId(dto.getProjectid());
						pr2.setResourceId("DB.IsticPeriodical");
						if(rlDTO.getProductid()!=null&&rlDTO.getProductid().length>0){					
							pr2.setProductid(Arrays.toString(new String[]{"Income.IsticPeriodical"}));//rlDTO.getProductid()
						}
						projectResourcesMapper.insert(pr2);
						addUserSetting(dto,rlDTO,com);
						continue;
					}
					ProjectResources pr = new ProjectResources();
					pr.setId(GetUuid.getId());
					pr.setUserId(com.getUserId());
					pr.setProjectId(dto.getProjectid());
					pr.setResourceId(rlDTO.getResourceid());
					String field=getField(rlDTO);
					if(field!=null){				
						pr.setContract(field);
					}
					if(rlDTO.getProductid()!=null&&rlDTO.getProductid().length>0){					
						pr.setProductid(Arrays.toString(rlDTO.getProductid()));//rlDTO.getProductid()
					}
					projectResourcesMapper.insert(pr);
					addUserSetting(dto,rlDTO,com);
				}
			}
		}else{
			log.error("user:"+com.getUserId()+",projectId:"+dto.getProjectid()+"没有查询到产品信息");
			ProjectResources pr = new ProjectResources();
			pr.setId(GetUuid.getId());
			pr.setUserId(com.getUserId());
			pr.setProjectId(dto.getProjectid());
			projectResourcesMapper.insert(pr);
		}
		WfksAccountidMapping am = new WfksAccountidMapping();
		if(dto.getProjectid().equals("HistoryCheck")){
			if(dto.getRelatedIdAccountType().equals("ViewHistoryCheck")){
				am.setMappingid(GetUuid.getId());
				am.setIdAccounttype(dto.getProjectid());
				am.setIdKey(com.getUserId());
				am.setRelatedidKey(com.getUserId());
				am.setRelatedidAccounttype(dto.getRelatedIdAccountType());
				am.setBegintime(DateUtil.stringToDate1(dto.getValidityStarttime()));
				am.setEndtime(DateUtil.stringToDate1(dto.getValidityEndtime()));
				am.setLastUpdatetime(DateUtil.stringToDate(DateUtil.getStringDate()));
				wfksAccountidMappingMapper.insert(am);
			}
		}
	}

	/**
	 * 更新项目资源信息
	 * @如后期无扩展此方法可以与addProjectResources方法合并优化
	 * */
	@Override
	public void updateProjectResources(InstitutionalUser com,ResourceDetailedDTO dto){
		List<ResourceLimitsDTO> list = dto.getRldto();
		if(list!=null){
			delUserSetting(dto,com);
			for(ResourceLimitsDTO rlDTO : list){
				if(StringUtils.isNotBlank(rlDTO.getResourceid())){
					if(rlDTO.getResourceid().equals("DB_PEDB")){
						ProjectResources pr1 = new ProjectResources();
						pr1.setId(GetUuid.getId());
						pr1.setUserId(com.getUserId());
						pr1.setProjectId(dto.getProjectid());
						pr1.setResourceId("DB_CSPD");
						if(rlDTO.getProductid()!=null&&rlDTO.getProductid().length>0){					
							pr1.setProductid(Arrays.toString(new String[]{"Income.PeriodicalFulltext"}));//rlDTO.getProductid()
						}
						projectResourcesMapper.insert(pr1);
						addUserSetting(dto,rlDTO,com);
						
						ProjectResources pr2 = new ProjectResources();
						pr2.setId(GetUuid.getId());
						pr2.setUserId(com.getUserId());
						pr2.setProjectId(dto.getProjectid());
						pr2.setResourceId("DB.IsticPeriodical");
						if(rlDTO.getProductid()!=null&&rlDTO.getProductid().length>0){					
							pr2.setProductid(Arrays.toString(new String[]{"Income.IsticPeriodical"}));//rlDTO.getProductid()
						}
						projectResourcesMapper.insert(pr2);
						addUserSetting(dto,rlDTO,com);
						continue;
					}
					ProjectResources pr = new ProjectResources();
					pr.setId(GetUuid.getId());
					pr.setUserId(com.getUserId());
					pr.setProjectId(dto.getProjectid());
					pr.setResourceId(rlDTO.getResourceid());
					String field=getField(rlDTO);
					if(field!=null){				
						pr.setContract(field);
					}
					if(rlDTO.getProductid()!=null&&rlDTO.getProductid().length>0){					
						pr.setProductid(Arrays.toString(rlDTO.getProductid()));//rlDTO.getProductid()
					}
					projectResourcesMapper.insert(pr);
					addUserSetting(dto,rlDTO,com);
				}
			}
		}else{
			log.error("user:"+com.getUserId()+",projectId:"+dto.getProjectid()+"没有查询到产品信息");
			ProjectResources prs = new ProjectResources();
			prs.setId(GetUuid.getId());
			prs.setUserId(com.getUserId());
			prs.setProjectId(dto.getProjectid());
			prs.setResourceId(null);
			prs.setContract(null);
			prs.setProductid(null);
			projectResourcesMapper.insert(prs);
		}
		if(dto.getProjectid().equals("HistoryCheck")){
			wfksAccountidMappingMapper.deleteByUserId(com.getUserId(),"ViewHistoryCheck");
			if(dto.getRelatedIdAccountType().equals("ViewHistoryCheck")){
				WfksAccountidMapping am = new WfksAccountidMapping();
				am.setMappingid(GetUuid.getId());
				am.setIdAccounttype("HistoryCheck");
				am.setIdKey(com.getUserId());
				am.setRelatedidKey(com.getUserId());
				am.setRelatedidAccounttype(dto.getRelatedIdAccountType());
				am.setBegintime(DateUtil.stringToDate1(dto.getValidityStarttime()));
				am.setEndtime(DateUtil.stringToDate1(dto.getValidityEndtime()));
				am.setLastUpdatetime(DateUtil.stringToDate(DateUtil.getStringDate()));
				wfksAccountidMappingMapper.insert(am);
			}
		}
	}

	/**
	 * 删除购买详情（权限）
	 * @return 
	 * */
	@Override
	public void deleteResources(InstitutionalUser com, ResourceDetailedDTO dto,boolean b){		
		ProjectResources p = new ProjectResources();
		p.setUserId(com.getUserId());
		if(!b){			
			p.setProjectId(dto.getProjectid());
		}
		projectResourcesMapper.deleteResources(p);
		if(dto.getProjectid().equals("HistoryCheck")){
			wfksAccountidMappingMapper.deleteByUserId(com.getUserId(),"ViewHistoryCheck");
		}
	}

	/**
	 * 删除购买详情（权限）
	 * @return 
	 * */
	@Override
	public void deleteResources(String userId,String projectId){		
		ProjectResources p = new ProjectResources();
		p.setUserId(userId);
		p.setProjectId(projectId);
		projectResourcesMapper.deleteResources(p);
	}

	/**
	 * 添加标准配置参数
	 * @param rdto
	 * @param com
	 */
	private void addUserSetting(ResourceDetailedDTO detail,ResourceLimitsDTO rdto, InstitutionalUser com) {
		if (STANDARD.equals(rdto.getResourceid())) {
			com.alibaba.fastjson.JSONObject obj = getStandard(rdto, com);
			//查询数据库，验证标准机构是否存在
			StandardUnit unit=standardUnitMapper.getStandardUnitById(com.getUserId());
			if (obj != null) {
				if(obj.getBooleanValue("isZJ")){//元数据+全文的才调用接口
					//如果存在，接口更新，数据库更新。如果不存在，接口注册，数据库新增
					String codeNum=standardUnitMapper.findMaxOrgCode();
					//code是2位平台字母，8位数字，测试库和生产的前两位字母要不一样
					if (codeNum == null) {
						codeNum = ORGCODE;
					} else {
						codeNum = StringUtil.formatStr(codeNum);
					}
					obj.put("OrgCode", codeNum);
					if (unit == null) {// 注册
						unit=new StandardUnit();
						unit.setUserId(com.getUserId());
						unit.setOrgCode(codeNum);
						unit.setCompany(rdto.getCompany());
						unit.setCompanySimp(rdto.getCompanySimp());
						unit.setOrgName(SALEAGTID+"_"+rdto.getOrgName());
						if(rdto.getFullIpRange()!=null){
							unit.setiPLimits(StringUtils.join(rdto.getFullIpRange(), ";"));
						}
						String msg=HttpClientUtil.orgReg(unit);
						JSONArray array =JSONArray.fromObject(msg);
						if(array!=null){
							JSONObject json=array.getJSONObject(0);
							if("0".equals(json.getString("errorCode"))){
								standardUnitMapper.insertStandardUnit(unit);
							}else{
								log.info("userId:"+unit.getUserId()+"注册标准账户失败");
							}
						}
					}else{//更新
						unit.setOrgName(SALEAGTID+"_"+rdto.getOrgName());
						unit.setCompany(rdto.getCompany());
						unit.setCompanySimp(rdto.getCompanySimp());
						if(rdto.getFullIpRange()!=null){
							unit.setiPLimits(StringUtils.join(rdto.getFullIpRange(), ";"));
						}
						String msg=HttpClientUtil.orgEdit(unit);
						JSONArray array =JSONArray.fromObject(msg);
						if(array!=null){
							JSONObject json=array.getJSONObject(0);
							if("0".equals(json.getString("errorCode"))){
								standardUnitMapper.updateStandardUnit(unit);
							}else{
								log.info("userId:"+unit.getUserId()+"更新标准账户失败");
							}
						}
					}
				}else{//网络包库调用接口
					//					int msg=WebServiceUtils.CreateNonAccountingUser(obj, 1);
					//					if(msg==1){
					//						log.info(com.getUserId()+"包库接口调用成功");
					//					}else{
					//						log.info(com.getUserId()+"包库更新失败");
					//					}
				}
				//wfks_user_setting表添加标准配置参数
				WfksUserSetting setting=new WfksUserSetting();
				setting.setUserType(detail.getProjectid());
				setting.setUserId(com.getUserId());
				setting.setPropertyName(STANDARD_CODE);
				setting.setPropertyValue(JSON.toJSONString(obj, SerializerFeature.WriteMapNullValue));
				wfksUserSettingMapper.insert(setting);
			}
		}
	}

	/**
	 * 删除标准配置删除
	 * @param detail
	 * @param com
	 */
	private void delUserSetting(ResourceDetailedDTO detail, InstitutionalUser com){
		// 先删除再添加
		WfksUserSettingKey key=new WfksUserSettingKey();
		key.setUserType(detail.getProjectid());
		key.setUserId(com.getUserId());
		key.setPropertyName(STANDARD_CODE);
		wfksUserSettingMapper.deleteByUserId(key);
	}


	public static String getField(ResourceLimitsDTO dto){
		JSONObject obj = new JSONObject();
		JSONArray Terms	= new JSONArray();//Terms
		if(dto.getPerioInfoClc()!=null && !dto.getPerioInfoClc().equals("") && !dto.getPerioInfoClc().equals(",")){
			addStringToTerms("perioInfo_CLC", "In", dto.getPerioInfoClc(), Terms, "String[]");
		}
		if(dto.getJournalClc()!=null && !dto.getJournalClc().equals("") && !dto.getJournalClc().equals(",")){
			addStringToTerms("journal_CLC", "In", dto.getJournalClc(), Terms, "String[]");
		}
		if(dto.getDegreeClc()!=null && !dto.getDegreeClc().equals("") && !dto.getDegreeClc().equals(",")){
			addStringToTerms("degree_CLC", "In", dto.getDegreeClc(), Terms, "String[]");
		}
		if(dto.getBooksClc()!=null && !dto.getBooksClc().equals("") && !dto.getBooksClc().equals(",")){
			addStringToTerms("books_CLC", "In", dto.getBooksClc(), Terms, "String[]");
		}
		if(dto.getConferenceClc()!=null && !dto.getConferenceClc().equals("") && !dto.getConferenceClc().equals(",")){
			addStringToTerms("conference_CLC", "In", dto.getConferenceClc(), Terms, "String[]");
		}
		if(dto.getPatentIpc()!=null && !dto.getPatentIpc().equals("") && !dto.getPatentIpc().equals(",") ){
			addStringToTerms("patent_IPC", "In", dto.getPatentIpc(), Terms, "String[]");
		}
		if(dto.getJournalIdno()!=null && !dto.getJournalIdno().equals("") && !dto.getJournalIdno().equals(",")){
			addStringToTerms("journal_IDNo","In",dto.getJournalIdno(),Terms,"String[]");
		}
		addTimeToTerms("journal_time",dto.getJournal_startTime(), dto.getJournal_endTime(),Terms);
		if(dto.getDegreeTypes()!=null){
			addStringToTerms("degree_types", "In", Arrays.toString(dto.getDegreeTypes()), Terms, "String[]");
		}
		addTimeToTerms("degree_time", dto.getDegreeStarttime(), dto.getDegreeEndtime(),Terms);
		if(dto.getConferenceNo()!=null && !dto.getConferenceNo().equals("") && !dto.getConferenceNo().equals(",")){
			addStringToTerms("conference_No","In",dto.getConferenceNo(),Terms,"String[]");
		}
		if(dto.getBooksIdno()!=null && !dto.getBooksIdno().equals("") && !dto.getBooksIdno().equals(",")){
			addStringToTerms("books_IDNo","In",dto.getBooksIdno(),Terms,"String[]");
		}
		//处理标准配置
		if (STANDARD.equals(dto.getResourceid())) {
			formatStandard(dto,Terms);
		}

		String gId = formatId(dto.getGazetteersId());
		String itemId = formatId(dto.getItemId());
		String gNType = dto.getGazetteersType();
		String gArea = dto.getGazetteersArea();
		String gAlbum = dto.getGazetteersAlbum();
		String gLevel = dto.getGazetteersLevel();
		String gStartTime=dto.getGazetteersStartTime();
		String gEndTime=dto.getGazetteersEndTime();
		String gOType=dto.getGazetteersOldType();
		String gOArea=dto.getGazetteersOldArea();
		String gOStartTime=dto.getGazetteersOldStartTime();
		String gOEndTime=dto.getGazetteersOldEndTime();
		//专辑数据库   -- 文化志
		String albumDatabase=dto.getAlbumDatabase();
		String gType=null; 
		String localType=dto.getLocalType();
		if(StringUtils.isNoneEmpty(localType)){
			if(localType.contains("FZ_New")&&StringUtils.isEmpty(gNType)){
				gNType="FZ_New";
			}
			if(localType.contains("FZ_Old")&&StringUtils.isEmpty(gOType)){
				gOType="FZ_Old";
			}
		}
		if(StringUtils.isBlank(gNType)){
			gStartTime=null;
			gEndTime=null;
			gArea=null;
			gAlbum=null;
		}
		if(StringUtils.isBlank(gOType)){
			gOStartTime=null;
			gOEndTime=null;
			gOArea=null;
		}
		int igst=StringUtils.isBlank(gStartTime)?0:Integer.parseInt(gStartTime);
		int iget=StringUtils.isBlank(gEndTime)?0:Integer.parseInt(gEndTime);
		int igost=StringUtils.isBlank(gOStartTime)?0:Integer.parseInt(gOStartTime);
		int igoet=StringUtils.isBlank(gOEndTime)?0:Integer.parseInt(gOEndTime);
		if(igst > 0 && iget > 0 && (igst > iget)){
			return null;
		}
		if(igost > 0 && igoet > 0 && (igost>igoet)){
			return null;
		}
		if(StringUtils.isEmpty(gNType)||StringUtils.isEmpty(gOType)||StringUtils.isEmpty(albumDatabase)){
			if(StringUtils.isNotEmpty(gNType)){
				gType=gNType;
			}
			if(StringUtils.isNotEmpty(gOType)&&StringUtils.isNotEmpty(gType)){
				gType=gType+","+gOType;
			}else if(StringUtils.isNotEmpty(gOType)&&StringUtils.isEmpty(gType)){
				gType=gOType;
			}
			
			if(StringUtils.isNotEmpty(albumDatabase)&&StringUtils.isNotEmpty(gType)){
				gType=gType+","+albumDatabase;
			}else if(StringUtils.isNotEmpty(albumDatabase)&&StringUtils.isEmpty(gType)){
				gType=albumDatabase;
			}
		}
		if (StringUtils.isNotEmpty(gId) || StringUtils.isNotEmpty(itemId)
				|| StringUtils.isNotEmpty(gArea) || StringUtils.isNotEmpty(gAlbum)
				|| StringUtils.isNotEmpty(gLevel)|| StringUtils.isNotEmpty(gStartTime)
				|| StringUtils.isNotEmpty(gEndTime)|| StringUtils.isNotEmpty(gOStartTime)
				|| StringUtils.isNotEmpty(gOEndTime)||StringUtils.isNotEmpty(gOArea)
				|| StringUtils.isNotEmpty(gType)) {
			if (StringUtils.isNotEmpty(gId) || StringUtils.isNotEmpty(itemId)) {
				if (StringUtils.isNotEmpty(gId)) {
					addStringToTerms("gazetteers_id", "Equal", gId, Terms, "String");
				}
				if (StringUtils.isNotEmpty(itemId)) {
					addStringToTerms("item_id", "Equal", itemId, Terms, "String");
				}
			} else {
				if (StringUtils.isNotEmpty(gAlbum)) {
					addStringToTerms("gazetteers_album", "Equal", gAlbum, Terms, "String");
				}
				if (StringUtils.isNotEmpty(gArea)) {
					addStringToTerms("gazetteers_area", "Equal", gArea, Terms, "String");
				}

				if (StringUtils.isNotEmpty(gType)) {
					addStringToTerms("gazetteers_type", "Equal", gType, Terms, "String");
				}
				if (StringUtils.isNotEmpty(gLevel)&&(StringUtils.isNotEmpty(gArea)||StringUtils.isNotEmpty(gAlbum)||StringUtils.isNotEmpty(gNType))) {
					addStringToTerms("gazetteers_level", "Equal", gLevel, Terms, "String");
				}
				if (StringUtils.isNotEmpty(gStartTime)) {
					addStringToTerms("gazetteers_startTime", "Equal", gStartTime, Terms, "String");
				}
				if (StringUtils.isNotEmpty(gEndTime)) {
					addStringToTerms("gazetteers_endTime", "Equal", gEndTime, Terms, "String");
				}
				if (StringUtils.isNotEmpty(gOArea)) {
					addStringToTerms("gazetteers_old_area", "Equal", gOArea, Terms, "String");
				}
				if (StringUtils.isNotEmpty(gOStartTime)) {
					addStringToTerms("gazetteers_old_startTime", "Equal", gOStartTime, Terms, "String");
				}
				if (StringUtils.isNotEmpty(gOEndTime)) {
					addStringToTerms("gazetteers_old_endTime", "Equal", gOEndTime, Terms, "String");
				}
			}
		}

		if(Terms.size()>0){
			obj.put("Terms", Terms);
			return obj.toString();
		}
		return null;
	}

	/**
	 * 处理标准配置
	 * @param dto
	 * @param Terms
	 */
	private static void formatStandard(ResourceLimitsDTO dto,JSONArray Terms){
		String standardtypes = dto.getStandardTypes()==null?"":Arrays.toString(dto.getStandardTypes());
		if(StringUtils.isNoneBlank(standardtypes)){
			addStringToTerms("standard_types", "In", standardtypes, Terms, "String[]");
			if(standardtypes.contains("质检出版社")){
				if(dto.getFullIpRange()!=null && !dto.getFullIpRange().equals("")){
					String FullIpRange=dto.getFullIpRange()==null?"":Arrays.toString(dto.getFullIpRange());
					Pattern p2 = Pattern.compile("[^0-9.-]");
					Matcher m2 = p2.matcher(FullIpRange);
					FullIpRange = m2.replaceAll(" ").trim();
					FullIpRange=FullIpRange.replaceAll("  ", " ");
					if(StringUtils.isNotBlank(FullIpRange)){
						addStringToTerms("full_IP_range","In",Arrays.toString(FullIpRange.split(" ")),Terms,"String[]");
					}
				}
				if(dto.getLimitedParcelStarttime()!=null && !dto.getLimitedParcelStarttime().equals("")){
					addTimeToTerms("limited_parcel_time",dto.getLimitedParcelStarttime(),dto.getLimitedParcelEndtime(),Terms);
				}

				if(dto.getOrgName()!=null && !dto.getOrgName().equals("")){
					addStringToTerms("org_name","Equal",dto.getOrgName(),Terms,"String");
					if(dto.getCompany()!=null && !dto.getCompany().equals("")){
						addStringToTerms("company","Equal",dto.getCompany(),Terms,"String");
					}
					if(dto.getCompanySimp()!=null && !dto.getCompanySimp().equals("")){
						addStringToTerms("company_simp","Equal",dto.getCompanySimp(),Terms,"String");
					}
				}else{
					if(dto.getReadingPrint()!=null&& !dto.getReadingPrint().equals("")){
						addStringToTerms("reading_print","Equal",dto.getReadingPrint().toString(),Terms,"String");
					}
					if(dto.getOnlineVisitor()!=null && !dto.getOnlineVisitor().equals("")){
						addStringToTerms("online_visitor","Equal",dto.getOnlineVisitor().toString(),Terms,"String");
					}
					if(dto.getCopyNo()!=null && !dto.getCopyNo().equals("")){
						addStringToTerms("copy_No","Equal",dto.getCopyNo().toString(),Terms,"String");
					}
					if(dto.getTotalPrintNo()!=null && !dto.getTotalPrintNo().equals("")){
						addStringToTerms("total_print_No","Equal",dto.getTotalPrintNo().toString(),Terms,"String");
					}
					if(dto.getSinglePrintNo()!=null && !dto.getSinglePrintNo().equals("")){
						addStringToTerms("single_print_No","Equal",dto.getSinglePrintNo().toString(),Terms,"String");
					}
				}
			}
		}
	}

	/**
	 * 获取标准对象
	 * @param dto
	 * @param com
	 */
	private static com.alibaba.fastjson.JSONObject getStandard(ResourceLimitsDTO dto,InstitutionalUser com){
		com.alibaba.fastjson.JSONObject obj=null;
		String standardtypes = dto.getStandardTypes()==null?"":Arrays.toString(dto.getStandardTypes());
		if(standardtypes.contains("质检出版社")){
			obj=new com.alibaba.fastjson.JSONObject();
			obj.put("UserId", com.getUserId());
			obj.put("Username", com.getInstitution());
			obj.put("UserEnName", com.getUserId());
			List<String> list=null;
			if(dto.getFullIpRange()!=null && !dto.getFullIpRange().equals("")){
				String FullIpRange=dto.getFullIpRange()==null?"":Arrays.toString(dto.getFullIpRange());
				Pattern p2 = Pattern.compile("[^0-9.-]");
				Matcher m2 = p2.matcher(FullIpRange);
				FullIpRange = m2.replaceAll(" ").trim();
				FullIpRange=FullIpRange.replaceAll("  ", " ");
				if(!StringUtils.isEmpty(FullIpRange)){
					list=Arrays.asList(FullIpRange.split(" "));
					dto.setFullIpRange(FullIpRange.split(" "));
				}
			}
			if(dto.getOrgName()!=null && !dto.getOrgName().equals("")){
				obj.put("isZJ", true);
				obj.put("StartTime", dto.getLimitedParcelStarttime()+"T00:00:00");
				obj.put("EndTime", dto.getLimitedParcelEndtime()+"T00:00:00");
				obj.put("OrgName", SALEAGTID+"_"+dto.getOrgName());
				obj.put("OrgCode", null); //下一个方法将给OrgCode赋值
				obj.put("CompanySimp", dto.getCompanySimp());
				obj.put("IPLimits", list);
				obj.put("isBK", false);
				obj.put("BK_StartTime", null);
				obj.put("BK_EndTime", null);
				obj.put("BK_IPLimits", null);
				obj.put("Rdptauth", null);
				obj.put("Onlines", 0);
				obj.put("Copys", 0);
				obj.put("Prints", 0);
				obj.put("Sigprint", 0);		
			}else if(dto.getReadingPrint()!=null){
				obj.put("isZJ", false);
				obj.put("StartTime", null);
				obj.put("EndTime", null);
				obj.put("OrgName", null);
				obj.put("OrgCode", null);
				obj.put("CompanySimp", null);
				obj.put("IPLimits", null);
				obj.put("isBK", true);
				obj.put("BK_StartTime", dto.getLimitedParcelStarttime()+"T00:00:00");
				obj.put("BK_EndTime", dto.getLimitedParcelEndtime()+"T00:00:00");
				obj.put("BK_IPLimits", list);
				obj.put("Rdptauth", dto.getReadingPrint().toString());
				obj.put("Onlines", dto.getOnlineVisitor());
				obj.put("Copys", dto.getCopyNo());
				obj.put("Prints", dto.getTotalPrintNo());
				obj.put("Sigprint", dto.getSinglePrintNo());
			}
		}
		return obj;
	}

	private static String formatId(String ids){
		if (ids == null || "".equals(ids)) {
			return null;
		}
		// 正则去除特殊字符
		Pattern p = Pattern.compile("[^0-9a-zA-Z_]");
		Matcher m = p.matcher(ids);
		ids = m.replaceAll(" ").trim();
		ids = ids.replaceAll(" +"," ").replaceAll(" ", ";");
		return ids;
	}

	private static void addStringToTerms(String Field, String Verb, String value, JSONArray Terms,String ValueType) {
		WfResourcesModel model = new WfResourcesModel();
		model.setField(Field);
		model.setVerb(Verb);
		model.setValueType(ValueType);
		model.setLogic("AND");
		if (Verb.equals("Equal")) {
			model.setValue(value);
		} else if (Verb.equals("In")) {
			value=value.replace("[", "").replace("]", "").replaceAll("，", ",");
			value=value.replaceAll("\"", "").replaceAll(" ", "").replaceAll("\r\n", ",");
			List<String> ls = new ArrayList<String>();
			String[] strs = value.split(",");
			for (String str : strs) {
				if (!StringUtils.isEmpty(str)) {
					ls.add(str);
				}
			}
			if (ls.size() > 0) {
				model.setValue(ls);
			}
		}
		if (model.getValue()!=null) {
			Terms.add(JSON.toJSONString(model,new PascalNameFilter()));
		}
	}

	private static void addTimeToTerms(String Field,String startTime,String endTime,JSONArray Terms){
		WfResourcesModel model = new WfResourcesModel();
		model.setField(Field);
		model.setValueType("DateTime[]");
		model.setLogic("AND");
		model.setVerb("WithIn");
		String[] str=new String[2];
		if(StringUtils.isBlank(startTime) && StringUtils.isNotBlank(endTime)){
			startTime="1900";
		}else if(StringUtils.isNotBlank(startTime) && StringUtils.isBlank(endTime)){
			endTime=DateUtil.getCurrentYear();
		}
		str[0] = startTime;
		str[1] = endTime;
		model.setValue(str);
		if (StringUtils.isNotBlank(startTime) && StringUtils.isNotBlank(endTime)) {
			Terms.add(JSON.toJSONString(model,new PascalNameFilter()));
		}
	}

	/**
	 *	读取Excel机构账号信息 
	 */
	@Override
	public List<Map<String, Object>> getExcelData(MultipartFile file,Map<String,Object> errorMap,List<Map<String,String>> errorList){
		//用户信息
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		String[] str = null;
		try{
			ExcelUtil.checkFile(file);
			//获得Workbook工作薄对象  
			Workbook workbook = ExcelUtil.getWorkBook(file);
			// 循环工作表Sheet
			Sheet sheet = workbook.getSheetAt(0);
			if(sheet != null){
				// 循环行Row
				for(int rowNum = 0; rowNum <= sheet.getLastRowNum(); rowNum++){
					Map<String,Object> map = new HashMap<String, Object>();
					Row row = sheet.getRow(rowNum);
					if(row != null){
						if(rowNum==0){
							str = ExcelUtil.readExcelTitle(row);
							if (str.length < 4) {
								errorMap.put("flag", "fail");
								errorMap.put("fail", "模版文件的列未按照规范排版，请下载标准的模版文件");
								break;
							}
							if(!"机构名称(必填)_institution".equals(str[0])){
								errorMap.put("flag", "fail");
								errorMap.put("fail", "机构名称列不存在或位置错误，请下载标准的模版文件");
								break;
							}else if(!"机构ID(必填)_userId".equals(str[1])){
								errorMap.put("flag", "fail");
								errorMap.put("fail", "机构ID列不存在或位置错误，请下载标准的模版文件");
								break;
							}else if(!"密码_password".equals(str[2])){
								errorMap.put("flag", "fail");
								errorMap.put("fail", "密码列不存在或位置错误，请下载标准的模版文件");
								break;
							}else if(!"账号IP段_ip".equals(str[3])){
								errorMap.put("flag", "fail");
								errorMap.put("fail", "账号IP段列不存在或位置错误，请下载标准的模版文件");
								break;
							}else{
								continue;
							}
						}
						map.put("institution", ExcelUtil.getValue(row.getCell(0)).trim());
						map.put("userId", ExcelUtil.getValue(row.getCell(1)).trim());
						map.put("password", ExcelUtil.getValue(row.getCell(2)).trim());
						//循环列Cell
						List<Map<String, String>> li = new ArrayList<Map<String, String>>();
						for(int i = 3; i < str.length; i++){
							Map<String,String> m = new HashMap<String, String>();
							String title = str[i].substring(str[i].indexOf("_") + 1,str[i].length());
							if ("IP".equals(title.toUpperCase())) {
								map.put("ip",ExcelUtil.getValue(row.getCell(i)).replace(" ", ""));
							} else {
								m.put("projectid", title);
								m.put("totalMoney", ExcelUtil.getValue(row.getCell(i)));
								li.add(m);
							}
						}
						boolean flag=false;
						Map<String, String> eMap=new HashMap<String,String>();
						for (Object v : map.values()) {
							if(!StringUtils.isEmpty((String)v)){
								flag=true;
							}
						}
						for(Map<String,String> m:li){
							if(!StringUtils.isEmpty(m.get("totalMoney"))){
								flag=true;
							}
						}
						if(flag&&StringUtils.isEmpty((String)map.get("institution"))&&StringUtils.isEmpty((String)map.get("userId"))){
							eMap.put("fail", "第"+(rowNum+1)+"行数据无机构名称和机构ID");
							errorList.add(eMap);
						}else if(!flag){
							continue;
						}else{
							map.put("projectList", li);
						}
					}else{
						System.out.println("Excel中某列为空");
					}
					if(map.size()>0){
						list.add(map);
					}
				}
			}else{
				System.out.println("无法找到sheet页");
			}
		}catch(IOException e){
			e.printStackTrace();
		}
		return list;
	}

	@Override
	public int updateUserInfo(InstitutionalUser com) {
		int i = 0;
		try {
			// 账号修改
			Person p = new Person();
			p.setUserId(com.getUserId());
			p.setInstitution(com.getInstitution());
			if (StringUtils.isNotBlank(com.getPassword())) {
				p.setPassword(PasswordHelper.encryptPassword(com.getPassword()));
			}
			if (!StringUtils.isEmpty(com.getAdminname())) {
				p.setPid(com.getAdminname());
			} else if (!StringUtils.isEmpty(com.getAdminOldName())) {
				p.setPid(com.getAdminOldName());
			} else {
				p.setPid("");
			}
			p.setLoginMode(Integer.parseInt(com.getLoginMode()));
			i = personMapper.updateRegisterInfo(p);
		} catch (Exception e) {
			log.error("机构修改异常：", e);
		}
		return i;
	}

	@Override
	public void updateUserIp(InstitutionalUser com){
		userIpMapper.deleteUserIp(com.getUserId());
		addUserIp(com);
	}

	@Override
	public void addUserIp(InstitutionalUser com){
		String[] arr_ip = com.getIpSegment().split("\r\n");
		int index=0;
		for(String ip : arr_ip){
			if(ip.contains("-")){				
				UserIp userIp = new UserIp();
				userIp.setId(GetUuid.getId());
				userIp.setUserId(com.getUserId());
				userIp.setBeginIpAddressNumber(IPConvertHelper.IPToNumber(ip.substring(0, ip.indexOf("-"))));
				userIp.setEndIpAddressNumber(IPConvertHelper.IPToNumber(ip.substring(ip.indexOf("-")+1, ip.length())));
				userIp.setSort(index++);
				userIpMapper.insert(userIp);
			}
		}
	}

	@Override
	public int setAccountRestriction(InstitutionalUser user, boolean isReset) {
		if (user.getpConcurrentnumber() == null && user.getsConcurrentnumber() == null) {
			if(isReset){
				userAccountRestrictionMapper.deleteAccountRestriction(user.getUserId());
			}
			return 1;
		}
		UserAccountRestriction account=userAccountRestrictionMapper.getAccountRestriction(user.getUserId());
		if(account==null||isReset){
			if(isReset){
				userAccountRestrictionMapper.deleteAccountRestriction(user.getUserId());
			}
			UserAccountRestriction acc = new UserAccountRestriction();
			try {
				acc.setUserId(user.getUserId());
				acc.setUpperlimit(user.getUpperlimit());
				acc.setChargebacks(user.getChargebacks());
				acc.setDownloadupperlimit(user.getDownloadupperlimit());
				acc.setpConcurrentnumber(user.getpConcurrentnumber());
				acc.setsConcurrentnumber(user.getsConcurrentnumber());
				acc.setsIsTrial(user.getsIsTrial().equals("isTrial")?"1":"0");
				SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd");
				acc.setsBegintime(sd.parse(user.getsBegintime()));
				acc.setsEndtime(sd.parse(user.getsEndtime()));
			} catch (ParseException e) {
				e.printStackTrace();
			}
			
			return userAccountRestrictionMapper.insert(acc);
		}else{
			UserAccountRestriction acc = new UserAccountRestriction();
			acc.setUserId(user.getUserId());
			if(user.getpConcurrentnumber()!=null){
				acc.setpConcurrentnumber(user.getpConcurrentnumber());
			}else{
				acc.setpConcurrentnumber(account.getpConcurrentnumber());
			}
			try {
				if(user.getsConcurrentnumber()!=null){
					acc.setUpperlimit(user.getUpperlimit());
					acc.setChargebacks(user.getChargebacks());
					acc.setDownloadupperlimit(user.getDownloadupperlimit());
					acc.setsConcurrentnumber(user.getsConcurrentnumber());
					acc.setsIsTrial(user.getsIsTrial().equals("isTrial")?"1":"0");
					SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd");
					acc.setsBegintime(sd.parse(user.getsBegintime()));
					acc.setsEndtime(sd.parse(user.getsEndtime()));
				}else{
					acc.setUpperlimit(account.getUpperlimit());
					acc.setChargebacks(account.getChargebacks());
					acc.setDownloadupperlimit(account.getDownloadupperlimit());
					acc.setsConcurrentnumber(account.getsConcurrentnumber());
					acc.setsIsTrial(account.getsIsTrial().equals("isTrial")?"1":"0");
					acc.setsBegintime(account.getsBegintime());
					acc.setsEndtime(account.getsEndtime());
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			return userAccountRestrictionMapper.updateAccount(acc);
		}
	}

	@Override
	public int setPartAccountRestriction(InstitutionalUser user) {
		UserAccountRestriction account=userAccountRestrictionMapper.getAccountRestriction(user.getUserId());
		if(account==null){
			UserAccountRestriction acc = new UserAccountRestriction();
			acc.setUserId(user.getUserId());
			acc.setUpperlimit(user.getUpperlimit());
			acc.setChargebacks(user.getChargebacks());
			acc.setDownloadupperlimit(user.getDownloadupperlimit());
			acc.setpConcurrentnumber(user.getpConcurrentnumber());
			acc.setsConcurrentnumber(user.getsConcurrentnumber());
			return userAccountRestrictionMapper.insert(acc);
		}else{
			UserAccountRestriction acc = new UserAccountRestriction();
			acc.setUserId(user.getUserId());
			//切记此处不能传机构用户并发数
			acc.setpConcurrentnumber(account.getpConcurrentnumber());
			acc.setUpperlimit(user.getUpperlimit());
			acc.setChargebacks(user.getChargebacks());
			acc.setDownloadupperlimit(user.getDownloadupperlimit());
			acc.setsConcurrentnumber(user.getsConcurrentnumber());
			return userAccountRestrictionMapper.updateAccount(acc);
		}
	}


	@Override
	public Person queryPersonInfo(String userId){
		Person per = personMapper.queryPersonInfo(userId);
		return per;
	}

	/**
	 *	读取Excel机构账号信息(锁定/解锁)
	 */
	@Override
	public List<String> getExceluser(MultipartFile file){
		List<String> list = new ArrayList<String>();
		try{
			ExcelUtil.checkFile(file);  
			//获得Workbook工作薄对象  
			Workbook workbook = ExcelUtil.getWorkBook(file);
			// 循环工作表Sheet
			Sheet sheet = workbook.getSheetAt(0);
			if(sheet != null){
				// 循环行Row
				for(int rowNum = 1; rowNum <= sheet.getLastRowNum(); rowNum++){
					Row row = sheet.getRow(rowNum);
					if(row != null){
						//循环列Cell
						list.add(ExcelUtil.getValue(row.getCell(0)));
					}else{
						System.out.println("Excel中某列为空");
					}
				}
			}else{
				System.out.println("无法找到sheet页");
			}
		}catch(IOException e){
			e.printStackTrace();
		}
		return list;
	}

	@Override
	public int updateUserFreeze(String userId,String radio){
		Map<String,String> map = new HashMap<String, String>();
		map.put("isFreeze", radio);
		map.put("userId", userId);
		return personMapper.updateUserFreeze(map);
	}

	@Override
	public List<Map<String, Object>> selectListByRid(String proid){
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		String [] str = proid.split(",");
		Set<String> set = new HashSet<String>();
		//筛选产品对应的数据库,防止重复
		for(String s : str){			
			Map<String, Object> map = resourcePriceMapper.getPriceByRid(s.trim());
			if(map!=null&&map.get("sourceCode")!=""){				
				set.add(map.get("sourceCode").toString());
			}
		} 
		if(set.contains("DB.IsticPeriodical")&&set.contains("DB_CSPD")){
			set.remove("DB.IsticPeriodical");
			set.add("DB_PEDB");
		}
		//对资源余额、限时数据库进行排序
				if(set.contains("DB_CSPD")
						&&set.contains("DB_CCPD")
						&&set.contains("DB_CDDB")
						&&set.contains("DB_WFSD")
						&&set.contains("DB_WFPD")
						&&set.contains("DB_CLRD")
						&&set.contains("DB_CSTAD")
						&&set.contains("DB_CLGD")
						&&set.contains("DB_Video")
						&&set.contains("DB_PEDB")
						&&set.contains("InstitutionDigest")
						&&set.contains("ExpertDigest")){
					List<String> listDB=new ArrayList<String>();
					listDB.add("DB_CSPD");
					listDB.add("DB_PEDB");
					listDB.add("DB_CDDB");
					listDB.add("DB_CCPD");
					listDB.add("DB_WFPD");
					listDB.add("DB_WFSD");
					listDB.add("DB_CLRD");
					listDB.add("DB_CLGD");
					listDB.add("DB_CSTAD");
					listDB.add("InstitutionDigest");
					listDB.add("ExpertDigest");
					listDB.add("DB_Video");
					//循环Set集合查询资源库信息
					for(String se : listDB){
						Map<String, Object> m = datamanagerMapper.selectDataByPsc(se);
						if(m!=null && m.get("productSourceCode")!=""){
							List<Map<String, Object>> rp = resourcePriceMapper.getPriceBySourceCode(m.get("productSourceCode").toString());
							if(se.equals("DB_PEDB")){
								List<Map<String,Object>> listdb =new ArrayList<Map<String,Object>>();
								Map<String, Object> mapDB1=new HashMap<String, Object>();
								Map<String, Object> mapDB2=new HashMap<String, Object>();
								mapDB1.put("sourceCode", "DB.IsticPeriodical");
								mapDB1.put("name", "中信所中文期刊");
								mapDB1.put("rid", "Income.IsticPeriodical");
								mapDB2.put("sourceCode", "DB_CSPD");
								mapDB2.put("name", "万方期刊全文");
								mapDB2.put("rid", "Income.PeriodicalFulltext");
								listdb.add(mapDB1);
								listdb.add(mapDB2);
								rp=listdb;
							}
							if(m.get("resType")==null){
								m.put("resType", "");
							}
							m.put("rp", rp);
							list.add(m);
						}
					}
					return list;
				}
		//循环Set集合查询资源库信息
		for(String se : set){
			Map<String, Object> m = datamanagerMapper.selectDataByPsc(se);
			if(m!=null && m.get("productSourceCode")!=""){
				List<Map<String, Object>> rp = resourcePriceMapper.getPriceBySourceCode(m.get("productSourceCode").toString());
				if(m.get("resType")==null){
					m.put("resType", "");
				}
				m.put("rp", rp);
				list.add(m);
			}
		}
		return list;
	}

	@Override
	public PageList findListInfo(Map<String, Object> map) throws Exception{
		//1、筛选user
		long time=System.currentTimeMillis();
		//查询solr里的数据
		Map<String,Object> allMap=this.getSolrList(map);
		//-查出来有多个solr的结果
		List<Object> userList = (List<Object>) allMap.get("data");
		//验证Ip   ip查询
		Long ipstart=(Long) map.get("ipstart");
		Long ipend=(Long) map.get("ipend");
		if(ipstart!=null&&ipend!=null&&ipstart.longValue()<=ipend.longValue()){
			List<String> userIdList=this.findUserIdByIp(ipstart, ipend,(String) map.get("userId"));
			if(userIdList!=null&&userIdList.size()>0){
				List<Object> listObj=new ArrayList<Object>();
				int count=0;
				for (Object obj : userList) {
					Map<String,String> js=(Map<String,String>)obj;
					if(userIdList.contains(js.get("Id"))){
						listObj.add(obj);
						count++;
					}
				}
				allMap.put("num", count);
				userList=listObj;
			}
			if(userIdList.size()<=0){
				userList.removeAll(userList);
				allMap.put("num", 0);
			}
		}
		long timeSql=System.currentTimeMillis()-time;
		//2、查询产品
		long time1=System.currentTimeMillis();
		Date nextDay = this.getDay();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日");
		//-所有的资源
		List<PayChannelModel> list_ = this.purchaseProject();
		for(Object object : userList){
			//将Object转换成 Map
			Map<String, Object> userMap = (Map<String,Object>) object;
			String userId = userMap.get("Id").toString();
			userMap.put("Password", PasswordHelper.decryptPassword(userMap.get("Password").toString()));
			int sortScore=Integer.parseInt(userMap.get("LoginMode").toString());
			if(null!=userMap.get("IsFreeze") && (boolean) userMap.get("IsFreeze")){
				sortScore+=1000;
			}
			boolean flag=false;//用户是否可用 true是不过期，false是过期
			List<WfksPayChannelResources> wfList=new ArrayList<WfksPayChannelResources>();
			List<WfksPayChannelResources> listWfks = wfksMapper.selectByUserId(userId);//获取wfks_pay_channel_resources购买项资源
			//查询余额表    限时表   次数表  的信息  防止迁徙过来的用户的信息不全
			List<wfks.accounting.handler.entity.BalanceLimitAccount> balanceLimitList=new ArrayList<wfks.accounting.handler.entity.BalanceLimitAccount>();
			List<wfks.accounting.handler.entity.TimeLimitAccount> timeLimitList=new ArrayList<wfks.accounting.handler.entity.TimeLimitAccount>();
			List<wfks.accounting.handler.entity.CountLimitAccount> countLimitList=new ArrayList<wfks.accounting.handler.entity.CountLimitAccount>();
			for(PayChannelModel pay:list_){
				for(WfksPayChannelResources res:listWfks){
					if(StringUtils.equals(pay.getId(), res.getPayChannelid())){
						wfList.add(res);
					}
				}
				//查询余额表
				if(pay.getType().equals("balance")){
					wfks.accounting.handler.entity.BalanceLimitAccount balanceLimit = (wfks.accounting.handler.entity.BalanceLimitAccount)accountDao.get(new AccountId(pay.getId(),userId), new HashMap<String,String>());
					if(balanceLimit!=null){
						balanceLimitList.add(balanceLimit);
					}
				}
				
				//查询限时表
				if(pay.getType().equals("time")){
					wfks.accounting.handler.entity.TimeLimitAccount timeLimit = (wfks.accounting.handler.entity.TimeLimitAccount)accountDao.get(new AccountId(pay.getId(),userId), new HashMap<String,String>());
					if(timeLimit!=null){
						timeLimitList.add(timeLimit);
					}
				}
				//查询次数表
				if(pay.getType().equals("count")){
					wfks.accounting.handler.entity.CountLimitAccount countLimit = (wfks.accounting.handler.entity.CountLimitAccount)accountDao.get(new AccountId(pay.getId(),userId), new HashMap<String,String>());
					if(countLimit!=null){
						countLimitList.add(countLimit);
					}
				}
			}
			//1、循环购买的余额  限时  次数
			//2、对比wfList里面是否有改资源如果没有new WfksPayChannelResources加入
			if(balanceLimitList!=null&&balanceLimitList.size()>0){
				int balanceCount=0;
				for (wfks.accounting.handler.entity.BalanceLimitAccount blance : balanceLimitList) {
					for (WfksPayChannelResources wfks : wfList) {
						if(wfks.getPayChannelid().equals(blance.getPayChannelId())){
							balanceCount++;
						}
					}
					if(balanceCount==0){
						WfksPayChannelResources wfks=new WfksPayChannelResources();
						wfks.setPayChannelid(blance.getPayChannelId());
						wfks.setUserId(userId);
						wfList.add(wfks);
					}
				}
			}
			if(timeLimitList!=null&&timeLimitList.size()>0){
				int timeCount=0;
				for (wfks.accounting.handler.entity.TimeLimitAccount timel : timeLimitList) {
					for (WfksPayChannelResources wfks : wfList) {
						if(wfks.getPayChannelid().equals(timel.getPayChannelId())){
							timeCount++;
						}
					}
					if(timeCount==0){
						WfksPayChannelResources wfks=new WfksPayChannelResources();
						wfks.setPayChannelid(timel.getPayChannelId());
						wfks.setUserId(userId);
						wfList.add(wfks);
					}
				}
			}
			if(countLimitList!=null&&countLimitList.size()>0){
				int countCount=0;
				for (wfks.accounting.handler.entity.CountLimitAccount count : countLimitList) {
					for (WfksPayChannelResources wfks : wfList) {
						if(wfks.getPayChannelid().equals(count.getPayChannelId())){
							countCount++;
						}
					}
					if(countCount==0){
						WfksPayChannelResources wfks=new WfksPayChannelResources();
						wfks.setPayChannelid(count.getPayChannelId());
						wfks.setUserId(userId);
						wfList.add(wfks);
					}
				}
			}
			
			// 购买项目是否试用
			Map<String, String> itemsMap = new HashMap<String, String>();
			Object obj=userMap.get("IsTrial");
			if(obj!=null){
				if(obj instanceof String){
					itemsMap.put(userMap.get("IsTrial").toString(), "trical");
				}else{
					List<String> trialList = (List<String>) userMap.get("IsTrial");
					if (trialList != null) {
						for (String payChannelId : trialList) {
							itemsMap.put(payChannelId, "trical");
						}
					}
				}
			}
			String AdministratorPassword=(String) userMap.get("AdministratorPassword");
			if(!StringUtils.isEmpty(AdministratorPassword)){
				userMap.put("AdministratorPassword", PasswordHelper.decryptPassword(AdministratorPassword));
			}
			String PartyAdminPassword=(String) userMap.get("PartyAdminPassword");
			if(!StringUtils.isEmpty(PartyAdminPassword)){
				userMap.put("PartyAdminPassword", PasswordHelper.decryptPassword(PartyAdminPassword));
			}
			//验证是否过期
			this.isExpired(userMap,"PartyAdminExpired","PartyAdminEndTIme");
			this.isExpired(userMap,"openWeChatExpired","WeChatEndTime");
			this.isExpired(userMap,"openAppExpired","AppEndTime");

			//购买项目列表
			List<Map<String, Object>> projectList = new ArrayList<Map<String, Object>>();
			List<Map<String, Object>> oldList = new ArrayList<Map<String, Object>>();
			for(WfksPayChannelResources wfks : wfList){
				Map<String, Object> libdata = new HashMap<String, Object>();// 组装条件Map
				Map<String, Object> extraData = new HashMap<String, Object>();// 购买的项目
				if(wfks.getPayChannelid().equals("HistoryCheck")){
					extraData.put("ViewHistoryCheck", "ViewHistoryCheck");
				}
				PayChannelModel pay = SettingPayChannels.getPayChannel(wfks.getPayChannelid());
				if(pay.getType().equals("balance")){
					wfks.accounting.handler.entity.BalanceLimitAccount account = (wfks.accounting.handler.entity.BalanceLimitAccount)accountDao.get(new AccountId(wfks.getPayChannelid(),wfks.getUserId()), new HashMap<String,String>());
					if(account!=null){
						extraData.put("name", pay.getName());
						extraData.put("type", pay.getType());
						extraData.put("balance", account.getBalance());
						extraData.put("beginDateTime", sdf.format(account.getBeginDateTime()));
						extraData.put("endDateTime", sdf.format(account.getEndDateTime()));
						boolean expired=this.getExpired(account.getEndDateTime(),nextDay);
						if(!expired){
							expired=account.getBalance().intValue()<0;
						}
						if(!expired&&account.getBalance().intValue()>0){
							flag=true;
						}
						extraData.put("expired",expired);
						extraData.put("totalConsume", account.getTotalConsume());
						extraData.put("payChannelid", account.getPayChannelId());
						extraData.put("mode", itemsMap.get(account.getPayChannelId()));
						//查询条件
						libdata.put("userId", account.getUserId());
						libdata.put("payChannelid", account.getPayChannelId());
					}
				}else if(pay.getType().equals("time")){
					wfks.accounting.handler.entity.TimeLimitAccount account = (wfks.accounting.handler.entity.TimeLimitAccount)accountDao.get(new AccountId(wfks.getPayChannelid(),wfks.getUserId()), new HashMap<String,String>());
					if(account!=null){
						extraData.put("beginDateTime", sdf.format(account.getBeginDateTime()));
						extraData.put("endDateTime", sdf.format(account.getEndDateTime()));
						boolean expired = this.getExpired(account.getEndDateTime(),nextDay);
						if(!expired){
							flag=true;
						}
						extraData.put("expired", expired);
						extraData.put("name", pay.getName());
						extraData.put("type", pay.getType());
						extraData.put("payChannelid", account.getPayChannelId());
						extraData.put("mode", itemsMap.get(account.getPayChannelId()));
						//查询条件
						libdata.put("userId", account.getUserId());
						libdata.put("payChannelid", account.getPayChannelId());
					}
				}else if(pay.getType().equals("count")){
					wfks.accounting.handler.entity.CountLimitAccount account = (wfks.accounting.handler.entity.CountLimitAccount)accountDao.get(new AccountId(wfks.getPayChannelid(),wfks.getUserId()), new HashMap<String,String>());
					if(account!=null){
						extraData.put("name", pay.getName());
						extraData.put("type", pay.getType());
						extraData.put("balance", account.getBalance());
						extraData.put("beginDateTime", sdf.format(account.getBeginDateTime()));
						extraData.put("endDateTime", sdf.format(account.getEndDateTime()));
						boolean expired = this.getExpired(account.getEndDateTime(), nextDay);
						if (!expired) {
							expired = account.getBalance() < 0;
						}
						if(!expired&&account.getBalance()>0){
							flag=true;
						}
						extraData.put("expired",expired);
						extraData.put("totalConsume", account.getTotalConsume());
						extraData.put("payChannelid", account.getPayChannelId());
						extraData.put("mode", itemsMap.get(account.getPayChannelId()));
						//查询条件
						libdata.put("userId", account.getUserId());
						libdata.put("payChannelid", account.getPayChannelId());
					}
				}
				List<Map<String, Object>> plList = wfksMapper.selectProjectLibrary(libdata);
				int count=0;
				for (Map<String, Object> map2 : plList) {
					if(map2.get("productSourceCode")!=null&&map2.get("productSourceCode")!=""&&map2.get("productSourceCode").equals("DB_CLGD")&&map2.get("contract")!=null && map2.get("contract")!=""){
						List<JSONObject> conlist=JSONObject.fromObject(map2.get("contract")).getJSONArray("Terms");
						for (JSONObject jsonObject : conlist) {
							if(jsonObject.get("Field").equals("gazetteers_type")
							 ||jsonObject.get("Field").equals("gazetteers_id")
							 ||jsonObject.get("Field").equals("item_id")){
								count++;
							}
						}
						//判断gazetteers_id和item_id都为空的情况下
						if(count==0){
							JSONObject json=new JSONObject();
							json.put("Field", "gazetteers_type");
							json.put("Logic", "AND");
							json.put("Value", "FZ_New;FZ_Old");
							json.put("ValueType", "String");
							json.put("Verb", "Equal");
							conlist.add(json);
							JSONObject json1=new JSONObject();
							json1.put("Terms", conlist);
							map2.put("contract", json1);

						}
					}
				}
				int dbCount=0;
						for (Map<String, Object> map1 : plList) {
							if(map1.get("payChannelid").equals("GBalanceLimit")&&map1.get("productSourceCode").equals("DB.IsticPeriodical")){
								plList.remove(map1);
								HashMap<String, Object> m=new HashMap<String, Object>();
								m.put("product_id", Arrays.toString(new String[]{"Income.IsticPeriodical","Income.PeriodicalFulltext"}));
								m.put("productSourceCode", "DB_PEDB");
								m.put("tableName", "期刊增强库");
								m.put("payChannelid", "GBalanceLimit");
								plList.add(m);
								dbCount++;
								break;
							}
						}
						for (Map<String, Object> map1 : plList) {
							if(map1.get("payChannelid").equals("GTimeLimit")&&map1.get("productSourceCode").equals("DB.IsticPeriodical")){
								plList.remove(map1);
								HashMap<String, Object> m=new HashMap<String, Object>();
								m.put("product_id", Arrays.toString(new String[]{"Income.IsticPeriodical","Income.PeriodicalFulltext"}));
								m.put("productSourceCode", "DB_PEDB");
								m.put("tableName", "期刊增强库");
								m.put("payChannelid", "GTimeLimit");
								plList.add(m);
								dbCount++;
								break;
							}
						}
						for (Map<String, Object> map2 : plList) {
							if(map2.containsKey("productSourceCode")&&map2.get("productSourceCode").equals("DB_CSPD")){
								dbCount++;
							}
						}
						if(dbCount>=2){
							for (Map<String, Object> map3 : plList) {
								if(map3.get("productSourceCode").equals("DB_CSPD")&&!map3.containsKey("contract")){
									plList.remove(map3);
									break;
								}
							}
						}
				List<Map<String, Object>> data = this.selectListByRid(pay.getProductDetail());//通过产品id反查资源库
				if(plList.size()>0){
					for(Map<String, Object> d : data){
						for(Map<String, Object> plmap : plList){
							if(d.get("productSourceCode").equals(plmap.get("productSourceCode"))){
								if(plmap.get("productSourceCode").equals("DB_WFSD")){
									//判断标准是否选择了详情
									if(plmap.containsKey("contract")){
										d.put("checked", "checked");
									}
								}else{
									d.put("checked", "checked");
								}
								if(plmap.get("contract")!=null && plmap.get("contract")!=""){
									d.put("contract", JSONObject.fromObject(plmap.get("contract")).getJSONArray("Terms"));
								}
								if(plmap.get("product_id")!=null && plmap.get("product_id")!=""){								
									d.put("product_id",plmap.get("product_id"));
								}
							}

						}
					}
				}
				if(plList.size()>0 && plList.get(0).get("tableName")!=null){					
					extraData.put("plList", data);
				}
				if(extraData.size()>0){
					if((boolean) extraData.get("expired")){
						oldList.add(extraData);
					}else{
						projectList.add(extraData);
					}
				}
			}
			projectList.addAll(oldList);
			if(projectList.size()>0){				
				userMap.put("proList", projectList);
			}
			if(!flag){
				sortScore=100+sortScore;
			}
			userMap.put("score", sortScore);
			long timeInf=System.currentTimeMillis()-time1;
			//查询个人绑定机构权限
			long time3=System.currentTimeMillis();
			SearchAccountAuthorityRequest request = SearchAccountAuthorityRequest.newBuilder().setUserId(userId).build();
			SearchAccountAuthorityResponse response = bindAuthorityChannel.getBlockingStub().searchAccountAuthority(request);
			List<AccountAuthority> accountList = response.getItemsList();
			BindAuthorityViewModel bindAuthorityModel = new BindAuthorityViewModel();
			if (accountList!=null&&accountList.size()>0){
				bindAuthorityModel.setOpenState(true);
				StringBuffer authority = new StringBuffer();
				for (AccountAuthority accountAuthority : accountList) {
					authority.append(bindAuthorityMapping.getAuthorityCn(accountAuthority.getBindAuthority())+"、");
				}
				bindAuthorityModel.setUserId(userId);
				bindAuthorityModel.setBindType(bindAuthorityMapping.getBindTypeName(response.getItems(0).getBindType().getNumber()));
				bindAuthorityModel.setBindLimit(response.getItems(0).getBindLimit());
				bindAuthorityModel.setBindValidity(response.getItems(0).getBindValidity());
				bindAuthorityModel.setDownloadLimit(response.getItems(0).getDownloadLimit());
				bindAuthorityModel.setBindAuthority(authority.toString().substring(0,authority.length()-1));
				bindAuthorityModel.setOpenTimeLimitState(new Date(response.getItems(0).getOpenStart().getSeconds()*1000));
				bindAuthorityModel.setOpenTimeLimitEnd(new Date(response.getItems(0).getOpenEnd().getSeconds()*1000));
				bindAuthorityModel.setEmail(response.getItems(0).getEmail());
				userMap.put("bindAuthority", bindAuthorityModel);
			}
			long timeSea=System.currentTimeMillis()-time3;
			log.info("数据库耗时:"+timeSql+"ms,接口耗时:"+timeInf+"ms,个人绑定机构耗时:"+timeSea+"ms");
		}
		log.info(userList.toString());
		//对userList排序
		Collections.sort(userList, new Comparator<Object>() {
			@Override
			public int compare(Object o1, Object o2) {
				Map<String, Object> map1 = (Map<String, Object>) o1;
				Map<String, Object> map2 = (Map<String, Object>) o2;
				int num1 = Integer.parseInt(map1.get("score").toString());
				int num2 = Integer.parseInt(map2.get("score").toString());
				if (num1 > num2) {
					return 1;
				} else if (num1 < num2) {
					return -1;
				} else {
					return 0;
				}
			}
		});
		PageList pageList = new PageList();
		pageList.setPageRow(userList);
		pageList.setTotalRow((int) allMap.get("num"));
		return pageList;
	}

	private void isExpired(Map<String, Object> userMap, String key, String value) {
		String endTime=(String) userMap.get(value);
		if(StringUtils.isEmpty(endTime)){
			return;
		}
		Date date=DateUtil.stringToDate1(endTime.replace("年","-").replace("月", "-").replace("日", "-"));
		userMap.put(key,this.getExpired(date,this.getDay()));
	}

	@Override
	public Map<String, Object> selectBalanceById(String userId){
		return projectBalanceMapper.selectBalanceById(userId);
	}

	@Override
	public int updateAllPid(String pid,String old_pid){
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("pid", pid);
		map.put("old_pid", old_pid);
		return personMapper.updateAllPid(map);
	}



	@Override
	public int deleteUserIp(String userId){
		return userIpMapper.deleteUserIp(userId);
	}

	@Override
	public int updatePid(Map<String, Object> map){
		return personMapper.updatePid(map);
	}

	@Override
	public Map<String, Object> findListInfoById(String userId){
		Map<String, Object> map = personMapper.findListInfoById(userId);
		if(map==null){
			map= new HashMap<>();
		}
		try {
			map.put("password", map.get("password")==null?"":PasswordHelper.decryptPassword(map.get("password").toString()));
		} catch (Exception e) {
			e.printStackTrace();
		}
		//查询该用户ip
		List<Map<String,Object>> list_ip = userIpMapper.findIpByUserId(userId);
		for(Map<String, Object> userIp : list_ip){
			String beginIpAddressNumber = IPConvertHelper.NumberToIP((long) userIp.get("beginIpAddressNumber"));
			userIp.put("beginIpAddressNumber", beginIpAddressNumber);
			String endIpAddressNumber = IPConvertHelper.NumberToIP((long) userIp.get("endIpAddressNumber"));
			userIp.put("endIpAddressNumber", endIpAddressNumber);
		}
		map.put("list_ip", list_ip);
		return map;
	}

	@Override
	public Map<String, Object> findInfoByPid(String pid){
		Map<String, Object> map = personMapper.findInfoByPid(pid);
		if(map==null){
			return new HashMap<String,Object>();
		}
		try {
			map.put("password", map.get("password")==null?"":PasswordHelper.decryptPassword(map.get("password").toString()));
			List<Map<String,Object>> list_ip = userIpMapper.findIpByUserId(pid);
			for(Map<String, Object> userIp : list_ip){
				String beginIpAddressNumber = IPConvertHelper.NumberToIP((long) userIp.get("beginIpAddressNumber"));
				userIp.put("beginIpAddressNumber", beginIpAddressNumber);
				String endIpAddressNumber = IPConvertHelper.NumberToIP((long) userIp.get("endIpAddressNumber"));
				userIp.put("endIpAddressNumber", endIpAddressNumber);
			}
			map.put("adminIP", list_ip);
		} catch (Exception e) {
			log.error("获取机构管理员信息失败：", e);
		}
		return map;
	}

	@Override
	public List<Map<String,Object>> validateIp(List<UserIp> list){
		return userIpMapper.validateIp(list);
	}

	@Override
	public List<Map<String,Object>> listIpByUserId(String userId) {
		List<Map<String,Object>> list=userIpMapper.listIpByUserId(userId);
		return list;
	}

	//通过机构名称查询下属管理员
	@Override
	public List<Map<String, Object>> findInstitutionAdmin(String institution,String userId){
		List<Map<String, Object>> limap =  personMapper.findInstitutionAdmin(institution,userId);
		for(Map<String, Object> map : limap) {
			try {
				map.put("password", map.get("password")==null?"":PasswordHelper.decryptPassword(map.get("password").toString()));
			} catch (Exception e) {
				e.printStackTrace();
			}
			List<Map<String,Object>> list_ip = userIpMapper.findIpByUserId(map.get("userId").toString());
			for(Map<String, Object> userIp : list_ip){
				String beginIpAddressNumber = IPConvertHelper.NumberToIP((long) userIp.get("beginIpAddressNumber"));
				userIp.put("beginIpAddressNumber", beginIpAddressNumber);
				String endIpAddressNumber = IPConvertHelper.NumberToIP((long) userIp.get("endIpAddressNumber"));
				userIp.put("endIpAddressNumber", endIpAddressNumber);
			}
			map.put("adminIP", list_ip);
		}
		return limap;
	}

	@Override
	public PageList getSonaccount(Map<String,Object> map) {
		List<Object> userList = personMapper.sonAccountNumber(map);// 获取子账号列表
		PageList pageList = new PageList();
		if (userList.size()==0) {
			pageList.setPageRow(userList);
			pageList.setTotalRow(0);
			return pageList;
		}
		for (Object object : userList) {
			Map<String, Object> userMap = (Map<String,Object>) object;
			try{
				userMap.put("password",PasswordHelper.decryptPassword(String.valueOf(userMap.get("password"))));
			}catch (Exception e){
				log.error("密码转化异常：",e);
			}
			String userId=userMap.get("userId").toString();
			String pid=userMap.get("pid").toString();
			//子账号ip
			List<Map<String,Object>> list_ip = userIpMapper.findIpByUserId(userId);
			if(list_ip.size()>0){
				for(Map<String, Object> userIp : list_ip){
					String beginIpAddressNumber = IPConvertHelper.NumberToIP((long) userIp.get("beginIpAddressNumber"));
					String endIpAddressNumber = IPConvertHelper.NumberToIP((long) userIp.get("endIpAddressNumber"));
					userIp.put("ip", beginIpAddressNumber+"-"+endIpAddressNumber);
				}
				userMap.put("list_ip", list_ip);
			}
			//购买项目
			List<PayChannelModel> list_ = this.purchaseProject();
			List<Map<String, String>> wfList=new ArrayList<Map<String, String>>();
			List<Map<String, String>> listWfks = wfksMapper.selectProjectLibraryName(pid);// 获取父账号购买项目
			for(PayChannelModel pay:list_){
				for(Map<String, String> res:listWfks){
					if(StringUtils.equals(pay.getId(), res.get("payChannelid"))){
						wfList.add(res);
					}
				}
			}
			//获取继承主账号的权限
			List<Map<String, Object>> tempList = new ArrayList<Map<String, Object>>();
			WfksAccountidMapping[] mapping = this.getWfksAccountidLimit(userId,"Group");
			if(mapping!=null){
				for(WfksAccountidMapping wf:mapping){
					Map<String, Object> extraData = new HashMap<String, Object>();// 购买的项目
					extraData.put("payChannelid", wf.getRelatedidAccounttype());
					if (wf.getBegintime() != null && wf.getEndtime() != null) {
						extraData.put("time",sdfSimp.format(wf.getBegintime()) + "-" + sdfSimp.format(wf.getEndtime()));
					}
					for(PayChannelModel pay:list_){
						if(pay.getId().equals(wf.getRelatedidAccounttype())){
							extraData.put("name", pay.getName());
							extraData.put("type", pay.getType());
							break;
						}
					}
					boolean isExists=false;
					for(Map<String, String> wfks : wfList){
						if(StringUtils.equals(wfks.get("payChannelid"),wf.getRelatedidAccounttype())){
							extraData.put("resouceName", wfks.get("tableName"));
							isExists=true;
						}
					}
					if(isExists){
						tempList.add(extraData);
					}
				}
			}
			//调用接口查询支付信息
			List<Map<String, Object>> projectList = new ArrayList<Map<String, Object>>();
			try{
				for(Map<String, String> wfks : wfList){
					PayChannelModel pay = SettingPayChannels.getPayChannel(wfks.get("payChannelid"));
					Map<String, Object> extraData = new HashMap<String, Object>();// 购买的项目
					if(pay.getType().equals("balance")){
						wfks.accounting.handler.entity.BalanceLimitAccount account = (wfks.accounting.handler.entity.BalanceLimitAccount)accountDao.get(new AccountId(wfks.get("payChannelid"),userId), new HashMap<String,String>());
						if(account!=null){
							extraData.put("name", pay.getName());
							extraData.put("payChannelid", account.getPayChannelId());
							extraData.put("type", pay.getType());
							extraData.put("balance", account.getBalance());
							extraData.put("time", sdfSimp.format(account.getBeginDateTime())+"-"+sdfSimp.format(account.getEndDateTime()));
							extraData.put("resouceName", wfks.get("tableName"));
						}
					}else if(pay.getType().equals("time")){
						wfks.accounting.handler.entity.TimeLimitAccount account = (wfks.accounting.handler.entity.TimeLimitAccount)accountDao.get(new AccountId(wfks.get("payChannelid"),userId), new HashMap<String,String>());
						if(account!=null){
							extraData.put("name", pay.getName());
							extraData.put("payChannelid", account.getPayChannelId());
							extraData.put("type", pay.getType());
							extraData.put("time", sdfSimp.format(account.getBeginDateTime())+"-"+sdfSimp.format(account.getEndDateTime()));
							extraData.put("resouceName", wfks.get("tableName"));
						}
					}else if(pay.getType().equals("count")){
						wfks.accounting.handler.entity.CountLimitAccount account = (wfks.accounting.handler.entity.CountLimitAccount)accountDao.get(new AccountId(wfks.get("payChannelid"),userId), new HashMap<String,String>());
						if(account!=null){
							extraData.put("name", pay.getName());
							extraData.put("payChannelid", account.getPayChannelId());
							extraData.put("type", pay.getType());
							extraData.put("count", account.getBalance());
							extraData.put("time", sdfSimp.format(account.getBeginDateTime())+"-"+sdfSimp.format(account.getEndDateTime()));
							extraData.put("resouceName", wfks.get("tableName"));
						}
					}
					if(extraData.size()>1){
						projectList.add(extraData);
					}
				}
			}catch(Exception e){
				log.error("子账号"+userId+"调用接口异常",e);
			}
			for(Map<String, Object> temp:tempList){
				boolean exists=true;
				for(Map<String, Object> project:projectList){
					if (StringUtils.equals(String.valueOf(temp.get("payChannelid")),String.valueOf(project.get("payChannelid")))) {
						exists = false;
					}
				}
				if(exists){
					projectList.add(temp);
				}
			}
			userMap.put("data", projectList);
		}
		pageList.setPageRow(userList);
		pageList.setTotalRow(personMapper.sonAccountNumberCount(map));
		return pageList;
	}

	@Override
	public List<Map<String, Object>> getProjectInfo(String userId){
		//通过userId查询详情限定列表

		List<WfksPayChannelResources> listWfks = wfksMapper.selectByUserId(userId);
		
		//判断项目ID是存在
		List<PayChannelModel> list_ = this.purchaseProject();
		//查询余额表    限时表   次数表  的信息  防止迁徙过来的用户的信息不全
		List<wfks.accounting.handler.entity.BalanceLimitAccount> balanceLimitList=new ArrayList<wfks.accounting.handler.entity.BalanceLimitAccount>();
		List<wfks.accounting.handler.entity.TimeLimitAccount> timeLimitList=new ArrayList<wfks.accounting.handler.entity.TimeLimitAccount>();
		List<wfks.accounting.handler.entity.CountLimitAccount> countLimitList=new ArrayList<wfks.accounting.handler.entity.CountLimitAccount>();
				
		List<WfksPayChannelResources> wfList=new ArrayList<WfksPayChannelResources>();
		for(PayChannelModel pay:list_){
			for(WfksPayChannelResources res:listWfks){
				if(StringUtils.equals(pay.getId(), res.getPayChannelid())){
					wfList.add(res);
				}
			}
			//查询余额表
			if(pay.getType().equals("balance")){
				wfks.accounting.handler.entity.BalanceLimitAccount balanceLimit = (wfks.accounting.handler.entity.BalanceLimitAccount)accountDao.get(new AccountId(pay.getId(),userId), new HashMap<String,String>());
				if(balanceLimit!=null){
					balanceLimitList.add(balanceLimit);
				}
			}
			
			//查询限时表
			if(pay.getType().equals("time")){
				wfks.accounting.handler.entity.TimeLimitAccount timeLimit = (wfks.accounting.handler.entity.TimeLimitAccount)accountDao.get(new AccountId(pay.getId(),userId), new HashMap<String,String>());
				if(timeLimit!=null){
					timeLimitList.add(timeLimit);
				}
			}
			//查询次数表
			if(pay.getType().equals("count")){
				wfks.accounting.handler.entity.CountLimitAccount countLimit = (wfks.accounting.handler.entity.CountLimitAccount)accountDao.get(new AccountId(pay.getId(),userId), new HashMap<String,String>());
				if(countLimit!=null){
					countLimitList.add(countLimit);
				}
			}
		}
		//1、循环购买的余额  限时  次数
		//2、对比wfList里面是否有改资源如果没有new WfksPayChannelResources加入
		if(balanceLimitList!=null&&balanceLimitList.size()>0){
			int balanceCount=0;
			for (wfks.accounting.handler.entity.BalanceLimitAccount blance : balanceLimitList) {
				for (WfksPayChannelResources wfks : wfList) {
					if(wfks.getPayChannelid().equals(blance.getPayChannelId())){
						balanceCount++;
					}
				}
				if(balanceCount==0){
					WfksPayChannelResources wfks=new WfksPayChannelResources();
					wfks.setPayChannelid(blance.getPayChannelId());
					wfks.setUserId(userId);
					wfList.add(wfks);
				}
			}
		}
		if(timeLimitList!=null&&timeLimitList.size()>0){
			int timeCount=0;
			for (wfks.accounting.handler.entity.TimeLimitAccount timel : timeLimitList) {
				for (WfksPayChannelResources wfks : wfList) {
					if(wfks.getPayChannelid().equals(timel.getPayChannelId())){
						timeCount++;
					}
				}
				if(timeCount==0){
					WfksPayChannelResources wfks=new WfksPayChannelResources();
					wfks.setPayChannelid(timel.getPayChannelId());
					wfks.setUserId(userId);
					wfList.add(wfks);
				}
			}
		}
		if(countLimitList!=null&&countLimitList.size()>0){
			int countCount=0;
			for (wfks.accounting.handler.entity.CountLimitAccount count : countLimitList) {
				for (WfksPayChannelResources wfks : wfList) {
					if(wfks.getPayChannelid().equals(count.getPayChannelId())){
						countCount++;
					}
				}
				if(countCount==0){
					WfksPayChannelResources wfks=new WfksPayChannelResources();
					wfks.setPayChannelid(count.getPayChannelId());
					wfks.setUserId(userId);
					wfList.add(wfks);
				}
			}
		}
		
		WfksAccountidMapping[] tricals=wfksAccountidMappingMapper.getWfksAccountid(userId, "trical");
		Map<String,String> itemsMap=new HashMap<String,String>();
		for(WfksAccountidMapping wm:tricals){
			itemsMap.put(wm.getRelatedidKey(), "trical");
		}
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String nextDay = sdf.format(this.getDay());
		//购买项目列表
		List<Map<String, Object>> projectList = new ArrayList<Map<String, Object>>();
		for(WfksPayChannelResources wfks : wfList){
			Map<String, Object> libdata = new HashMap<String, Object>();//组装条件Map
			Map<String, Object> extraData = new HashMap<String, Object>();//购买的项目
			//已发表论文检测项目特殊处理
			if(wfks.getPayChannelid().equals("HistoryCheck")){
				WfksAccountidMapping[] mapping = wfksAccountidMappingMapper.getWfksAccountid(userId,"ViewHistoryCheck");
				extraData.put("ViewHistoryCheck", mapping==null?"not":"is");
			}
			PayChannelModel pay = SettingPayChannels.getPayChannel(wfks.getPayChannelid());
			if(pay.getType().equals("balance")){
				wfks.accounting.handler.entity.BalanceLimitAccount account = (wfks.accounting.handler.entity.BalanceLimitAccount)accountDao.get(new AccountId(wfks.getPayChannelid(),wfks.getUserId()), new HashMap<String,String>());
				if(account!=null){
					extraData.put("name", pay.getName());
					extraData.put("payChannelid", account.getPayChannelId());
					extraData.put("type", pay.getType());
					extraData.put("balance", account.getBalance());
					extraData.put("beginDateTime", sdf.format(account.getBeginDateTime()));
					extraData.put("endDateTime", sdf.format(account.getEndDateTime()));
					extraData.put("mode", itemsMap.get(account.getPayChannelId()));
					extraData.put("nextDay", nextDay);
					//查询条件
					libdata.put("userId", account.getUserId());
					libdata.put("payChannelid", account.getPayChannelId());
				}
			}else if(pay.getType().equals("time")){
				wfks.accounting.handler.entity.TimeLimitAccount account = (wfks.accounting.handler.entity.TimeLimitAccount)accountDao.get(new AccountId(wfks.getPayChannelid(),wfks.getUserId()), new HashMap<String,String>());
				if(account!=null){
					extraData.put("beginDateTime", sdf.format(account.getBeginDateTime()));
					extraData.put("endDateTime", sdf.format(account.getEndDateTime()));
					extraData.put("payChannelid", account.getPayChannelId());
					extraData.put("name", pay.getName());
					extraData.put("type", pay.getType());
					extraData.put("mode", itemsMap.get(account.getPayChannelId()));
					extraData.put("nextDay", nextDay);
					//查询条件
					libdata.put("userId", account.getUserId());
					libdata.put("payChannelid", account.getPayChannelId());
				}
			}else if(pay.getType().equals("count")){
				wfks.accounting.handler.entity.CountLimitAccount account = (wfks.accounting.handler.entity.CountLimitAccount)accountDao.get(new AccountId(wfks.getPayChannelid(),wfks.getUserId()), new HashMap<String,String>());
				if(account!=null){
					extraData.put("name", pay.getName());
					extraData.put("payChannelid", account.getPayChannelId());
					extraData.put("type", pay.getType());
					extraData.put("balance", account.getBalance());
					extraData.put("beginDateTime", sdf.format(account.getBeginDateTime()));
					extraData.put("endDateTime", sdf.format(account.getEndDateTime()));
					extraData.put("totalConsume", account.getTotalConsume());
					extraData.put("mode", itemsMap.get(account.getPayChannelId()));
					extraData.put("nextDay", nextDay);
					//查询条件
					libdata.put("userId", account.getUserId());
					libdata.put("payChannelid", account.getPayChannelId());
				}
			}
			List<Map<String, Object>> plList = wfksMapper.selectProjectLibrary(libdata);//已购买资源库
			int count=0;
			for (Map<String, Object> map2 : plList) {
				if(map2.get("productSourceCode")!=null&&map2.get("productSourceCode")!=""&&map2.get("productSourceCode").equals("DB_CLGD")&&map2.get("contract")!=null && map2.get("contract")!=""){
					List<JSONObject> conlist=JSONObject.fromObject(map2.get("contract")).getJSONArray("Terms");
					for (JSONObject jsonObject : conlist) {
						if(jsonObject.get("Field").equals("gazetteers_type")
						 ||jsonObject.get("Field").equals("gazetteers_id")
						 ||jsonObject.get("Field").equals("item_id")){
							count++;
						}
					}
					if(count==0){
						JSONObject json=new JSONObject();
						json.put("Field", "gazetteers_type");
						json.put("Logic", "AND");
						json.put("Value", "FZ_New;FZ_Old");
						json.put("ValueType", "String");
						json.put("Verb", "Equal");
						conlist.add(json);
						JSONObject json1=new JSONObject();
						json1.put("Terms", conlist);
						map2.put("contract", json1);
					}
				}   
			}
			int dbCount=0;
			for (Map<String, Object> map : plList) {
				if(map.get("payChannelid").equals("GBalanceLimit")&&map.get("productSourceCode").equals("DB.IsticPeriodical")){
					plList.remove(map);
					HashMap<String, Object> m=new HashMap<String, Object>();
					m.put("product_id", Arrays.toString(new String[]{"Income.IsticPeriodical","Income.PeriodicalFulltext"}));
					m.put("productSourceCode", "DB_PEDB");
					m.put("tableName", "期刊增强库");
					m.put("payChannelid", "GBalanceLimit");
					plList.add(m);
					dbCount++;
					break;
				}
			}
			for (Map<String, Object> map : plList) {
				if(map.get("payChannelid").equals("GTimeLimit")&&map.get("productSourceCode").equals("DB.IsticPeriodical")){
					plList.remove(map);
					HashMap<String, Object> m=new HashMap<String, Object>();
					m.put("product_id", Arrays.toString(new String[]{"Income.IsticPeriodical","Income.PeriodicalFulltext"}));
					m.put("productSourceCode", "DB_PEDB");
					m.put("tableName", "期刊增强库");
					m.put("payChannelid", "GTimeLimit");
					plList.add(m);
					dbCount++;
					break;
				}
			}
			for (Map<String, Object> map : plList) {
				if(map.containsKey("productSourceCode")&&map.get("productSourceCode").equals("DB_CSPD")){
					dbCount++;
				}
			}
			if(dbCount>=2){
				for (Map<String, Object> map : plList) {
					if(map.get("productSourceCode").equals("DB_CSPD")&&!map.containsKey("contract")){
						plList.remove(map);
						break;
					}
				}
			}
			
			List<Map<String, Object>> data = this.selectListByRid(pay.getProductDetail());//通过产品id反查资源库 
			if(plList.size()>0){				
				for(Map<String, Object> d : data){
					for(Map<String, Object> plmap : plList){
						if(d.get("productSourceCode").equals(plmap.get("productSourceCode"))){
							if(plmap.get("productSourceCode").equals("DB_WFSD")){
								//判断标准是否选择了详情
								if(plmap.containsKey("contract")){
									d.put("checked", "checked");
								} 
							}else{
							d.put("checked", "checked");
							}
							if(plmap.get("contract")!=null && plmap.get("contract")!=""){
								d.put("contract", JSONObject.fromObject(plmap.get("contract")).getJSONArray("Terms"));
							}
							if(plmap.get("product_id")!=null && plmap.get("product_id")!=""){								
								d.put("product_id",plmap.get("product_id"));
							}
						}

					}
				}
			}
			if(!pay.getType().equals("count") && plList.size()>0 && plList.get(0).get("tableName")!=null){					
				extraData.put("plList", data);
			}
			if(plList.size()==0){
				extraData.put("plList", data);
			}
			if(extraData.size()>0){				
				projectList.add(extraData);
			}
		}
		return projectList;
	}

	@Override
	public List<PayChannelModel> purchaseProject() {
		List<PayChannelModel> list1 = SettingPayChannels.getPayChannels();
		List<PayChannelModel> list = new ArrayList<PayChannelModel>(); 
		for (PayChannelModel payChannelModel : list1) {
			if(payChannelModel.getAccountType().equals("Group")){
				list.add(payChannelModel);
			}
		}
		return list;
	}

	@Override
	public void updateInstitution(String institution, String oldins) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("institution", institution);
		map.put("oldins", oldins);
		personMapper.updateAllInstitution(map);
	}

	@Override
	public WfksAccountidMapping[] getWfksAccountid(String userId,String type){
		return wfksAccountidMappingMapper.getWfksAccountid(userId,type);
	}

	@Override
	public WfksAccountidMapping[] getWfksAccountidLimit(String userId,String type){
		return wfksAccountidMappingMapper.getWfksAccountidLimit(userId,type);
	}
	@Override
	public WfksAccountidMapping[] getWfksAccountidByRelatedidKey(String relatedidKey) {
		return wfksAccountidMappingMapper.getWfksAccountidByRelatedidKey(relatedidKey);
	}

	@Override
	public WfksUserSetting[] getUserSetting(String userId,String type) {
		WfksUserSettingKey key = new WfksUserSettingKey();
		key.setUserType(type);
		key.setUserId(userId);
		return wfksUserSettingMapper.selectByUserId(key);
	}

	@Override
	public int setPartyAdmin(InstitutionalUser com){
		String type = "PartyAdminTime";
		String userId=com.getUserId();
		String partyAdmin=com.getPartyAdmin();
		String password = com.getPartyPassword();
		String partyLimit=com.getPartyLimit();
		int i = wfksAccountidMappingMapper.deleteByUserId(userId,type);
		WfksUserSetting setting = new WfksUserSetting();
		setting.setUserId(userId);
		setting.setUserType("Group");
		setting.setPropertyName(type);
		setting.setPropertyValue(partyAdmin);
		i = wfksUserSettingMapper.deleteByUserId(setting);
		if(!StringUtils.isEmpty(partyLimit)){
			WfksAccountidMapping mapping = new WfksAccountidMapping();
			mapping.setMappingid(GetUuid.getId());
			mapping.setIdAccounttype("Group");
			mapping.setIdKey(userId);
			mapping.setRelatedidAccounttype(type);
			mapping.setRelatedidKey(partyAdmin);
			try{
				SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd");
				mapping.setBegintime(sd.parse(com.getPartyBegintime()));
				mapping.setEndtime(sd.parse(com.getPartyEndtime()));
				mapping.setLastUpdatetime(sd.parse(sd.format(new Date())));
			}catch(ParseException e){
				e.printStackTrace();
			}
			i = wfksAccountidMappingMapper.insert(mapping);
			i = wfksUserSettingMapper.insert(setting);
			Person per = new Person();
			per.setUserId(partyAdmin);
			try {
				per.setPassword(PasswordHelper.encryptPassword(password));
			} catch (Exception e) {
				e.printStackTrace();
			}
			per.setLoginMode(1);	//密码登录
			per.setUsertype(4);	//服务权限用户
			per.setIsFreeze(2);	//解冻
			per.setRegistrationTime(DateUtil.getStringDate());
			JSONObject json = new JSONObject();
			json.put("RelatedGroupId", userId);
			if("isTrial".equals(com.getIsTrial())){//是否试用
				json.put("IsTrialPartyAdminTime", true);
			}else{
				json.put("IsTrialPartyAdminTime", false);
			}
			per.setExtend(json.toString());
			Person person = this.queryPersonInfo(partyAdmin);
			if (person != null) {
				i = personMapper.updateRegisterAdmin(per);
			} else {
				i = personMapper.addRegisterAdmin(per);
			} 
		}
		return i;
	}

	@Override
	public List<Person> queryPersonInId(List<String> userIds) {
		return personMapper.queryPersonInId(userIds);
	}

	@Override
	public List<StandardUnit> findStandardUnit(String orgName, String companySimp) {
		List<StandardUnit> list=standardUnitMapper.findStandardUnit(SALEAGTID+"_"+orgName, companySimp);
		for(StandardUnit su:list){
			su.setOrgName(su.getOrgName().replace(SALEAGTID+"_", ""));
		}
		return list;
	}

	@Override
	public void addUserIns(InstitutionalUser com) {
		// 添加统计分析
		String tongji = com.getTongji();
		userInstitutionMapper.deleteUserIns(com.getUserId());
		if (tongji == null) {
			tongji = "";
		}
		UserInstitution ins = new UserInstitution();
		ins.setUserId(com.getUserId());
		JSONObject obj=new JSONObject();
		obj.put("database_statistics", tongji.contains("database_statistics")?1:0);
		obj.put("resource_type_statistics", tongji.contains("resource_type_statistics")?1:0);
		ins.setStatisticalAnalysis(obj.toString());
		userInstitutionMapper.addUserIns(ins);
	}
	@Override
	public void openBindAuthority(BindAuthorityModel bindAuthorityModel){
		String[] userIds = bindAuthorityModel.getUserId().split(",");
		String[] authoritys = bindAuthorityModel.getBindAuthority().split(",");
		List<String> authorityList = new ArrayList<>();
		for (String authority : authoritys) {
			authorityList.add(bindAuthorityMapping.getAuthorityName(authority));
		}
		OpenBindRequest.Builder request = OpenBindRequest.newBuilder().addAllUserId(Arrays.asList(userIds))
				.setBindType(BindType.forNumber(bindAuthorityModel.getBindType()))
				.setBindLimit(bindAuthorityModel.getBindLimit())
				.setBindValidity(bindAuthorityModel.getBindValidity())
				.setDownloadLimit(bindAuthorityModel.getDownloadLimit())
				.addAllBindAuthority(authorityList)
				.setEmail(bindAuthorityModel.getEmail())
				.setOpenStart(Timestamps.fromMillis(bindAuthorityModel.getOpenBindStart().getTime()))
				.setOpenEnd(Timestamps.fromMillis(bindAuthorityModel.getOpenBindEnd().getTime()));
		bindAuthorityChannel.getBlockingStub().openBindAuthority(request.build());
	}

	@Override
	public BindAuthorityModel getBindAuthority(String userId) {
		BindAuthorityModel bindAuthorityModel = new BindAuthorityModel();
		SearchAccountAuthorityRequest.Builder request = SearchAccountAuthorityRequest.newBuilder().setUserId(userId);
		SearchAccountAuthorityResponse response = bindAuthorityChannel.getBlockingStub().searchAccountAuthority(request.build());
		List<AccountAuthority> accountList = response.getItemsList();
		if (accountList!=null&&accountList.size()>0){
			bindAuthorityModel.setOpenState(true);
			List<String> authorityList = new ArrayList<>();
			for (AccountAuthority accountAuthority : accountList) {
				authorityList.add(accountAuthority.getBindAuthority());
			}
			bindAuthorityModel.setBindType(response.getItems(0).getBindType().getNumber());
			bindAuthorityModel.setBindLimit(response.getItems(0).getBindLimit());
			bindAuthorityModel.setBindValidity(response.getItems(0).getBindValidity());
			bindAuthorityModel.setDownloadLimit(response.getItems(0).getDownloadLimit());
			bindAuthorityModel.setBindAuthority(authorityList.toString());
			bindAuthorityModel.setEmail(response.getItems(0).getEmail());
			bindAuthorityModel.setOpenBindStart(new Date(response.getItems(0).getOpenStart().getSeconds()*1000));
			bindAuthorityModel.setOpenBindEnd(new Date(response.getItems(0).getOpenEnd().getSeconds()*1000));
		}else {
			bindAuthorityModel.setOpenState(false);
		}
		return bindAuthorityModel;
	}
	@Override
	public int getBindAuthorityCount(String userId) {
		SearchAccountAuthorityRequest.Builder request = SearchAccountAuthorityRequest.newBuilder().setUserId(userId);
		SearchAccountAuthorityResponse response =bindAuthorityChannel.getBlockingStub().searchAccountAuthority(request.build());
		List<AccountAuthority> accountList = response.getItemsList();
		if (accountList!=null&&accountList.size()>0){
			return accountList.size();
		}else {
			return 0;
		}
	}

	@Override
	public ServiceResponse editBindAuthority(BindAuthorityModel bindAuthorityModel){
		String[] userIds = bindAuthorityModel.getUserId().split(",");
		String[] authoritys = bindAuthorityModel.getBindAuthority().split(",");
		List<String> authorityList = new ArrayList<>();
		for (String authority : authoritys) {
			authorityList.add(bindAuthorityMapping.getAuthorityName(authority));
		}
		EditBindRequest.Builder request = EditBindRequest.newBuilder().addAllUserId(Arrays.asList(userIds))
				.setBindType(BindType.forNumber(bindAuthorityModel.getBindType()))
				.setBindLimit(bindAuthorityModel.getBindLimit())
				.setBindValidity(bindAuthorityModel.getBindValidity())
				.setDownloadLimit(bindAuthorityModel.getDownloadLimit())
				.addAllBindAuthority(authorityList)
				.setOpenStart(Timestamps.fromMillis(bindAuthorityModel.getOpenBindStart().getTime()))
				.setOpenEnd(Timestamps.fromMillis(bindAuthorityModel.getOpenBindEnd().getTime()))
				.setEmail(bindAuthorityModel.getEmail());
		return  bindAuthorityChannel.getBlockingStub().editBindAuthority(request.build());
	}

	@Override
	public void closeBindAuthority(BindAuthorityModel bindAuthorityModel){

		String[] userIds = bindAuthorityModel.getUserId().split(",");
		CloseBindRequest.Builder request = CloseBindRequest.newBuilder().addAllUserId(Arrays.asList(userIds));
		bindAuthorityChannel.getBlockingStub().closeBindAuthority(request.build());
	}

	@Override
	public List<String> checkBindLimit(List<Map<String, Object>> listMap,Integer bindLimit){
		List<String> beyondId = new ArrayList<>();
		for(Map<String, Object> map : listMap){
			String userId = map.get("userId").toString();
			List<com.wanfangdata.rpc.bindauthority.AccountId> accountIds = new ArrayList<>();
			com.wanfangdata.rpc.bindauthority.AccountId accountId = com.wanfangdata.rpc.bindauthority.AccountId.newBuilder().setAccounttype("Group").setKey(userId).build();
			accountIds.add(accountId);
			SearchBindDetailsRequest countRequest = SearchBindDetailsRequest.newBuilder().addAllRelatedid(accountIds).build();
			SearchBindDetailsResponse countResponse = bindAccountChannel.getBlockingStub().searchBindDetailsOrderUser(countRequest);
			int count = countResponse.getTotalCount();
			if (count>bindLimit){
				beyondId.add(userId);
			}
		}
		return  beyondId;
	}


	@Override
	public UserInstitution getUserInstitution(String userId) {
		return userInstitutionMapper.getUserIns(userId);
	}

	/**
	 * 比较日期大小
	 * 
	 * @param date
	 * @return
	 */
	private boolean getExpired(Date date, Date next) {
		return next.getTime() > date.getTime();
	}

	/**
	 * 获取下一天的日期
	 * 
	 * @return
	 */
	private Date getDay() {
		try {
			SimpleDateFormat fm = new SimpleDateFormat("yyyyMMdd");
			Calendar cal = Calendar.getInstance();
			return fm.parse(fm.format(cal.getTime()));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public void updateSubaccount(InstitutionalUser com,String adminId) throws Exception{
		long time=System.currentTimeMillis();
		String pid = com.getUserId();
		List<String> ls = personMapper.getSubaccount(pid);
		if (ls == null || ls.size() == 0) {
			return;
		}
		Map<String,String> map=SettingUtil.getResouceLimit();
		for(String id:ls){
			//修改项目
			com.setUserId(id);
			List<ResourceDetailedDTO> list=com.getRdlist();
			for(ResourceDetailedDTO dto : list){
				if(dto.getProjectid()!=null){
					//只允许四种资源子账号延期
					if (map.get(dto.getProjectid())==null) {
						continue;
					}
					if(dto.getProjectType().equals("balance")){
						wfks.accounting.handler.entity.BalanceLimitAccount account = (wfks.accounting.handler.entity.BalanceLimitAccount)accountDao.get(new AccountId(dto.getProjectid(),id), new HashMap<String,String>());
						if(account!=null){
							dto.setTotalMoney("0.0");
							this.chargeProjectBalance(com, dto, adminId,new HashMap<String, Object>());
						}
					}else if(dto.getProjectType().equals("time")){
						wfks.accounting.handler.entity.TimeLimitAccount account = (wfks.accounting.handler.entity.TimeLimitAccount)accountDao.get(new AccountId(dto.getProjectid(),id), new HashMap<String,String>());
						if(account!=null){
							this.addProjectDeadline(com, dto,adminId,new HashMap<String, Object>());
						}
					}else if(dto.getProjectType().equals("count")){
						wfks.accounting.handler.entity.CountLimitAccount account = (wfks.accounting.handler.entity.CountLimitAccount)accountDao.get(new AccountId(dto.getProjectid(),id), new HashMap<String,String>());
						if(account!=null){
							dto.setPurchaseNumber("0");
							this.chargeCountLimitUser(com, dto, adminId);
						}
					}
				}
			}
		}
		com.setUserId(pid);
		if(log.isInfoEnabled()){
			log.info("子账号延期处理，耗时："+(System.currentTimeMillis()-time)+"ms");
		}
	}

	@Override
	public void addWfksAccountidMapping(InstitutionalUser com) {
		//先删除再添加
		wfksAccountidMappingMapper.deleteByUserIdAndType(com.getUserId(),"Limit");
		// APP嵌入
		setOpenApp(com);
		// 微信嵌入
		setWebChat(com);
		// 是否添加使用
		List<ResourceDetailedDTO> rdlist = com.getRdlist();
		for(ResourceDetailedDTO dto:rdlist){
			WfksAccountidMapping am = new WfksAccountidMapping();
			am.setMappingid(GetUuid.getId());
			am.setIdAccounttype("Limit");
			am.setIdKey(com.getUserId());
			if(StringUtils.equals(dto.getMode(), "trical")){
				am.setRelatedidAccounttype("trical");
				am.setRelatedidKey(dto.getProjectid());;
			}else{
				continue;
			}
			am.setBegintime(null);
			am.setEndtime(null);
			am.setLastUpdatetime(DateUtil.stringToDate(DateUtil.getStringDate()));
			wfksAccountidMappingMapper.insert(am);
		}

	}

	@Override
	public void updateWfksAccountidMapping(InstitutionalUser com) {
		// APP嵌入
		if(!StringUtils.isEmpty(com.getOpenApp())){
			wfksAccountidMappingMapper.deleteByUserId(com.getUserId(), "openApp");
			setOpenApp(com);
		}
		// 微信嵌入
		if(!StringUtils.isEmpty(com.getOpenWeChat())){
			wfksAccountidMappingMapper.deleteByUserId(com.getUserId(), "openWeChat");
			setWebChat(com);
		}
		// 是否添加使用
		List<ResourceDetailedDTO> rdlist = com.getRdlist();
		for(ResourceDetailedDTO dto:rdlist){
			WfksAccountidMapping am = new WfksAccountidMapping();
			am.setMappingid(GetUuid.getId());
			am.setIdAccounttype("Limit");
			am.setIdKey(com.getUserId());
			if(StringUtils.equals(dto.getMode(), "trical")){
				am.setRelatedidAccounttype("trical");
				am.setRelatedidKey(dto.getProjectid());;
			}else{
				continue;
			}
			wfksAccountidMappingMapper.deleteByIdKeyRelatedId(com.getUserId(), dto.getProjectid());
			am.setBegintime(null);
			am.setEndtime(null);
			am.setLastUpdatetime(DateUtil.stringToDate(DateUtil.getStringDate()));
			wfksAccountidMappingMapper.insert(am);
		}

	}

	//APP嵌入
	private void setOpenApp(InstitutionalUser com){
		if(StringUtils.isEmpty(com.getOpenApp())){
			return;
		}
		WfksAccountidMapping am = new WfksAccountidMapping();
		am.setMappingid(GetUuid.getId());
		am.setIdAccounttype("Limit");
		am.setIdKey(com.getUserId());
		am.setRelatedidAccounttype("openApp");
		am.setRelatedidKey("");
		am.setBegintime(DateUtil.stringToDate1(com.getAppBegintime()));
		am.setEndtime(DateUtil.stringToDate1(com.getAppEndtime()));
		am.setLastUpdatetime(DateUtil.stringToDate(DateUtil.getStringDate()));
		wfksAccountidMappingMapper.insert(am);
	}

	//微信嵌入
	private void setWebChat(InstitutionalUser com){
		//1、先删除
		WfksUserSettingKey key=new WfksUserSettingKey();
		key.setUserId(com.getUserId());
		key.setUserType("WeChat");
		key.setPropertyName("email");
		wfksUserSettingMapper.deleteByUserId(key);
		//2、再添加
		if(StringUtils.isEmpty(com.getOpenWeChat())){
			return;
		}
		WfksAccountidMapping am = new WfksAccountidMapping();
		am.setMappingid(GetUuid.getId());
		am.setIdAccounttype("Limit");
		am.setIdKey(com.getUserId());
		am.setRelatedidAccounttype("openWeChat");
		am.setRelatedidKey("");
		am.setBegintime(DateUtil.stringToDate1(com.getWeChatBegintime()));
		am.setEndtime(DateUtil.stringToDate1(com.getWeChatEndtime()));
		am.setLastUpdatetime(DateUtil.stringToDate(DateUtil.getStringDate()));
		wfksAccountidMappingMapper.insert(am);
		//微信嵌入服务要发邮件
		if(!StringUtils.isEmpty(com.getSendMail())){
			Mail mail=new Mail();
			mail.setReceiver(com.getWeChatEamil());
			mail.setName("后台管理");
			mail.setSubject("已开通微信公众号嵌入服务");
			mail.setMessage(SettingUtil.getSetting("WeChatAppUrl")+Des.enDes(com.getUserId(),com.getPassword()));
			SendMail2 util=new SendMail2();
			util.sendEmail(mail);
		}
		//微信嵌入服务保存邮件地址
		WfksUserSetting setting=new WfksUserSetting();
		setting.setUserType("WeChat");
		setting.setUserId(com.getUserId());
		setting.setPropertyName("email");
		setting.setPropertyValue(com.getWeChatEamil());
		wfksUserSettingMapper.insert(setting);
	}

	@Override
	public List<Person> findInstitutionAllUser(String institution) {
		return personMapper.findInstitutionAllUser(institution);
	}

	@Override
	public UserAccountRestriction getAccountRestriction(String userId) {
		return userAccountRestrictionMapper.getAccountRestriction(userId);
	}

	@Override
	public int addGroupInfo(InstitutionalUser com) {
		groupInfoMapper.deleteGroupInfo(com.getUserId());
		GroupInfo info=new GroupInfo();
		info.setUserId(com.getUserId());
		info.setInstitution(com.getInstitution());
		info.setOrganization(com.getOrganization());
		if(!StringUtils.isEmpty(com.getAdminname())){
			info.setPid(com.getAdminname());
		}else{
			info.setPid(com.getAdminOldName());
		}
		info.setCountryRegion(com.getCountryRegion());
		info.setOrderType(com.getOrderType());
		info.setOrderContent(com.getOrderContent());
		info.setPostCode(com.getPostCode());

		return groupInfoMapper.insertGroupInfo(info);
	}

	@Override
	public int updateGroupInfo(GroupInfo info) {
		return groupInfoMapper.updateGroupInfo(info);
	}

	@Override
	public GroupInfo getGroupInfo(String userId) {
		return groupInfoMapper.getGroupInfo(userId);
	}

	@Override
	public List<String> findUserIdByIp(long start, long end,String userId) {
		UserIp userIp=new UserIp();
		userIp.setBeginIpAddressNumber(start);
		userIp.setEndIpAddressNumber(end);
		userIp.setUserId(userId);
		return userIpMapper.findUserIdByIp(userIp);
	}

	/**
	 * solr查询公用方法
	 * @param map
	 * @return
	 */
	public Map<String,Object> getSolrList(Map<String, Object> map){
		Map<String,Object> allMap=new HashMap<>();
		try{
			SolrService.getInstance(hosts+"/GroupInfo");
			SolrQuery sq=new SolrQuery();
			sq.set("collection", "GroupInfo");
			Integer pageSize=(Integer) map.get("pageSize");
			Integer pageNum=(Integer) map.get("pageNum");

			String institution="";
			if(map.get("institution")==null){
				institution=null;
			}else{
				String[] institutionArray=map.get("institution").toString().trim().split(" ");
				institution+="(";
				for (String string : institutionArray) {
					institution+="*"+string+"* AND ";
				}
				institution=institution.substring(0, institution.length()-4);
				institution+=")";
			}

			sq.setRows(pageSize);
			sq.setStart(pageSize*pageNum);
			StringBuffer query=new StringBuffer("");
			InstitutionUtils.addField(query,"Id",(String) map.get("userId"));//机构ID
			InstitutionUtils.addField(query,"Institution",institution);//机构ID
			InstitutionUtils.addField(query,"ParentId",(String) map.get("pid"));//机构管理员Id
			InstitutionUtils.addField(query,"PayChannelId",(String) map.get("resource"));//购买项目
			InstitutionUtils.addField(query,"Organization",(String) map.get("Organization"));//机构类型
			InstitutionUtils.addField(query,"PostCode",(String) map.get("PostCode"));//地区
			InstitutionUtils.addField(query,"OrderType",(String) map.get("OrderType"));//工单类型
			//内部工单
			if(StringUtils.equals((String) map.get("OrderType"), "inner")){
				InstitutionUtils.addField(query,"OrderContent",(String) map.get("OrderContent"));
			}
			//是否有机构管理员
			if(!StringUtils.isEmpty((String) map.get("admin"))){
				InstitutionUtils.addField(query,"ParentId","*");
			}
			//是否有机构子账号
			if(!StringUtils.isEmpty((String) map.get("Subaccount"))){
				InstitutionUtils.addField(query,"HasChildGroup","true");
			}
			//是否开通统计分析权限
			if(!StringUtils.isEmpty((String) map.get("tongji"))){
				InstitutionUtils.addField(query,"StatisticalAnalysis","*");
			}
			//购买项目是否试用
			if(!StringUtils.isEmpty((String) map.get("trical"))){
				InstitutionUtils.addField(query,"IsTrial","*");
			}
			//开通app权限
			if(!StringUtils.isEmpty((String) map.get("openApp"))){
				InstitutionUtils.addField(query,"AppStartTime","*");
			}
			//开通微信app权限
			if(!StringUtils.isEmpty((String) map.get("openWeChat"))){
				InstitutionUtils.addField(query,"WeChatStartTime","*");
			}
			//开通党建管理员权限
			if(!StringUtils.isEmpty((String) map.get("PartyAdminTime"))){
				InstitutionUtils.addField(query,"PartyAdminId","*");
			}

			//验证Ip
			Long ipstart=(Long) map.get("ipstart");
			Long ipend=(Long) map.get("ipend");
			if(ipstart!=null&&ipend!=null&&ipstart.longValue()<=ipend.longValue()){
				List<String> userIdList=this.findUserIdByIp(ipstart, ipend,(String) map.get("userId"));
				if(userIdList!=null&&userIdList.size()>0){
					if(query.length()>0){
						query.append(" AND ");
					}
					query.append(" Id:("+String.join(" ", userIdList)+")");
				}
			}
			sq.setQuery(query.toString());
			if(log.isInfoEnabled()){
				log.info("查询条件"+query.toString());
			}
			List<SortClause> scList=new ArrayList<>();
			scList.add(new SortClause("LoginMode", ORDER.asc));//登录方式排序
			scList.add(new SortClause("IsFreeze", ORDER.asc));//按照冻结排序
			sq.setSorts(scList);
			SolrDocumentList sdList=SolrService.getDataList(sq);
			allMap.put("data",InstitutionUtils.getFieldMap(sdList));
			Long num=sdList.getNumFound();
			allMap.put("num",num.intValue());
		}catch(Exception e){
			log.error("solr查询异常", e);
		}
		return allMap;
	}

	@Override
	public List<Map<String, Set<String>>> getProjectCheck(
			InstitutionalUser user, String userid) {

		List<Map<String, Set<String>>> projectCheck=new ArrayList<Map<String, Set<String>>>();
		List<ResourceDetailedDTO> userrdlist=user.getRdlist();
		List<Map<String, Object>> projectlist=getProjectInfo(userid);
		for(int i=0;i<projectlist.size();i++){
			Map<String,Set<String>> errormap=new HashMap<>();
			String name=(String) projectlist.get(i).get("name");
			if(projectlist.get(i).containsKey("plList")){
				//获取重复ip用户的购买数据库信息
				List<Map<String, Object>> tableplList=(List<Map<String, Object>>) projectlist.get(i).get("plList");
				//用于存放冲突库信息
				Set<String> errorSet=new HashSet();
				for(int h=0;h<tableplList.size();h++){
					if(tableplList.get(h).containsKey("checked")&&tableplList.get(h).get("checked").equals("checked")){
						String tableproduct=(String)tableplList.get(h).get("productSourceCode");
						for(int y=0;y<userrdlist.size();y++){
							if(userrdlist.get(y).getProjectid()!=null&&userrdlist.get(y).getRldto()!=null){
								List<ResourceLimitsDTO> listrld = userrdlist.get(y).getRldto();
								for(int d=0;d<listrld.size();d++){
									//判断冲突用户选择的数据库  注册用户有没有选择  如果选择
									if(StringUtils.isNotEmpty(listrld.get(d).getResourceid()) && listrld.get(d).getResourceid().equals(tableproduct)){
										if(tableplList.get(h).containsKey("contract")){
											//判断详情是否冲突
											boolean boo=Contrast(tableplList.get(h),listrld.get(d));
											if(boo){
												errorSet.add(tableplList.get(h).get("abbreviation")+"库详情设置");
											}
										}else{
											errorSet.add(tableplList.get(h).get("abbreviation")+"库");
										}
									}
								}
								if(errorSet.size()>0){
									errormap.put(name, errorSet);
								}
							}
						}
					}
				}
			}else{
				//没有选择数据库的检测（万方检测 或者万方分析）
				String payChannelid= (String) projectlist.get(i).get("payChannelid");
				String projectname=(String) projectlist.get(i).get("name");
				for(int y=0;y<userrdlist.size();y++){
					Set<String> set=new HashSet<>();
					if(userrdlist.get(y).getProjectid()!=null){
						String projectid=userrdlist.get(y).getProjectid();
						if(projectname.startsWith("万方分析")&&userrdlist.get(y).getProjectname().startsWith("万方分析")){
							//万方分析冲突
							errormap.put(projectname, set);
						}else if(payChannelid.equals(projectid)||payChannelid.equals(projectid+"Count")||projectid.equals(payChannelid+"Count")){
							//冲突
							errormap.put(projectname, set);
						}
					}	
				}
			}
			if(errormap.size()>0){
				projectCheck.add(errormap);
			}
		} 
		return projectCheck;
	}
	private boolean Contrast(Map<String, Object> tableContract,ResourceLimitsDTO rld){
		boolean boo=false;
		String source="source";
		if(tableContract.containsKey("productSourceCode")){
			source=(String) tableContract.get("productSourceCode");
		}
		//学位详情
		if(source.equals("DB_CDDB")){
			List<JSONObject> conlist=(List<JSONObject>) tableContract.get("contract");
			for(int i=0;i<conlist.size();i++){ 
				JSONArray json=(JSONArray) conlist.get(i).get("Value");
				String[] value=new String[json.size()];
				for(int m=0;m<json.size();m++){
					value[m]=(String) json.get(m);
				}
				String[] resource=transfer(rld.getDegreeClc()).split(",");//注册用户数据库详情去除特殊字符  转换成数组
				for(int y=0;y<value.length;y++){
					for(int t=0;t<resource.length;t++){
						if(resource[t].startsWith(value[y])||value[y].startsWith(resource[t])){
							boo=true;
							break;
						}
					}
				}
			}
		}
		//会议详情
		if(source.equals("DB_CCPD")){
			List<JSONObject> conlist=(List<JSONObject>) tableContract.get("contract");
			for(int i=0;i<conlist.size();i++){
				JSONArray json=(JSONArray) conlist.get(i).get("Value");
				String[] value=new String[json.size()];
				for(int m=0;m<json.size();m++){
					value[m]=(String) json.get(m);
				}
				String[] resource=transfer(rld.getConferenceClc()).split(",");//注册用户数据库详情去除特殊字符  转换成数组
				for(int y=0;y<value.length;y++){
					for(int t=0;t<resource.length;t++){
						if(resource[t].startsWith(value[y])||value[y].startsWith(resource[t])){
							boo=true;
							break;
						}
					}
				}
			}
		}

		//地方志
		if(source.equals("DB_CLGD")){
			List<JSONObject> conlist=(List<JSONObject>) tableContract.get("contract");
			//存储的值
			String[] tableGazetteersId=null;
			String[] tableItemId=null;
			String tableGazetteersArea=null;
			String[] tableGazetteersAlbum=null;
			String tableGazetteersType=null;
			String tableGazetteersLevel=null;

			String tableGazetteersStartTime=null;
			String tableGazetteersEndTime=null;
			String tableGazetteersOldArea=null;
			String tableGazetteersOldStartTime=null;
			String tableGazetteersOldEndTime=null;
			//页面上的值
			String gazetteersType=null;
			String gazetteersLevel=null;
			String[] gazetteerId=null;
			String[] itemId=null;
			String gazetteersArea=null;
			String[] gazetteersAlbum=null;

			String gazetteersOldType=null;
			String gazetteersStartTime=null;
			String gazetteersEndTime=null;
			String gazetteersOldArea=null;
			String gazetteersOldStartTime=null;
			String gazetteersOldEndTime=null;

			if(StringUtils.isNoneEmpty(rld.getGazetteersType())){
				gazetteersType=rld.getGazetteersType();
			}
			if(StringUtils.isNoneEmpty(rld.getGazetteersLevel())){
				gazetteersLevel=rld.getGazetteersLevel();
			}
			if(StringUtils.isNoneEmpty(rld.getGazetteersId())){
				gazetteerId=rld.getGazetteersId().split(";");
			}
			if(StringUtils.isNoneEmpty(rld.getItemId())){
				itemId=rld.getItemId().split(";");
			}
			if(StringUtils.isNoneEmpty(rld.getGazetteersArea())){
				gazetteersArea= rld.getGazetteersArea();
			}
			if(StringUtils.isNoneEmpty(rld.getGazetteersAlbum())){
				String json=rld.getGazetteersAlbum();
				gazetteersAlbum=json.split(";");
			}
			if(StringUtils.isNoneEmpty(rld.getGazetteersOldType())){
				gazetteersOldType=rld.getGazetteersOldType();
			}
			if(StringUtils.isNoneEmpty(rld.getGazetteersStartTime())){
				gazetteersStartTime=rld.getGazetteersStartTime();
			}
			if(StringUtils.isNoneEmpty(rld.getGazetteersEndTime())){
				gazetteersEndTime=rld.getGazetteersEndTime();
			}
			if(StringUtils.isNoneEmpty(rld.getGazetteersOldArea())){
				gazetteersOldArea=rld.getGazetteersOldArea();
			}
			if(StringUtils.isNoneEmpty(rld.getGazetteersOldStartTime())){
				gazetteersOldStartTime=rld.getGazetteersOldStartTime();
			}
			if(StringUtils.isNoneEmpty(rld.getGazetteersOldEndTime())){
				gazetteersOldEndTime=rld.getGazetteersOldEndTime();
			}
			if(gazetteersType!=null&&gazetteersOldType!=null){
				gazetteersType=null;
			}else if(gazetteersType==null&&gazetteersOldType!=null){
				gazetteersType=gazetteersOldType;
			}
			if(gazetteersType==null&&
					gazetteerId==null&&
					itemId==null&&
					gazetteersArea==null&&
					gazetteersAlbum==null&&
					gazetteersStartTime==null&&
					gazetteersEndTime==null&&
					gazetteersOldArea==null&&
					gazetteersOldType==null&&
					gazetteersOldStartTime==null&&
					gazetteersOldEndTime==null
					){
				boo=true;
			}
			for(int i=0;i<conlist.size();i++){
				//自定义导入正本数据读取
				if(conlist.get(i).get("Field").equals("gazetteers_id")){
					String json=(String) conlist.get(i).get("Value");
					tableGazetteersId=json.split(";");
				}
				if(conlist.get(i).get("Field").equals("item_id")){
					String json=(String) conlist.get(i).get("Value");
					tableItemId=json.split(";");
				}
				if(conlist.get(i).get("Field").equals("gazetteers_type")){
					tableGazetteersType=(String) conlist.get(i).get("Value");
				}
				if(conlist.get(i).get("Field").equals("gazetteers_level")){
					tableGazetteersLevel=(String) conlist.get(i).get("Value");
				}
				if(conlist.get(i).get("Field").equals("gazetteers_area")){
					tableGazetteersArea=(String) conlist.get(i).get("Value");
				}
				if(conlist.get(i).get("Field").equals("gazetteers_album")){
					String jsona=(String) conlist.get(i).get("Value");
					tableGazetteersAlbum=jsona.split(";");
				}
				if(conlist.get(i).get("Field").equals("gazetteers_startTime")){
					tableGazetteersStartTime=(String) conlist.get(i).get("Value");
				}
				if(conlist.get(i).get("Field").equals("gazetteers_endTime")){
					tableGazetteersEndTime=(String) conlist.get(i).get("Value");
				}
				if(conlist.get(i).get("Field").equals("gazetteers_old_area")){
					tableGazetteersOldArea=(String) conlist.get(i).get("Value");
				}
				if(conlist.get(i).get("Field").equals("gazetteers_old_startTime")){
					tableGazetteersOldStartTime=(String) conlist.get(i).get("Value");
				}
				if(conlist.get(i).get("Field").equals("gazetteers_old_endTime")){
					tableGazetteersOldEndTime=(String) conlist.get(i).get("Value");
				}

			}
			boolean bao=false;
			//新方志地区对比
			if(tableGazetteersArea!=null && gazetteersArea!=null){
				String[] tableArea=tableGazetteersArea.split("_");
				String[] area=gazetteersArea.split("_");
				if(tableArea[0].equals(area[0])){
					if(tableArea.length>=area.length){
						bao=area[area.length-1].equals(tableArea[area.length-1]);
					}else{
						bao=area[tableArea.length-1].equals(tableArea[tableArea.length-1]);
					}
				}
			}
			//旧方志地区对比  
			if(tableGazetteersOldArea!=null && gazetteersOldArea!=null){
				String[] tableOldArea=tableGazetteersOldArea.split("_");
				String[] oldArea=gazetteersOldArea.split("_");
				if(tableOldArea[0].equals(oldArea[0])){
					if(tableOldArea.length>=oldArea.length){
						bao=oldArea[oldArea.length-1].equals(tableOldArea[oldArea.length-1]);
					}else{
						bao=oldArea[tableOldArea.length-1].equals(tableOldArea[tableOldArea.length-1]);
					}
				}
			}
			//判断分类自定义
			if(tableGazetteersId!=null||tableItemId!=null || gazetteerId!=null||itemId!=null){
				//自定义对比
				if(tableGazetteersId!=null&&gazetteerId!=null){
					for(int y=0;y<tableGazetteersId.length;y++){
						for(int t=0;t<gazetteerId.length;t++){
							if(tableGazetteersId[y].equals(gazetteerId[t])){
								boo=true;
								break;
							}
						}
					}
				}
				if(tableItemId!=null&&itemId!=null){
					for(int y=0;y<tableItemId.length;y++){
						for(int t=0;t<itemId.length;t++){
							if(tableItemId[y].equals((itemId[t]))){
								boo=true;
								break;
							}
						}
					}
				}
			}else{
				//判断是否有冲突
				boolean zyfl=false;  //资源分类
				boolean dq=bao;      //地区
				boolean sjfl=false;  //数据分类
				boolean zjfl=false;  //专辑分类
				boolean zygxsj=false; //资源更新时间
				if(tableGazetteersType==null||gazetteersType==null){
					zyfl=true;
				}
				if(tableGazetteersType!=null&&gazetteersType!=null&&tableGazetteersType.equals(gazetteersType)){
					zyfl=true;
				}
				//判断新方志时间是否冲突
				int itgst=StringUtils.isBlank(tableGazetteersStartTime)?0:Integer.parseInt(tableGazetteersStartTime);
				int itget=StringUtils.isBlank(tableGazetteersEndTime)?9999:Integer.parseInt(tableGazetteersEndTime);
				int igst=StringUtils.isBlank(gazetteersStartTime)?0:Integer.parseInt(gazetteersStartTime);
				int iget=StringUtils.isBlank(gazetteersEndTime)?9999:Integer.parseInt(gazetteersEndTime);
				if(iget>=itgst&&igst<=itget){
					zygxsj=true;
				}
				//判断旧方志时间是否冲突
				int itgost=StringUtils.isBlank(tableGazetteersOldStartTime)?0:Integer.parseInt(tableGazetteersOldStartTime);
				int itgoet=StringUtils.isBlank(tableGazetteersOldEndTime)?9999:Integer.parseInt(tableGazetteersOldEndTime);
				int igost=StringUtils.isBlank(gazetteersOldStartTime)?0:Integer.parseInt(gazetteersOldStartTime);
				int igoet=StringUtils.isBlank(gazetteersOldEndTime)?9999:Integer.parseInt(gazetteersOldEndTime);
				if(igoet>=itgost&&igost<=itgoet){
					zygxsj=true;
				}

				if(tableGazetteersLevel.equals(gazetteersLevel)){
					sjfl=true;
					if(tableGazetteersAlbum==null||gazetteersAlbum==null){
						zjfl=true;
					}
				}
				if(sjfl&&tableGazetteersAlbum!=null && gazetteersAlbum!=null){
					for(int y=0;y<tableGazetteersAlbum.length;y++){
						for(int t=0;t<gazetteersAlbum.length;t++){
							if(tableGazetteersAlbum[y].equals(gazetteersAlbum[t])){
								zjfl=true;
								break;
							}
						}
					}
				}
				if(tableGazetteersArea==null || gazetteersArea==null){
					dq=true;
				}
				if(zygxsj||dq||zjfl){
					boo=true;
				}
			}
		}
		//期刊   需判断选刊还是选文献还是都选
		if(source.equals("DB_CSPD")){
			List<JSONObject> conlist=(List<JSONObject>) tableContract.get("contract");
			if(StringUtils.isBlank(rld.getPerioInfoClc())&&StringUtils.isBlank(rld.getJournalClc())){
				boo=true;
			}
			for(int i=0;i<conlist.size();i++){
				if(conlist.get(i).get("Field").equals("perioInfo_CLC")){
					JSONArray json=(JSONArray) conlist.get(i).get("Value");
					String[] value=new String[json.size()];
					for(int m=0;m<json.size();m++){
						value[m]=(String) json.get(m);
					}
					String[] resource=null;
					if(StringUtils.isNotBlank(rld.getPerioInfoClc())){
						resource=transfer(rld.getPerioInfoClc()).split(",");
					}
					//注册用户数据库详情去除特殊字符  转换成数组
					for(int y=0;y<value.length;y++){
						if(resource!=null){
							for(int t=0;t<resource.length;t++){
								if(resource[t].startsWith(value[y])||value[y].startsWith(resource[t])){
									boo=true;
									break;
								}
							}
						}
					}
				}
				if(conlist.get(i).get("Field").equals("journal_CLC")){
					JSONArray json=(JSONArray) conlist.get(i).get("Value");
					String[] value=new String[json.size()];
					for(int m=0;m<json.size();m++){
						value[m]=(String) json.get(m);
					}
					String[] resource=null;
					if(StringUtils.isNotBlank(rld.getJournalClc())){
						resource=transfer(rld.getJournalClc()).split(",");
					}
					for(int y=0;y<value.length;y++){
						if(resource!=null){
							for(int t=0;t<resource.length;t++){
								if(resource[t].startsWith(value[y])||value[y].startsWith(resource[t])){
									boo=true;
									break;
								}
							}
						}
					}
				}
			}
		}
		// 标准
		if(source.equals("DB_WFSD")){
			List<JSONObject> conlist=(List<JSONObject>) tableContract.get("contract");
			System.out.println("rld="+rld);
			//存放查询出来的值
			String[] tableStandardTypes=null;
			//存放页面比较值
			String[] standardTypes=null;

			for(int i=0;i<conlist.size();i++){
				if(conlist.get(i).get("Field").equals("standard_types")){
					JSONArray json=(JSONArray) conlist.get(i).get("Value");
					tableStandardTypes=new String[json.size()];
					for(int m=0;m<json.size();m++){
						tableStandardTypes[m]=(String) json.get(m);
					}
				}
				if(StringUtils.isNoneEmpty(rld.getStandardTypes())){
					standardTypes=rld.getStandardTypes();
					if(standardTypes.length==0){
						String[] str=new String[]{"WFLocal","质检出版社"};
						standardTypes=str;
					}
					for (String string : standardTypes) {
						for (int j = 0; j < tableStandardTypes.length; j++) {
							if(tableStandardTypes[j].equals(string)){
								boo=true;
								break;
							}
						}
					}
				}

			}
			//判断行业标准是否冲突
			for(int i=0;i<tableStandardTypes.length;i++){
				if(standardTypes==null){
					boo=true;
					break;
				} 
				if(tableStandardTypes[i].equals("WFLocal")){
					for(int y=0;y<standardTypes.length;y++){
						if(standardTypes[y].equals("WFLocal")){
							boo=true;
							break;
						}
					}
				}
			}
		}
		//专利
		if(source.equals("DB_WFPD")){
			List<JSONObject> conlist=(List<JSONObject>) tableContract.get("contract");
			for(int i=0;i<conlist.size();i++){
				JSONArray json=(JSONArray) conlist.get(i).get("Value");
				String[] value=new String[json.size()];
				for(int m=0;m<json.size();m++){
					value[m]=(String) json.get(m);
				}
				String[] resource=transfer(rld.getPatentIpc()).split(",");//注册用户数据库详情去除特殊字符  转换成数组
				for(int y=0;y<value.length;y++){
					for(int t=0;t<resource.length;t++){
						if(resource[t].startsWith(value[y])||value[y].startsWith(resource[t])){
							boo=true;
							break;
						}
					}
				}
			}
		}
		return boo;
	}
	public static String transfer(String param) {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < param.length(); i++) {
			char c = param.charAt(i);
			if (c == '[' ||c==']' ||c=='{' || c=='}' ) {
				continue;
			}
			sb.append(c);
		}

		return sb.toString();
	}
	public static String trans(String param) {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < param.length(); i++) {
			char c = param.charAt(i);
			if (c=='1'||c=='2'||c=='3'||c=='4'||c=='5'||c=='6'||c=='7'||c=='8'||c=='9'||c=='.'||c=='0') {
				sb.append(c);
			}
		}
		return sb.toString();
	}
}
