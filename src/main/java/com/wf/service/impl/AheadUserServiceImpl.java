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

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.lang3.StringUtils;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

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
import com.utils.SendMail2;
import com.utils.SettingUtil;
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
	public boolean registerInfo(InstitutionalUser com) {
		// 添加机构名称
		this.addRegisterInfo(com);
		// 添加用户IP
		if (com.getLoginMode().equals("0") || com.getLoginMode().equals("2")) {
			this.addUserIp(com);
		}
		// 机构子账号限定
		this.setAccountRestriction(com);
		// 添加党建管理员
		this.setPartyAdmin(com);
		// 添加机构管理员
		this.addAdmin(com);
		// 统计分析权限
		this.addUserIns(com);
		// 开通用户角色
		this.addWfksAccountidMapping(com);
		// 开通用户权限
		this.addGroupInfo(com);
		return true;
	}
	
	@Transactional(propagation = Propagation.REQUIRED , readOnly = false)
	@Override
	public boolean updateinfo(InstitutionalUser com) {		
		// 修改机构名称
		this.updateUserInfo(com);
		//修改用户IP
		if (com.getLoginMode().equals("0") || com.getLoginMode().equals("2")) {
			this.updateUserIp(com);
		} else {
			this.deleteUserIp(com.getUserId());
		}
		// 机构子账号限定
		this.setAccountRestriction(com);
		// 党建管理员
		this.setPartyAdmin(com);
		// 机构管理员
		this.addAdmin(com);
		// 统计分析权限
		this.addUserIns(com);
		// 用户权限
		this.addWfksAccountidMapping(com);
		// 开通用户权限
		this.addGroupInfo(com);
		//修改机构名称
		if(!StringUtils.equals(com.getInstitution(), com.getOldInstitution())){
			this.updateInstitution(com.getInstitution(),com.getOldInstitution());
		}
		return true;
	}
	
	@Transactional(propagation = Propagation.REQUIRED , readOnly = false)
	@Override
	public boolean batchRegisterInfo(InstitutionalUser com,Map<String,Object> map) {
		// 添加机构名称
		this.addRegisterInfo(com);
		// 添加用户IP 批量的登录方式(用户密码、用户密码+IP)
		if ("2".equals(com.getLoginMode())) {
			String ip=(String) map.get("ip");
			ip=ip.replace("\r\n", "\n").replace("\n", "\r\n");
			com.setIpSegment(ip);
			this.updateUserIp(com);
		}
		// 机构子账号限定
		this.setAccountRestriction(com);
		// 添加党建管理员
		this.setPartyAdmin(com);
		// 添加机构管理员
		this.addAdmin(com);
		//统计分线权限
		this.addUserIns(com);
		// 开通用户角色
		this.addWfksAccountidMapping(com);
		// 开通用户权限
		this.addGroupInfo(com);
		return true;
	}

	@Transactional(propagation = Propagation.REQUIRED , readOnly = false)
	@Override
	public boolean batchUpdateInfo(InstitutionalUser com) {
		// TODO Auto-generated method stub
		return false;
	}
	
	//添加机构管理员
	private void addAdmin(InstitutionalUser com){
		if (StringUtils.isEmpty(com.getManagerType())) {
			return;
		}
		if("new".equals(com.getManagerType())&&StringUtils.isEmpty(com.getAdminname())||
				"old".equals(com.getManagerType())&&StringUtils.isEmpty(com.getAdminOldName())){
			return;
		}
		String adminId = com.getManagerType().equals("new") ? com.getAdminname() : com
				.getAdminOldName();
		Person per=this.queryPersonInfo(adminId);
		if(per!=null){
			this.updateRegisterAdmin(com);
		}else{
			this.addRegisterAdmin(com);
		}
		if(StringUtils.isNotBlank(com.getAdminIP())){
			this.deleteUserIp(com.getAdminname());
			this.addUserAdminIp(com);
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
		if(StringUtils.isNotBlank(com.getAdminname()) || StringUtils.isNotBlank(com.getAdminOldName())){	
			if(com.getManagerType().equals("new")){
				p.setPid(com.getAdminname());
			}else{
				if(com.getAdminOldName().indexOf("/")!=-1){
					p.setPid(com.getAdminOldName().substring(0, com.getAdminOldName().indexOf("/")));
				}else{
					p.setPid(com.getAdminOldName());
				}
			}
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
		} catch (Exception e) {
			e.printStackTrace();
		}
		per.setLoginMode(1);
		per.setUsertype(1);
		per.setIsFreeze(2);
		per.setRegistrationTime(DateUtil.getStringDate());
		per.setInstitution(com.getInstitution());
		per.setAdminEmail(com.getAdminEmail());
		return personMapper.addRegisterAdmin(per);
	}
	
	@Override
	public int updateRegisterAdmin(InstitutionalUser com){
		//机构管理员注册
		Person per = new Person();
		per.setUserId(com.getAdminname());
		try {
			per.setPassword(PasswordHelper.encryptPassword(com.getAdminpassword()));
		} catch (Exception e) {
			e.printStackTrace();
		}
		per.setInstitution(com.getInstitution());
		per.setAdminEmail(com.getAdminEmail());
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
				userIp.setUserId(com.getAdminname());
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

		boolean isSuccess = groupAccountUtil.deleteAccount(account, httpRequest.getRemoteAddr(),adminId);
		int flag = 0;
		if (isSuccess) {
			flag = 1;
		} else {
			flag = 0;
		}
		return flag;
	}
	
	@Override
	public int addProjectBalance(InstitutionalUser com, ResourceDetailedDTO dto, String adminId)
			throws Exception {
		
		//创建一个余额账户
		BalanceLimitAccount account = new BalanceLimitAccount();
		account.setUserId(com.getUserId());// 机构用户名
		account.setOrganName(com.getInstitution());// 机构名称
		account.setPayChannelId(dto.getProjectid());// 支付渠道id
		SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd");
		account.setBeginDateTime(sd.parse(dto.getValidityStarttime()));
		account.setEndDateTime(sd.parse(dto.getValidityEndtime()));// 失效时间，可以精确到秒
		account.setBalance(BigDecimal.valueOf(dto.getTotalMoney()));
		// 根据token可获取管理员登录信息
		// String authToken = "Admin."+adminId;
		// 调用添加注册余额限时账户方法
		// 第一个参数如果是充值，就传入充值前的账户信息，如果是注册就传入null
		// 第二个参数起传递账户信息,userIP,auto_token,是否重置金额
		boolean isSuccess = groupAccountUtil.addBalanceLimitAccount(null, account, httpRequest.getRemoteAddr(), adminId, false);
		int flag = 0;
		if (isSuccess) {
			flag = 1;
		} else {
			flag = 0;
		}
		return flag;
	}
	
	@Override
	public int addProjectDeadline(InstitutionalUser com, ResourceDetailedDTO dto, String adminId)
			throws Exception {
		
		if (StringUtils.equals(dto.getValidityStarttime(), dto.getValidityStarttime2())
				&& StringUtils.equals(dto.getValidityEndtime(), dto.getValidityEndtime2())
				&& !StringUtils.equals("1", dto.getIschange())) {
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
		boolean isSuccess = groupAccountUtil.addTimeLimitAccount(account, httpRequest.getRemoteAddr(), adminId);//提交注册或充值请求
		int flag = 0;
		if (isSuccess) {
			flag = 1;
		} else {
			flag = 0;
		}
		if(flag==1&&StringUtils.equals(dto.getIschange(),"1")){//限时转化为余额
			UserAccount acc = new UserAccount();
			acc.setUserId(com.getUserId());
			acc.setPayChannelId("GBalanceLimit");
			acc.setOrganName(com.getInstitution());
			acc.setBeginDateTime(sd.parse(dto.getValidityStarttime()));
			acc.setEndDateTime(sd.parse(dto.getValidityEndtime()));
			boolean succ=groupAccountUtil.deleteAccount(acc, httpRequest.getRemoteAddr(), adminId);
			if(!succ){
				flag=1;
			}
		}
		return flag;
	}
	
	@Override
	public int addProjectNumber(InstitutionalUser com, ResourceDetailedDTO dto, String adminId)
			throws Exception {
		
		// 创建一个次数账户
		CountLimitAccount account = new CountLimitAccount();
		account.setUserId(com.getUserId());// 机构用户名
		account.setOrganName(com.getInstitution());// 机构名称
		account.setPayChannelId(dto.getProjectid());// 支付渠道id
		SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd");
		account.setBeginDateTime(sd.parse(dto.getValidityStarttime()));
		account.setEndDateTime(sd.parse(dto.getValidityEndtime()));// 失效时间，可以精确到秒
		account.setBalance(dto.getPurchaseNumber());// 充值次数
		// 根据token可获取管理员登录信息
		// String authToken = "Admin."+adminId;
		// 调用添加注册余额限时账户方法
		// 第一个参数如果是充值，就传入充值前的账户信息，如果是注册就传入null
		// 第二个参数起传递账户信息，userIP，auto_token
        boolean isSuccess = groupAccountUtil.addCountLimitAccount(null, account, httpRequest.getRemoteAddr(), adminId,false);
        int flag = 0;
		if (isSuccess) {
        	flag = 1;
        } else {
        	flag = 0;
        }
		return flag;
	}
	
    /**
     * 为按次数计费用户充值(更新项目)
     *
     * @throws Exception
     */
	@Override
	public int chargeCountLimitUser(InstitutionalUser com, ResourceDetailedDTO dto, String adminId)
			throws Exception {
    	
    	if(dto.getPurchaseNumber()==0&&StringUtils.equals(dto.getValidityStarttime(), dto.getValidityStarttime2())
				&& StringUtils.equals(dto.getValidityEndtime(), dto.getValidityEndtime2())){
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
		count.setBalance(dto.getPurchaseNumber());
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
	    	}
	    	resetCount = true;
		}
        boolean isSuccess = groupAccountUtil.addCountLimitAccount(before, count, httpRequest.getRemoteAddr(), adminId, resetCount);
		int flag = 0;
		if (isSuccess) {
			flag = 1;
		} else {
			flag = 0;
		}
		return flag;
    }
    
    /**
     * 为机构余额账户充值
     */
	@Override
	public int chargeProjectBalance(InstitutionalUser com, ResourceDetailedDTO dto, String adminId)
			throws Exception {
		
		if (dto.getTotalMoney() == 0
				&& StringUtils.equals(dto.getValidityStarttime(), dto.getValidityStarttime2())
				&& StringUtils.equals(dto.getValidityEndtime(), dto.getValidityEndtime2())) {
			return 1;
		}
		// 需要更新的数据
		BalanceLimitAccount account = new BalanceLimitAccount();
		account.setUserId(com.getUserId());// 机构用户名
		account.setOrganName(com.getInstitution());// 机构名称
		account.setPayChannelId(dto.getProjectid());// 支付渠道id
		SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd");
		account.setBeginDateTime(sd.parse(dto.getValidityStarttime()));
		account.setEndDateTime(sd.parse(dto.getValidityEndtime()));
		account.setBalance(BigDecimal.valueOf(dto.getTotalMoney()));
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
            }
			resetMoney = true;
		}
		int flag = 0;
        boolean isSuccess = groupAccountUtil.addBalanceLimitAccount(before, account, httpRequest.getRemoteAddr(), adminId, resetMoney);
		if(StringUtils.equals(dto.getIschange(),"1")){//限时转化为余额
			UserAccount acc = new UserAccount();
			acc.setUserId(com.getUserId());
			acc.setPayChannelId("GTimeLimit");
			acc.setOrganName(com.getInstitution());
			acc.setBeginDateTime(sd.parse(dto.getValidityStarttime()));
			acc.setEndDateTime(sd.parse(dto.getValidityEndtime()));
			boolean succ=groupAccountUtil.deleteAccount(account, httpRequest.getRemoteAddr(), adminId);
			if(!succ){
				flag=1;
			}
		}
		if (isSuccess) {
			flag = 1;
		} else {
			flag = 0;
		}
		return flag;
    }
	
	@Override
    public boolean checkLimit(InstitutionalUser com,ResourceDetailedDTO dto) throws Exception{
    	try{
			if ("balance".equals(dto.getProjectType())) {
				if (dto.getTotalMoney() == 0 && StringUtils.equals(dto.getValidityStarttime(), dto.getValidityStarttime2())
						&& StringUtils.equals(dto.getValidityEndtime(), dto.getValidityEndtime2())) {
					return true;
				}
	    		wfks.accounting.handler.entity.BalanceLimitAccount account = (wfks.accounting.handler.entity.BalanceLimitAccount)
    	    		accountDao.get(new AccountId(dto.getProjectid(),com.getUserId()), new HashMap<String,String>());
	    		if(account==null){
	    			if(dto.getTotalMoney()<=0){
	    				return false;
	    			}
	    			return true;
	    		}
	    		if(account.getBalance().intValue()+dto.getTotalMoney()<0){
	    			return false;
	    		}
			} else if ("count".equals(dto.getProjectType())) {
				if (dto.getPurchaseNumber()==0 && StringUtils.equals(dto.getValidityStarttime(), dto.getValidityStarttime2())
						&& StringUtils.equals(dto.getValidityEndtime(), dto.getValidityEndtime2())) {
					return true;
				}
	        	wfks.accounting.handler.entity.CountLimitAccount account = (wfks.accounting.handler.entity.CountLimitAccount)
                	accountDao.get(new AccountId(dto.getProjectid(),com.getUserId()), new HashMap<String,String>());
	    		if(account==null){
	    			if(dto.getPurchaseNumber()<=0){
	    				return false;
	    			}
	    			return true;
	    		}
	    		if(account.getBalance()+dto.getPurchaseNumber()<0){
	    			return false;
	    		}
			}
        } catch (Exception e) {
        	e.printStackTrace();
        }
		return true;
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
	 * 添加标准配置参数
	 * @param dto
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
					int msg=WebServiceUtils.CreateNonAccountingUser(obj, 1);
					if(msg==1){
						log.info(com.getUserId()+"包库接口调用成功");
					}else{
						log.info(com.getUserId()+"包库更新失败");
					}
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
		String gArea = dto.getGazetteersArea();
		String gAlbum = dto.getGazetteersAlbum();
		String gLevel = dto.getGazetteersLevel();
		if (StringUtils.isNotEmpty(gId) || StringUtils.isNotEmpty(itemId)
				|| StringUtils.isNotEmpty(gArea) || StringUtils.isNotEmpty(gAlbum)
				|| StringUtils.isNotEmpty(gLevel)) {
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
				String gType = dto.getGazetteersType();
				if (StringUtils.isNotEmpty(gType)) {
					addStringToTerms("gazetteers_type", "Equal", gType, Terms, "String");
				}
				if (StringUtils.isNotEmpty(gLevel)&&(StringUtils.isNotEmpty(gArea)||StringUtils.isNotEmpty(gAlbum)||StringUtils.isNotEmpty(gType))) {
					addStringToTerms("gazetteers_level", "Equal", gLevel, Terms, "String");
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
	 * @param Terms
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
	public List<Map<String, Object>> getExcelData(MultipartFile file){
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
							continue;
						}
						map.put("institution", ExcelUtil.getValue(row.getCell(0)).trim());
						map.put("userId", ExcelUtil.getValue(row.getCell(1)).trim());
						map.put("password", ExcelUtil.getValue(row.getCell(2)).trim());
						//循环列Cell
						List<Map<String, String>> li = new ArrayList<Map<String, String>>();
						for(int i = 3; i < str.length; i++){
							Map<String,String> m = new HashMap<String, String>();
							if(StringUtils.isNotBlank(ExcelUtil.getValue(row.getCell(i)))){
								String title = str[i].substring(str[i].indexOf("_") + 1,str[i].length());
								if ("IP".equals(title.toUpperCase())) {
									map.put("ip",ExcelUtil.getValue(row.getCell(i)).replace(" ", ""));
								} else {
									m.put("projectid", title);
									m.put("totalMoney", ExcelUtil.getValue(row.getCell(i)));
									li.add(m);
								}
							}else{
								continue;
							}
						}
						map.put("projectList", li);
					}else{
						System.out.println("Excel中某列为空");
					}
					list.add(map);
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
			if (com.getManagerType().equals("new")) {
				p.setPid(com.getAdminname());
			} else if (com.getManagerType().equals("old")) {
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
	public int updateRegisterInfo(InstitutionalUser com,String pid,String adminId){
		//批量更新机构账号(当前账号无管理员添加新的，已有管理员不做任何操作) 
		Person p = new Person();
		p.setUserId(com.getUserId());
		p.setInstitution(com.getInstitution());
		try {
			if(StringUtils.isNotBlank(com.getPassword())){
				p.setPassword(PasswordHelper.encryptPassword(com.getPassword()));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		if(StringUtils.isNotBlank(com.getAdminname()) || StringUtils.isNotBlank(com.getAdminOldName())){				
			if(com.getManagerType().equals("new")){					
				p.setPid(com.getAdminname());
			}else{
				p.setPid(com.getAdminOldName().substring(0, com.getAdminOldName().indexOf("/")));
			}
		}else{
			p.setPid("");
		}
		p.setLoginMode(Integer.parseInt(com.getLoginMode()));
		return personMapper.updateRegisterInfo(p);
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
	public int setAccountRestriction(InstitutionalUser com){
		userAccountRestrictionMapper.deleteAccountRestriction(com.getUserId());
		if (com.getpConcurrentnumber() == null && com.getsConcurrentnumber() == null) {
			return 0;
		}
		UserAccountRestriction acc = new UserAccountRestriction();
		acc.setUserId(com.getUserId());
		acc.setUpperlimit(com.getUpperlimit());
		acc.setChargebacks(com.getChargebacks());
		acc.setDownloadupperlimit(com.getDownloadupperlimit());
		acc.setpConcurrentnumber(com.getpConcurrentnumber());
		acc.setsConcurrentnumber(com.getsConcurrentnumber());
		return userAccountRestrictionMapper.insert(acc);
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
		List<Object> userList = personMapper.findListInfoSimp(map);
		int i = personMapper.findListCountSimp(map);
		long timeSql=System.currentTimeMillis()-time;
		//2、查询产品
		long time1=System.currentTimeMillis();
		Date nextDay = this.getDay();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日");
		List<PayChannelModel> list_ = this.purchaseProject();
		for(Object object : userList){
			//将Object转换成 Map
			Map<String, Object> userMap = (Map<String,Object>) object;
			String userId = userMap.get("userId").toString();
			int sortScore=1+Integer.parseInt(userMap.get("loginMode").toString());
			boolean flag=false;//用户是否过期
			try{
				userMap.put("password",PasswordHelper.decryptPassword(String.valueOf(userMap.get("password"))));
			}catch (Exception e){
				log.error("密码转化异常：",e);
			}
			List<Map<String,Object>> list_ip = userIpMapper.findIpByUserId(userId);
			if(userMap.get("loginMode")!=null&&!userMap.get("loginMode").toString().equals("1")){
				for(Map<String, Object> userIp : list_ip){
					String beginIpAddressNumber = IPConvertHelper.NumberToIP((long) userIp.get("beginIpAddressNumber"));
					userIp.put("beginIpAddressNumber", beginIpAddressNumber);
					String endIpAddressNumber = IPConvertHelper.NumberToIP((long) userIp.get("endIpAddressNumber"));
					userIp.put("endIpAddressNumber", endIpAddressNumber);
				}
				userMap.put("list_ip", list_ip);
			}
			List<WfksPayChannelResources> wfList=new ArrayList<WfksPayChannelResources>();
			List<WfksPayChannelResources> listWfks = wfksMapper.selectByUserId(userId);
			for(PayChannelModel pay:list_){
				for(WfksPayChannelResources res:listWfks){
					if(StringUtils.equals(pay.getId(), res.getPayChannelid())){
						wfList.add(res);
					}
				}
			}

			//查询权限信息
			Map<String,String> itemsMap=new HashMap<String,String>();
			String viewChack="ViewHistoryCheck";
			this.getUserAccountidMapping(userId,itemsMap,userMap,viewChack);
			//查询机构管理员
			String pid=userMap.get("pid")==null?"":userMap.get("pid").toString();
			if(!"".equals(pid)){
				userMap.put("admin", this.findInfoByPid(pid));
			}
			//查询机构子账号
			this.getAccount(userMap);
			//查询统计分析
			UserInstitution ins=this.getUserInstitution(userId);
			if(ins!=null){
				userMap.put("tongji", ins.getStatisticalAnalysis());
			}
			userMap.put("groupInfo", this.getGroupInfo(userId));
			//购买项目列表
			List<Map<String, Object>> projectList = new ArrayList<Map<String, Object>>();
			List<Map<String, Object>> oldList = new ArrayList<Map<String, Object>>();
			for(WfksPayChannelResources wfks : wfList){
				Map<String, Object> libdata = new HashMap<String, Object>();// 组装条件Map
				Map<String, Object> extraData = new HashMap<String, Object>();// 购买的项目
				if(wfks.getPayChannelid().equals("HistoryCheck")){
					extraData.put("ViewHistoryCheck", viewChack);
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
						extraData.put("expired", this.getExpired(account.getEndDateTime(),nextDay));
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
				List<Map<String, Object>> data = this.selectListByRid(pay.getProductDetail());//通过产品id反查资源库
				if(plList.size()>0){
					for(Map<String, Object> d : data){
						for(Map<String, Object> plmap : plList){
							if(d.get("productSourceCode").equals(plmap.get("productSourceCode"))){
								d.put("checked", "checked");
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
						flag=true;
					}else{
						projectList.add(extraData);
					}
				}
			}
			projectList.addAll(oldList);
			if(projectList.size()>0){				
				userMap.put("proList", projectList);
			}
			if(flag){
				sortScore=100*sortScore;
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
		pageList.setTotalRow(i);
		return pageList;
	}
	
	//机构子账号
	private void getAccount(Map<String, Object> userMap) {
		UserAccountRestriction uar=this.getAccountRestriction(userMap.get("userId").toString());
		if(uar==null){
			return;
		}
		userMap.put("upperlimit", uar.getUpperlimit());
		userMap.put("sConcurrentnumber", uar.getsConcurrentnumber());
		userMap.put("pConcurrentnumber", uar.getpConcurrentnumber());
		userMap.put("downloadupperlimit", uar.getDownloadupperlimit());
		userMap.put("chargebacks", uar.getChargebacks());
	}

	//获取权限信息
	private void getUserAccountidMapping(String userId, Map<String, String> itemsMap,
			Map<String, Object> userMap, String viewCheck) throws Exception{
		
		WfksAccountidMapping[] mapping = wfksAccountidMappingMapper.getWfksAccountidByIdKey(userId);
		for (WfksAccountidMapping wm : mapping) {
			if ("trical".equals(wm.getRelatedidAccounttype())) {
				itemsMap.put(wm.getRelatedidKey(), "trical");
			}
			if ("ViewHistoryCheck".equals(wm.getRelatedidAccounttype())) {
				viewCheck = "可以";
			}
			if("openApp".equals(wm.getRelatedidAccounttype())){
				userMap.put("openApp", DateUtil.DateToFromatStr(wm.getBegintime())+"-"
						+DateUtil.DateToFromatStr(wm.getEndtime()));
			}
			if("openWeChat".equals(wm.getRelatedidAccounttype())){
				Map<String,String> wechat=new HashMap<String,String>();
				wechat.put("time",  DateUtil.DateToFromatStr(wm.getBegintime())+"-"
						+DateUtil.DateToFromatStr(wm.getEndtime()));
				WfksUserSettingKey key=new WfksUserSettingKey();
				key.setUserId(userId);
				key.setUserType("WeChat");
				key.setPropertyName("email");
				WfksUserSetting[] setting=wfksUserSettingMapper.selectByUserId(key);
				if(setting.length>0){
					wechat.put("email", setting[0].getPropertyValue());
				}
				userMap.put("openWeChat", wechat);
			}
			if("PartyAdminTime".equals(wm.getRelatedidAccounttype())){
				Map<String,String> party=new HashMap<String,String>();
				party.put("time",DateUtil.DateToFromatStr(wm.getBegintime()) + "-"
						+ DateUtil.DateToFromatStr(wm.getEndtime()));
				Person per = personMapper.queryPersonInfo(wm.getRelatedidKey());
				party.put("userId", per.getUserId());
				try{
					party.put("password",PasswordHelper.decryptPassword(per.getPassword()));
				}catch (Exception e){
					log.error("密码转化异常：",e);
				}
				String json = String.valueOf(per.getExtend());
				if(!StringUtils.isEmpty(json)){
					JSONObject obj = JSONObject.fromObject(json);
					party.put("trical", String.valueOf(obj.getBoolean("IsTrialPartyAdminTime")));
				}
				userMap.put("party", party);
			}
		}
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
	public List<Map<String,Object>> sonAccountNumber(String userId, String sonId, String start_time, String end_time){
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("sonId", sonId);
		map.put("userId", userId);
		map.put("start_time", start_time);
		map.put("end_time", end_time);
		List<Map<String, Object>> lm = personMapper.sonAccountNumber(map);// 获取子账号列表
		List<WfksPayChannelResources> list = wfksMapper.selectByUserId(userId);// 获取父账号购买项目
		if (lm.size() > 0 && list.size() > 0) {
			for (Map<String, Object> ma : lm) {
				String id=ma.get("userId").toString();
				List<Map<String, Object>> projectList = new ArrayList<Map<String, Object>>();
				try{
					for(WfksPayChannelResources wfks : list){
						PayChannelModel pay = SettingPayChannels.getPayChannel(wfks.getPayChannelid());
						Map<String, Object> extraData = new HashMap<String, Object>();// 购买的项目
						if(pay.getType().equals("balance")){
							wfks.accounting.handler.entity.BalanceLimitAccount account = (wfks.accounting.handler.entity.BalanceLimitAccount)accountDao.get(new AccountId(wfks.getPayChannelid(),id), new HashMap<String,String>());
							if(account!=null){
								extraData.put("name", pay.getName());
								extraData.put("payChannelid", account.getPayChannelId());
								extraData.put("type", pay.getType());
								extraData.put("balance", account.getBalance());
								extraData.put("beginDateTime", sdfSimp.format(account.getBeginDateTime()));
								extraData.put("endDateTime", sdfSimp.format(account.getEndDateTime()));
							}
						}else if(pay.getType().equals("time")){
							wfks.accounting.handler.entity.TimeLimitAccount account = (wfks.accounting.handler.entity.TimeLimitAccount)accountDao.get(new AccountId(wfks.getPayChannelid(),id), new HashMap<String,String>());
							if(account!=null){
								extraData.put("beginDateTime", sdfSimp.format(account.getBeginDateTime()));
								extraData.put("endDateTime", sdfSimp.format(account.getEndDateTime()));
								extraData.put("payChannelid", account.getPayChannelId());
								extraData.put("name", pay.getName());
								extraData.put("type", pay.getType());
							}
						}else if(pay.getType().equals("count")){
							wfks.accounting.handler.entity.CountLimitAccount account = (wfks.accounting.handler.entity.CountLimitAccount)accountDao.get(new AccountId(wfks.getPayChannelid(),id), new HashMap<String,String>());
							if(account!=null){
								extraData.put("name", pay.getName());
								extraData.put("payChannelid", account.getPayChannelId());
								extraData.put("type", pay.getType());
								extraData.put("balance", account.getBalance());
								extraData.put("beginDateTime", sdfSimp.format(account.getBeginDateTime()));
								extraData.put("endDateTime", sdfSimp.format(account.getEndDateTime()));
								extraData.put("totalConsume", account.getTotalConsume());
							}
						}
						if(extraData.size()>0){
							projectList.add(extraData);
						}
					}
				}catch(Exception e){
					log.error("子账号"+id+"调用接口异常",e);
				}
				ma.put("sonProjectList", projectList);
			}			
		}
		return lm;
	}

	@Override
	public List<Map<String, Object>> getProjectInfo(String userId){
		//通过userId查询详情限定列表
		List<WfksPayChannelResources> listWfks = wfksMapper.selectByUserId(userId);
		//判断项目ID是存在
		List<PayChannelModel> list_ = this.purchaseProject();
		List<WfksPayChannelResources> wfList=new ArrayList<WfksPayChannelResources>();
		for(PayChannelModel pay:list_){
			for(WfksPayChannelResources res:listWfks){
				if(StringUtils.equals(pay.getId(), res.getPayChannelid())){
					wfList.add(res);
				}
			}
		}
		WfksAccountidMapping[] tricals=wfksAccountidMappingMapper.getWfksAccountid(userId, "trical");
		Map<String,String> itemsMap=new HashMap<String,String>();
		for(WfksAccountidMapping wm:tricals){
			itemsMap.put(wm.getRelatedidKey(), "trical");
		}
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
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
					//查询条件
					libdata.put("userId", account.getUserId());
					libdata.put("payChannelid", account.getPayChannelId());
				}
			}
			List<Map<String, Object>> plList = wfksMapper.selectProjectLibrary(libdata);//已购买资源库
			List<Map<String, Object>> data = this.selectListByRid(pay.getProductDetail());//通过产品id反查资源库 
			if(plList.size()>0){				
				for(Map<String, Object> d : data){
					for(Map<String, Object> plmap : plList){
						if(d.get("productSourceCode").equals(plmap.get("productSourceCode"))){
							d.put("checked", "checked");
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
					.addAllBindAuthority(authorityList);
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
				.addAllBindAuthority(authorityList);
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
							dto.setTotalMoney(0.0);
							this.chargeProjectBalance(com, dto, adminId);
						}
					}else if(dto.getProjectType().equals("time")){
						wfks.accounting.handler.entity.TimeLimitAccount account = (wfks.accounting.handler.entity.TimeLimitAccount)accountDao.get(new AccountId(dto.getProjectid(),id), new HashMap<String,String>());
						if(account!=null){
							this.addProjectDeadline(com, dto,adminId);
						}
					}else if(dto.getProjectType().equals("count")){
						wfks.accounting.handler.entity.CountLimitAccount account = (wfks.accounting.handler.entity.CountLimitAccount)accountDao.get(new AccountId(dto.getProjectid(),id), new HashMap<String,String>());
						if(account!=null){
							dto.setPurchaseNumber(0);
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
		if(com.getManagerType().equals("new")){
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
	public int updateGroupInfo(InstitutionalUser com) {
		GroupInfo info=new GroupInfo();
		info.setUserId(com.getUserId());
		info.setCountryRegion(com.getCountryRegion());
		info.setOrderType(com.getOrderType());
		info.setOrderContent(com.getOrderContent());
		info.setPostCode(com.getPostCode());
		info.setOrganization(com.getOrganization());
		return groupInfoMapper.updateGroupInfo(info);
	}

	@Override
	public GroupInfo getGroupInfo(String userId) {
		return groupInfoMapper.getGroupInfo(userId);
	}


}
