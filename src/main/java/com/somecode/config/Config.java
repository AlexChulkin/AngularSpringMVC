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

@Profile("dev")
@Configuration
@EnableWebMvc
@ComponentScan(basePackages = {"com.somecode"})
@Import(PersistenceJPAConfig.class)
@EnableAspectJAutoProxy
public class Config extends WebMvcConfigurerAdapter {
    private final static String RESOURCE_HANDLER = "/resources/**";
    private final static String RESOURCE_LOCATIONS = "/resources/";
    private final static String MESSAGES_BASENAME = "classpath:messages";

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler(RESOURCE_HANDLER).addResourceLocations(RESOURCE_LOCATIONS);
    }

    @Bean
    public ReloadableResourceBundleMessageSource messageSource() {
        ReloadableResourceBundleMessageSource source = new ReloadableResourceBundleMessageSource();
        source.setBasename(MESSAGES_BASENAME);
        return source;
    }

    @Bean
    public SessionLocaleResolver sessionLocaleResolver() {
        SessionLocaleResolver resolver = new SessionLocaleResolver();
        resolver.setDefaultLocale(Locale.ENGLISH);
        return resolver;
    }

    @Bean
    public ViewResolver viewResolver(){
        UrlBasedViewResolver urlBasedViewResolver = new UrlBasedViewResolver();
        urlBasedViewResolver.setViewClass(JstlView.class);
        return urlBasedViewResolver;
    }

    @Bean
    public Utils helper() {
        return new Utils();
    }
}
