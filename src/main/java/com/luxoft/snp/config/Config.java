package com.luxoft.snp.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import java.util.Date;


@EnableWebMvc
@ComponentScan(basePackages = {"com.luxoft.snp"})
@Import(PersistenceJPAConfig.class)
@org.springframework.context.annotation.Configuration
public class Config extends WebMvcConfigurerAdapter {


    @Bean
    public Date dateTest(){
        System.out.println("date bean test");
        Date date = new Date();
        return date;
    }


}
