package com.only.test.mybatis;

import com.only.test.mybatis.entity.User;
import com.only.test.mybatis.mapper.RoleMapper;
import com.only.test.mybatis.mapper.UserMapper;
import com.only.test.mybatis.plugin.Page;
import org.apache.commons.io.FileUtils;
import org.apache.ibatis.session.ExecutorType;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.FileInputStream;
import java.util.List;
import java.util.UUID;

public class MybatisMain {
    SqlSessionFactory sqlSessionFactory = null;
    SqlSession session = null;

    @Before
    public void before() throws Exception {
        FileInputStream inputStream = FileUtils.openInputStream(new File("src/main/java/com/only/test/mybatis/mybatis-config.xml"));
        // SqlSessionFactoryBuilder 构建会话工厂，基于mybatis.config.xml,构建完成后即可丢弃。
        sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);
        // SqlSessionFactory 用于生成会话的工厂，作用于整个应用运行期间，不需要构建多个，一个会话工厂即可。

        // SqlSesson 作用于单次会话，如web一次请求期间，不能作用于某个对象的一个属性，也不能在多个线程间共享，因为它是线程不安全的。
        session = sqlSessionFactory.openSession(true);
    }

    @Test
    public void testPage() throws Exception {
        UserMapper mapper = session.getMapper(UserMapper.class);
        List<User> list = mapper.getByUsernameTPage("jojo", new Page(1, 2));
        System.out.println(list);
    }

    /**
     * 测试二级缓存
     * <p>
     * 如果两个namespace对同一张表进行操作，是否会导致当前namespace的二级缓存失效，答案 不失效
     */
    @Test
    public void testCache()throws Exception {


        UserMapper userMapper = session.getMapper(UserMapper.class);

        User user1 = userMapper.getByUserId(1);

        RoleMapper roleMapper = session.getMapper(RoleMapper.class);
        roleMapper.updateByUserId("taobao",1);
//        roleMapper.updateByRoleId("huawei", 1);

        session.close();

        Thread thread2 = new Thread(() -> {
            UserMapper mapper2 = sqlSessionFactory.openSession(true).getMapper(UserMapper.class);
            User user2 = mapper2.getByUserId(1);

        });

        thread2.start();
        thread2.join();


    }


    @Test
    public void test() throws Exception {
        UserMapper mapper1 = session.getMapper(UserMapper.class);
        UserMapper mapper2 = session.getMapper(UserMapper.class);
        System.out.println(mapper1 == mapper2);// false 注意 这个是不一样的，因为每次都会创建一个新的动态代理对象
        User user1 = mapper1.getByUserId(1);
        User byUserId = mapper1.getByUserId(1);

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

    @Test
    public void testBatchInsertByBatch(){

        sqlSessionFactory.getConfiguration().setDefaultExecutorType(ExecutorType.BATCH);
        SqlSession sqlSession = sqlSessionFactory.openSession(true);


        UserMapper userMapper = sqlSession.getMapper(UserMapper.class);
        long start = System.currentTimeMillis();
        for (int i = 0; i < 10000; i++) {

            User user = User.builder()
                    .username(UUID.randomUUID().toString())
                    .build();

            userMapper.saveUser(user);
        }
        System.out.println("耗时：" + (System.currentTimeMillis() - start));// 1665

        sqlSession.commit();


    }

    @Test
    public void testBatchInsertBySimple(){

        sqlSessionFactory.getConfiguration().setDefaultExecutorType(ExecutorType.SIMPLE);
        SqlSession sqlSession = sqlSessionFactory.openSession(true);


        UserMapper userMapper = sqlSession.getMapper(UserMapper.class);
        long start = System.currentTimeMillis();
        for (int i = 0; i < 10000; i++) {

            User user = User.builder()
                    .username(UUID.randomUUID().toString())
                    .build();

            userMapper.saveUser(user);
        }
        System.out.println("耗时：" + (System.currentTimeMillis() - start));// 12128

        sqlSession.commit();


    }



}
