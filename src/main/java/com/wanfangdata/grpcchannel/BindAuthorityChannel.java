package com.wanfangdata.grpcchannel;

import com.wanfangdata.rpc.bindauthority.BindAuthorityGrpc;
import io.grpc.ManagedChannel;
import io.grpc.netty.NegotiationType;
import io.grpc.netty.NettyChannelBuilder;


/**
 * Created by 01 on 2018/1/17.
 */

/**
 * 个人绑定机构权限channel类（机构权限：机构开通、修改、查询权限等）
 * 主要用于依赖注入并调用grpc服务中的方法
 */
public class BindAuthorityChannel{

    private BindAuthorityGrpc.BindAuthorityBlockingStub blockingStub;

    private BindAuthorityChannel(){

    }

    public BindAuthorityGrpc.BindAuthorityBlockingStub getBlockingStub() {
        return blockingStub = BindAuthorityGrpc.newBlockingStub(GrpcChannel.getInstance());
    }

    public void setBlockingStub(BindAuthorityGrpc.BindAuthorityBlockingStub blockingStub) {

      this.blockingStub = blockingStub;
    }
}
