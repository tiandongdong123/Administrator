package com.webservice;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.GregorianCalendar;
import java.util.List;

import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.lang3.StringUtils;

import com.utils.WebServiceConsts;
import com.wf.bean.CommonEntity;
import com.wf.bean.ResourceDetailedDTO;
import com.wf.bean.ResourceLimitsDTO;
import com.wf.bean.WfksAccountidMapping;
import com.wf.bean.WfksUserSetting;
import com.wf.service.impl.AheadUserServiceImpl;

public class WebServiceUtil {
	
	/**
	 *	新旧平台用户同步接口。
	 *	参数一：传值DTO
	 *	参数二：true添加、false修改
	 *	参数三：角色权限对象
	 */
	public static String submitOriginalDelivery(CommonEntity com,boolean b) throws Exception{
		String lm = "";
		String msg = "";
		try{
			//用户
			WFUser user = new WFUser();
			user.setUserID(com.getUserId());
			user.setUserRealName(com.getInstitution());
			if(com.getLoginMode()!=null){
				lm = com.getLoginMode().equals("1")?"Pwd":(com.getLoginMode().equals("0")?"Ip":"PwdAndIp");
				user.setLoginType(UserDisposeEnum.fromValue(lm));
			}
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
			List<WFContract> list = contracts.getWFContract();
			boolean hasPerson=false;
			if(com.getRdlist()!=null){
				for(ResourceDetailedDTO dto : com.getRdlist()){
					if(dto.getProjectid()!=null){
						if("SingleCheck".equals(dto.getProjectid())||"WBatchCheck".equals(dto.getProjectid())){
							hasPerson=true;
						}
						//详情限定权限
						if(dto.getRldto()!=null){
							for(ResourceLimitsDTO rldto : dto.getRldto()){
								if(rldto.getProductid()==null){
									continue;
								}
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
					}
				}
			}
			UserManagerService ser = new UserManagerService();
			UserManagerServiceSoap soap = ser.getUserManagerServiceSoap();
			if(b){
				int backmsg = soap.addOrganizationUser(user, contracts, null, null, hasPerson);
				msg=String.valueOf(backmsg);
			}else{
				int backmsg = soap.editOrganizationUser(user, hasPerson);
				if(WebServiceConsts.Succed==backmsg){//1000是用户接口正常返回
					msg=backmsg+"_"+soap.editContract(contracts, user.getUserID());
				}else{
					msg=String.valueOf(backmsg);
				}
			}
		}catch(Exception e){
			msg="-1";
			e.printStackTrace();
		}
		return msg;
	}
	
	public static String submitService(WfksAccountidMapping[] wfks,WfksUserSetting[] setting,ArrayOfWFUser wfuser,String userId){
		String msg="";
		try{
			//角色权限
			ArrayOfAccountIdMapping arrayMapping = new ArrayOfAccountIdMapping();
			List<AccountIdMapping> list_mapping = arrayMapping.getAccountIdMapping();
			ArrayOfUserSetting arrayOfUserSetting = new ArrayOfUserSetting();
			List<UserSetting> list_setting = arrayOfUserSetting.getUserSetting();
			if(wfks!=null && wfks.length>0){
				for (WfksAccountidMapping mapping : wfks) {
					AccountIdMapping accountIdMapping = new AccountIdMapping();
					//时间格式转化
					DatatypeFactory dtf = DatatypeFactory.newInstance();
					GregorianCalendar gc = new GregorianCalendar();
					XMLGregorianCalendar xgc = null;
					if (mapping.getBegintime() != null) {
						gc.setTime(mapping.getBegintime());
						xgc = dtf.newXMLGregorianCalendar(gc);
						accountIdMapping.setBeginTime(xgc);
					}
					if (mapping.getEndtime() != null) {
						gc.setTime(mapping.getEndtime());
						xgc = dtf.newXMLGregorianCalendar(gc);
						accountIdMapping.setEndTime(xgc);
					}
					AccountId accountId = new AccountId();
					accountId.setKey(mapping.getIdKey());
					accountId.setAccountType(mapping.getIdAccounttype());
					AccountId accountId2 = new AccountId();
					accountId2.setKey(mapping.getRelatedidKey());
					accountId2.setAccountType(mapping.getRelatedidAccounttype());
					accountIdMapping.setId(accountId);
					accountIdMapping.setRelatedId(accountId2);
					accountIdMapping.setMappingId(mapping.getMappingid());
					list_mapping.add(accountIdMapping);
				}
			}
			if(setting!=null && setting.length>0){
				for(WfksUserSetting set : setting) {
					//权限setting对象
					UserSetting userSetting = new UserSetting();
					userSetting.setUserId(set.getUserId());
					userSetting.setUserType(set.getUserType());
					userSetting.setPropertyName(set.getPropertyName());
					userSetting.setPropertyValue(set.getPropertyValue());
					list_setting.add(userSetting);
				}
			}
			//发送接口
			UserManagerService ser = new UserManagerService();
			UserManagerServiceSoap soap = ser.getUserManagerServiceSoap();
			int account = soap.editAccountIdMapping(arrayMapping, wfuser, userId);
			int set = soap.editUserSetting(arrayOfUserSetting, userId);
			msg = account + "_" + set;
			return msg;
		}catch(Exception e){
			msg="-1";
			e.printStackTrace();
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
