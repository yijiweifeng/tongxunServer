package com.lt.domain.req;

import java.io.Serializable;

/**
 * @author sj
 * @date 2019/8/15 11:26
 */
public class JoinGroupReq implements Serializable {
    private Long userId;
    private Long groupId;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getGroupId() {
        return groupId;
    }

    public void setGroupId(Long groupId) {
        this.groupId = groupId;
    }
}
