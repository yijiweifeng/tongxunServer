package com.lt.nettyServer;


import com.lt.common.Enum.MessageType;
import com.lt.common.utils.SpringHelper;
import com.lt.domain.req.AddNotSendInfoReq;
import com.lt.domain.service.IUserService;
import com.lt.nettyServer.protobuf.MessageProtobuf;

import java.util.Timer;
import java.util.TimerTask;

/**
 * <b>
 * <p>@Description:     消息发送超时定时器，每一条消息对应一个定时器</p>
 * </b>
 */
public class MsgTimeoutTimer extends Timer {

    IUserService iUserService = (IUserService) SpringHelper.getBean("userServiceImpl");
    private MessageProtobuf.Msg msg;// 发送的消息
    private int currentResendCount;// 当前重发次数
    private MsgTimeoutTask task;// 消息发送超时任务

    public MsgTimeoutTimer(MessageProtobuf.Msg msg) {

        this.msg = msg;
        task = new MsgTimeoutTask();
        this.schedule(task, 2000, 2000);
    }

    /**
     * 消息发送超时任务
     */
    private class MsgTimeoutTask extends TimerTask {

        @Override
        public void run() {


            currentResendCount++;
            if (currentResendCount > 3) {
                // 重发次数大于可重发次数，离线缓存
                try {
                    //
                    System.out.println("离线缓存");
                    if(msg.getHead().getMsgType()==MessageType.SINGLE_CHAT.getMsgType()){
                        // 离线  缓存离线消息和缓存消息库
                        AddNotSendInfoReq addNotSendInfoReq=new AddNotSendInfoReq();
                        addNotSendInfoReq.setInfoId(msg.getHead().getMsgId());
                        addNotSendInfoReq.setFromId(Long.parseLong(msg.getHead().getFromId()));
                        addNotSendInfoReq.setToId(Long.parseLong(msg.getHead().getToId()));
                        addNotSendInfoReq.setContent(msg.getBody());
                        addNotSendInfoReq.setSendTime(msg.getHead().getTimestamp()+"");
                        addNotSendInfoReq.setContentType(msg.getHead().getMsgType());
                        addNotSendInfoReq.setUploadUrl("/");
                        addNotSendInfoReq.setInfoType(1);
                        iUserService.addNotSendInfo(addNotSendInfoReq);
                        System.out.println(msg.getHead().getToId()+"不在线 缓存离线消息");
                    }


                }catch (Exception e){

                }
                finally {
                    // 从消息发送超时管理器移除该消息
                    MsgTimeoutTimerManager.getInstance().remove(msg.getHead().getMsgId());
                    currentResendCount = 0;
                }
            } else {
                // 发送消息，但不再加入超时管理器，达到最大发送失败次数就算了
                sendMsg();
            }
        }
    }

    public void sendMsg() {
        System.out.println("正在重发消息，message=" + msg);
        NettyChannel activeChannelByUserId = ChannelContainer.getInstance().getActiveChannelByUserId(msg.getHead().getToId());
        if(activeChannelByUserId!=null){
            activeChannelByUserId.getChannel().writeAndFlush(msg);
        }else {
            System.out.println("掉线");
        }

    }

    public MessageProtobuf.Msg getMsg() {
        return msg;
    }

    @Override
    public void cancel() {
        if (task != null) {
            task.cancel();
            task = null;
        }

        super.cancel();
    }
}
