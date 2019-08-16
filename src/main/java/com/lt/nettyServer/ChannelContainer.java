package com.lt.nettyServer;

import io.netty.channel.Channel;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author sj
 * @date 2019/8/14 10:10
 */
public  class ChannelContainer {

    private ChannelContainer() {

    }

    private static final ChannelContainer INSTANCE = new ChannelContainer();

    public static ChannelContainer getInstance() {
        return INSTANCE;
    }

    private final Map<String, NettyChannel> CHANNELS = new ConcurrentHashMap<String, NettyChannel>();

    public int channelSize(){
        return CHANNELS.size();
    }

    public void saveChannel(NettyChannel channel) {
        if (channel == null) {
            return;
        }
        CHANNELS.put(channel.getChannelId(), channel);
    }

    public NettyChannel removeChannelIfConnectNoActive(Channel channel) {
        if (channel == null) {
            return null;
        }

        String channelId = channel.id().toString();

        return removeChannelIfConnectNoActive(channelId);
    }

    public NettyChannel removeChannelIfConnectNoActive(String channelId) {
        if (CHANNELS.containsKey(channelId) && !CHANNELS.get(channelId).isActive()) {
            return CHANNELS.remove(channelId);
        }

        return null;
    }

    public String getUserIdByChannel(Channel channel) {
        return getUserIdByChannel(channel.id().toString());
    }

    public String getUserIdByChannel(String channelId) {
        if (CHANNELS.containsKey(channelId)) {
            return CHANNELS.get(channelId).getUserId();
        }

        return null;
    }

    public NettyChannel getActiveChannelByUserId(String userId) {
        for (Map.Entry<String, NettyChannel> entry : CHANNELS.entrySet()) {
            if (entry.getValue().getUserId().equals(userId) && entry.getValue().isActive()) {
                return entry.getValue();
            }
        }
        return null;
    }
}


