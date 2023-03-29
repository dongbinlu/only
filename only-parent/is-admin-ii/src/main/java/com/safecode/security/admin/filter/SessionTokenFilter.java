package com.safecode.security.admin.filter;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import com.safecode.security.admin.entity.TokenInfo;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class SessionTokenFilter extends ZuulFilter {

    @Autowired
    private ObjectMapper objectMapper;

    private RestTemplate restTemplate = new RestTemplate();

    @Override
    public Object run() throws ZuulException {

        RequestContext requestContext = RequestContext.getCurrentContext();
        HttpServletRequest request = requestContext.getRequest();

        TokenInfo tokenInfo = (TokenInfo) request.getSession().getAttribute("token");
        if (tokenInfo != null) {
            String tokenValue = tokenInfo.getAccess_token();
            log.info("token expireTime is {}", tokenInfo.getExpireTime());
            //如果令牌过期
            if (tokenInfo.isExpired()) {
                log.info("token 过期，token expireTime is {}", tokenInfo.getExpireTime());

                String oauthServiceUrl = "http://gateway.safecode.com:8040/auth/oauth/token";

                HttpHeaders headers = new HttpHeaders();
                headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
                headers.setBasicAuth("adminii", "123456");

                MultiValueMap<String, Object> params = new LinkedMultiValueMap<String, Object>();
                params.add("refresh_token", tokenInfo.getRefresh_token());
                params.add("grant_type", "refresh_token");

                HttpEntity<MultiValueMap<String, Object>> entity = new HttpEntity<>(params, headers);

                //try,catch的目的是防止refresh_token也过期，还有就是服务调用异常的处理，主要是防止refresh_token过期
                try {
                    ResponseEntity<TokenInfo> newTokenInfo = restTemplate.exchange(oauthServiceUrl, HttpMethod.POST, entity,
                            TokenInfo.class);

                    log.info("newToken info is {}", newTokenInfo.getBody().toString());
                    request.getSession().setAttribute("token", newTokenInfo.getBody().init());
                    tokenValue = newTokenInfo.getBody().getAccess_token();
                } catch (RestClientException e) {
                    //1,首先停止路由
                    requestContext.setSendZuulResponse(false);
                    //2,响应
                    requestContext.getResponse().setContentType("application/json;charset-UTF-8");
                    requestContext.setResponseStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
                    try {
                        requestContext.setResponseBody(objectMapper.writeValueAsString("refresh token fail"));
                    } catch (JsonProcessingException e1) {
                        throw new RuntimeException(e1);
                    }
                }
            }
            requestContext.addZuulRequestHeader("Authorization", "Bearer " + tokenValue);
        }
        return null;
    }

    @Override
    public boolean shouldFilter() {
        return true;
    }

    @Override
    public String filterType() {
        return "pre";
    }

    @Override
    public int filterOrder() {
        return 0;
    }

}
