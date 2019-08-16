package com.lt.web.controller;

import com.lt.domain.bean.JsonResult;
import com.lt.domain.req.*;
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

    @ApiOperation(value = "所有用户列表", notes = "")
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

    @ApiOperation(value = "添加好友", notes = "")
    @RequestMapping(value = "add_friend",method = RequestMethod.POST)
    public JsonResult addFriend(AddFriendReq req){

        return  iUserService.addFriend(req);
    }

    @ApiOperation(value = "获取好友列表 通过用户id", notes = "")
    @RequestMapping(value = "get_friend_list",method = RequestMethod.POST)
    public JsonResult getFriendList(CommonReq req){

        return  iUserService.getFriendList(req);
    }

    @ApiOperation(value = "创建群", notes = "")
    @RequestMapping(value = "add_group",method = RequestMethod.POST)
    public JsonResult addGroup(AddGroupReq req){

        return  iUserService.addGroup(req);
    }

    @ApiOperation(value = "加入群 通过群id", notes = "")
    @RequestMapping(value = "join_group ",method = RequestMethod.POST)
    public JsonResult joinGroup(JoinGroupReq req){

        return  iUserService.joinGroup(req);
    }

    @ApiOperation(value = "获取我创建的群列表 通过用户id", notes = "")
    @RequestMapping(value = "get_my_cteate_group_list",method = RequestMethod.POST)
    public JsonResult getMyCreateGroup(CommonReq req){

        return  iUserService.getMyCreateGroup(req);
    }

    @ApiOperation(value = "获取我加入的群列表 通过用户id", notes = "")
    @RequestMapping(value = "get_my_join_group_list",method = RequestMethod.POST)
    public JsonResult getMyJoinGroup(CommonReq req){

        return  iUserService.getMyJoinGroup(req);
    }

    @ApiOperation(value = "获取某个群内的用户列表 通过群id", notes = "")
    @RequestMapping(value = "get_group_friend_list",method = RequestMethod.POST)
    public JsonResult getGroupFreidList(CommonReq req){

        return  iUserService.getGroupFreidList(req);
    }

    @ApiOperation(value = "获取待接收信息列表 通过用户id", notes = "")
    @RequestMapping(value = "get_not_received_list",method = RequestMethod.POST)
    public JsonResult getNotReceivedInfoList(CommonReq req){

        return  iUserService.getNotReceiveInfoByUserId(req);
    }

    @ApiOperation(value = "以发消息入库 ", notes = "")
    @RequestMapping(value = "add_finish_info",method = RequestMethod.POST)
    public JsonResult addFinishSendInfo(AddFinishSendInfoReq req){

        return  iUserService.addFinishSendInfo(req);
    }
    @ApiOperation(value = "离线消息入库 ", notes = "")
    @RequestMapping(value = "add_not_received_info",method = RequestMethod.POST)
    public JsonResult addNotSendInfo(AddNotSendInfoReq req){
        return iUserService.addNotSendInfo(req);
    }

    @ApiOperation(value = "获取某个历史消息 by用户id和好友id/群id ", notes = "")
    @RequestMapping(value = "get_friend_info_list",method = RequestMethod.POST)
    public JsonResult getHistoryInfoList(GetHistoryInfoReq req){
        return iUserService.getHistoryInfoList(req);
    }






}
