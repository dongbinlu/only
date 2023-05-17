package com.only.test.mybatis;

import com.google.common.collect.Maps;
import com.only.test.mybatis.entity.User;
import com.only.test.mybatis.mapper.UserMapper;
import com.only.test.mybatis.plugin.Page;
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
    public void testPage() throws Exception {
        UserMapper mapper = session.getMapper(UserMapper.class);
        List<User> list = mapper.getByUsernameTPage("jojo", new Page(1, 2));
        System.out.println(list);
    }

    @Test
    public void test() throws Exception {
        UserMapper mapper1 = session.getMapper(UserMapper.class);
        UserMapper mapper2 = session.getMapper(UserMapper.class);
        User user1 = mapper1.getByUserId(1);

         /*
         如果另一个session并发修改数据，1级缓存还是会生效，不同的session有不通的缓存空间
          */
        Thread thread = new Thread(() -> {
            System.out.println("inner执行了");
            SqlSession sqlSession = sqlSessionFactory.openSession();
            UserMapper userMapper2 = sqlSession.getMapper(UserMapper.class);
            User user2 = userMapper2.getByUserId(1);

            userMapper2.updateByUserId("rrrrrrrrr", 1);
            sqlSession.commit();
            System.out.println("执行结束");
        });
        thread.start();
        thread.join();


        User user2 = mapper1.getByUserId(1);

        System.out.println(user1 == user2);


        // 只有关闭会话后才会进行二级缓存
        session.close();

        /**
         * MyBatis的二级缓存是应用程序级别的缓存，它会缓存数据库查询结果，提高应用程序的性能。
         * 在使用MyBatis二级缓存时，可能会出现从缓存中取出来的对象地址不一样的情况，
         * 这是因为MyBatis缓存中缓存的是反序列化后的对象，而不是原始对象。
         * 当从缓存中获取对象时，MyBatis会新建一个实例，并将缓存中反序列化得到的对象属性值复制到新建的对象中
         * ，因此，新建的对象的内存地址是不同的，但是属性值是相同的。
         */
        Thread thread2 = new Thread(() -> {
            UserMapper mapper3 = sqlSessionFactory.openSession(true).getMapper(UserMapper.class);
            User user3 = mapper3.getByUserId(1);

            System.out.println(user2 == user3);
        });

        thread2.start();
        thread2.join();


    }


}
