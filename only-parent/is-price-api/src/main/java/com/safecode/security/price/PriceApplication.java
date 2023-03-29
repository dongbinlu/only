package com.safecode.security.price;

import java.security.Principal;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.provider.authentication.OAuth2AuthenticationDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.safecode.security.price.auth.AuthHolder;
import com.safecode.security.price.auth.UserInfo;

import lombok.extern.slf4j.Slf4j;

@SpringBootApplication
@RestController
@EnableResourceServer
@Slf4j
public class PriceApplication {

    public static void main(String[] args) {
        SpringApplication.run(PriceApplication.class, args);
    }

    @GetMapping("/hello")
    public String hello() {
        return "hello";
    }

    @GetMapping("/test")
    public Object test() throws Exception {

        UserInfo userInfo = AuthHolder.getUserInfo();

        return userInfo;
    }

    @GetMapping("/me")
    public Map<String, Object> me(Authentication authentication, @AuthenticationPrincipal UserDetails userDetails,
                                  Principal principal, @AuthenticationPrincipal String username) {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        OAuth2AuthenticationDetails oAuth2AuthenticationDetails = (OAuth2AuthenticationDetails) auth.getDetails();
        String tokenValue = oAuth2AuthenticationDetails.getTokenValue();

        log.info("authentication: "
                + ReflectionToStringBuilder.toString(authentication, ToStringStyle.MULTI_LINE_STYLE));

        // userDetails为空
        // log.info("userDetails: " +
        // ReflectionToStringBuilder.toString(userDetails,
        // ToStringStyle.MULTI_LINE_STYLE));

        log.info("principal: " + ReflectionToStringBuilder.toString(principal, ToStringStyle.MULTI_LINE_STYLE));

        log.info("username: " + ReflectionToStringBuilder.toString(username, ToStringStyle.MULTI_LINE_STYLE));

        log.info("auth: " + ReflectionToStringBuilder.toString(auth, ToStringStyle.MULTI_LINE_STYLE));

        Map<String, Object> maps = new HashMap<>();
        maps.put("authentication", authentication);
        maps.put("userDetails", userDetails);
        maps.put("principal", principal);
        maps.put("username", username);
        maps.put("auth", auth);

        return maps;
    }

}
