package com.lt.nettyServer.websocket;

import com.google.protobuf.MessageLite;
import com.google.protobuf.MessageLiteOrBuilder;
import com.lt.nettyServer.ServerIdleStateTrigger;
import com.lt.nettyServer.WebsocketHandle;
import com.lt.nettyServer.protobuf.MessageProtobuf;
import com.lt.nettyServer.service.Service;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;
import io.netty.handler.codec.MessageToMessageDecoder;
import io.netty.handler.codec.MessageToMessageEncoder;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.BinaryWebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.codec.http.websocketx.extensions.compression.WebSocketServerCompressionHandler;
import io.netty.handler.codec.protobuf.ProtobufDecoder;
import io.netty.handler.stream.ChunkedWriteHandler;
import io.netty.handler.timeout.IdleStateHandler;

import java.util.List;

import static io.netty.buffer.Unpooled.wrappedBuffer;

/**
 * websocket 服务
 * @author sj
 * @date 2019/8/14 9:46
 */
public class NettyServer {

    private int port=8866;
    private int readerIdleTimeSeconds=30;
    public NettyServer(){

    }
    public NettyServer(int port){
        this.port=port;
    }
    public static void main(String []args){
        NettyServer server=new NettyServer();
        server.startServer();
    }

    public  void startServer() {



        //boss线程监听端口，worker线程负责数据读写
        EventLoopGroup boss = new NioEventLoopGroup();
        EventLoopGroup worker = new NioEventLoopGroup();

        try {
            //辅助启动类
            ServerBootstrap bootstrap = new ServerBootstrap();
            //设置线程池
            bootstrap.group(boss, worker);
            //设置socket工厂
            bootstrap.channel(NioServerSocketChannel.class);
            //设置管道工厂
            bootstrap.childHandler(new ChannelInitializer<SocketChannel>() {
                @Override
                protected void initChannel(SocketChannel socketChannel) throws Exception {
                    ChannelPipeline pipeline = socketChannel.pipeline();
                    // HTTP请求的解码和编码
                    pipeline.addLast(new HttpServerCodec());
                    // 把多个消息转换为一个单一的FullHttpRequest或是FullHttpResponse，
                    // 原因是HTTP解码器会在每个HTTP消息中生成多个消息对象HttpRequest/HttpResponse,HttpContent,LastHttpContent
                    pipeline.addLast(new HttpObjectAggregator(65536));
                    // 主要用于处理大数据流，比如一个1G大小的文件如果你直接传输肯定会撑暴jvm内存的; 增加之后就不用考虑这个问题了
                    pipeline.addLast(new ChunkedWriteHandler());
                    // WebSocket数据压缩
                    pipeline.addLast(new WebSocketServerCompressionHandler());
                    // 协议包长度限制
                    pipeline.addLast(new WebSocketServerProtocolHandler("/ws", null, true));

                    pipeline.addLast("idleStateHandler", new IdleStateHandler(readerIdleTimeSeconds, 0, 0));
                    pipeline.addLast("idleStateTrigger", new ServerIdleStateTrigger());
                    pipeline.addLast("frameEncoder", new LengthFieldPrepender(2));
                    pipeline.addLast("frameDecoder", new LengthFieldBasedFrameDecoder(65535,
                            0, 2, 0, 2));
                    // 协议包解码
                    pipeline.addLast(new MessageToMessageDecoder<WebSocketFrame>() {
                        @Override
                        protected void decode(ChannelHandlerContext ctx, WebSocketFrame frame, List<Object> objs) throws Exception {
                            ByteBuf buf = ((BinaryWebSocketFrame) frame).content();
                            objs.add(buf);
                            buf.retain();
                        }
                    });
                    // 协议包编码
                    pipeline.addLast(new MessageToMessageEncoder<MessageLiteOrBuilder>() {
                        @Override
                        protected void encode(ChannelHandlerContext ctx, MessageLiteOrBuilder msg, List<Object> out) throws Exception {
                            ByteBuf result = null;
                            if (msg instanceof MessageLite) {
                                result = wrappedBuffer(((MessageLite) msg).toByteArray());
                            }
                            if (msg instanceof MessageLite.Builder) {
                                result = wrappedBuffer(((MessageLite.Builder) msg).build().toByteArray());
                            }

                            // ==== 上面代码片段是拷贝自TCP ProtobufEncoder 源码 ====
                            // 然后下面再转成websocket二进制流，因为客户端不能直接解析protobuf编码生成的

                            WebSocketFrame frame = new BinaryWebSocketFrame(result);
                            out.add(frame);
                        }
                    });

                    // 协议包解码时指定Protobuf字节数实例化为CommonProtocol类型
                    pipeline.addLast(new ProtobufDecoder(MessageProtobuf.Msg.getDefaultInstance()));

                    // websocket定义了传递数据的6中frame类型
                    pipeline.addLast(new WebsocketHandle());
                }
            });

            //设置TCP参数
            //1.链接缓冲池的大小（ServerSocketChannel的设置）
            bootstrap.option(ChannelOption.SO_BACKLOG, 1024);
            //维持链接的活跃，清除死链接(SocketChannel的设置)
            bootstrap.childOption(ChannelOption.SO_KEEPALIVE, true);
            //关闭延迟发送
            bootstrap.childOption(ChannelOption.TCP_NODELAY, true);

            //绑定端口
            ChannelFuture future = bootstrap.bind(port).sync();
            if(future.isSuccess()){
                // 启动队列任务服务
                new Service().initAndStart();
                System.out.println("队列任务服务 start success...... ");
                System.out.println("IM Netty start success...... 端口"+port);
            }else {
                System.out.println("server start fail.....");
            }


            //等待服务端监听端口关闭
            future.channel().closeFuture().sync();

        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            //优雅退出，释放线程池资源
            boss.shutdownGracefully();
            worker.shutdownGracefully();
        }
    }
}
