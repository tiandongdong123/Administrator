package com.webservice;

import java.util.GregorianCalendar;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import com.alibaba.fastjson.JSONObject;
import com.utils.DateUtil;
import com.utils.Pinyin4jUtil;
import com.utils.SignUtil;
import com.utils.WebService;
import com.xxl.conf.core.XxlConfClient;

public class WebServiceUtils {
	
	private static Logger log = Logger.getLogger(WebServiceUtils.class);
	
	private static String GB168STANDARDKEY=XxlConfClient.get("wf-admin.gb168standardkey",null);
	
	/**
	 * 注册用户
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public static int CreateNonAccountingUser(JSONObject obj,int UserState) {
		long time=System.currentTimeMillis();
		// UserState 用户状态=1：正常,0：停用
		int msg = 0;
		try{
			String Username = obj.getString("UserId");
			String enName = getEnName(Username);
			DatatypeFactory dtf = DatatypeFactory.newInstance();
			GregorianCalendar cal = new GregorianCalendar();
			String startTime=obj.getString("BK_StartTime").replace("T", " ");
			String endTime=obj.getString("BK_EndTime").replace("T", " ");
			cal.setTime(DateUtil.stringToDate(startTime));
			XMLGregorianCalendar BK_StartTime = dtf.newXMLGregorianCalendar(cal);
			cal.setTime(DateUtil.stringToDate(endTime));
			XMLGregorianCalendar BK_EndTime = dtf.newXMLGregorianCalendar(cal);
			String Rdptauth = obj.getString("Rdptauth");
			int Onlines = obj.getIntValue("Onlines");
			int Copys = obj.getIntValue("Copys");
			int Prints = obj.getIntValue("Prints");
			int Sigprint = obj.getIntValue("Sigprint");
			List<String> list = (List<String>) obj.get("BK_IPLimits");
			String ipStr = StringUtils.join(list, ";");
			String keyStr = enName + Username + UserState + GB168STANDARDKEY;
			String token = SignUtil.toSHA1Base64(keyStr);
			log.info("enName:" + enName + ",BK_StartTime:" + startTime + ",BK_EndTime:" + endTime
					+ ",Rdptauth:" + Rdptauth + ",Onlines:" + Onlines + ",Copys:" + Copys + ",Prints:"
					+ Prints + ",Sigprint:"+Sigprint + ",ipStr:" + ipStr + ",Username:" + Username + ",UserState:"
					+ UserState+",token:"+token);
			WFDataCenterInterface service=new WFDataCenterInterface();
			WFDataCenterInterfaceSoap soap=service.getWFDataCenterInterfaceSoap();
			boolean flag=soap.isExistedNonAccountingUser(enName);
			if(!flag&&"0".equals(UserState)){
				return -1;
			}
			if(flag){ // 更新接口
				msg=soap.updateNonAccountingUser(enName, BK_StartTime, BK_EndTime, Rdptauth, Onlines, Copys, Prints, Sigprint, ipStr, "", Username, UserState, token);
				if(msg==1){
					log.info("接口修改用户:"+enName+"成功,耗时："+(System.currentTimeMillis()-time)+"ms");
				}else{
					log.info("接口修改用户："+enName+"失败，错误是："+WebService.getName(msg));
				}
				
			}else{ // 注册接口
				msg=soap.createNonAccountingUser(enName, BK_StartTime, BK_EndTime, Rdptauth, Onlines, Copys, Prints, Sigprint, ipStr, "", Username, UserState, token);
				if(msg==1){
					log.info("接口注册用户:"+enName+"成功,耗时："+(System.currentTimeMillis()-time)+"ms");
				}else{
					log.info("接口注册用户："+enName+"失败，错误是："+WebService.getName(msg));
				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return msg;
	}
	
	/**
	 * 获取enName
	 * @param username
	 * @return
	 */
	public static String getEnName(String username) {
		try {
			Pattern p = Pattern.compile("[\\u4e00-\\u9fa5]");
			Matcher m = p.matcher(username);
			if (m.find()) {
				StringBuffer sb = new StringBuffer("");
				for (int i = 0; i < username.length(); i++) {
					String temp = username.substring(i, i + 1);
					m = p.matcher(temp);
					if (m.find()) {
						String pinyin = Pinyin4jUtil.getPinyin(temp);
						if (!StringUtils.isEmpty(pinyin))
							sb.append(pinyin.substring(0, 1));
					} else {
						sb.append(temp);
					}
				}
				return sb.toString()+"0830";// 0830是接口固定的
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return username;
	}

}
