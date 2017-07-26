package com.channel;

import java.util.List;

import wfks.accounting.setting.PayChannelModel;
import wfks.accounting.setting.SettingPayChannels;

/**
 * 查询支付渠道列表
 * 
 * @author vimes
 * 
 */
public class WfksPayChannelDataList {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

		List<PayChannelModel> list = SettingPayChannels.getPayChannels();
		 System.out.println("list.list=" + list.size());
		
		// 删除支付渠道
		// SettingPayChannels.removePayChannel(p.getId());
		
		for (PayChannelModel p : list) {
			if (p.getAccountType().equals("Group") && p.getType() != null) {
				System.out.println("PayChannelModel=" + p);
//				SettingPayChannels.removePayChannel(p.getId());
			}
		}
	}
}
