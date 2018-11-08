package com.wanfangdata.grpcchannel;

import io.grpc.ManagedChannel;
import io.grpc.netty.NegotiationType;
import io.grpc.netty.NettyChannelBuilder;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Properties;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

public class GrpcServer {

    private static final Logger log = LogManager.getLogger(GrpcServer.class);

    private static ManagedChannel originChannel;

    private static final String GRPCSERVER_CONFIG="grpcServer.properties";

    //访问地址
    private static String host;
    //访问端口号
    private static Integer port;

    public  GrpcServer() {}
    static{
        Properties pro = new Properties();
        InputStream in = Thread.currentThread().getContextClassLoader().getResourceAsStream(GRPCSERVER_CONFIG);
        try {
            pro.load(new InputStreamReader(in,"UTF-8"));
            GrpcServer.host = "mqserver";
            GrpcServer.port = Integer.valueOf(pro.getProperty("MQServer_port"));
            in.close();
        } catch (Exception var8) {
            log.error("无法加载配置文件" + GRPCSERVER_CONFIG);
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
            synchronized (GrpcServer.class) {
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
    	GrpcServer.host = host;
    }

    public static Integer getPort() {
        return port;
    }

    public static void setPort(Integer port) {
    	GrpcServer.port = port;
    }
}
