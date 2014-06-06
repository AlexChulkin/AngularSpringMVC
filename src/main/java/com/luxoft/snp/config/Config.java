package com.luxoft.snp.config;

import org.apache.commons.dbcp.BasicDataSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import javax.sql.DataSource;
import java.util.Date;


@EnableWebMvc
@ComponentScan(basePackages = {"com.luxoft.snp"})
@Import(DbConfig.class)
@org.springframework.context.annotation.Configuration
public class Config extends WebMvcConfigurerAdapter {


    @Bean
    public Date dateTest(){
        System.out.println("date bean test");
        Date date = new Date();
        return date;
    }
    @Bean
    public DataSource dataSource() {
        System.out.println("dataSource");
        BasicDataSource basicDataSource = new BasicDataSource();
        basicDataSource.setDriverClassName("com.mysql.jdbc.Driver");
        basicDataSource.setUrl("jdbc:mysql://127.0.0.1:3306/registrationtest");
        basicDataSource.setUsername("root");
        basicDataSource.setPassword("root");
        basicDataSource.setMaxActive(10);
        basicDataSource.setMaxWait(100);
        basicDataSource.setMaxIdle(5);

        return  basicDataSource;
    }

    @Bean
    public LocalContainerEntityManagerFactoryBean entityManagerFactory(){

        LocalContainerEntityManagerFactoryBean entityManagerFactoryBean = new LocalContainerEntityManagerFactoryBean();
        entityManagerFactoryBean.setDataSource(dataSource());

        HibernateJpaVendorAdapter jpaVendorAdapter = new HibernateJpaVendorAdapter();
        jpaVendorAdapter.setShowSql(true);
        jpaVendorAdapter.setGenerateDdl(false);

        entityManagerFactoryBean.setJpaVendorAdapter(jpaVendorAdapter);

        return entityManagerFactoryBean;
    }


}
