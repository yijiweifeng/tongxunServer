package com.lt.domain.req;

import java.io.Serializable;

/**
 * @author sj
 * @date 2019/8/15 15:07
 */
public class GetHistoryInfoReq implements Serializable {
    private Long userId;
    private Long friendOrGroupId;
    private Integer infoType;

    public Integer getInfoType() {
        return infoType;
    }

    public void setInfoType(Integer infoType) {
        this.infoType = infoType;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getFriendOrGroupId() {
        return friendOrGroupId;
    }

    public void setFriendOrGroupId(Long friendOrGroupId) {
        this.friendOrGroupId = friendOrGroupId;
    }
}
