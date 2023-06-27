package com.only.mvc.plus.mvc;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import freemarker.template.TemplateExceptionHandler;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.core.LocalVariableTableParameterNameDiscoverer;
import org.springframework.core.ParameterNameDiscoverer;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class HandlerServlet extends HttpServlet {


    private WebApplicationContext webApplicationContext;
    private MvcBeanFactory mvcBeanFactory;
    // 参数名称处理器
    final ParameterNameDiscoverer parameterNameUtil = new LocalVariableTableParameterNameDiscoverer();
    // 配置freemarke
    private Configuration configuration;

    @Override
    public void init() throws ServletException {
        webApplicationContext = WebApplicationContextUtils.getWebApplicationContext(getServletContext());
        mvcBeanFactory = new MvcBeanFactory(webApplicationContext);
        Configuration configuration = null;

        try {
            configuration = webApplicationContext.getBean(Configuration.class);
        } catch (NoSuchBeanDefinitionException e) {

        }
        if (configuration == null) {
            configuration = new Configuration(Configuration.VERSION_2_3_23);
            configuration.setDefaultEncoding("UTF-8");
            configuration.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);
            try {
                configuration.setDirectoryForTemplateLoading(new File(getServletContext().getRealPath("/ftl/")));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        this.configuration = configuration;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doHandler(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doHandler(req, resp);
    }

    private void doHandler(HttpServletRequest req, HttpServletResponse resp) {

        String uri = req.getServletPath();
        if (uri.equals("/favicon.ico")) {
            return;
        }
        MvcBeanFactory.MvcBean mvcBean = mvcBeanFactory.getMvcBean(uri);

        if (mvcBean == null) {
            throw new IllegalArgumentException(String.format("not found %s mapping", uri));
        }

        Object[] args = buildParams(mvcBean, req, resp);
        try {
            Object result = mvcBean.run(args);
            processResult(result, resp);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void processResult(Object result, HttpServletResponse resp) throws IOException, TemplateException {

        if (result instanceof FreemarkerView) {
            FreemarkerView view = (FreemarkerView) result;
            Template template = configuration.getTemplate(view.getFtlPath());
            ByteArrayOutputStream output = new ByteArrayOutputStream();
            resp.setContentType("text/html;charset=utf-8");
            resp.setCharacterEncoding("utf-8");
            resp.setStatus(200);
            template.process(view.getModels(), resp.getWriter());
        }

        // TODO JSTL VIEW

        // TODO JSON VIEW

    }

    private Object[] buildParams(MvcBeanFactory.MvcBean mvcBean, HttpServletRequest req, HttpServletResponse resp) {

        Method method = mvcBean.getTargetMethod();
        List<String> paramNames = Arrays.asList(parameterNameUtil.getParameterNames(method));
        // 反射
        Class<?>[] paramTypes = method.getParameterTypes();

        // a.b.c
        Object[] args = new Object[paramTypes.length];

        for (int i = 0; i < paramNames.size(); i++) {
            if (paramTypes[i].isAssignableFrom(HttpServletRequest.class)) {
                args[i] = req;
            } else if (paramTypes[i].isAssignableFrom(HttpServletResponse.class)) {
                args[i] = resp;
            } else {
                if (req.getParameter(paramNames.get(i)) == null) {
                    args[i] = null;
                } else {
                    args[i] = convert(req.getParameter(paramNames.get(i)), paramTypes[i]);
                }
            }
        }
        return args;
    }

    private <T> T convert(String parameter, Class<T> paramType) {

        Object result = null;

        if (parameter == null) {
            return null;
        } else if (Integer.class.equals(paramType)) {
            result = Integer.parseInt(parameter);
        } else if (Long.class.equals(paramType)) {
            result = Long.parseLong(parameter);
        } else if (Date.class.equals(paramType)) {
            try {
                result = new SimpleDateFormat("").parse(parameter);
            } catch (ParseException e) {
                throw new IllegalArgumentException("date format Illegal must be 'yyyy-MM-dd HH:mm:ss'");
            }
        } else if (String.class.equals(paramType)) {
            result = parameter;
        }
        // TODO Serializable 进行自动封装
        else {
            throw new RuntimeException(String.format("not suport param type %s", paramType.getName()));
        }
        return (T) result;

    }
}
