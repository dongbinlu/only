package com.only.mvc.config;

/**
 * 通过SPI机制加载classpath下的ServletContainerInitializer
 */
//public class OnlyStarterInitializer extends AbstractAnnotationConfigDispatcherServletInitializer {
//
//    /**
//     * 父容器的启动类
//     * @return
//     */
//    @Override
//    protected Class<?>[] getRootConfigClasses() {
//        return new Class[]{RootConfig.class};
//    }
//
//    /**
//     * IOC子容器配置，web容器配置
//     * @return
//     */
//    @Override
//    protected Class<?>[] getServletConfigClasses() {
//        return new Class[]{WebAppConfig.class};
//    }
//
//    /**
//     * 前端控制器的拦截路径
//     * @return
//     */
//    @Override
//    protected String[] getServletMappings() {
//        return new String[]{"/*"};
//    }
//}
