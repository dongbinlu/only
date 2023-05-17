package com.only.test.mybatis.mapper;

import com.only.test.mybatis.plugin.Page;
import com.only.test.mybatis.entity.User;
import org.apache.ibatis.annotations.CacheNamespace;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.apache.ibatis.cache.decorators.LruCache;
import org.apache.ibatis.cache.impl.PerpetualCache;

import java.util.List;

//@CacheNamespace(
//        implementation= PerpetualCache.class, //缓存实现 Cache接口实现类
//        eviction= LruCache.class,// 缓存算法
//        flushInterval=60000, // 刷新时间，单位：毫秒；这里的刷新是指缓存数据的有效期
//        size=1024,   // 最大缓存引用对象
//        readWrite=true, // 是否可写,也就是是否需要进行拷贝，true:必须实现序列化接口
//        blocking=false  // 是否阻塞，防止缓存击穿。
//)

public interface UserMapper {

    //@Select("select * from user where user_id = #{userId}")
    User getByUserId(Integer userId);

    @Update("update user set username = #{username} where user_id = #{userId}")
    void updateByUserId(String username, Integer userId);

    User getByUsername(String username);

    @Select("select * from user where username = #{username}")
    List<User> getByUsernameTPage(String username, Page page);


}
