package com.somecode.service;

import com.somecode.config.PersistenceJPAConfig;
import org.dbunit.DataSourceDatabaseTester;
import org.dbunit.util.fileloader.XlsDataFileLoader;
import org.springframework.context.annotation.*;

import static com.somecode.config.PersistenceJPAConfig.dataSource;

@Configuration
@ComponentScan(basePackages = {"com.somecode"})
@Import(PersistenceJPAConfig.class)
@Profile("test")
public class DaoTestConfig {

    @Bean(name = "databaseTester")
    public DataSourceDatabaseTester dataSourceDatabaseTester() {
        return new DataSourceDatabaseTester(dataSource());
    }

    @Bean(name = "xlsDataFileLoader")
    public XlsDataFileLoader xlsDataFileLoader() {
        return new XlsDataFileLoader();
    }
}
