package client;

import io.netty.channel.ChannelHandlerContext;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * User: joker
 * Date: 2019/8/12
 * Time: 14:45
 * Description: No Description
 */
public class HeatTask implements Runnable {

    private ChannelHandlerContext ctx;

    public HeatTask(ChannelHandlerContext ctx) {
        this.ctx = ctx;
    }

    @Override
    public void run() {
        ctx.channel().writeAndFlush(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()) + " this is client heat\r\n");
    }

}
