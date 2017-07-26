package com.channel;


import wfks.accounting.setting.PayChannelModel;
import wfks.accounting.setting.SettingPayChannels;

/**
 * 支付渠道数据初始化
 * @author vimes
 *
 */
public class WfksPayChannelData {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

		// 资源余额
		PayChannelModel payChannelModel = new PayChannelModel();
		payChannelModel.setId("BalanceLimit");
		payChannelModel.setName("资源余额");
		payChannelModel.setType("balance");
		payChannelModel.setAccountType("Group");
		payChannelModel
				.setProductDetail("Income.PeriodicalFulltext,ConferenceFulltext,ThesisFulltext,StandardDigest,StandardFulltext,"
						+ "PatentFulltext,CLawFulltext,CstadDigest,InstitutionDigest,ExpertDigest,"
						+ "LocalChronicleItemFulltext,LocalChronicle");
		SettingPayChannels.addPayChannel(payChannelModel);

		// 资源限时
		payChannelModel = new PayChannelModel();
		payChannelModel.setId("TimeLimit");
		payChannelModel.setName("资源限时");
		payChannelModel.setType("time");
		payChannelModel.setAccountType("Group");
		payChannelModel
				.setProductDetail("PeriodicalFulltext,ConferenceFulltext,ThesisFulltext,StandardDigest,"
						+ "StandardFulltext,PatentFulltext,CLawFulltext,CstadDigest,InstitutionDigest,ExpertDigest,"
						+ "LocalChronicleItemFulltext,LocalChronicle");
		SettingPayChannels.addPayChannel(payChannelModel);

		// 批量论文检测系统
		payChannelModel = new PayChannelModel();
		payChannelModel.setId("BatchCheck");
		payChannelModel.setName("批量论文检测系统");
		payChannelModel.setType("balance");
		payChannelModel.setAccountType("Group");
		payChannelModel.setProductDetail("Check");
		SettingPayChannels.addPayChannel(payChannelModel);

		// 次数
		payChannelModel = new PayChannelModel();
		payChannelModel.setId("CountLimit");
		payChannelModel.setName("资源限次");
		payChannelModel.setType("count");
		payChannelModel.setAccountType("Group");
		payChannelModel.setDescription("按次数计费");
		SettingPayChannels.addPayChannel(payChannelModel);

		// 科技文献限时
		payChannelModel = new PayChannelModel();
		payChannelModel.setId("SLTimeLimit");
		payChannelModel.setName("科技文献限时");
		payChannelModel.setType("time");
		payChannelModel.setAccountType("Group");
		payChannelModel.setProductDetail("ThesisFulltext,CstadDigest");
		SettingPayChannels.addPayChannel(payChannelModel);

		// 已发表论文检测系统
		payChannelModel = new PayChannelModel();
		payChannelModel.setId("HistoryCheck");
		payChannelModel.setName("已发表论文检测系统");
		payChannelModel.setType("balance");
		payChannelModel.setAccountType("Group");
		payChannelModel.setProductDetail("PublishedCheck");
		SettingPayChannels.addPayChannel(payChannelModel);

		// 大学生检测系统
		payChannelModel = new PayChannelModel();
		payChannelModel.setId("CollegeCheck");
		payChannelModel.setName("大学生检测系统");
		payChannelModel.setType("balance");
		payChannelModel.setAccountType("Group");
		payChannelModel.setProductDetail("CollegeCheck");
		payChannelModel.setDescription("按余额计费");
		SettingPayChannels.addPayChannel(payChannelModel);

		// Nstl原文传递
		payChannelModel = new PayChannelModel();
		payChannelModel.setId("NstlBalanceLimit");
		payChannelModel.setName("Nstl原文传递");
		payChannelModel.setType("balance");
		payChannelModel.setAccountType("Group");
		payChannelModel.setProductDetail("NstlDeliver");
		payChannelModel.setDescription("原文传递按余额计费");
		SettingPayChannels.addPayChannel(payChannelModel);

		// 钱包余额
		payChannelModel = new PayChannelModel();
		payChannelModel.setId("Person");
		payChannelModel.setName("钱包余额");
		payChannelModel.setAccountType("Person");
		payChannelModel
				.setProductDetail("PeriodicalFulltext,ThesisFulltext,StandardDigest,PatentFulltext,"
						+ "CLawFulltext,CstadDigest,InstitutionDigest,ExpertDigest,LocalChronicleItemFulltext,Check");
		payChannelModel.setDescription("不能购买标准全文，会议全文，旧方志整本阅读");
		SettingPayChannels.addPayChannel(payChannelModel);

		// 支付宝
		payChannelModel = new PayChannelModel();
		payChannelModel.setId("Alipay");
		payChannelModel.setName("支付宝");
		payChannelModel.setAccountType("Person");
		payChannelModel
				.setProductDetail("PeriodicalFulltext,ThesisFulltext,StandardDigest,PatentFulltext,CLawFulltext,"
						+ "CstadDigest,InstitutionDigest,ExpertDigest,LocalChronicleItemFulltext,Check");
		payChannelModel.setDescription("原文传递按余额计费");
		SettingPayChannels.addPayChannel(payChannelModel);

		// 银联
		payChannelModel = new PayChannelModel();
		payChannelModel.setId("UnionPay");
		payChannelModel.setName("银联");
		payChannelModel.setAccountType("Person");
		payChannelModel
				.setProductDetail("PeriodicalFulltext,ThesisFulltext,StandardDigest,PatentFulltext,"
						+ "CLawFulltext,CstadDigest,InstitutionDigest,ExpertDigest,LocalChronicleItemFulltext,Check");
		payChannelModel.setDescription("不能购买标准全文，会议全文，旧方志整本阅读");
		SettingPayChannels.addPayChannel(payChannelModel);

		// 系统充值
		payChannelModel = new PayChannelModel();
		payChannelModel.setId("Admin");
		payChannelModel.setName("系统充值");
		payChannelModel.setAccountType("Admin");
		payChannelModel
				.setProductDetail("BalanceLimit,TimeLimit,CountLimit,SLTimeLimit,HistoryCheck,CollegeCheck,"
						+ "NstlBalanceLimit,Person,Check");
		payChannelModel.setDescription("管理员用户可以给机构或个人用户充值");
		SettingPayChannels.addPayChannel(payChannelModel);

	}

}
