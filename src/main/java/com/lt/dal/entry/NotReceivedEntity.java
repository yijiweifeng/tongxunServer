package com.lt.dal.entry;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * @author sj
 * @date 2019/8/15 14:12
 */
@Entity
@Table(name = "not_received")
public class NotReceivedEntity implements Serializable {
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
    @Column(name = "content")
    private String content;
    @Column(name = "content_type")
    private Integer contentType;
    @Column(name = "upload_url")
    private String uploadUrl;
    @Column(name = "info_type")
    private Integer infoType;
    @Column(name = "group_id")
    private Long groupId;

    public Long getGroupId() {
        return groupId;
    }

    public void setGroupId(Long groupId) {
        this.groupId = groupId;
    }

    public Integer getInfoType() {
        return infoType;
    }

    public void setInfoType(Integer infoType) {
        this.infoType = infoType;
    }

    public String getSendTime() {
        return sendTime;
    }

    public void setSendTime(String sendTime) {
        this.sendTime = sendTime;
    }

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
