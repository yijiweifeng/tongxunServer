package com.lt.web.controller;

import com.lt.domain.bean.JsonResult;
import com.lt.domain.req.LoginReq;
import com.lt.domain.req.RegiserReq;
import com.lt.domain.service.IUserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @author sj
 * @date 2019/8/14 12:42
 */
@Api(value = "/api/im", tags = "用户接口")
@RestController
@RequestMapping("/api/im")
public class UserController {
    @Resource
    IUserService iUserService;

    @ApiOperation(value = "好友列表", notes = "")
    @RequestMapping(value = "get_user_list",method = RequestMethod.GET)
    public JsonResult getUserListById(){

        return  iUserService.getUserList();
    }

    @ApiOperation(value = "登录", notes = "")
    @RequestMapping(value = "login",method = RequestMethod.POST)
    public JsonResult login(LoginReq req){

        return  iUserService.login(req);
    }
    @ApiOperation(value = "注册", notes = "")
    @RequestMapping(value = "regiser",method = RequestMethod.POST)
    public JsonResult regiser(RegiserReq req){

        return  iUserService.regiser(req);
    }

}
