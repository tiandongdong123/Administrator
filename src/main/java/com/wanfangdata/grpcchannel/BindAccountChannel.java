package com.wanfangdata.grpcchannel;


import com.wanfangdata.rpc.bindauthority.BindAccountGrpc;


/**
 * 个人绑定机构权限channel类（个人权限）
 * 主要用于依赖注入并调用grpc服务中的方法
 */
public class BindAccountChannel {

    private BindAccountGrpc.BindAccountBlockingStub blockingStub;

    private BindAccountChannel(){
    }


    public BindAccountGrpc.BindAccountBlockingStub getBlockingStub() {
        return blockingStub = BindAccountGrpc.newBlockingStub(GrpcChannel.getInstance().get(GrpcChannel.BINDACCOUNT_CHANNEL));
    }

    public void setBlockingStub(BindAccountGrpc.BindAccountBlockingStub blockingStub) {
        this.blockingStub = blockingStub;
    }

}
