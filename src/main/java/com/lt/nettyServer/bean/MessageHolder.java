package com.lt.nettyServer.bean;

import com.lt.nettyServer.protobuf.MessageProtobuf;
import io.netty.channel.Channel;

/**
 * @author sj
 * @date 2019/8/16 13:14
 */
public class MessageHolder {
    private MessageProtobuf.Msg msg;
    private Channel   channel;

    public MessageProtobuf.Msg getMsg() {
        return msg;
    }

    public void setMsg(MessageProtobuf.Msg msg) {
        this.msg = msg;
    }

    public Channel getChannel() {
        return channel;
    }

    public void setChannel(Channel channel) {
        this.channel = channel;
    }
}
