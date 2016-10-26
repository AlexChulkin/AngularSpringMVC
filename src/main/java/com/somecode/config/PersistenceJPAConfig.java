package com.somecode.config;

import org.apache.commons.dbcp.BasicDataSource;
import org.apache.log4j.Logger;
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


@Configuration
@EnableTransactionManagement
@EnableJpaRepositories("com.somecode")
public class PersistenceJPAConfig {

    private final static String USERNAME_PROP = "username";
    private final static String PASSWORD_PROP = "password";
    private final static String URL_PROP = "url";
    private final static String CLASSNAME_PROP = "className";
    private final static String SHOW_SQL_PROP = "hibernate.show_sql";
    private final static String PACKAGE_TO_SCAN = "com.somecode";
    private final static String DEV_PROPS_FILENAME = "db-dev.properties";
    private final static String ERROR_IN_DB_PROPERTIES = "config.errorInDbProperties";
    private final static String DEV_PROFILE = "dev";
    private final static boolean GENERATED_DDL = true;
    private final static int DATASOURCE_MAX_ACTIVE = 10;
    private final static int DATASOURCE_MAX_WAIT = 100;
    private final static int DATASOURCE_INITIAL_SIZE = 3;

    protected static Logger log;
    protected Properties dbProperties;

    @Autowired
    ApplicationContext context;

    @PostConstruct
    protected void setProperties() {
        dbProperties = new Properties();
        try {
            dbProperties.load(PersistenceJPAConfig.class.getClassLoader().getResourceAsStream(DEV_PROPS_FILENAME));
        } catch (IOException e) {
            log.fatal(getMessage(ERROR_IN_DB_PROPERTIES, null), e);
        }
    }

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

    @Bean
    public PlatformTransactionManager transactionManager(EntityManagerFactory emf) {
        JpaTransactionManager transactionManager = new JpaTransactionManager();
        transactionManager.setEntityManagerFactory(emf);

        return transactionManager;
    }

    @Bean
    public PersistenceExceptionTranslationPostProcessor exceptionTranslation() {
        return new PersistenceExceptionTranslationPostProcessor();
    }
}
