package com.safecode.security.permission.config;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.safecode.security.permission.common.HttpInterceptor;
import com.safecode.security.permission.filter.AclControlFilter;
import com.safecode.security.permission.filter.LoginFilter;

import redis.clients.jedis.ShardedJedisPool;

@Configuration
public class WebConfig extends WebMvcConfigurerAdapter {

    @Autowired
    private HttpInterceptor httpInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(httpInterceptor);
    }

    @Bean("loginFilter")
    public FilterRegistrationBean loginFilter() {
        FilterRegistrationBean filterRegistrationBean = new FilterRegistrationBean();
        LoginFilter loginFilter = new LoginFilter();
        filterRegistrationBean.setFilter(loginFilter);

        // 需要拦截的url
        List<String> urls = Lists.newArrayList();
        urls.add("/sys/*");
        urls.add("/admin/*");
        urls.add("/isLogin");
        filterRegistrationBean.setUrlPatterns(urls);
        filterRegistrationBean.setOrder(1);
        return filterRegistrationBean;
    }

    @Bean("aclControlFilter")
    public FilterRegistrationBean aclControlFilter() {
        FilterRegistrationBean filterRegistrationBean = new FilterRegistrationBean();
        AclControlFilter aclControlFilter = new AclControlFilter();
        filterRegistrationBean.setFilter(aclControlFilter);
        // 需要拦截的url
        List<String> urls = Lists.newArrayList();
        urls.add("/sys/*");
        urls.add("/admin/*");
        filterRegistrationBean.setUrlPatterns(urls);
        filterRegistrationBean.setOrder(2);

        // 初始化数据
        Map<String, String> map = Maps.newHashMap();
        map.put("exclusionUrls", "/isLogin");
        filterRegistrationBean.setInitParameters(map);
        return filterRegistrationBean;
    }

}
