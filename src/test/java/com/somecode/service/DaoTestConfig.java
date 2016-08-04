package com.somecode.service;

import com.somecode.config.PersistenceJPAConfig;
import org.apache.log4j.Logger;
import org.dbunit.DataSourceDatabaseTester;
import org.dbunit.util.fileloader.XlsDataFileLoader;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;
import java.io.IOException;
import java.util.Properties;

@Profile("test")
@Configuration
@ComponentScan(basePackages = {"com.somecode"})
public class DaoTestConfig extends PersistenceJPAConfig {

    @Override
    @PostConstruct
    protected void setPropertiesAndLogger() {
        super.setPropertiesAndLogger();
    }

    @Override
    protected void setProperties() {
        properties = new Properties();
        try {
            properties.load(PersistenceJPAConfig.class.getClassLoader().getResourceAsStream("db_test.properties"));
        } catch (IOException e) {
            LOGGER.error("Error in properties", e);
        }
    }

    @Override
    protected void setLogger() {
        LOGGER = Logger.getLogger(DaoTestConfig.class);
    }

    @Override
    @Bean
    public DataSource dataSource() {
        return new EmbeddedDatabaseBuilder()
                .setType(EmbeddedDatabaseType.H2)
                .addScript("classpath:META-INF/config/test/test-schema.sql")
                .build();
    }

    @Bean(name = "databaseTester")
    public DataSourceDatabaseTester dataSourceDatabaseTester() {
        return new DataSourceDatabaseTester(dataSource());
    }

    @Bean(name = "xlsDataFileLoader")
    public XlsDataFileLoader xlsDataFileLoader() {
        return new XlsDataFileLoader();
    }
}
