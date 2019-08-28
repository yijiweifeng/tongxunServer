package com.lt.nettyServer;

import com.lt.nettyServer.protobuf.MessageProtobuf;
import io.netty.util.internal.StringUtil;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * <p>@Description:     消息发送超时管理器，用于管理消息定时器的新增、移除等</p>
 * </b>
 */
public class MsgTimeoutTimerManager {

    private static Map<String, MsgTimeoutTimer> mMsgTimeoutMap = new ConcurrentHashMap<String, MsgTimeoutTimer>();

    private MsgTimeoutTimerManager() {

    }
    private static final MsgTimeoutTimerManager INSTANCE = new MsgTimeoutTimerManager();

    public static MsgTimeoutTimerManager getInstance() {
        return INSTANCE;
    }

    /**
     * 添加消息到发送超时管理器
     *
     * @param msg
     */
    public void add(MessageProtobuf.Msg msg) {
        if (msg == null || msg.getHead() == null) {
            return;
        }



        String id = msg.getHead().getMsgId()+msg.getHead().getToId();
        if (!mMsgTimeoutMap.containsKey(id)) {
            MsgTimeoutTimer timer = new MsgTimeoutTimer(msg);
            mMsgTimeoutMap.put(id, timer);
        }

        System.out.println("添加消息超发送超时管理器，message=" + msg + "\t当前管理器消息数：" + mMsgTimeoutMap.size());
    }

    /**
     * 从发送超时管理器中移除消息，并停止定时器
     *
     * @param msgIdToId
     * msgID+TpId
     */
    public void remove(String msgIdToId) {
        if (StringUtil.isNullOrEmpty(msgIdToId)) {
            return;
        }
        System.out.println(msgIdToId);
        MessageProtobuf.Msg msg = null;
        if(mMsgTimeoutMap.containsKey(msgIdToId)) {

            MsgTimeoutTimer timer = mMsgTimeoutMap.remove(msgIdToId);

            if (timer != null) {
                msg = timer.getMsg();
                timer.cancel();
                timer = null;
            }
        }

        System.out.println("从发送消息管理器移除消息，message=" + msg);
    }


}
