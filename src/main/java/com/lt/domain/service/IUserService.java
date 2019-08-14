package com.lt.domain.service;

import com.lt.dal.entry.UserEntity;
import com.lt.domain.bean.JsonResult;
import com.lt.domain.req.LoginReq;
import com.lt.domain.req.RegiserReq;


/**
 * @author sj
 * @date 2019/8/14 13:50
 */
public interface IUserService {
    public JsonResult<UserEntity> getUserList();

    JsonResult login(LoginReq req);

    JsonResult regiser(RegiserReq req);
}
