package com.safecode.security.user.dao;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;

import com.safecode.security.user.entity.User;

/**
 * <p>
 *
 * @author ludongbin
 * @Title: UserRepository.java
 * <p>
 * <p>
 * @Description: JpaSpecificationExecutor
 * <T>接口为了做一些动态的查询,参数：实体类,CrudRepository<T, ID>做一些增删改查的操作,参数1：实体类，参数二：主键类型
 * </p>
 * @date 2019年10月18日上午11:53:48
 */
public interface UserRepository extends JpaSpecificationExecutor<User>, CrudRepository<User, Long> {

    User findByUsername(String username);

}
