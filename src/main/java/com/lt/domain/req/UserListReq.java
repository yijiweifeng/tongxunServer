package com.lt.domain.req;

import lombok.Data;

import java.io.Serializable;

/**
 * Created with IntelliJ IDEA.
 * User: joker
 * Date: 2019/8/19
 * Time: 16:44
 * Description: No Description
 */
@Data
public class UserListReq implements Serializable {

    private Long tel;
    private String name;

}
