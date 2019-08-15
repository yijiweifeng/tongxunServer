package com.lt.dal.mapper;

import com.lt.dal.entry.UserEntity;
import tk.mybatis.mapper.common.Mapper;

/**
 * @author sj
 * @date 2019/8/14 13:00
 */
public interface UserMapper  extends Mapper<UserEntity> {
//    @Select("SELECT id,name,tel FROM user_info")
//    @Results({
//            @Result(property = "id",  column = "id"),
//            @Result(property = "name", column = "name"),
//            @Result(property = "tel", column = "tel")
//    })
//    List<UserEntity> getAll();
//
//    @Select("SELECT id,name,tel FROM user_info WHERE tel = #{tel} and password = #{password}")
//    @Results({
//            @Result(property = "id",  column = "id"),
//            @Result(property = "name", column = "name"),
//            @Result(property = "tel", column = "tel")
//    })
//    UserEntity login(UserEntity user);
//    @Select("SELECT id,name,tel FROM user_info WHERE id = #{id}")
//    @Results({
//            @Result(property = "id",  column = "id"),
//            @Result(property = "name", column = "name"),
//            @Result(property = "tel", column = "tel")
//    })
//    UserEntity getOne(Long id);
//
//    @Insert("INSERT INTO user_info(name,tel,password) VALUES(#{name}, #{tel}, #{password})")
//    void insert(UserEntity user);
//
//    @Update("UPDATE user_info SET name=#{name},password=#{password} WHERE id =#{id}")
//    void update(UserEntity user);
//
//    @Delete("DELETE FROM user_info WHERE id =#{id}")
//    void delete(Long id);



}
