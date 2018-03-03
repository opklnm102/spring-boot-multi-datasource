package me.ethan.multidatasource;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by ethan.kim on 2018. 3. 3..
 */
@Configuration
@EnableJpaRepositories(basePackages = "me.ethan.multidatasource",
        entityManagerFactoryRef = "entityManagerFactory",
        transactionManagerRef = "transactionManager")
public class MultiDbConfiguration {

    @Bean(name = "routingDatasource")
    @DependsOn(value = {"feedDataSource", "productDataSource"})
    @Primary
    public DataSource routingDatasource(@Qualifier("feedDataSource") DataSource feedDataSource,
                                        @Qualifier("productDataSource") DataSource productDataSource) {
        AbstractRoutingDataSource routingDataSource = new AbstractRoutingDataSource() {
            @Override
            protected Object determineCurrentLookupKey() {

//                TransactionSynchronizationManager.isCurrentTransactionReadOnly();  read only면 slave를 바라보도록

                // 어떤거에 의해 sourceType이 결정되게 해야하는가...???
                return DbContextHolder.getDbRoutong();
            }
        };

        routingDataSource.setDefaultTargetDataSource(feedDataSource);

        Map<Object, Object> dataSourceMap = new HashMap<>();
        dataSourceMap.put(DbType.FEED, feedDataSource);
        dataSourceMap.put(DbType.PRODUCT, productDataSource);
        routingDataSource.setTargetDataSources(dataSourceMap);
        return routingDataSource;
    }

    // transactional 시작하면서 transactionManager가 datasource를 결정하는지 check
    // 시작하면서 이미 고른다면 LazyConnectionDataSourceProxy는 소용 없는듯...?
//    @Bean
//    @Primary
//    public DataSource dataSource(@Qualifier("routingDatasource") DataSource routingDatasource) {
//        return new LazyConnectionDataSourceProxy(routingDatasource);
//    }

    @Bean(name = "entityManagerFactory")
    public LocalContainerEntityManagerFactoryBean entityManagerFactory(@Qualifier("routingDatasource") DataSource dataSource,
                                                                       EntityManagerFactoryBuilder builder) {
        return builder.dataSource(dataSource)
                .packages("me.ethan.multidatasource")
                .build();
    }

    @Bean(name = "transactionManager")
    public PlatformTransactionManager transactionManager(@Qualifier("entityManagerFactory") EntityManagerFactory entityManagerFactory) {
        JpaTransactionManager transactionManager = new JpaTransactionManager(entityManagerFactory);
        return transactionManager;
    }

}
