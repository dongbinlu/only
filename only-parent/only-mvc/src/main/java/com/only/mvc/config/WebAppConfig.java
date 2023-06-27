package com.only.mvc.config;


import com.only.mvc.interceptor.OnlyInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * IOC子容器，web子容器
 */
@Configuration
@ComponentScan(basePackages = "com.only.mvc.controller.annotation", includeFilters = {
        @ComponentScan.Filter(type = FilterType.ANNOTATION, value = {RestController.class, Controller.class})
},useDefaultFilters = false)
@EnableWebMvc
public class WebAppConfig implements WebMvcConfigurer {

    /**
     * 配置拦截器
     */
    @Bean
    public OnlyInterceptor onlyInterceptor(){
        return new OnlyInterceptor();
    }

    /**
     * 配置文件上传下载组件
     * @return
     */
/*    @Bean
    public MultipartResolver multipartResolver(){
        CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver();
        multipartResolver.setDefaultEncoding("UTF-8");
        multipartResolver.setMaxUploadSize(1024*1024*10);
        return multipartResolver;
    }*/

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(onlyInterceptor())
                .addPathPatterns("/*");
    }

    /**
     * 配置试图解析器
     * @return
     */
//    @Bean
//    public InternalResourceViewResolver internalResourceViewResolver(){
//        InternalResourceViewResolver viewResolver = new InternalResourceViewResolver();
//        viewResolver.setSuffix(".jsp");
//        viewResolver.setPrefix("/WEB-INF/jsp/");
//        return viewResolver;
//    }

    /**
     * 配置消息转换器
     * @param converters
     */
//    @Override
//    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
//        converters.add(new MappingJackson2HttpMessageConverter());
//    }
}
