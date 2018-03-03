package me.ethan.multidatasource;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.autoconfigure.transaction.TransactionManagerCustomizers;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

import lombok.extern.slf4j.Slf4j;

/**
 * Created by ethan.kim on 2018. 3. 3..
 */
@Configuration
// repositoryBaseClass 이용시 상속을 사용하여 datasource를 적용할 수 있다? -> https://docs.spring.io/spring-data/jpa/docs/current/reference/html/#repositories.custom-implementations
@EnableJpaRepositories(basePackages = "me.ethan.multidatasource.feed",  // repository base package
        entityManagerFactoryRef = "feedEntityManagerFactory",
        transactionManagerRef = "feedTransactionManager")
@Slf4j
public class FeedDataSourceConfiguration {

    @Bean(name = "feedDataSourceProperties")
    @Primary
    @ConfigurationProperties(prefix = "spring.datasource")
    public DataSourceProperties feedDataSourceProperties() {
        return new DataSourceProperties();
    }

    @Bean(name = "feedDataSource")
    @Primary
    @ConfigurationProperties(prefix = "spring.datasource")
    public DataSource feedDataSource(@Qualifier("feedDataSourceProperties") DataSourceProperties properties) {
        return properties.initializeDataSourceBuilder().build();
    }


    @Bean(name = "feedEntityManagerFactory")
    @Primary
    public LocalContainerEntityManagerFactoryBean entityManagerFactory(@Qualifier("feedDataSource") DataSource dataSource,
                                                                       EntityManagerFactoryBuilder builder) {
        /*
        private final JpaVendorAdapter jpaVendorAdapter;
	    private final PersistenceUnitManager persistenceUnitManager;
	    private final Map<String, Object> jpaProperties;
	    private final URL persistenceUnitRootLocation;
	    를 알고 있는 builder가 auto configuration에 의해 생성되어 넘어 온다

	    JpaBaseConfiguration Line 116
         */

        return builder.dataSource(dataSource)
                .packages("me.ethan.multidatasource.feed")  // entity base package
                .persistenceUnit("feed")  // EntityManagerFactory config(EntityManagerFactory 구성, entity class, mapping metadata 등)의 logical grouping name. application은 하나 이상의 persistenceUnit을 가질 수 있다
                .build();
    }

    @Bean(name = "feedTransactionManager")
    @Primary
    public PlatformTransactionManager feedTransactionManager(@Qualifier("feedEntityManagerFactory") EntityManagerFactory entityManagerFactory,
                                                              TransactionManagerCustomizers transactionManagerCustomizers) {
        JpaTransactionManager transactionManager = new JpaTransactionManager(entityManagerFactory);
//        transactionManager.setPersistenceUnitName("feed");

        log.info("ethan test feedTransactionManager - customizers {}", transactionManagerCustomizers);

        if (transactionManagerCustomizers != null) {  // auto configuration에 의해 생성됨
            /*
            spring.transaction 을 읽어 온다
            spring.transaction.default-timeout=30
            spring.transaction.rollback-on-commit-failure=true
             */
            transactionManagerCustomizers.customize(transactionManager);
        }

        return transactionManager;
    }
}
