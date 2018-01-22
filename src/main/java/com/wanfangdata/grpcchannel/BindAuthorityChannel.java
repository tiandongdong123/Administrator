package com.wanfangdata.grpcchannel;

import com.wanfangdata.rpc.bindauthority.BindAuthorityGrpc;
import io.grpc.ManagedChannel;
import io.grpc.netty.NegotiationType;
import io.grpc.netty.NettyChannelBuilder;


/**
 * Created by 01 on 2018/1/17.
 */
public class BindAuthorityChannel{



    private ManagedChannel originChannel;
    private BindAuthorityGrpc.BindAuthorityBlockingStub blockingStub;
    //访问地址
    private String host;
    //访问端口号
    private Integer port;

    private BindAuthorityChannel(){
    }

    private BindAuthorityChannel(String host,Integer port){
        originChannel = NettyChannelBuilder.forAddress(host,port)
                .negotiationType(NegotiationType.PLAINTEXT)
                .build();
        this.blockingStub = BindAuthorityGrpc.newBlockingStub(originChannel);
    }

    public BindAuthorityGrpc.BindAuthorityBlockingStub getBlockingStub() {
        return blockingStub;
    }

    public void setBlockingStub(BindAuthorityGrpc.BindAuthorityBlockingStub blockingStub) {

      this.blockingStub = blockingStub;
    }

    public ManagedChannel getOriginChannel() {

        return originChannel;
    }

    public void setOriginChannel(ManagedChannel originChannel) {
        this.originChannel = originChannel;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public Integer getPort() {
        return port;
    }

    public void setPort(Integer port) {
        this.port = port;
    }

}
