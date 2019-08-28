package com.lt.domain.service.impl;

import com.lt.common.utils.MapperUtil;
import com.lt.dal.entry.*;
import com.lt.dal.mapper.*;
import com.lt.dal.mapper.ext.UserMapperExt;
import com.lt.domain.bean.JsonResult;
import com.lt.domain.req.*;
import com.lt.domain.resq.UserResq;
import com.lt.domain.service.IUserService;
import com.lt.nettyServer.ChannelContainer;
import com.lt.nettyServer.NettyChannel;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author sj
 * @date 2019/8/14 15:27
 */
@Service
public class UserServiceImpl implements IUserService {

    private static Logger logger = LogManager.getLogger(UserServiceImpl.class);

    @Resource
    MapperUtil mapperUtil;
    @Resource
    UserMapper userMapper;
    @Resource
    UserMapperExt userMapperExt;
    @Resource
    GoodFriendMapper goodFriendMapper;
    @Resource
    UserGroupRelationMapper userGroupRelationMapper;
    @Resource
    GroupMapper groupMapper;
    @Resource
    NotReceivedMapper notReceivedMapper;
    @Resource
    InfoReceivedMapper infoReceivedMapper;

    @Override
    public JsonResult<UserEntity> getUserList(UserListReq req) {
        UserEntity userEntity = new UserEntity();
        userEntity.setTel(req.getTel());
        List<UserEntity> all = userMapper.select(userEntity);
        List<UserResq> list = (mapperUtil.map(all, UserResq.class));
        return JsonResult.getSuccessResult(list, "success");
    }

    @Override
    public JsonResult login(LoginReq req) {

        Example example = new Example(UserEntity.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("tel", Long.parseLong(req.getTel()));
        criteria.andEqualTo("password", req.getPassword());
        List<UserEntity> userlist = userMapper.selectByExample(example);

        if (CollectionUtils.isEmpty(userlist)) {
            return JsonResult.getFailResult("密码或用户名称错误");
        }
        if (userlist.size() > 1) {
            logger.error("账号异常" + req.getTel());
        }
        userlist.get(0).setPassword(null);
        NettyChannel channel = ChannelContainer.getInstance().getActiveChannelByUserId(userlist.get(0).getId());
        if(channel==null){
            return JsonResult.getSuccessResult(userlist.get(0), "登录成功!");
        }else {
            return JsonResult.getFailResult("该账户已经登录不能再登录了!");
        }



    }

    @Override
    public JsonResult regiser(RegiserReq req) {
        Example example = new Example(UserEntity.class);
        example.createCriteria().andEqualTo("tel", req.getTel());
        List<UserEntity> userEntities = userMapper.selectByExample(example);
        if (userEntities != null && userEntities.size() > 0) {
            return JsonResult.getFailResult("注册失败,该手机号已经注册");
        }

        try {
            UserEntity userEntity = new UserEntity();
            userEntity.setName(req.getName());
            userEntity.setTel(Long.valueOf(req.getTel()));
            userEntity.setPassword(req.getPassword());
            userMapper.insertSelective(userEntity);
            List<UserEntity> select = userMapper.select(userEntity);
            return JsonResult.getSuccessResult(select.get(0), "注册成功,请登录!");
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("注册发生错误！");

        }
        return JsonResult.getFailResult("注册失败!");
    }

    @Override
    public JsonResult addFriend(AddFriendReq req) {
        Example example = new Example(GoodFriendEntity.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("userId", req.getId());
        criteria.andEqualTo("friendId", req.getFriendId());
        List<GoodFriendEntity> goodFriendEntities = goodFriendMapper.selectByExample(example);
        if (goodFriendEntities.size() > 0) {
            return JsonResult.getFailResult("已经是你好友了!");
        }
        GoodFriendEntity friendEntity = new GoodFriendEntity();
        friendEntity.setUserId(req.getId());
        friendEntity.setFriendId(req.getFriendId());
        int i = goodFriendMapper.insertSelective(friendEntity);
        if (i > 0) {

            Example example2 = new Example(GoodFriendEntity.class);
            Example.Criteria criteria2 = example2.createCriteria();
            criteria2.andEqualTo("userId", req.getFriendId());
            criteria2.andEqualTo("friendId", req.getId());
            List<GoodFriendEntity> goodFriendEntities2 = goodFriendMapper.selectByExample(example2);
            if (CollectionUtils.isEmpty(goodFriendEntities2)) {
                friendEntity.setUserId(req.getFriendId());
                friendEntity.setFriendId(req.getId());
                i = goodFriendMapper.insertSelective(friendEntity);
            }
            return JsonResult.getSuccessResult("添加成功");

        }

        return JsonResult.getFailResult("添加失败");
    }

    @Override
    public JsonResult getFriendList(CommonReq req) {
        List<UserEntity> friednList = userMapperExt.getFriednList(req.getId());
        List<UserResq> list = (mapperUtil.map(friednList, UserResq.class));

        return JsonResult.getSuccessResult(list, "获取好友成功");
    }

    @Override
    public JsonResult addGroup(AddGroupReq req) {
        GroupEntry groupEntry = new GroupEntry();
        groupEntry.setGroupName(req.getName());
        groupEntry.setGroupType(req.getType());
        groupEntry.setCreateUserId(req.getUserId());
        Long result = userMapperExt.addGroup(groupEntry);

        if (result != null) {
            UserGroupRelationEntry userGroupRelationEntry = new UserGroupRelationEntry();
            userGroupRelationEntry.setGroupId(groupEntry.getId());
            userGroupRelationEntry.setUserId(req.getUserId());
            int insert = userGroupRelationMapper.insert(userGroupRelationEntry);
            if (insert > 0) {
                return JsonResult.getSuccessResult(groupEntry, "创建成功");

            }
        }
        return JsonResult.getFailResult("创建失败");
    }

    @Override
    public JsonResult getMyCreateGroup(CommonReq req) {
        List<GroupEntry> myCreateGroup = userMapperExt.getMyCreateGroup(req.getId());
        return JsonResult.getSuccessResult(myCreateGroup, "获取我创建的群列表成功");
    }

    @Override
    public JsonResult getMyJoinGroup(CommonReq req) {
        List<GroupEntry> myCreateGroup = userMapperExt.getMyJoinGroup(req.getId());
        return JsonResult.getSuccessResult(myCreateGroup, "获取我加入的群列表成功");
    }

    @Override
    public JsonResult joinGroup(JoinGroupReq req) {

        GroupEntry groupEntry = groupMapper.selectByPrimaryKey(req.getGroupId());
        if (groupEntry == null) {
            return JsonResult.getFailResult("没有该群");
        }


        UserGroupRelationEntry userGroupRelationEntry = new UserGroupRelationEntry();
        userGroupRelationEntry.setUserId(req.getUserId());
        userGroupRelationEntry.setGroupId(req.getGroupId());
        UserGroupRelationEntry userGroupRelationEntry1 = userGroupRelationMapper.selectOne(userGroupRelationEntry);
        if (userGroupRelationEntry1 != null) {
            return JsonResult.getFailResult("你已在该群");
        }
        int i = userGroupRelationMapper.insertSelective(userGroupRelationEntry);
        if (i > 0) {
            return JsonResult.getSuccessResult("加入成功");
        }
        return JsonResult.getFailResult("加入失败");

    }

    @Override
    public JsonResult getGroupFreidList(CommonReq req) {
        List<UserEntity> groupFreidList = userMapperExt.getGroupFreidList(req.getId());
        List<UserResq> list = (mapperUtil.map(groupFreidList, UserResq.class));
        return JsonResult.getSuccessResult(list, "获取该群成员列表成功");
    }

    @Override
    public JsonResult getNotReceiveInfoByUserId(CommonReq req) {
        Example example = new Example(NotReceivedEntity.class);
        example.createCriteria().andEqualTo("toId", req.getId());
        List<NotReceivedEntity> notReceivedEntities = notReceivedMapper.selectByExample(example);
        if (notReceivedEntities != null && notReceivedEntities.size() > 0) {
            return JsonResult.getSuccessResult(notReceivedEntities, "获取待接收信息成功");
        } else {
            return JsonResult.getFailResult("没有待接收的信息");
        }

    }

    public JsonResult addFinishSendInfo(AddFinishSendInfoReq req) {
        InfoReceivedEntity entity = mapperUtil.map(req, InfoReceivedEntity.class);
        int insert = infoReceivedMapper.insert(entity);
        if (insert > 0) {
            return JsonResult.getSuccessResult("缓存消息成功");
        }
        return JsonResult.getFailResult("缓存消息失败");
    }

    @Override
    public JsonResult getHistoryInfoList(GetHistoryInfoReq req) {

        Map<String, List<InfoReceivedEntity>> result = new HashMap<String, List<InfoReceivedEntity>>();

        if (req.getInfoType() == 1) {
            Example example = new Example(InfoReceivedEntity.class);
            example.createCriteria().andEqualTo("fromId", req.getUserId())
                    .andEqualTo("toId", req.getFriendOrGroupId())
                    .andEqualTo("infoType", 1);
            List<InfoReceivedEntity> mySendList = infoReceivedMapper.selectByExample(example);

            Example example2 = new Example(InfoReceivedEntity.class);
            example2.createCriteria().andEqualTo("fromId", req.getFriendOrGroupId())
                    .andEqualTo("toId", req.getUserId())
                    .andEqualTo("infoType", 1);
            List<InfoReceivedEntity> myreceivedList = infoReceivedMapper.selectByExample(example2);
            result.put("mySendList", mySendList);
            result.put("myReceivedList", myreceivedList);
        } else if (req.getInfoType() == 2) {
            Example example = new Example(InfoReceivedEntity.class);
            example.createCriteria()
                    .andEqualTo("groupId", req.getFriendOrGroupId())
                    .andEqualTo("infoType", 2);
            List<InfoReceivedEntity> groupInfo = infoReceivedMapper.selectByExample(example);

            result.put("groupInfo", groupInfo);
        }

        return JsonResult.getSuccessResult(result, "获取好友历史消息成功");
    }

    public JsonResult addNotSendInfo(AddNotSendInfoReq req) {
        NotReceivedEntity entity = mapperUtil.map(req, NotReceivedEntity.class);
        int insert = notReceivedMapper.insert(entity);
        if (insert > 0) {
            return JsonResult.getSuccessResult("缓存离线消息成功");
        }
        return JsonResult.getFailResult("缓存离线消息失败");
    }

    public void delNotReceidedInfoById(Long id) {
        notReceivedMapper.deleteByPrimaryKey(id);
    }


}
