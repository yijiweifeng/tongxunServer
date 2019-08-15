package com.lt.dal.mapper.ext;

import com.lt.dal.entry.GroupEntry;
import com.lt.dal.entry.UserEntity;

import java.util.List;

/**
 * @author sj
 * @date 2019/8/15 9:41
 */

public interface UserMapperExt {
   List<UserEntity> getAll();
   List<UserEntity> getFriednList(Long id);
   Long addGroup(GroupEntry groupEntry);
   List<GroupEntry> getMyCreateGroup(Long id);
   List<GroupEntry> getMyJoinGroup(Long id);
   List<UserEntity>getGroupFreidList(Long id);
}
