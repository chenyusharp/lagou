package com.lagou.test;

import com.lagou.dao.IUserDao;
import com.lagou.io.Resources;
import com.lagou.pojo.User;
import com.lagou.sqlSession.SqlSession;
import com.lagou.sqlSession.SqlSessionFactory;
import com.lagou.sqlSession.SqlSessionFactoryBuilder;
import org.junit.Assert;
import org.junit.Test;

import java.io.InputStream;
import java.util.List;

public class IPersistenceTest {

    @Test
    public void test() throws Exception {
        InputStream resourceAsSteam = Resources.getResourceAsSteam("sqlMapConfig.xml");
        SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(resourceAsSteam);
        SqlSession sqlSession = sqlSessionFactory.openSession();

        //调用
        User user = new User();
        user.setId(2);
        user.setUsername("tom");
        User user2 = sqlSession.selectOne("com.lagou.dao.IUserDao.findByCondition", user);
//
        System.out.println(user2);

        /*List<User> users = sqlSession.selectList("user.selectList");
        for (User user1 : users) {
            System.out.println(user1);
        }*/

        IUserDao userDao = sqlSession.getMapper(IUserDao.class);

        List<User> all = userDao.findAll();
        for (User user1 : all) {
            System.out.println(user1);
        }
        /*User addUser = new User();
        addUser.setUsername("xiazhenyu");
        addUser.setBirthday("7-7");
        addUser.setPassword("123456");
        int insertCount = userDao.addUser(addUser);

        User deleteUser = new User();
        deleteUser.setId(4);
        int deleteCount = userDao.deleteById(deleteUser);
        Assert.assertEquals(deleteCount, 1);*/

        User updateUser = new User();
        updateUser.setId(9);
        updateUser.setUsername("haha");
        int updateCount = userDao.updateUser(updateUser);
        Assert.assertEquals(updateCount, 1);


    }


}
