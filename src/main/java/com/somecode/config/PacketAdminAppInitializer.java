/*
 * Copyright (c) 2016.  Alex Chulkin
 */

package com.somecode.config;

import org.springframework.web.WebApplicationInitializer;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRegistration;

/**
 * The Servlet 3 Code-Based Configuration initializer.
 */
public class PacketAdminAppInitializer implements WebApplicationInitializer {
    /**
     * The config location
     */
    private final static String CONFIG_LOCATION = "com.somecode.config.Config";
    /**
     * The application servlet value.
     */
    private final static String APP_SERVLET = "appServlet";
    /**
     * The default mapping value.
     */
    private final static String DEFAULT_MAPPING = "/";
    @Override
    public void onStartup(ServletContext container) throws ServletException {
        AnnotationConfigWebApplicationContext appContext = new AnnotationConfigWebApplicationContext();
        appContext.setConfigLocation(CONFIG_LOCATION);
        ServletRegistration.Dynamic dispatcher = container.addServlet(APP_SERVLET, new DispatcherServlet(appContext));
        dispatcher.addMapping(DEFAULT_MAPPING);
    }
}
