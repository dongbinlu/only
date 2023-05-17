package com.safecode.security.user.filter;

import com.lambdaworks.crypto.SCryptUtil;
import com.safecode.security.user.dao.UserRepository;
import com.safecode.security.user.entity.User;
import com.safecode.security.user.entity.UserInfo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.util.Base64Utils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
@Order(2)
public class BasicAuthencationFilter extends OncePerRequestFilter {

    @Autowired
    private UserRepository userRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String authHeader = request.getHeader("Authorization");

        if (StringUtils.isNotBlank(authHeader)) {

            String token64 = StringUtils.substringAfter(authHeader, "Basic ");
            String token = new String(Base64Utils.decodeFromString(token64));
            String[] items = StringUtils.splitByWholeSeparatorPreserveAllTokens(token, ":");

            String username = items[0];
            String password = items[1];

            User user = userRepository.findByUsername(username);

            if (user != null && SCryptUtil.check(password, user.getPassword())) {
                UserInfo info = user.buildInfo();
                request.getSession().setAttribute("user", info);
                request.getSession().setAttribute("temp", "yes");

            }

        }
        try {
            doFilter(request, response, filterChain);
        } finally {
            //Filter响应回去的时候调用  没有Interceptor直观
            if (request.getSession().getAttribute("temp") != null) {
                request.getSession().invalidate();
            }
        }
    }

}
