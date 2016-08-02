package com.somecode.service;

import com.somecode.config.PersistenceJPAConfig;
import org.apache.log4j.Logger;
import org.dbunit.DataSourceDatabaseTester;
import org.dbunit.util.fileloader.XlsDataFileLoader;
import org.springframework.context.annotation.*;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;

@Profile("test")
@Configuration
@ComponentScan(basePackages = {"com.somecode"})
@Import(PersistenceJPAConfig.class)
@PropertySource(value = "classpath:db.properties")
public class DaoTestConfig extends PersistenceJPAConfig {

    @Override
    @PostConstruct
    protected void setProperties() {
        super.setProperties();
    }

    @Override
    protected void setLOGGER() {
        LOGGER = Logger.getLogger(DaoTestConfig.class);
    }

    @Override
    @Bean
    public DataSource dataSource() {
        return new EmbeddedDatabaseBuilder()
                .setType(EmbeddedDatabaseType.H2)
                .addScript("classpath:META-INF/config/schema.sql")
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
