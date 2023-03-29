package com.safecode.security.subject.filter;

import java.io.IOException;
import java.util.Date;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 过滤器添加到spring容器中，直接添加@Component注解就可以
 * <p>
 * 假如有个第三方的Filter，第三方的Filter没有此@Component注解，怎样将第三方的Filter加入到Spring 容器中呢？
 * 解决方式：采用java配置的方式 假设TimeFilter 是第三方的Filter 而且没有@Component注解 见WebConfig.java
 * 唯一不同的是 采用java配置的方式可以指定那些url需要执行该过滤器
 *
 * @author v_boy RESTful API拦截 需求 记录所有rest服务的时间 过滤器，拦截器，切片
 * 1，过滤器只能拿到http的请求和响应以及一些参数，它并不知道是哪个控制器哪个方法 ，因为过滤器仅仅是javaEE的规范
 * 2，如果想知道具体的信息，如哪些控制器，哪些方法，要用拦截器Interceptor ，因为拦截器是Spring框架所拥有的
 * 3，拦截器也能拿到http的请求和响应以及一些参数但是拦截器Interceptor无法拿到具体调用的时候传的值 ，想要拿到传的什么参数
 * 需要切片来完成 ，但是切片无法拿到具体的http请求和响应，Spring AOP
 * <p>
 * 执行顺序： Filter->Interceptor->Aspect->到达Controller
 */
// @Component("timeFilter")
public class TimeFilter implements Filter {

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        logger.info("TimeFilter init 初始化");

    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        logger.info("TimeFilter doFilter 开始");
        long start = new Date().getTime();
        chain.doFilter(request, response);
        logger.info("TimeFilter doFilter 执行时长：" + (new Date().getTime() - start));
        logger.info("TimeFilter 执行完成");
    }

    @Override
    public void destroy() {
        logger.info("TimeFilter destroy 销毁");

    }

}
