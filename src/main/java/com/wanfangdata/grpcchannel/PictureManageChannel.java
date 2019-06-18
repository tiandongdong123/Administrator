package com.wanfangdata.grpcchannel;



import com.wanfangdata.rpc.picturemanager.PictureManagerGrpc;


/**
 * 图片channel类
 *
 */
public class PictureManageChannel {
    private PictureManagerGrpc.PictureManagerBlockingStub blockingStub;

    public PictureManageChannel() {
    }

    public PictureManagerGrpc.PictureManagerBlockingStub getBlockingStub() {
        return blockingStub = PictureManagerGrpc.newBlockingStub(GrpcChannel.getInstance().get(GrpcChannel.PICTURE_CHANNEL));
    }

    public void setBlockingStub(PictureManagerGrpc.PictureManagerBlockingStub blockingStub) {
        this.blockingStub = blockingStub;
    }
}
