/*
 * Copyright (C) 2019 maximen39
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.mahixcode.smp.config;

import com.mahixcode.smp.config.json.Config;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
import java.util.Properties;

/**
 * @author maximen39
 */
@Configuration
@EnableTransactionManagement
@EnableJpaRepositories("com.mahixcode.smp.dao")
public class HibernateConfig {

    private final Config config;

    public HibernateConfig(Config config) {
        this.config = config;
    }

    @Bean(name = "entityManagerFactory")
    public LocalContainerEntityManagerFactoryBean entityManagerFactory() {
        LocalContainerEntityManagerFactoryBean managerFactoryBean = new LocalContainerEntityManagerFactoryBean();
        managerFactoryBean.setJpaVendorAdapter(jpaVendorAdapter());
        managerFactoryBean.setDataSource(dataSource());
        managerFactoryBean.setPersistenceUnitName("jpaPersistenceUnit");
        managerFactoryBean.setPackagesToScan("com.mahixcode.smp.model");
        managerFactoryBean.setJpaProperties(hibernateProperties());
        return managerFactoryBean;
    }

    @Bean(name = "transactionManager")
    public PlatformTransactionManager transactionManager() {
        return new JpaTransactionManager(entityManagerFactory().getObject());
    }

    @Bean
    public JpaVendorAdapter jpaVendorAdapter() {
        return new HibernateJpaVendorAdapter();
    }

    @Bean
    public DataSource dataSource() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName(config.dataSourceDriver);
        dataSource.setUrl(config.dataSourceUrl);
        dataSource.setUsername(config.dataSourceUsername);
        dataSource.setPassword(config.dataSourcePassword);
        return dataSource;
    }

    private Properties hibernateProperties() {
        Properties hibernateProperties = new Properties();
        hibernateProperties.setProperty("hibernate.show_sql", String.valueOf(config.hibernateShowSql));
        hibernateProperties.setProperty("hibernate.hbm2ddl.auto", config.hibernateDDL); //create-drop in release replace to update!
        hibernateProperties.setProperty("hibernate.dialect", config.hibernateDialect);
        return hibernateProperties;
    }
}
