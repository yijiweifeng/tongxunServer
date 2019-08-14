package client;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.timeout.IdleStateEvent;

import java.net.SocketAddress;
import java.util.HashMap;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: joker
 * Date: 2019/8/12
 * Time: 15:28
 * Description: No Description
 */
public class ServerIdleStateTrigger extends ChannelInboundHandlerAdapter {

    private static Map<SocketAddress,Integer> map = new HashMap<>();

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        SocketAddress socketAddress = ctx.channel().remoteAddress();
        IdleStateEvent event = (IdleStateEvent)evt;
        switch (event.state()){
            case READER_IDLE:
//                readIdleTimes ++; // 读空闲的计数加1
                if(map.get(socketAddress) != null){
                    map.put(socketAddress,map.get(socketAddress) + 1);
                }else{
                    map.put(socketAddress,1);
                }
                System.out.println("读空闲计数+1");
                break;
            case WRITER_IDLE:
                // 写空闲的计数加1
                break;
            case ALL_IDLE:
//                readIdleTimes ++; // 读写空闲的计数加1
                break;
        }
//        if(map.get(socketAddress) > 3){
//            ctx.channel().writeAndFlush("[client]读空闲超过3次，关闭连接");
//            ctx.close();
//        }
    }
}
