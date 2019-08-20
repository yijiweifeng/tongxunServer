package com.lt.nettyServer.service;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.lt.common.Enum.MessageType;
import com.lt.common.utils.SpringHelper;
import com.lt.dal.entry.NotReceivedEntity;
import com.lt.domain.bean.JsonResult;
import com.lt.domain.req.AddFinishSendInfoReq;
import com.lt.domain.req.AddNotSendInfoReq;
import com.lt.domain.req.CommonReq;
import com.lt.domain.resq.UserResq;
import com.lt.domain.service.IUserService;
import com.lt.nettyServer.ChannelContainer;
import com.lt.nettyServer.NettyChannel;
import com.lt.nettyServer.bean.MessageHolder;
import com.lt.nettyServer.group.GroupManager;
import com.lt.nettyServer.protobuf.MessageProtobuf;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.util.ReferenceCountUtil;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 业务分发器.
 *
 * @author sj.
 */
public class Dispatcher {
    private static Logger logger = LogManager.getLogger(Dispatcher.class);
    IUserService iUserService = (IUserService) SpringHelper.getBean("userServiceImpl");

    public static void dispatch(MessageHolder messageHolder) {
        MessageProtobuf.Msg message= messageHolder.getMsg();
        Channel channel=messageHolder.getChannel();
        int msgType = message.getHead().getMsgType();
        switch (msgType) {
            // 握手消息
            case 1001: {
                String fromId = message.getHead().getFromId();
                JSONObject jsonObj = JSON.parseObject(message.getHead().getExtend());
                String token = jsonObj.getString("token");
                JSONObject resp = new JSONObject();
                if (token.equals("token_" + fromId)) {
                    resp.put("status", 1);
                    // 握手成功后，保存用户通道
                    ChannelContainer.getInstance().saveChannel(new NettyChannel(fromId, channel));
                    // 响应握手
                    message = message.toBuilder().setHead(message.getHead().toBuilder().setExtend(resp.toString()).build()).build();
                    ChannelContainer.getInstance().getActiveChannelByUserId(fromId).getChannel().writeAndFlush(message);
                    // 推送待接收离线消息
                   new Dispatcher().sendNotReceivedInfo(fromId);
                } else {
                    resp.put("status", -1);
                    ChannelContainer.getInstance().removeChannelIfConnectNoActive(channel);
                    // 响应握手
                    message = message.toBuilder().setHead(message.getHead().toBuilder().setExtend(resp.toString()).build()).build();
                    channel.writeAndFlush(message);
                }
                break;
            }

            // 心跳消息
            case 1002: {
                // 收到心跳消息，原样返回
                String fromId = message.getHead().getFromId();
                ChannelContainer.getInstance().getActiveChannelByUserId(fromId).getChannel().writeAndFlush(message);
                break;
            }
            // 单聊消息
            case 2001: {
                // 收到2001或3001消息，返回给客户端消息发送状态报告
                new Dispatcher().sendPersonalInfo(message);
                break;
            }
            // 群聊
            case 3001: {
                new Dispatcher().sendGroupInfo(message);
                break;
            }

            default:
                break;
        }

        // 释放buffer
        ReferenceCountUtil.release(messageHolder);
    }

    // 群聊
    public   void sendGroupInfo(MessageProtobuf.Msg message){
        // toId可以是群id，根据群id查找所有在线用户的id，循环遍历channel发送即可。
        String fromId = message.getHead().getFromId();
        String toId = message.getHead().getToId();
        // 通知客户端发送成功 不管在线不在线
        responseClient(message, MessageType.StatusReportEnum.SUCCESS_1.getTypeCode()
                ,MessageType.SERVER_MSG_SENT_STATUS_REPORT.getMsgType());


        // 先在内存中找group
        List<String> members = GroupManager.groupsQuery(toId);
        if(members!=null){
            // 修改讨论组最后一次活跃时间
            GroupManager.groupTimesUpdate(toId, message.getHead().getTimestamp());
            logger.info("内存 讨论组<" + toId + "> 修改时间戳");
            sendGroupMessage(members,message);
        }else {
            // 查询数据
            CommonReq req =new CommonReq();
            req.setId(Long.parseLong(toId));
            JsonResult groupFreidList = iUserService.getGroupFreidList(req);
            List<UserResq> friendlist= (List<UserResq>) groupFreidList.getData();

             if(!CollectionUtils.isEmpty(friendlist)){
                 List<String> memberIds=new ArrayList<>();
                 for(UserResq u:friendlist){
                     memberIds.add(u.getId().toString());
                 }
                 sendGroupMessage(memberIds,message);
             }

        }










    }

    public void sendGroupMessage(List<String> memberIds,MessageProtobuf.Msg message){

        // toId可以是群id，根据群id查找所有在线用户的id，循环遍历channel发送即可。
        String fromId = message.getHead().getFromId();
        String toId = message.getHead().getToId();

        if(memberIds!=null&&memberIds.size()>0){

            // 缓存消息
            AddFinishSendInfoReq addFinishSendInfoReq=new AddFinishSendInfoReq();
            addFinishSendInfoReq.setInfoId(message.getHead().getMsgId());
            addFinishSendInfoReq.setFromId(Long.parseLong(fromId));
            addFinishSendInfoReq.setToId(Long.parseLong(toId));
            addFinishSendInfoReq.setContent(message.getBody());
            addFinishSendInfoReq.setSendTime(message.getHead().getTimestamp()+"");
            addFinishSendInfoReq.setReceiveTime(System.currentTimeMillis()+"");
            addFinishSendInfoReq.setContentType(message.getHead().getMsgType());
            addFinishSendInfoReq.setUploadUrl("/");
            addFinishSendInfoReq.setInfoType(2);
            addFinishSendInfoReq.setGroupId(Long.parseLong(toId));
            iUserService.addFinishSendInfo(addFinishSendInfoReq);

            for (String id:memberIds){
                if(!id.equals(fromId)){
                    NettyChannel nettyChannel = ChannelContainer.getInstance().getActiveChannelByUserId(id);
                    if (nettyChannel==null){
                        // 离线  缓存离线消息和缓存消息库
                        AddNotSendInfoReq addNotSendInfoReq=new AddNotSendInfoReq();
                        addNotSendInfoReq.setInfoId(message.getHead().getMsgId());
                        addNotSendInfoReq.setFromId(Long.parseLong(fromId));
                        addNotSendInfoReq.setToId(Long.parseLong(id));
                        addNotSendInfoReq.setContent(message.getBody());
                        addNotSendInfoReq.setSendTime(message.getHead().getTimestamp()+"");
                        addNotSendInfoReq.setContentType(message.getHead().getMsgType());
                        addNotSendInfoReq.setUploadUrl("/");
                        addNotSendInfoReq.setInfoType(2);
                        addNotSendInfoReq.setGroupId(Long.parseLong(toId));
                        iUserService.addNotSendInfo(addNotSendInfoReq);
                        logger.info(id+"不在线  群发 离线缓存");
                    }else {
                        nettyChannel.getChannel().writeAndFlush(message)
                                .addListener(new GenericFutureListener<Future<? super Void>>() {
                                    @Override
                                    public void operationComplete(Future<? super Void> future) throws Exception {
                                        if(future.isSuccess()){
                                            logger.info(id+"在线 转发 群发"+future.isSuccess());
                                        }else {
                                            ChannelContainer.getInstance().getActiveChannelByUserId(id)
                                                    .getChannel().writeAndFlush(message)
                                                    .addListener(new GenericFutureListener<Future<? super Void>>() {
                                                        @Override
                                                        public void operationComplete(Future<? super Void> future) throws Exception {
                                                            if(future.isSuccess()){

                                                            }
                                                            logger.info(id+"在线 转发 群发"+future.isSuccess());
                                                        }
                                                    });
                                        }
                                    }
                                });

                    }
                }

            }
        }
    }

    // 推送待接收的离线消息
    public void sendNotReceivedInfo(String fromId){
        // 推送待接收消息
        CommonReq req =new CommonReq();
        req.setId(Long.parseLong(fromId));
        JsonResult result = iUserService.getNotReceiveInfoByUserId(req);
        if("200".equals(result.getResult())){
            List<NotReceivedEntity> notReceList = (List<NotReceivedEntity>) result.getData();
            for(NotReceivedEntity n:notReceList){
                MessageProtobuf.Msg.Builder sentReportMsgBuilder = MessageProtobuf.Msg.newBuilder();
                MessageProtobuf.Head.Builder sentReportHeadBuilder = MessageProtobuf.Head.newBuilder();
                sentReportHeadBuilder.setMsgId(n.getInfoId());
                sentReportHeadBuilder.setToId(n.getToId()+"");
                // 消息类型 待接收
                if(n.getInfoType()==2){
                    // 群聊 发送人id-组id
                    sentReportHeadBuilder.setFromId(n.getFromId()+"-"+n.getGroupId());
                    sentReportHeadBuilder.setMsgType(MessageType.GROUP_CHAT_NOT.getMsgType());
                }else {
                    // 单聊
                    sentReportHeadBuilder.setFromId(n.getFromId()+"");
                    sentReportHeadBuilder.setMsgType(MessageType.SINGLE_CHAT_NOT.getMsgType());
                }
                sentReportHeadBuilder.setMsgContentType(MessageType.MessageContentType.TEXT.getMsgContentType());
                sentReportHeadBuilder.setTimestamp(System.currentTimeMillis());
                sentReportHeadBuilder.setStatusReport(1);
                sentReportMsgBuilder.setHead(sentReportHeadBuilder.build());
                sentReportMsgBuilder.setBody(n.getContent());
                ChannelFuture channelFuture = ChannelContainer.getInstance().getActiveChannelByUserId(fromId).getChannel().writeAndFlush(sentReportMsgBuilder.build());
                channelFuture.addListener(new GenericFutureListener<Future<? super Void>>() {
                    @Override
                    public void operationComplete(Future<? super Void> future) throws Exception {
                        // 回调监听发送成功 失败再尝试发送一次
                        if(future.isSuccess()){
                            // 推送待接收消息后删除待接收信息
                            iUserService.delNotReceidedInfoById(n.getId());
                            logger.info("转发待接收"+future.isSuccess());
                        }else {
                            ChannelContainer.getInstance().getActiveChannelByUserId(fromId)
                                    .getChannel().writeAndFlush(sentReportMsgBuilder.build())
                                    .addListener(new GenericFutureListener<Future<? super Void>>() {
                                        @Override
                                        public void operationComplete(Future<? super Void> future) throws Exception {
                                            if(future.isSuccess()){
                                                // 推送待接收消息后删除待接收信息
                                                iUserService.delNotReceidedInfoById(n.getId());

                                            }
                                            logger.info("转发待接收"+future.isSuccess());
                                        }
                                    });
                        }

                    }
                });
            }

        }
    }

    // 单聊消息
    public void sendPersonalInfo(MessageProtobuf.Msg message){
        String fromId = message.getHead().getFromId();
        // 同时转发消息到接收方
        String toId = message.getHead().getToId();
        NettyChannel nettyChannel = ChannelContainer.getInstance().getActiveChannelByUserId(toId);

        // 缓存消息
        AddFinishSendInfoReq addFinishSendInfoReq=new AddFinishSendInfoReq();
        addFinishSendInfoReq.setInfoId(message.getHead().getMsgId());
        addFinishSendInfoReq.setFromId(Long.parseLong(fromId));
        addFinishSendInfoReq.setToId(Long.parseLong(toId));
        addFinishSendInfoReq.setContent(message.getBody());
        addFinishSendInfoReq.setSendTime(message.getHead().getTimestamp()+"");
        addFinishSendInfoReq.setReceiveTime(System.currentTimeMillis()+"");
        addFinishSendInfoReq.setContentType(message.getHead().getMsgType());
        addFinishSendInfoReq.setUploadUrl("/");
        addFinishSendInfoReq.setInfoType(1);


        if(nettyChannel==null){
            // 离线  缓存离线消息和缓存消息库
            AddNotSendInfoReq addNotSendInfoReq=new AddNotSendInfoReq();
            addNotSendInfoReq.setInfoId(message.getHead().getMsgId());
            addNotSendInfoReq.setFromId(Long.parseLong(fromId));
            addNotSendInfoReq.setToId(Long.parseLong(toId));
            addNotSendInfoReq.setContent(message.getBody());
            addNotSendInfoReq.setSendTime(message.getHead().getTimestamp()+"");
            addNotSendInfoReq.setContentType(message.getHead().getMsgType());
            addNotSendInfoReq.setUploadUrl("/");
            addNotSendInfoReq.setInfoType(1);
            iUserService.addFinishSendInfo(addFinishSendInfoReq);
            iUserService.addNotSendInfo(addNotSendInfoReq);
            logger.info(toId+"不在线 缓存离线消息");
            responseClient(message,MessageType.StatusReportEnum.SUCCESS_1.getTypeCode()
                    ,MessageType.SERVER_MSG_SENT_STATUS_REPORT.getMsgType());
        }else {
            // 在线 发送消息成功后缓存消息
            ChannelFuture channelFuture = nettyChannel.getChannel().writeAndFlush(message);
            channelFuture.addListener(new GenericFutureListener<Future<? super Void>>() {
                @Override
                public void operationComplete(Future<? super Void> future) throws Exception {
                    if(future.isSuccess()){
                        iUserService.addFinishSendInfo(addFinishSendInfoReq);
                        // 响应客户端转发成功
                        responseClient(message, MessageType.StatusReportEnum.SUCCESS_1.getTypeCode()
                                ,MessageType.SERVER_MSG_SENT_STATUS_REPORT.getMsgType());
                        logger.info(toId+"在线 转发成功 缓存消息"+future.isSuccess());
                    }else {
                        ChannelContainer.getInstance().getActiveChannelByUserId(toId)
                                .getChannel().writeAndFlush(message)
                                .addListener(new GenericFutureListener<Future<? super Void>>() {
                                    @Override
                                    public void operationComplete(Future<? super Void> future) throws Exception {
                                        if(future.isSuccess()){
                                            iUserService.addFinishSendInfo(addFinishSendInfoReq);
                                            // 响应客户端转发成功
                                            responseClient(message, MessageType.StatusReportEnum.SUCCESS_1.getTypeCode()
                                                    ,MessageType.SERVER_MSG_SENT_STATUS_REPORT.getMsgType());
                                        }else {
                                            responseClient(message, MessageType.StatusReportEnum.FAIL.getTypeCode()
                                                    ,MessageType.SERVER_MSG_SENT_STATUS_REPORT.getMsgType());
                                        }
                                        logger.info(toId+"在线 转发成功 缓存消息"+future.isSuccess());
                                    }
                                });
                    }

                }
            });

        }
    }

    public void responseClient( MessageProtobuf.Msg message,int status,int msgType){
        String fromId = message.getHead().getFromId();
        MessageProtobuf.Msg.Builder sentReportMsgBuilder = MessageProtobuf.Msg.newBuilder();
        MessageProtobuf.Head.Builder sentReportHeadBuilder = MessageProtobuf.Head.newBuilder();
        sentReportHeadBuilder.setMsgId(message.getHead().getMsgId());
        sentReportHeadBuilder.setMsgType(msgType);
        sentReportHeadBuilder.setTimestamp(System.currentTimeMillis());
        sentReportHeadBuilder.setStatusReport(status);
        sentReportMsgBuilder.setHead(sentReportHeadBuilder.build());
        NettyChannel activeChannelByUserId = ChannelContainer.getInstance().getActiveChannelByUserId(fromId);
        if(activeChannelByUserId!=null){
            activeChannelByUserId.getChannel().writeAndFlush(sentReportMsgBuilder.build())
                    .addListener(new GenericFutureListener<Future<? super Void>>() {
                        @Override
                        public void operationComplete(Future<? super Void> future) throws Exception {
                            if(!future.isSuccess()){
                                responseClient(message,status,msgType);
                            }
                        }
                    });
        }

    }
}
