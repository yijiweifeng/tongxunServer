package client;

import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.timeout.IdleStateEvent;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

/**
 * Created with IntelliJ IDEA.
 * User: joker
 * Date: 2019/8/10
 * Time: 16:21
 * Description: No Description
 */
public class MyChatClientHandler extends SimpleChannelInboundHandler<String> {

    private MyChatClient imConnection = MyChatClient.getMyChatClient();
    private ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
    private ScheduledFuture<?> heartBeat;

    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        this.heartBeat = this.scheduler.scheduleWithFixedDelay(new HeatTask(ctx),0,1, TimeUnit.SECONDS);
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, String msg) throws Exception {
        System.out.println(msg);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("server is close");
        ctx.fireChannelInactive();
        if(!imConnection.isConnect){
            imConnection.doConnect();
        }
    }

}
