package com.somecode.config;

import org.springframework.web.WebApplicationInitializer;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRegistration;

public class PacketAdminAppInitializer implements WebApplicationInitializer {
    @Override
    public void onStartup(ServletContext container) throws ServletException {
        AnnotationConfigWebApplicationContext appContext = new AnnotationConfigWebApplicationContext();
        appContext.setConfigLocation("com.somecode.config.Config");
        ServletRegistration.Dynamic dispatcher = container.addServlet("appServlet", new DispatcherServlet(appContext));
        dispatcher.addMapping("/");
    }
}
