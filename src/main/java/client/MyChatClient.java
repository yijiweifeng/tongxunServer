package client;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Created with IntelliJ IDEA.
 * User: joker
 * Date: 2019/8/10
 * Time: 16:19
 * Description: No Description
 */
public class MyChatClient {
    private static EventLoopGroup eventLoopGroup = new NioEventLoopGroup();
    private static Bootstrap bootstrap = new Bootstrap();
    private Channel channel;
    private static MyChatClient myChatClient = new MyChatClient();
    private static BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
    public static boolean isConnect = false;

    static {
        bootstrap.group(eventLoopGroup);
        bootstrap.channel(NioSocketChannel.class);
        bootstrap.option(ChannelOption.SO_KEEPALIVE, true);
        bootstrap.option(ChannelOption.ALLOW_HALF_CLOSURE, true);
        bootstrap.handler(new MyChatClientInitializer());
    }

    public static MyChatClient getMyChatClient(){
        return myChatClient;
    }

    public static void main(String[] args) throws Exception {
        myChatClient.testClientRun();
    }

    public Channel doConnect() throws Exception {
        testClientRun();
        return channel;
    }

    private Channel connect(Bootstrap bootstrap) throws InterruptedException {
        System.out.println("connect...");
        isConnect = true;
        boolean isFail = false;
        ChannelFuture sync = null;
        try {
            sync = bootstrap.connect("192.168.9.64", 8899).sync();
        } catch (Exception e) {
            isFail = true;
        }
        while (isFail) {
            Thread.sleep(3000);
            isFail = false;
            System.out.println("now reconnect ");
            try {
                sync = bootstrap.connect("192.168.9.64", 8899).sync();
            } catch (Exception e) {
                isFail = true;
            }
        }
        System.out.println("connect success");
        isConnect = false;
        return sync.channel();
    }

    public void testClientRun() throws Exception {
        try {
            channel = connect(bootstrap);
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                channel.writeAndFlush(line + "\r\n");
            }
        } finally {
            eventLoopGroup.shutdownGracefully();
        }
    }

}
