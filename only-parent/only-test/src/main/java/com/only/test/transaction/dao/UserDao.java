package com.only.test.transaction.dao;

import com.only.test.transaction.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class UserDao {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public int insert(User user) {
        String sql = "insert into user(username) values (?)";
        return jdbcTemplate.update(sql, user.getUsername());
    }

}
