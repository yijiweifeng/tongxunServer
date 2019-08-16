package com.lt.common.Enum;

/**
 * <p>@ProjectName:     NettyChat</p>
 * <p>@ClassName:       MessageType.java</p>
 * <p>@PackageName:     com.freddy.chat.im</p>
 * <b>
 * <p>@Description:     消息类型</p>
 * </b>
 * <p>@author:          FreddyChen</p>
 * <p>@date:            2019/04/08 00:04</p>
 * <p>@email:           chenshichao@outlook.com</p>
 */
public enum MessageType {

    /*
     * 握手消息
     */
    HANDSHAKE(1001),

    /*
     * 心跳消息
     */
    HEARTBEAT(1002),

    /*
     * 客户端提交的消息接收状态报告 不要了
     */
    CLIENT_MSG_RECEIVED_STATUS_REPORT(1009),

    /*
     * 服务端返回的消息发送状态报告  消息状态statusReport 0:发送失败 1：发送成功
     */
    SERVER_MSG_SENT_STATUS_REPORT(1010),


    /**
     * 单聊消息
     */
    SINGLE_CHAT(2001),

    /**
     * 单聊消息离线
     */
    SINGLE_CHAT_NOT(2002),

    /**
     * 群聊消息
     */
    GROUP_CHAT(3001),

    /**
     * 群聊消息离线
     */
    GROUP_CHAT_NOT(3002)
    ;

    private int msgType;

    MessageType(int msgType) {
        this.msgType = msgType;
    }

    public int getMsgType() {
        return this.msgType;
    }

    public enum MessageContentType {

        /**
         * 文本消息
         */
        TEXT(101),

        /**
         * 图片消息
         */
        IMAGE(102),

        /**
         * 语音消息
         */
        VOICE(103);

        private int msgContentType;

        MessageContentType(int msgContentType) {
            this.msgContentType = msgContentType;
        }

        public int getMsgContentType() {
            return this.msgContentType;
        }
    }

    public enum StatusReportEnum {

        /**
         * 消息转发失败
         */
        FAIL(0),

        /**
         * 消息转发成功
         */
        SUCCESS_1(1),

        /**
         * 消息转发成功 离线消息
         */
        SUCCESS_2(2);

        private int statusReport;

        StatusReportEnum(int msgContentType) {
            this.statusReport = msgContentType;
        }

        public int getTypeCode() {
            return this.statusReport;
        }
    }
}
