package com.safecode.security.admin.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.safecode.security.admin.entity.Credentials;
import com.safecode.security.admin.entity.TokenInfo;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/users")
@Slf4j
public class UserController {

    private RestTemplate restTemplate = new RestTemplate();

    @GetMapping("/login")
    public void login(Credentials credentials, HttpServletRequest request) {

        String oauthServiceUrl = "http://gateway.safecode.com:8040/auth/oauth/token";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        headers.setBasicAuth("admin", "123456");

        MultiValueMap<String, Object> params = new LinkedMultiValueMap<String, Object>();
        params.add("username", credentials.getUsername());
        params.add("password", credentials.getPassword());
        params.add("grant_type", "password");
        params.add("scope", "read write");

        HttpEntity<MultiValueMap<String, Object>> entity = new HttpEntity<>(params, headers);

        ResponseEntity<TokenInfo> response = restTemplate.exchange(oauthServiceUrl, HttpMethod.POST, entity,
                TokenInfo.class);

        log.info("token info is {}", response.getBody().toString());

        request.getSession().setAttribute("token", response.getBody());

    }

    @GetMapping("/logout")
    public void logout(HttpServletRequest request) {

        request.getSession().invalidate();

    }

    @GetMapping("/isLogin")
    public void isLogin(HttpServletRequest request) {

        TokenInfo tokenInfo = (TokenInfo) request.getSession().getAttribute("token");
        log.info("token info is {}", tokenInfo.toString());

    }

}
