package com.safecode.security.gateway.filter;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import com.safecode.security.gateway.entity.TokenInfo;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class OAuth2Filter extends ZuulFilter {

    private RestTemplate restTemplate = new RestTemplate();

    /*
     * 真正需要些的业务逻辑
     */
    public Object run() throws ZuulException {

        log.info("OAuth2 start");

        RequestContext requestContext = RequestContext.getCurrentContext();
        HttpServletRequest request = requestContext.getRequest();

        // 判断当前的请求是否是以/auth开头的，如果是，直接放行，去往认证服务的接口放行
        if (StringUtils.startsWith(request.getRequestURI(), "/auth")) {
            return null;
        }

        String authHeader = request.getHeader("Authorization");

        // 如果请求头获取为空，也需要放行
        if (StringUtils.isBlank(authHeader)) {
            return null;
        }

        // 判断请求头是否是以Bearer 开头的，因为Authorization会有多个，比如：Basic
        // 如果不是以Bearer开头，比如说以Basic开头，也放行
        if (!StringUtils.startsWithIgnoreCase(authHeader, "Bearer ")) {
            return null;
        }

        try {
            // 如果获取token信息异常，也需要放行，往下走

            TokenInfo info = getTokenInfo(authHeader);
            request.setAttribute("tokenInfo", info);

        } catch (Exception e) {
            log.error("get token info fail ", e);
        }

        return null;
    }

    private TokenInfo getTokenInfo(String authHeader) {

        String token = StringUtils.substringAfter(authHeader, "Bearer ");
        String oauthServiceUrl = "http://auth.safecode.com:8030/oauth/check_token";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        headers.setBasicAuth("gateway", "123456");

        MultiValueMap<String, Object> params = new LinkedMultiValueMap<String, Object>();
        params.add("token", token);

        HttpEntity<MultiValueMap<String, Object>> entity = new HttpEntity<>(params, headers);

        ResponseEntity<TokenInfo> response = restTemplate.exchange(oauthServiceUrl, HttpMethod.POST, entity,
                TokenInfo.class);

        log.info("token info is {}", response.getBody().toString());

        return response.getBody();
    }

    /*
     * 需要写一段逻辑，管理这个Filter是不是起作用，改为true，永远起作用
     */
    public boolean shouldFilter() {
        return true;
    }

    /*
     * 过滤器的执行顺序
     */
    @Override
    public int filterOrder() {
        return 1;
    }

    /*
     * 过滤器类型有四种
     * pre,post,error,route
     * pre:在业务逻辑执行之前，执行run里面的东西
     * post:在业务逻辑执行之后，会执行run里面的逻辑
     * error:在业务逻辑抛出异常之后，执行run里面的逻辑
     * route:用来控制路由的，一般不会用到，zuul本身做了这件事
     */
    @Override
    public String filterType() {
        return "pre";
    }

}
