package com.only.multids.busi.mapper;

import com.only.multids.busi.bean.User;
import org.apache.ibatis.annotations.Insert;

public interface UserMapper {


    @Insert(" insert into user${tableSuffix}(user_id,order_id,money) values(#{userId},#{orderId},#{money}) ")
    void insertUser(User user);


}
