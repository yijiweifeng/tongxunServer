package com.lt.dal.entry;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * @author sj
 * @date 2019/8/15 14:04
 */
@Entity
@Table(name = "information_received")
public class InfoReceivedEntity implements Serializable {
    @Id
    @Column(name = "id")
    private Long id;
    @Column(name = "info_id")
    private String infoId;
    @Column(name = "sender_id")
    private Long fromId;
    @Column(name = "receiver")
    private Long toId;
    @Column(name = "send_time")
    private String sendTime;
    @Column(name = "receiving_time")
    private String receiveTime;
    @Column(name = "content")
    private String content;
    @Column(name = "content_type")
    private Integer contentType;
    @Column(name = "upload_url")
    private String uploadUrl;

    public String getInfoId() {
        return infoId;
    }

    public void setInfoId(String infoId) {
        this.infoId = infoId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getFromId() {
        return fromId;
    }

    public void setFromId(Long fromId) {
        this.fromId = fromId;
    }

    public Long getToId() {
        return toId;
    }

    public void setToId(Long toId) {
        this.toId = toId;
    }

    public String getSendTime() {
        return sendTime;
    }

    public void setSendTime(String sendTime) {
        this.sendTime = sendTime;
    }

    public String getReceiveTime() {
        return receiveTime;
    }

    public void setReceiveTime(String receiveTime) {
        this.receiveTime = receiveTime;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Integer getContentType() {
        return contentType;
    }

    public void setContentType(Integer contentType) {
        this.contentType = contentType;
    }

    public String getUploadUrl() {
        return uploadUrl;
    }

    public void setUploadUrl(String uploadUrl) {
        this.uploadUrl = uploadUrl;
    }
}
