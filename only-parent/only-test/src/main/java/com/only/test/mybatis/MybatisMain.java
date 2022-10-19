package com.only.test.mybatis;

import com.only.test.mybatis.mapper.UserMapper;
import org.apache.commons.io.FileUtils;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.FileInputStream;
import java.util.List;

public class MybatisMain {
    SqlSessionFactory sqlSessionFactory = null;
    SqlSession session = null;

    @Before
    public void before() throws Exception {
        FileInputStream inputStream = FileUtils.openInputStream(new File("D:\\only\\only-parent\\only-test\\src\\main\\java\\com\\only\\test\\mybatis\\mybatis-config.xml"));
        sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);
        session = sqlSessionFactory.openSession(true);
    }

    @Test
    public void test() throws Exception {
        UserMapper mapper = session.getMapper(UserMapper.class);
        //User user = mapper.getByUserId(1);
        // 只有关闭会话后才会进行二级缓存
        //session.close();
        //mapper.updateByUserId("boy", 1);

        //UserMapper mapper1 = sqlSessionFactory.openSession(true).getMapper(UserMapper.class);
        //User user2 = mapper1.getByUserId(1);

        //User user3 = mapper1.getByUsername("boy");

        List<User> users = mapper.getByUsernameTPage("boy", new Page(2, 1));

        //System.out.println(user == user2);

    }


}
