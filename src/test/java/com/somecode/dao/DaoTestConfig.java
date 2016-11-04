/*
 * Copyright (c) 2016.  Alex Chulkin
 */

package com.somecode.dao;

import com.somecode.config.PersistenceJPAConfig;
import lombok.extern.log4j.Log4j;
import org.dbunit.DataSourceDatabaseTester;
import org.dbunit.util.fileloader.XlsDataFileLoader;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;
import java.io.IOException;
import java.util.Properties;

/**
 * The DAO test config that extends the {@link com.somecode.config.PersistenceJPAConfig}.
 */
@Profile("test")
@Configuration
@ComponentScan(basePackages = {"com.somecode"})
@Log4j
public class DaoTestConfig extends PersistenceJPAConfig {
    /**
     * The messages basename for the reloadable message source
     */
    private final static String MESSAGES_BASENAME = "classpath:messages";

    /**
     * The post-construct method that sets the test database properties.
     */
    @Override
    @PostConstruct
    protected void setProperties() {
        dbProperties = new Properties();
        try {
            dbProperties.load(PersistenceJPAConfig.class.getClassLoader().getResourceAsStream("db-test.properties"));
        } catch (IOException e) {
            log.fatal("Error in properties", e);
        }
    }

    /**
     * Returns the datasource bean.
     *
     * @return datasource bean.
     */
    @Override
    @Bean
    public DataSource dataSource() {
        return new EmbeddedDatabaseBuilder()
                .setType(EmbeddedDatabaseType.H2)
                .addScript("classpath:META-INF/config/test/test-schema.sql")
                .build();
    }

    /**
     * Returns the message source bean.
     *
     * @return message source bean.
     */
    @Bean
    public ReloadableResourceBundleMessageSource messageSource() {
        ReloadableResourceBundleMessageSource source = new ReloadableResourceBundleMessageSource();
        source.setBasename(MESSAGES_BASENAME);
        return source;
    }

    /**
     * Returns the datasource database tester bean.
     * @return database tester bean.
     */
    @Bean(name = "databaseTester")
    public DataSourceDatabaseTester dataSourceDatabaseTester() {
        return new DataSourceDatabaseTester(dataSource());
    }

    /**
     * Returns the xls file loader bean.
     * @return xls file loader bean.
     */
    @Bean(name = "xlsDataFileLoader")
    public XlsDataFileLoader xlsDataFileLoader() {
        return new XlsDataFileLoader();
    }
}
