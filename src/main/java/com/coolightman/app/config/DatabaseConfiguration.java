package com.coolightman.app.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;
import java.util.Properties;

/**
 * The type Database configuration.
 */
@PropertySource("classpath:database.properties")
@EnableTransactionManagement
@EnableJpaRepositories(basePackages = {
        "com.coolightman.app.repository"
})
public class DatabaseConfiguration {

    @Value("${connection.driver_class}")
    private String driverClass;

    @Value("${connection.url}")
    private String url;

    @Value("${connection.username}")
    private String username;

    @Value("${connection.password}")
    private String password;

    /**
     * Entity manager factory local container entity manager factory bean.
     *
     * @return the local container entity manager factory bean
     */
    @Bean
    public LocalContainerEntityManagerFactoryBean entityManagerFactory() {
        LocalContainerEntityManagerFactoryBean entityManagerFactoryBean = new LocalContainerEntityManagerFactoryBean();
        entityManagerFactoryBean.setDataSource(dataSource());
        entityManagerFactoryBean.setPackagesToScan("com.coolightman.app.model");

        JpaVendorAdapter adapter = new HibernateJpaVendorAdapter();
        entityManagerFactoryBean.setJpaVendorAdapter(adapter);
        entityManagerFactoryBean.setJpaProperties(JpaProperties());
        return entityManagerFactoryBean;
    }

    /**
     * Transaction manager platform transaction manager.
     *
     * @param entityManagerFactory the entity manager factory
     * @return the platform transaction manager
     */
    @Bean
    public PlatformTransactionManager transactionManager(EntityManagerFactory entityManagerFactory) {
        JpaTransactionManager transactionManager = new JpaTransactionManager();
        transactionManager.setEntityManagerFactory(entityManagerFactory);
        return transactionManager;
    }

    /**
     * Data source data source.
     *
     * @return the data source
     */
    @Bean
    public DataSource dataSource() {
        DriverManagerDataSource driver = new DriverManagerDataSource();
        driver.setDriverClassName(driverClass);
        driver.setUrl(url);
        driver.setUsername(username);
        driver.setPassword(password);
        return driver;
    }

    /**
     * Jpa properties properties.
     *
     * @return the properties
     */
    @Bean
    public Properties JpaProperties() {
        Properties properties = new Properties();
        properties.setProperty("hibernate.hbm2ddl.auto", "validate");
        properties.setProperty("hibernate.show_sql", "true");
        properties.setProperty("hibernate.dialect", "org.hibernate.dialect.PostgreSQL95Dialect");
        return properties;
    }
}
