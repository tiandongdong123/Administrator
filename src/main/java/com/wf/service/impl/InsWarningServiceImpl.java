package com.wf.service.impl;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import wfks.accounting.account.AccountDao;
import wfks.accounting.handler.entity.BalanceLimitAccount;
import wfks.accounting.handler.entity.CountLimitAccount;
import wfks.accounting.setting.PayChannelModel;
import wfks.accounting.setting.SettingPayChannels;
import wfks.authentication.AccountId;

import com.exportExcel.ExportExcel;
import com.utils.SendMail2;
import com.wf.bean.Mail;
import com.wf.bean.WarningInfo;
import com.wf.bean.WfksPayChannelResources;
import com.wf.dao.PersonMapper;
import com.wf.dao.WfksPayChannelResourcesMapper;
import com.wf.service.InsWarningService;

@Service
public class InsWarningServiceImpl implements InsWarningService {
	
	@Autowired
	WfksPayChannelResourcesMapper wfksMapper;
	
	@Autowired
	private AccountDao accountDao;
	
	@Autowired
	private PersonMapper personMapper;

	@Override
	public boolean sendMail(WarningInfo war) {
		boolean flag=false;
		try{
			List<Map<String,Object>> dataList=new ArrayList<Map<String,Object>>();//组装数据
			// 1、检查发送规则
			int balance = war.getAmountthreshold();// 获取数量预警
			int count = war.getCountthreshold();// 获取次数预警
			String rceiver = war.getRemindemail();// 需要提醒人的人
			// 2、查询机构信息
			/*Map<String,Object> mapUnit=new HashMap<String, Object>();
			int allNum=personMapper.findListCount(mapUnit);
			System.out.println("机构总数是"+allNum);
			mapUnit.put("pageNum", 0);
			mapUnit.put("pageSize", allNum);*/
			List<Object> list = personMapper.findListIns();
			System.out.println("返回的list结果数:"+list.size());
			for(Object object : list) {
				Map<String, Object> userMap = (Map<String, Object>) object;
				String userId = userMap.get("userId") == null ? "" : userMap.get("userId").toString();
				String userName = userMap.get("institution") == null ? "": userMap.get("institution").toString();
				//通过userId查询详情限定列表
				List<WfksPayChannelResources> listWfks = wfksMapper.selectByUserId(userId);
				for(WfksPayChannelResources wfks : listWfks) {
					try{
						PayChannelModel model = SettingPayChannels.getPayChannel(wfks.getPayChannelid());
						if("balance".equals(model.getType())){
							BalanceLimitAccount account = (wfks.accounting.handler.entity.BalanceLimitAccount)accountDao.get(new AccountId(wfks.getPayChannelid(),wfks.getUserId()), new HashMap<String,String>());
							if(account!=null){
								if(account.getBalance().intValue()<balance){
									Map<String,Object> map=new HashMap<String,Object>();
									map.put("userId", wfks.getUserId());
									map.put("userName", userName);
									map.put("payChannelId", account.getPayChannelId());
									map.put("name", model.getName());
									map.put("balance", account.getBalance());
									map.put("date", getStringDate(account.getEndDateTime()));
									map.put("balanceDesc", "资源余额不足"+balance+"元");
									dataList.add(map);
								}
							}
						}else if("count".equals(model.getType())){
							CountLimitAccount account = (wfks.accounting.handler.entity.CountLimitAccount)accountDao.get(new AccountId(wfks.getPayChannelid(),wfks.getUserId()), new HashMap<String,String>());
							if(account!=null){
								if(account.getBalance()<count){
									Map<String,Object> map=new HashMap<String,Object>();
									map.put("userId", wfks.getUserId());
									map.put("userName", userName);
									map.put("payChannelId", account.getPayChannelId());
									map.put("name", model.getName());
									map.put("count", account.getBalance());
									map.put("date", getStringDate(account.getEndDateTime()));
									map.put("countDesc", "项目次数不足"+count+"次");
									dataList.add(map);
								}
							}
						}
					}catch(Exception e){
						System.out.println(wfks.getPayChannelid()+"发生异常");
					}
				}
			}
			// 3、组装excel
			List<String> namelist=new ArrayList<String>();
			namelist.add("机构名称");
			namelist.add("机构ID");
			namelist.add("购买项目名称");
			namelist.add("截至日期");
			namelist.add("项目余额");
			namelist.add("项目剩余次数");
			namelist.add("过期情况");
			namelist.add("余额情况");
			namelist.add("次数情况");
			ExportExcel exc= new ExportExcel();
			Map<String,String> fileMap=exc.exportExccel(dataList,namelist);
			// 4、发送预警邮件
			Mail mail=new Mail();
			mail.setReceiver(rceiver);
			mail.setName("后台管理");
			mail.setSubject("机构用户预警提醒");
			mail.setMessage("具体内容见附件");
			mail.setAffix(fileMap.get("filePath"));
			mail.setAffixName(fileMap.get("fileName"));
			SendMail2 util=new SendMail2();
			flag=util.sendEmail(mail);
			//删除临时附件
			File f = new File(fileMap.get("filePath"));
			if(f.exists()){
				f.delete();
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return flag;
	}
	
    private String getStringDate(Date date){  
        SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd");        
        return format.format(date);  
    }  

}
