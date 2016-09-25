package com.somecode.config;

import com.somecode.helper.Helper;
import org.springframework.context.annotation.*;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.view.JstlView;
import org.springframework.web.servlet.view.UrlBasedViewResolver;

@Profile("dev")
@Configuration
@EnableWebMvc
@ComponentScan(basePackages = {"com.somecode"})
@Import(PersistenceJPAConfig.class)
@EnableAspectJAutoProxy
public class Config extends WebMvcConfigurerAdapter {
    private final static String RESOURCE_HANDLER = "/resources/**";
    private final static String RESOURCE_LOCATIONS = "/resources/";
    private final static String MESSAGES_LOCATION = "properties/messages";

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler(RESOURCE_HANDLER).addResourceLocations(RESOURCE_LOCATIONS);
    }

    @Bean
    public ResourceBundleMessageSource messageSource() {
        ResourceBundleMessageSource source = new ResourceBundleMessageSource();
        source.setBasenames(MESSAGES_LOCATION);
        source.setUseCodeAsDefaultMessage(true);
        return source;
    }

    @Bean
    public ViewResolver viewResolver(){
        UrlBasedViewResolver urlBasedViewResolver = new UrlBasedViewResolver();
        urlBasedViewResolver.setViewClass(JstlView.class);
        return urlBasedViewResolver;
    }

    @Bean
    public Helper helper() {
        return new Helper();
    }
}
