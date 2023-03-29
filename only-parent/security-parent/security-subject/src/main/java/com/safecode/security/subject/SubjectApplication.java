package com.safecode.security.subject;

import java.security.Principal;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.social.connect.web.ProviderSignInUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.ServletWebRequest;

import com.safecode.security.core.properties.SecurityProperties;
import com.safecode.security.subject.vo.SysUserVo;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;

@SpringBootApplication
@MapperScan("com.safecode.security.subject.mapper")
@RestController
@ComponentScan("com.safecode.security")
public class SubjectApplication {

    @Autowired
    private ProviderSignInUtils providerSignInUtils;

    @Autowired
    private SecurityProperties securityProperties;

    public static void main(String[] args) {
        SpringApplication.run(SubjectApplication.class, args);
    }

    // 社交注册用户或绑定
    @PostMapping("/regist")
    public void regist(SysUserVo vo, HttpServletRequest request) {
        String username = vo.getUsername();
        providerSignInUtils.doPostSignUp(username, new ServletWebRequest(request));
        System.err.println("不管是注册用户还是绑定用户，都会拿到一个用户唯一标识");
    }

    @GetMapping("/hello")
    public String hello() {
        return "hello spring security !!!";

    }

    @GetMapping("/session")
    public Object getCurrentUser() {
        return SecurityContextHolder.getContext().getAuthentication();
    }

    @GetMapping("/auth")
    public Object getCurrentUser(Authentication authentication) {
        return authentication;
    }

    // 只返回principal
    @GetMapping("/userDetails")
    public Object getCurrentUser(@AuthenticationPrincipal UserDetails userDetails) {
        return userDetails;
    }

    // 只返回principal
    @GetMapping("/principal")
    public Principal getCurrentUser(Principal principal) {
        return principal;
    }

    @GetMapping("/me")
    public Object me() {
        return SecurityContextHolder.getContext().getAuthentication();
    }

    @GetMapping("/jwt")
    public Object jwt(HttpServletRequest request) throws Exception {
        // 自己解析token，解析自定义token的增强
        String header = request.getHeader("Authorization");
        String token = StringUtils.substringAfter(header, "Bearer");
        Claims claims = Jwts.parser().setSigningKey(securityProperties.getOauth2().getJwtSigningKey().getBytes("UTF-8"))
                .parseClaimsJws(token).getBody();
        String company = (String) claims.get("company");

        return company;
    }
}
