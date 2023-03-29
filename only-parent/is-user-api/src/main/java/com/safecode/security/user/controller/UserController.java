package com.safecode.security.user.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.safecode.security.user.entity.UserInfo;
import com.safecode.security.user.service.UserService;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private UserService userService;

    @GetMapping("/login")
    public void login(UserInfo info, HttpServletRequest request) {
        UserInfo user = userService.login(info);
        /*
         * 当你找不到session的时候是不是要创建一个session
         * 默认的没有参数的会创建一个session
         * 如果为false的话，是不会创建一个session
         */
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }
        if (user != null) {
            request.getSession(true).setAttribute("user", user);
        }
    }

    @GetMapping("/logout")
    public void logout(HttpServletRequest request) {
        request.getSession().invalidate();
    }

    /**
     * <p>@Title: create<p>
     * <p>@Description: 创建</p>
     *
     * @param user
     * @return
     * @date 2019年10月17日 下午6:01:29
     */
    @PostMapping
    public UserInfo create(@Validated UserInfo user) {
        return userService.create(user);
    }

    /**
     * <p>@Title: update<p>
     * <p>@Description: 修改</p>
     *
     * @param user
     * @date 2019年10月17日 下午6:02:11
     */
    @PutMapping("/{id}")
    public UserInfo update(UserInfo user) {

        return userService.update(user);
    }

    /**
     * <p>@Title: delete<p>
     * <p>@Description: 删除</p>
     *
     * @param id
     * @date 2019年10月17日 下午6:02:25
     */
    @DeleteMapping("/{id}")
    public void delete(@PathVariable("id") Long id) {
        userService.delete(id);
    }

    /**
     * <p>@Title: get<p>
     * <p>@Description: 根据ID查询</p>
     *
     * @param id
     * @return
     * @date 2019年10月18日 上午9:10:50
     */
    @GetMapping("/{id}")
    public UserInfo get(@PathVariable("id") Long id, HttpServletRequest request) {

        UserInfo user = (UserInfo) request.getSession().getAttribute("user");

        if (user == null || !user.getId().equals(id)) {
            throw new RuntimeException("身份认证信息异常，获取用户信息失败");
        }
        return userService.get(id);
    }

    /**
     * <p>@Title: query<p>
     * <p>@Description: 查询  SQL注入攻击：127.0.0.1:8080/users?name=' or 1=1 or name= '</p>
     *
     * @param user
     * @return
     * @date 2019年10月17日 下午6:03:28
     */
    @GetMapping
    public List query(UserInfo user) {
		/*
				String sql = "SELECT * FROM USER WHERE name = '" + user.getName() + "'";
		
				// SELECT * FROM USER WHERE name = '' or 1=1 or name= ''
				List list = jdbcTemplate.queryForList(sql);
				
		*/
        List userList = userService.query(user);
        return userList;
    }

}
