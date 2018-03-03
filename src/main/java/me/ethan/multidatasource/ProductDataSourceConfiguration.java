package me.ethan.multidatasource;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

/**
 * Created by ethan.kim on 2018. 3. 3..
 */
@Configuration
@EnableJpaRepositories(basePackages = "me.ethan.multidatasource.product",
        entityManagerFactoryRef = "productEntityManagerFactory",
        transactionManagerRef = "productTransactionManager")
public class ProductDataSourceConfiguration {

    @Bean(name = "productDataSourceProperties")
    @ConfigurationProperties(prefix = "product.datasource")
    public DataSourceProperties promoDataSourceProperties() {
        return new DataSourceProperties();
    }

    @Bean(name = "productDataSource")
    @ConfigurationProperties(prefix = "product.datasource")
    public DataSource dataSource(@Qualifier("productDataSourceProperties") DataSourceProperties properties) {
        return properties.initializeDataSourceBuilder().build();
    }

    @Bean(name = "productEntityManagerFactory")
    public LocalContainerEntityManagerFactoryBean productEntityManagerFactory(@Qualifier("productDataSource") DataSource dataSource,
                                                                             EntityManagerFactoryBuilder builder) {
        return builder.dataSource(dataSource)
                .packages("me.ethan.multidatasource.product")
                .persistenceUnit("product")
                .build();
    }

    @Bean(name = "productTransactionManager")
    public PlatformTransactionManager productTransactionManager(@Qualifier("productEntityManagerFactory") EntityManagerFactory entityManagerFactory) {
        JpaTransactionManager transactionManager = new JpaTransactionManager(entityManagerFactory);
        return transactionManager;
    }
}
