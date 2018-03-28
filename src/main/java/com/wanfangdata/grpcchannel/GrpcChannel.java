package com.wanfangdata.grpcchannel;

import io.grpc.ManagedChannel;
import io.grpc.netty.NegotiationType;
import io.grpc.netty.NettyChannelBuilder;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Properties;

public class GrpcChannel {

    private static final Logger log = LogManager.getLogger(GrpcChannel.class);

    private static ManagedChannel originChannel;

    private static final String GRPC_CONFIG="grpc.properties";

    //访问地址
    private static String host;
    //访问端口号
    private static Integer port;


    private GrpcChannel() {
        Properties pro = new Properties();
        InputStream in = Thread.currentThread().getContextClassLoader().getResourceAsStream(GRPC_CONFIG);
        try {
            pro.load(new InputStreamReader(in,"UTF-8"));
            GrpcChannel.host = pro.getProperty("grpc_host");
            GrpcChannel.port = Integer.valueOf(pro.getProperty("grpc_port"));
            in.close();
        } catch (Exception var8) {
            log.error("无法加载配置文件" + GRPC_CONFIG);
        } finally {
            try {
                in.close();
            } catch (IOException e) {
                log.error("关闭输入流失败");
            }
        }
    }

    public static ManagedChannel getInstance() {

        if (originChannel == null) {
            synchronized (GrpcChannel.class) {
                if (null == originChannel) {
                    originChannel = NettyChannelBuilder.forAddress(host, port)
                            .negotiationType(NegotiationType.PLAINTEXT)
                            .build();
                }
            }
        }
        return originChannel;
    }

    public static String getHost() {
        return host;
    }

    public static void setHost(String host) {
        GrpcChannel.host = host;
    }

    public static Integer getPort() {
        return port;
    }

    public static void setPort(Integer port) {
        GrpcChannel.port = port;
    }
}
