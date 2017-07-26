package com.webservice;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.lang3.StringUtils;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import com.wf.bean.CommonEntity;
import com.wf.bean.ResourceDetailedDTO;
import com.wf.bean.ResourceLimitsDTO;
import com.wf.service.impl.AheadUserServiceImpl;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class WebServiceUtil {
	
	/**
	 *	新旧平台用户同步接口。参数一：传值DTO,参数二：true添加、false修改
	 */
	public static int submitOriginalDelivery(CommonEntity com,boolean b){
		//用户
		WFUser user = new WFUser();
		String lm = com.getLoginMode().equals("1")?"Pwd":(com.getLoginMode().equals("0")?"Ip":"PwdAndIp");
		user.setUserID(com.getUserId());
		user.setLoginType(UserDisposeEnum.fromValue(lm));
		user.setUserRealName(com.getInstitution());
		if(lm.equals("Pwd") || lm.equals("PwdAndIp")){			
			user.setPassword(com.getPassword());
		}
		//IP
		if(!lm.equals("Pwd")){			
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
				}
			}
		}
		
		UserManagerService ser = new UserManagerService();
		UserManagerServiceSoap soap = ser.getUserManagerServiceSoap();
		int msg = 0;
		if(b){
			msg = soap.addOrganizationUser(user, contracts, new ArrayOfAccountIdMapping());
		}else{
			msg = soap.editOrganizationUser(user, contracts, new ArrayOfAccountIdMapping());
		}
		System.out.println(msg);
		return msg;
	}
	
	
	public static void main(String[] args){
		List<String> array = new ArrayList<String>();
		array.add("234");
		array.add("567");
		System.out.println(StringUtils.strip(array.toString(),"[]"));
	}
	
	public static String main1(CommonEntity com,String host,String SOAPAction){
		HttpClient client = new HttpClient();
		PostMethod postMethod = new PostMethod(host);
		String msg = "";
		try {
			StringBuffer soap=new StringBuffer();
			soap.append("<?xml version=\"1.0\" encoding=\"utf-8\"?>");
			soap.append("<soap:Envelope xmlns:xsi='http://www.w3.org/2001/XMLSchema-instance' xmlns:xsd='http://www.w3.org/2001/XMLSchema' xmlns:soap='http://schemas.xmlsoap.org/soap/envelope/'>");
			soap.append("<soap:Body>");
			soap.append("<"+SOAPAction+" xmlns='http://tempuri.org/'"+SOAPAction+"'>");
			soap.append("<user>");
			String lm = com.getLoginMode()=="1"?"10":(com.getLoginMode()=="0"?"20":"30");
			soap.append("<LoginType>"+lm+"</LoginType>");
			soap.append("<WFIPLimit>");
				String [] str = com.getIpSegment().split("\n");
				for(int i = 0; i < str.length; i++){		
					String beginIp = str[i].substring(0, str[i].indexOf("-"));
					String endIp = str[i].substring(str[i].indexOf("-")+1, str[i].length());
					soap.append("<WFIPLimit>");
					soap.append("<ExtensionData xsi:nil='true'/>");
					soap.append("<BeginIPAddress>"+beginIp+"</BeginIPAddress>");
					soap.append("<EndIPAddress>"+endIp+"</EndIPAddress>");
					//soap.append("<ID></ID>");
					soap.append("<UserID>"+com.getUserId()+"</UserID>");
					soap.append("</WFIPLimit>");
				}
			soap.append("</WFIPLimit>");
			if(lm.equals("10") || lm.equals("30")){				
				soap.append("<Password>"+com.getPassword()+"</Password>");
			}
			soap.append("<UserID>"+com.getUserId()+"</UserID>");
			soap.append("</user>");
			soap.append("<contract>");
			soap.append("<TransferIn>string</TransferIn>");
			soap.append("<TransferOut>string</TransferOut>");
			soap.append("<ExtensionData/>");
			soap.append("<Terms>");
			for(ResourceDetailedDTO dto : com.getRdlist()){
				if(dto.getProjectid()!=null){
					for(ResourceLimitsDTO rldto : dto.getRldto()){
						if(AheadUserServiceImpl.getField(rldto)!=null){							
							JSONArray array = JSONObject.fromObject(AheadUserServiceImpl.getField(rldto)).getJSONArray("Terms");
							for(Object obj : array){
								JSONObject json = JSONObject.fromObject(obj);
								soap.append("<ContractTerm>");
								soap.append("<ExtensionData xsi:nil='true'/>");
								soap.append("<Field>"+json.getString("Field")+"</Field>");
								soap.append("<Value>"+json.getString("Value")+"</Value>");
								soap.append("<ValueType>"+json.getString("ValueType")+"</ValueType>");
								soap.append("<Verb>"+json.getString("Verb")+"</Verb>");
								soap.append("</ContractTerm>");
							}
						}
					}
				}
			}
			soap.append("</Terms>");
			soap.append("</contract>");
			soap.append("</AddOrganizationUser>");
			soap.append("</soap:Body>");
			soap.append("</soap:Envelope>");
			System.out.println(soap.toString());
			postMethod.setRequestBody(soap.toString());
			//设置头部
			postMethod.setRequestHeader("POST","/UserManagerService.asmx HTTP/1.1");
			postMethod.setRequestHeader("Host",host);
			postMethod.setRequestHeader("Content-Type","text/xml; charset=utf-8");
			postMethod.setRequestHeader("SOAPAction","http://tempuri.org/"+SOAPAction);
			int code = client.executeMethod(postMethod);
			String result = postMethod.getResponseBodyAsString();
			if(code == 200 & result != null){
				Document doc = DocumentHelper.parseText(result);
				Element root = doc.getRootElement();
				Element body = root.element("Body");
				Element response = body.element("AddOrganizationUserResponse");
				Element wf_result = response.element("AddOrganizationUserResult");
				msg = wf_result.getText();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return msg;
	}

}
