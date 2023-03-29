package com.safecode.security.permission.filter;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpStatus;

import com.google.common.base.Splitter;
import com.google.common.collect.Sets;
import com.safecode.security.permission.common.ApplicationContextHelper;
import com.safecode.security.permission.common.JsonData;
import com.safecode.security.permission.common.RequestHolder;
import com.safecode.security.permission.entity.SysUser;
import com.safecode.security.permission.service.impl.SysCoreService;
import com.safecode.security.permission.utils.JsonMapper;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class AclControlFilter implements Filter {

    private static Set<String> exclusionUrlSet = Sets.newConcurrentHashSet();

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

        String exclusionUrls = filterConfig.getInitParameter("exclusionUrls");
        List<String> exclusionUrlList = Splitter.on(",").trimResults().omitEmptyStrings().splitToList(exclusionUrls);
        exclusionUrlSet = Sets.newConcurrentHashSet(exclusionUrlList);
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;

        String servletPath = request.getServletPath();
        Map<String, String[]> requestMap = request.getParameterMap();

        // 白名单释放
        if (exclusionUrlSet.contains(servletPath)) {
            chain.doFilter(servletRequest, servletResponse);
            return;
        }

        SysUser sysUser = RequestHolder.getCurrentUser();
        if (sysUser == null) {
            log.info("someone visit {}, but no login , parameter:{}", servletPath, JsonMapper.obj2String(requestMap));
            noAuthorize(request, response);
            return;
        }
        SysCoreService sysCoreService = ApplicationContextHelper.popBean(SysCoreService.class);
        // 判断是否有访问权限
        if (!sysCoreService.hasUrlAcl(servletPath)) {
            log.info("{} visit {} , but no authorize , parameter:{}", JsonMapper.obj2String(sysUser), servletPath,
                    JsonMapper.obj2String(requestMap));
            noAuthorize(request, response);
            return;
        }
        chain.doFilter(servletRequest, servletResponse);
        return;
    }

    @Override
    public void destroy() {
    }

    private void noAuthorize(HttpServletRequest request, HttpServletResponse response) throws IOException {
        JsonData jsonData = JsonData.fail("您没有访问权限，如需访问，请联系管理员");
        response.setStatus(HttpStatus.FORBIDDEN.value());
        response.setContentType("application/json;charset=utf-8");
        response.getWriter().print(JsonMapper.obj2String(jsonData));
    }

}
