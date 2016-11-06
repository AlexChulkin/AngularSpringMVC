
/*
 * Copyright (c) 2016.  Alex Chulkin
 */

package com.somecode.config;

import lombok.extern.log4j.Log4j;
import org.apache.commons.dbcp.BasicDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.dao.annotation.PersistenceExceptionTranslationPostProcessor;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;
import java.io.IOException;
import java.util.Properties;

import static com.somecode.utils.Utils.getMessage;

/**
 * The persistence jpa config class.
 * @version 1.0
 */

@Configuration
@EnableTransactionManagement
@EnableJpaRepositories("com.somecode")
@Log4j
public class PersistenceJPAConfig {
    /**
     * The hibernate username property
     */
    private final static String USERNAME_PROP = "username";
    /**
     * The hibernate password property
     */
    private final static String PASSWORD_PROP = "password";
    /** The hibernate URL property */
    private final static String URL_PROP = "url";
    /** The hibernate driver classname property */
    private final static String CLASSNAME_PROP = "className";
    /** The hibernate show sql property */
    private final static String SHOW_SQL_PROP = "hibernate.show_sql";
    /** The packages to scan value */
    private final static String PACKAGE_TO_SCAN = "com.somecode";
    /** The dev properties filename */
    private final static String DEV_PROPS_FILENAME = "db-dev.properties";
    /** The error in DB properties message */
    private final static String ERROR_IN_DB_PROPERTIES = "config.errorInDbProperties";
    /** The dev profile value */
    private final static String DEV_PROFILE = "dev";
    /** The generated DDL property for the vendor adapter */
    private final static boolean GENERATED_DDL = true;
    /** The datasource max active property */
    private final static int DATASOURCE_MAX_ACTIVE = 10;
    /** The datasource max wait property */
    private final static int DATASOURCE_MAX_WAIT = 100;
    /** The datasource initial size property */
    private final static int DATASOURCE_INITIAL_SIZE = 3;

    /** The DB properties */
    protected Properties dbProperties;

    /** The application context */
    @Autowired
    ApplicationContext context;

    /**
     * Method that sets the necessary config properties after the construction.
     */
    @PostConstruct
    protected void setProperties() {
        dbProperties = new Properties();
        try {
            dbProperties.load(PersistenceJPAConfig.class.getClassLoader().getResourceAsStream(DEV_PROPS_FILENAME));
        } catch (IOException e) {
            log.fatal(getMessage(ERROR_IN_DB_PROPERTIES, null), e);
        }
    }

    /**
     * Returns the EM factory bean.
     * @return the EM factory bean.
     */
    @Bean
    public LocalContainerEntityManagerFactoryBean entityManagerFactory() {
        LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();

        em.setDataSource(dataSource());
        em.setPackagesToScan(PACKAGE_TO_SCAN);
        HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
        vendorAdapter.setGenerateDdl(GENERATED_DDL);
        vendorAdapter.setShowSql(Boolean.parseBoolean(dbProperties.getProperty(SHOW_SQL_PROP)));

        em.setJpaVendorAdapter(vendorAdapter);
        em.setJpaProperties(dbProperties);

        return em;
    }

    /**
     * Returns the datasource bean.
     * @return the datasource bean.
     */
    @Profile(DEV_PROFILE)
    @Bean
    public DataSource dataSource() {
        BasicDataSource dataSource = new BasicDataSource();
        dataSource.setDriverClassName(dbProperties.getProperty(CLASSNAME_PROP));
        dataSource.setUrl(dbProperties.getProperty(URL_PROP));
        dataSource.setUsername(dbProperties.getProperty(USERNAME_PROP));
        dataSource.setPassword(dbProperties.getProperty(PASSWORD_PROP));
        dataSource.setMaxActive(DATASOURCE_MAX_ACTIVE);
        dataSource.setMaxWait(DATASOURCE_MAX_WAIT);
        dataSource.setInitialSize(DATASOURCE_INITIAL_SIZE);

        return dataSource;
    }

    /**
     * Returns the transaction manager mean.
     * @param emf the entity manager factory bean.
     * @return the transaction manager bean.
     */
    @Bean
    public PlatformTransactionManager transactionManager(EntityManagerFactory emf) {
        JpaTransactionManager transactionManager = new JpaTransactionManager();
        transactionManager.setEntityManagerFactory(emf);

        return transactionManager;
    }

    /**
     * Returns the persistence exception translation post processor bean.
     * @return the persistence exception translation post processor bean.
     */
    @Bean
    public PersistenceExceptionTranslationPostProcessor exceptionTranslation() {
        return new PersistenceExceptionTranslationPostProcessor();
    }
}
