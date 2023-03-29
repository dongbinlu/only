package com.safecode.security.admin.controller;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.safecode.security.admin.entity.TokenInfo;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping
@Slf4j
public class UserController {

    private RestTemplate restTemplate = new RestTemplate();

    @GetMapping("/oauth/callback")
    public void callback(@RequestParam("code") String code, String state, HttpServletRequest request,
                         HttpServletResponse response) throws Exception {

        log.info("code is {} , state is :{}", code, state);

        String oauthServiceUrl = "http://gateway.safecode.com:8040/auth/oauth/token";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        headers.setBasicAuth("adminii", "123456");

        MultiValueMap<String, Object> params = new LinkedMultiValueMap<String, Object>();
        params.add("code", code);
        params.add("grant_type", "authorization_code");
        // redirect_uri要和之前发送的uri一直
        params.add("redirect_uri", "http://adminii.safecode.com:9010/oauth/callback");

        HttpEntity<MultiValueMap<String, Object>> entity = new HttpEntity<>(params, headers);

        ResponseEntity<TokenInfo> token = restTemplate.exchange(oauthServiceUrl, HttpMethod.POST, entity,
                TokenInfo.class);

        log.info("token info is {}", token.getBody().toString());
        // 1,基于Session的SSO
        request.getSession().setAttribute("token", token.getBody().init());

        // 2,基于token的SSO
/*		
		Cookie accessTokenCookie = new Cookie("safecode_access_token", token.getBody().getAccess_token());
		accessTokenCookie.setMaxAge(token.getBody().getExpires_in().intValue() - 3);
		accessTokenCookie.setDomain("safecode.com");
		accessTokenCookie.setPath("/");
		response.addCookie(accessTokenCookie);

		Cookie refreshTokenCookie = new Cookie("safecode_refresh_token", token.getBody().getRefresh_token());
		refreshTokenCookie.setMaxAge(2592000);
		refreshTokenCookie.setDomain("safecode.com");
		refreshTokenCookie.setPath("/");
		response.addCookie(refreshTokenCookie);
*/
        // 可以根据state跳转
        response.sendRedirect("/");

    }

    @GetMapping("/logout")
    public void logout(HttpServletRequest request, HttpServletResponse response) {
        // 1,基于session的SSO
        request.getSession().invalidate();

        // 2,基于token的SSO
/*		
		Cookie accessTokenCookie = new Cookie("safecode_access_token", null);
		accessTokenCookie.setMaxAge(0);
		accessTokenCookie.setPath("/");
		response.addCookie(accessTokenCookie);
		
		Cookie refreshTokenCookie = new Cookie("safecode_refresh_token", null);
		refreshTokenCookie.setMaxAge(0);
		refreshTokenCookie.setPath("/");
		response.addCookie(refreshTokenCookie);
*/
    }

    @GetMapping("/me")
    public void isLogin(HttpServletRequest request) {

        TokenInfo tokenInfo = (TokenInfo) request.getSession().getAttribute("token");
        log.info("token info is {}", tokenInfo.toString());

    }

}
