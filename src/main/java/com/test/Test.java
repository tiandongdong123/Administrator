package com.test;

import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

import com.redis.RedisUtil;


import wfks.accounting.setting.PayChannelModel;
import wfks.accounting.setting.SettingPayChannels;

public class Test {
	
	public static void main(String[] args) {

		// double f = 2;
		// BigDecimal bg = new BigDecimal(f);
		// double f1 = bg.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
		// System.out.println(f1);

//        PayChannelModel payChannelModel=new PayChannelModel();
//        payChannelModel.setId("BalanceLimit" + new Date().getTime());
//        payChannelModel.setName("资源-qk");
//        payChannelModel.setType("balance");
//        payChannelModel.setAccountType("Group");
//        payChannelModel.setProductDetail("PeriodicalFulltext,ConferenceFulltext" +
//                "ThesisFulltext,StandardDigest,StandardFulltext,CLawFulltext" +
//                "CstaDigest,InstitutionDigest,ExpertDigest,LocalChronicleItemFulltext" +
//                "LocalChronicle");
//        payChannelModel.setDescription("测试");
//        SettingPayChannels.addPayChannel(payChannelModel);
		
//        PayChannelModel item = SettingPayChannels.getPayChannel(payChannelModel.getId());
    
		RedisUtil redis = new RedisUtil();
		redis.get("bjdx1",2);
		System.out.println(redis.get("bjdx1",2));
		
		
	}
}
