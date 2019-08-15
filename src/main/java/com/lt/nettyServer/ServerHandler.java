package com.lt.nettyServer;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.lt.domain.bean.JsonResult;
import com.lt.domain.req.AddFinishSendInfoReq;
import com.lt.domain.req.AddNotSendInfoReq;
import com.lt.domain.req.CommonReq;
import com.lt.domain.resq.UserResq;
import com.lt.domain.service.IUserService;
import com.lt.nettyServer.protobuf.MessageProtobuf;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author sj
 * @date 2019/8/14 10:10
 */
class ServerHandler extends ChannelInboundHandlerAdapter {
    private static Logger logger = LogManager.getLogger(ServerHandler.class);
    @Resource
    IUserService iUserService;

    private static final String TAG = ServerHandler.class.getSimpleName();

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        super.channelActive(ctx);
        System.out.println("ServerHandler channelActive()" + ctx.channel().remoteAddress());
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        super.channelInactive(ctx);
        System.out.println("ServerHandler channelInactive()");
        // 用户断开连接后，移除channel
        ChannelContainer.getInstance().removeChannelIfConnectNoActive(ctx.channel());
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        super.exceptionCaught(ctx, cause);
        System.out.println("ServerHandler exceptionCaught()");
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        super.userEventTriggered(ctx, evt);
        System.out.println("ServerHandler userEventTriggered()");
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        MessageProtobuf.Msg message = (MessageProtobuf.Msg) msg;
        System.out.println("收到来自客户端的消息：" + message);
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
                    ChannelContainer.getInstance().saveChannel(new NettyChannel(fromId, ctx.channel()));
                } else {
                    resp.put("status", -1);
                    ChannelContainer.getInstance().removeChannelIfConnectNoActive(ctx.channel());
                }

                message = message.toBuilder().setHead(message.getHead().toBuilder().setExtend(resp.toString()).build()).build();
                ChannelContainer.getInstance().getActiveChannelByUserId(fromId).getChannel().writeAndFlush(message);
                break;
            }

            // 心跳消息
            case 1002: {
                // 收到心跳消息，原样返回
                String fromId = message.getHead().getFromId();
                ChannelContainer.getInstance().getActiveChannelByUserId(fromId).getChannel().writeAndFlush(message);
                break;
            }

            case 2001: {
                // 收到2001或3001消息，返回给客户端消息发送状态报告
                String fromId = message.getHead().getFromId();
                MessageProtobuf.Msg.Builder sentReportMsgBuilder = MessageProtobuf.Msg.newBuilder();
                MessageProtobuf.Head.Builder sentReportHeadBuilder = MessageProtobuf.Head.newBuilder();
                sentReportHeadBuilder.setMsgId(message.getHead().getMsgId());
                sentReportHeadBuilder.setMsgType(1010);
                sentReportHeadBuilder.setTimestamp(System.currentTimeMillis());
                sentReportHeadBuilder.setStatusReport(1);
                sentReportMsgBuilder.setHead(sentReportHeadBuilder.build());
                ChannelContainer.getInstance().getActiveChannelByUserId(fromId).getChannel().writeAndFlush(sentReportMsgBuilder.build());

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
                iUserService.addFinishSendInfo(addFinishSendInfoReq);

                if(nettyChannel==null){
                    // 缓存离线消息
                    AddNotSendInfoReq addNotSendInfoReq=new AddNotSendInfoReq();
                    addNotSendInfoReq.setInfoId(message.getHead().getMsgId());
                    addNotSendInfoReq.setFromId(Long.parseLong(fromId));
                    addNotSendInfoReq.setToId(Long.parseLong(toId));
                    addNotSendInfoReq.setContent(message.getBody());
                    addNotSendInfoReq.setSendTime(message.getHead().getTimestamp()+"");
                    addNotSendInfoReq.setContentType(message.getHead().getMsgType());
                    addNotSendInfoReq.setUploadUrl("/");
                    iUserService.addNotSendInfo(addNotSendInfoReq);
                    logger.info(toId+"不在线 缓存离线消息");
                }else {
                    ChannelFuture channelFuture = nettyChannel.getChannel().writeAndFlush(message);
                    logger.info(toId+"在线 转发成功 缓存消息");
                }

                break;
            }

            case 3001: {
                // todo 群聊，自己实现吧，toId可以是群id，根据群id查找所有在线用户的id，循环遍历channel发送即可。
                String fromId = message.getHead().getFromId();
                String toId = message.getHead().getToId();
                CommonReq req =new CommonReq();
                req.setId(Long.parseLong(toId));
                JsonResult groupFreidList = iUserService.getGroupFreidList(req);
                List<UserResq> friendlist= (List<UserResq>) groupFreidList.getData();

                for (UserResq u:friendlist){
                    NettyChannel nettyChannel = ChannelContainer.getInstance().getActiveChannelByUserId(u.getId()+"");
                    if (nettyChannel==null){
                        logger.info(toId+"不在线 群发");
                    }else {
                         nettyChannel.getChannel().writeAndFlush(message);
                         logger.info(toId+"在线 转发成功 群发");
                    }
                }

                break;
            }

            default:
                break;
        }

    }
}
