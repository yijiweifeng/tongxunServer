package com.lt.domain.service;

import com.lt.dal.entry.UserEntity;
import com.lt.domain.bean.JsonResult;
import com.lt.domain.req.*;


/**
 * @author sj
 * @date 2019/8/14 13:50
 */
public interface IUserService {
    public JsonResult<UserEntity> getUserList(UserListReq req);

    JsonResult login(LoginReq req);

    JsonResult regiser(RegiserReq req);

    JsonResult addFriend(AddFriendReq req);

    JsonResult getFriendList(CommonReq req);

    JsonResult addGroup(AddGroupReq req);

    JsonResult getMyCreateGroup(CommonReq req);

    JsonResult getMyJoinGroup(CommonReq req);

    JsonResult joinGroup(JoinGroupReq req);

    JsonResult getGroupFreidList(CommonReq req);

    JsonResult getNotReceiveInfoByUserId(CommonReq req);

    public JsonResult addNotSendInfo(AddNotSendInfoReq req);

    public JsonResult addFinishSendInfo(AddFinishSendInfoReq req);

    public JsonResult getHistoryInfoList(GetHistoryInfoReq req);

    void delNotReceidedInfoById(Long id);
}
