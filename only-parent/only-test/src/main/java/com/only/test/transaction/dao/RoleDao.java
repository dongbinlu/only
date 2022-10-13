package com.only.test.transaction.dao;

import com.only.test.transaction.entity.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class RoleDao {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public int insert(Role role) {
        String sql = "insert into role(role_name) values (?)";
        return jdbcTemplate.update(sql, role.getRoleName());
    }

}
