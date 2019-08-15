package com.lt.domain.req;

import java.io.Serializable;

/**
 * @author sj
 * @date 2019/8/15 10:00
 */
public class AddFriendReq implements Serializable {
    private Long id;
    private Long friendId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getFriendId() {
        return friendId;
    }

    public void setFriendId(Long friendId) {
        this.friendId = friendId;
    }
}
