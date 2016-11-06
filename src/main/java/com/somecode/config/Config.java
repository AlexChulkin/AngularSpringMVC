
/*
 * Copyright (c) 2016.  Alex Chulkin
 */

package com.somecode.config;

import com.somecode.utils.Utils;
import org.springframework.context.annotation.*;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;
import org.springframework.web.servlet.view.JstlView;
import org.springframework.web.servlet.view.UrlBasedViewResolver;

import java.util.Locale;

/**
 * The main config class.
 * @version 1.0
 */
@Profile("dev")
@Configuration
@EnableWebMvc
@ComponentScan(basePackages = {"com.somecode"})
@Import(PersistenceJPAConfig.class)
@EnableAspectJAutoProxy
public class Config extends WebMvcConfigurerAdapter {
    /**
     * The resources handler mapping value
     */
    private final static String RESOURCE_HANDLER = "/resources/**";
    /**
     * The resources locations mapping value
     */
    private final static String RESOURCE_LOCATIONS = "/resources/";
    /** The messages source filename value */
    private final static String MESSAGES_BASENAME = "classpath:messages";

    /**
     * Adds the resources handler to the config.
     *
     * @param registry the {@link org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry} instance.
     */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler(RESOURCE_HANDLER).addResourceLocations(RESOURCE_LOCATIONS);
    }

    /**
     * Returns the message source bean.
     *
     * @return the message source bean with the messages source set.
     */
    @Bean
    public ReloadableResourceBundleMessageSource messageSource() {
        ReloadableResourceBundleMessageSource source = new ReloadableResourceBundleMessageSource();
        source.setBasename(MESSAGES_BASENAME);
        return source;
    }

    /**
     * Returns the session locale resolver bean with the english default locale set.
     *
     * @return the session locale resolver bean.
     */
    @Bean
    public SessionLocaleResolver sessionLocaleResolver() {
        SessionLocaleResolver resolver = new SessionLocaleResolver();
        resolver.setDefaultLocale(Locale.ENGLISH);
        return resolver;
    }

    /**
     * Returns the view resolver bean with set JSTL view.
     *
     * @return the view resolver class instance.
     */
    @Bean
    public ViewResolver viewResolver(){
        UrlBasedViewResolver urlBasedViewResolver = new UrlBasedViewResolver();
        urlBasedViewResolver.setViewClass(JstlView.class);
        return urlBasedViewResolver;
    }

    /**
     * Returns the utils bean.
     *
     * @return the utils class instance.
     */
    @Bean
    public Utils utils() {
        return new Utils();
    }
}
