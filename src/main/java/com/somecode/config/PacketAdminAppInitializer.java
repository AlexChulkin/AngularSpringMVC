package com.somecode.config;

import org.springframework.web.WebApplicationInitializer;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRegistration;

public class PacketAdminAppInitializer implements WebApplicationInitializer {
    private final static String CONFIG_LOCATION = "com.somecode.config.Config";
    private final static String APP_SERVLET = "appServlet";
    @Override
    public void onStartup(ServletContext container) throws ServletException {
        AnnotationConfigWebApplicationContext appContext = new AnnotationConfigWebApplicationContext();
        appContext.setConfigLocation(CONFIG_LOCATION);
        ServletRegistration.Dynamic dispatcher = container.addServlet(APP_SERVLET, new DispatcherServlet(appContext));
        dispatcher.addMapping("/");
    }
}
