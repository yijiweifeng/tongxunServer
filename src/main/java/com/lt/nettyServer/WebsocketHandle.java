package com.lt.nettyServer;

import com.lt.nettyServer.bean.MessageHolder;
import com.lt.nettyServer.protobuf.MessageProtobuf;
import com.lt.nettyServer.queue.TaskQueue;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.concurrent.BlockingQueue;

/**
 * @author sj
 * @date 2019/8/22 12:02
 */
public class WebsocketHandle extends ChannelInboundHandlerAdapter {
    private static Logger logger = LogManager.getLogger(ServerHandler.class);

    private final BlockingQueue<MessageHolder> taskQueue;

    private static long  total=0;



    private static final String TAG = ServerHandler.class.getSimpleName();

    public WebsocketHandle(){taskQueue = TaskQueue.getQueue();}

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("ServerHandler channelActive()" + ctx.channel().remoteAddress());

        System.out.println("channel size :"+ChannelContainer.getInstance().channelSize());
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        super.channelInactive(ctx);
        System.out.println("ServerHandler channelInactive()");

        // 用户断开连接后，移除channel
        ChannelContainer.getInstance().removeChannelIfConnectNoActive(ctx.channel());
        System.out.println("channel size :"+ChannelContainer.getInstance().channelSize());
        if (ctx != null) {
            ctx.close();
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        // super.exceptionCaught(ctx, cause);
        logger.error("异常消息"+cause);
        System.out.println("ServerHandler exceptionCaught()");
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        super.userEventTriggered(ctx, evt);
        System.out.println("ServerHandler userEventTriggered()");
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        total++;
        try {
            MessageProtobuf.Msg message = (MessageProtobuf.Msg) msg;
            ctx.writeAndFlush(message);

            System.out.println("channel size :"+ChannelContainer.getInstance().channelSize());
            System.out.println("收到来自客户端的消息：" + message);
            System.out.println("处理消息数：" + total);
            MessageHolder messageHolder =new MessageHolder();
            messageHolder.setChannel(ctx.channel());
            messageHolder.setMsg(message);
            // 添加到任务队列
            boolean offer = taskQueue.offer(messageHolder);
            logger.info("TaskQueue添加任务: taskQueue=" + taskQueue.size());
            if (!offer) {
                // 服务器繁忙
                logger.warn("服务器繁忙，拒绝服务");
                // 繁忙响应
            }
        }catch (Exception e){

            logger.error("接收消息异常"+msg);
        }


    }


}
