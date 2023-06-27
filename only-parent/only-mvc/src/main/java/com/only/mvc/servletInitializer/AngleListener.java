package com.only.mvc.servletInitializer;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

public class AngleListener implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        System.out.println("angle listener contextinitialized");
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        System.out.println("angle listener contextDestroyed");
    }
}
