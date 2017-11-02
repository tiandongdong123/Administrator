package com.webservice;

import java.util.GregorianCalendar;
import java.util.List;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import com.alibaba.fastjson.JSONObject;
import com.utils.DateUtil;
import com.utils.SignUtil;
import com.xxl.conf.core.XxlConfClient;

public class WebServiceUtils {
	
	private static Logger log = Logger.getLogger(WebServiceUtils.class);
	
	private static String GB168STANDARDKEY=XxlConfClient.get("wf-admin.gb168standardkey",null);
	
	/**
	 * 注册用户
	 * @return
	 * @throws Exception
	 */
	public static int CreateNonAccountingUser(JSONObject obj,int UserState) {
		long time=System.currentTimeMillis();
		// UserState 用户状态=1：正常,0：停用
		int msg = 0;
		try{
			String enName = obj.getString("UserId") + "0830";// 0830是接口固定的
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
			String Username = obj.getString("Username");
			String keyStr = enName + Username + UserState + GB168STANDARDKEY;
			String token = SignUtil.toBase64(SignUtil.SHA1(keyStr));
			log.info("enName:" + enName + ",BK_StartTime:" + BK_StartTime + ",BK_EndTime:" + BK_EndTime
					+ ",Rdptauth:" + Rdptauth + ",Onlines:" + Onlines + ",Copys:" + Copys + ",Prints:"
					+ Prints + ",Sigprint" + ",ipStr:" + ipStr + ",Username:" + Username + ",UserState:"
					+ UserState);
			WFDataCenterInterface service=new WFDataCenterInterface();
			WFDataCenterInterfaceSoap soap=service.getWFDataCenterInterfaceSoap();
			boolean flag=soap.isExistedNonAccountingUser(enName);
			if(!flag&&"0".equals(UserState)){
				return -1;
			}
			if(flag){ // 更新接口
				msg=soap.updateNonAccountingUser(enName, BK_StartTime, BK_EndTime, Rdptauth, Onlines, Copys, Prints, Sigprint, ipStr, null, Username, UserState, token);
				log.info("修改用户enName:"+enName+(msg==1?"成功":"失败")+",耗时："+(System.currentTimeMillis()-time)+"ms");
			}else{ // 注册接口
				msg=soap.createNonAccountingUser(enName, BK_StartTime, BK_EndTime, Rdptauth, Onlines, Copys, Prints, Sigprint, ipStr, null, Username, UserState, token);
				log.info("注册用户enName:"+enName+(msg==1?"成功":"失败")+",耗时："+(System.currentTimeMillis()-time)+"ms");
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return msg;
	}

}
