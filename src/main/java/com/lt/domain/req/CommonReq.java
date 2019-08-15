package com.lt.domain.req;

import java.io.Serializable;

/**
 * @author sj
 * @date 2019/8/15 10:11
 */
public class CommonReq implements Serializable {
    private Long id;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
