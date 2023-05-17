package com.safecode.security.user.interceptor;

import com.safecode.security.user.dao.AuditLogRepository;
import com.safecode.security.user.entity.AuditLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class AuditLogInterceptor extends HandlerInterceptorAdapter {

    @Autowired
    private AuditLogRepository auditLogRepository;

    // 在控制器的方法调用之前
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        AuditLog log = new AuditLog();
        log.setMethod(request.getMethod());
        log.setPath(request.getRequestURI());

        auditLogRepository.save(log);

        request.setAttribute("auditLogId", log.getId());
        return true;
    }

    // 在控制器的方法处理之后调用 如果控制器里的方法出现异常，则不会被调用
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
                           ModelAndView modelAndView) throws Exception {

    }


    // 不管是控制器的方法出异常，还是正常执行都会被调用
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
            throws Exception {
        Long auditLogId = (Long) request.getAttribute("auditLogId");
        AuditLog log = auditLogRepository.findById(auditLogId).get();
        log.setStatus(response.getStatus());
        auditLogRepository.save(log);
    }

}
