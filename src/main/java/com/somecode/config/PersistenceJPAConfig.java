package com.somecode.config;

import org.apache.log4j.Logger;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.dao.annotation.PersistenceExceptionTranslationPostProcessor;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
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

@Configuration
@EnableTransactionManagement
@EnableJpaRepositories("com.somecode")
public class PersistenceJPAConfig {
    protected static Logger LOGGER;
    private final String PROPERTIES_PATH = "db.properties";
    protected Properties properties = new Properties();

    @PostConstruct
    protected void setLOGGERAndProperties() {
        setLOGGER();
        setProperties();
    }

    protected void setLOGGER() {
        LOGGER = Logger.getLogger(PersistenceJPAConfig.class);
    }

    protected void setProperties() {
        try {
            properties.load(PersistenceJPAConfig.class.getClassLoader().getResourceAsStream(PROPERTIES_PATH));
        } catch (IOException e) {
            LOGGER.error("Error in properties", e);
        }
    }

    protected Properties getProperties() {
        return properties;
    }

    @Bean
    public DataSource dataSource() {
        DataSource d = new EmbeddedDatabaseBuilder()
                .setType(EmbeddedDatabaseType.H2)
//                .addScript("classpath:META-INF/config/schema.sql")
                .addScript("classpath:META-INF/config/test-data.sql")
                .build();
        LOGGER.info("datasource created");
        return d;
    }

    @Bean
    public LocalContainerEntityManagerFactoryBean entityManagerFactory() {
        LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();

        em.setDataSource(dataSource());
        LOGGER.info("datasource set");
        em.setPackagesToScan("com.somecode");
        HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
        vendorAdapter.setGenerateDdl(true);
        vendorAdapter.setShowSql(Boolean.parseBoolean(properties.getProperty("hibernate.show_sql")));

        em.setJpaVendorAdapter(vendorAdapter);
        em.setJpaProperties(getProperties());

        return em;
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
