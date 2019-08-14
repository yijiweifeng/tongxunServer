package com.lt.domain.service.impl;

import com.lt.dal.entry.UserEntity;
import com.lt.dal.mapper.UserMapper;
import com.lt.domain.bean.JsonResult;
import com.lt.domain.req.LoginReq;
import com.lt.domain.req.RegiserReq;
import com.lt.domain.service.IUserService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author sj
 * @date 2019/8/14 15:27
 */
@Service
public class UserServiceImpl implements IUserService {

    private static Logger logger = LogManager.getLogger(UserServiceImpl.class);

    @Resource
    UserMapper userMapper;
    @Override
    public JsonResult<UserEntity> getUserList() {
        List<UserEntity> all = userMapper.getAll();
        return JsonResult.getSuccessResult(all,"success");
    }

    @Override
    public JsonResult login(LoginReq req) {
        UserEntity  userEntity =new UserEntity();
        userEntity.setTel( Long.parseLong(req.getTel()));
        userEntity.setPassword(req.getPassword());
        UserEntity login = userMapper.login(userEntity);
        if(login==null){
            return JsonResult.getFailResult("密码或用户名称错误");
        }
        return JsonResult.getSuccessResult(login,"登录成功!");

    }

    @Override
    public JsonResult regiser(RegiserReq req) {

        try {
            UserEntity  userEntity =new UserEntity();
            userEntity.setName(req.getName());
            userEntity.setTel( Long.parseLong(req.getTel()));
            userEntity.setPassword(req.getPassword());
            userMapper.insert(userEntity);
        }catch (Exception e){
            e.printStackTrace();
            logger.error("注册发生错误！");
            return JsonResult.getFailResult("注册失败!");
        }

        return JsonResult.getSuccessResult("注册成功,请登录!");
    }
}
