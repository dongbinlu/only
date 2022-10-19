package com.only.test.mybatis.mapper;

import com.only.test.mybatis.Page;
import com.only.test.mybatis.User;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

public interface UserMapper {

    User getByUserId(Integer userId);

    @Update("update user set username = #{username} where user_id = #{userId}")
    void updateByUserId(String username, Integer userId);

    User getByUsername(String username);

    @Select("select * from user where username = #{username}")
    List<User> getByUsernameTPage(String username, Page page);


}
