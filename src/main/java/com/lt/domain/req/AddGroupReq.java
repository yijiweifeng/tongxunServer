package com.lt.domain.req;

import java.io.Serializable;

/**
 * @author sj
 * @date 2019/8/15 10:37
 */
public class AddGroupReq implements Serializable {
    private Long userId;
    private String name;
    private Integer type;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }
}
