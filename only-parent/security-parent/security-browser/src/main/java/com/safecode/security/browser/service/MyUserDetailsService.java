package com.safecode.security.browser.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.social.security.SocialUser;
import org.springframework.social.security.SocialUserDetails;
import org.springframework.social.security.SocialUserDetailsService;
import org.springframework.stereotype.Component;

//处理用户信息获取逻辑
@Component("myUserDetailsService")
public class MyUserDetailsService implements UserDetailsService, SocialUserDetailsService {

    /*
     * PasswordEncoder  实现类BCryptPasswordEncoder
     * String encode(CharSequence rawPassword);用户注册的时候需要此方法进行密码加密
     * boolean matches(CharSequence rawPassword, String encodedPassword);此方法油SpringSecurity自己来调用
     * matches(用户传上来的明文密码，数据库里的暗文密码)进行匹配
     * 同样一个密码每次加密后的结果都不一样，因为它会随机生成一个盐，把生成的盐混在一个字符串里，在每次判断的时候，它会拿着随机生成的盐反推回来当时加密的串是什么，判断是否匹配
     */
    @Autowired
    private PasswordEncoder passwordEncoder;

    private Logger logger = LoggerFactory.getLogger(getClass());

    // 验证成功后将用户信息放到UserDetails接口中
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        logger.info("登录的用户名：" + username);
        return buildUser(username);
    }

    // 社交登录 springsocial通过openid 查出userid 通过userid构建userdetails的实现
    @Override
    public SocialUserDetails loadUserByUserId(String userId) throws UsernameNotFoundException {
        logger.info("社交登录用户ID：" + userId);
        return buildUser(userId);
    }

    private SocialUserDetails buildUser(String userId) {
        // 根据查找到的用户判断是否冻结...用User的另一个构造
        // 第二个参数是数据库里的暗文密码 ，同一个密码每次加密后的值是不一样的，
        // 第三个参数将逗号分隔的字符串转换为GrantedAuthority集合，做授权用

        String password = passwordEncoder.encode("123456");
        logger.info("数据库密码是：" + password);

        return new SocialUser(userId, password, true, true, true, true,
                AuthorityUtils.commaSeparatedStringToAuthorityList("ADMIN,ROLE_USER"));
    }

}
