package com.wanfangdata.hotwordsetting;

import io.grpc.ManagedChannel;
import io.grpc.netty.NegotiationType;
import io.grpc.netty.NettyChannelBuilder;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Properties;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;


public class HotWordSetting {

    private static final Logger log = LogManager.getLogger(HotWordSetting.class);

    private static ManagedChannel originChannel;

    private static final String HOTWORDSETTING_CONFIG="hotWordSetting.properties";

    //时间
    private static String get_time;

    public  HotWordSetting() {}
    static{
        Properties pro = new Properties();
        InputStream in = Thread.currentThread().getContextClassLoader().getResourceAsStream(HOTWORDSETTING_CONFIG);
        try {
            pro.load(new InputStreamReader(in,"UTF-8"));
            HotWordSetting.get_time=pro.getProperty("get_time");
            in.close();
        } catch (Exception var8) {
            log.error("无法加载配置文件" + HOTWORDSETTING_CONFIG);
        } finally {
            try {
                in.close();
            } catch (IOException e) {
                log.error("关闭输入流失败");
            }
        }
    }
	public static ManagedChannel getOriginChannel() {
		return originChannel;
	}
	public static void setOriginChannel(ManagedChannel originChannel) {
		HotWordSetting.originChannel = originChannel;
	}
	public static String getGet_time() {
		return get_time;
	}
	public static void setGet_time(String get_time) {
		HotWordSetting.get_time = get_time;
	}
	public static Logger getLog() {
		return log;
	}
	public static String getHotwordsettingConfig() {
		return HOTWORDSETTING_CONFIG;
	}

   
}
