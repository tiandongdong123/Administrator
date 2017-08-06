package com.webservice;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.lang3.StringUtils;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.springframework.beans.factory.annotation.Autowired;

import com.utils.DateUtil;
import com.wf.bean.CommonEntity;
import com.wf.bean.ResourceDetailedDTO;
import com.wf.bean.ResourceLimitsDTO;
import com.wf.bean.WfksAccountidMapping;
import com.wf.bean.WfksUserSetting;
import com.wf.service.AheadUserService;
import com.wf.service.impl.AheadUserServiceImpl;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class WebServiceUtil {
	
	@Autowired
	private AheadUserService aheadUserService;
	
	
	/**
	 *	新旧平台用户同步接口。
	 *	参数一：传值DTO
	 *	参数二：true添加、false修改
	 *	参数三：角色权限对象
	 */
	public static int submitOriginalDelivery(CommonEntity com,boolean b, WfksAccountidMapping wfks, WfksUserSetting setting) throws DatatypeConfigurationException{
		String lm = "";
		int msg = 0;
		//用户
		WFUser user = new WFUser();
		if(com.getLoginMode()!=null){			
			lm = com.getLoginMode().equals("1")?"Pwd":(com.getLoginMode().equals("0")?"Ip":"PwdAndIp");
			user.setLoginType(UserDisposeEnum.fromValue(lm));
		}
		user.setUserID(com.getUserId());
		user.setUserRealName(com.getInstitution());
		if(lm.equals("Pwd") || lm.equals("PwdAndIp")){			
			user.setPassword(com.getPassword());
		}
		//IP
		if(StringUtils.isNotBlank(com.getIpSegment()) && !lm.equals("Pwd")){			
			ArrayOfWFIPLimit arrayOfWFIPLimit  = new ArrayOfWFIPLimit();
			List<WFIPLimit> lp = arrayOfWFIPLimit.getWFIPLimit();
			String [] str = com.getIpSegment().split("\r\n");
			for(int i = 0; i < str.length; i++){
				String beginIp = str[i].substring(0, str[i].indexOf("-"));
				String endIp = str[i].substring(str[i].indexOf("-")+1, str[i].length());
				WFIPLimit wfipLimit = new WFIPLimit();
				wfipLimit.setBeginIPAddress(beginIp);
				wfipLimit.setEndIPAddress(endIp);
				wfipLimit.setUserID(com.getUserId());
				lp.add(wfipLimit);
			}
			user.setWFIPLimit(arrayOfWFIPLimit);
		}
		//购买项目权限
		ArrayOfWFContract contracts = new ArrayOfWFContract();
		//角色权限
		ArrayOfAccountIdMapping arrayMapping = new ArrayOfAccountIdMapping();
		ArrayOfUserSetting arrayOfUserSetting = new ArrayOfUserSetting();
		List<WFContract> list = contracts.getWFContract();
		if(com.getRdlist()!=null){
			for(ResourceDetailedDTO dto : com.getRdlist()){
				if(dto.getProjectid()!=null){
					//详情限定权限
					if(dto.getRldto()!=null){					
						for(ResourceLimitsDTO rldto : dto.getRldto()){
							WFContract wfContract = new WFContract();
							wfContract.setTransferOut(dto.getProjectid()+"."+com.getUserId());
							wfContract.setTransferIn(StringUtils.strip(Arrays.toString(rldto.getProductid()),"[]"));
							if(AheadUserServiceImpl.getField(rldto)!=null){							
								ArrayOfContractTerm arrayOfContractTerm = new ArrayOfContractTerm();
								List<ContractTerm> li = arrayOfContractTerm.getContractTerm();
								//Terms详情限定
								JSONArray array = JSONObject.fromObject(AheadUserServiceImpl.getField(rldto)).getJSONArray("Terms");
								for(Object obj : array){
									JSONObject json = JSONObject.fromObject(obj);
									ContractTerm term = new ContractTerm();
									term.setField(json.getString("Field"));
									term.setValue(json.getString("Value"));
									term.setValueType(json.getString("ValueType"));
									term.setVerb(Verb.fromValue(json.getString("Verb")));
									li.add(term);
								}
								wfContract.setTerms(arrayOfContractTerm);
							}
							list.add(wfContract);
						}
					}
					if(wfks!=null){
						List<AccountIdMapping> list_mapping = arrayMapping.getAccountIdMapping();
						AccountIdMapping accountIdMapping = new AccountIdMapping();
						if(wfks.getBegintime()!=null){				
							accountIdMapping.setBeginTime(DatatypeFactory.newInstance().newXMLGregorianCalendar(DateUtil.dateToString(wfks.getBegintime())));
						}
						if(wfks.getEndtime()!=null){				
							accountIdMapping.setEndTime(DatatypeFactory.newInstance().newXMLGregorianCalendar(DateUtil.dateToString(wfks.getEndtime())));
						}
						AccountId accountId = new AccountId();
						accountId.setKey(wfks.getIdKey());
						accountId.setAccountType(wfks.getRelatedidAccounttype());
						accountIdMapping.setId(accountId);
						accountIdMapping.setMappingId(wfks.getMappingid());
						if(setting!=null){
							AccountId accountId2 = new AccountId();
							accountId2.setKey(setting.getPropertyName());
							accountId2.setAccountType(setting.getPropertyValue());
							accountIdMapping.setRelatedId(accountId2);
							//权限setting对象
							List<UserSetting> list_setting = arrayOfUserSetting.getUserSetting();
							UserSetting userSetting = new UserSetting();
							userSetting.setUserId(setting.getUserId());
							userSetting.setUserType(setting.getUserType());
							userSetting.setPropertyName(setting.getPropertyName());
							userSetting.setPropertyValue(setting.getPropertyValue());
							list_setting.add(userSetting);
						}
						list_mapping.add(accountIdMapping);
					}
				}
			}
		}
		UserManagerService ser = new UserManagerService();
		UserManagerServiceSoap soap = ser.getUserManagerServiceSoap();
		if(b){
			msg = soap.addOrganizationUser(user, contracts, arrayMapping, arrayOfUserSetting);
		}else{
			msg = soap.editOrganizationUser(user, contracts, arrayMapping, arrayOfUserSetting);
		}
		return msg;
	}
	
	
	public static void main(String[] args){
		List<String> array = new ArrayList<String>();
		array.add("234");
		array.add("567");
		System.out.println(StringUtils.strip(array.toString(),"[]"));
	}

}
