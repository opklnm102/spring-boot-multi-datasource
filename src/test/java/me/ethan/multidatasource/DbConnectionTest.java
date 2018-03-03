package me.ethan.multidatasource;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

import com.zaxxer.hikari.HikariDataSource;
import lombok.extern.slf4j.Slf4j;
import me.ethan.multidatasource.feed.Feed;
import me.ethan.multidatasource.feed.FeedRepository;
import me.ethan.multidatasource.product.Product;
import me.ethan.multidatasource.product.ProductRepository;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Created by ethan.kim on 2018. 3. 3..
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
public class DbConnectionTest {

    /**
     * feed
     */
    @Autowired
    @Qualifier("feedEntityManagerFactory")
    private LocalContainerEntityManagerFactoryBean feedEntityManagerFactory;

    @PersistenceContext(unitName = "feed")
    private EntityManager feedEntityManager;

    @Autowired
    @Qualifier("feedTransactionManager")
    public PlatformTransactionManager feedTransactionManager;

    @Autowired
    private FeedRepository feedRepository;

    /**
     * product
     */
    @Autowired
    @Qualifier("productEntityManagerFactory")
    private LocalContainerEntityManagerFactoryBean productEntityManagerFactory;

    @PersistenceContext(unitName = "product")
    private EntityManager productEntityManager;

    @Autowired
    @Qualifier("productTransactionManager")
    public PlatformTransactionManager productTransactionManager;

    @Autowired
    private ProductRepository productRepository;

    @Transactional
    @Test
    public void test() throws Exception {
        log.info("ethan test : {}", feedEntityManagerFactory.getPersistenceUnitName());
        log.info("ethan test : {}", feedEntityManagerFactory.getPersistenceUnitInfo().getSharedCacheMode());
        log.info("ethan test : {}", feedEntityManager.getProperties());

        feedEntityManagerFactory.getJpaPropertyMap()
                .forEach((s, o) -> log.info("ethan test jpa : {} - {}", s, o));
        feedEntityManagerFactory.getJpaVendorAdapter().getJpaPropertyMap()
                .forEach((s, o) -> log.info("ethan test jpa vendor - {} : {}", s, o));

        HikariDataSource dataSource = (HikariDataSource) feedEntityManagerFactory.getDataSource();
        log.info("ethan test hikariCP - {}", dataSource.getMaxLifetime());

        JpaTransactionManager transactionManager = (JpaTransactionManager) feedTransactionManager;
        log.info("ethan test transactionManager - {}", transactionManager.getPersistenceUnitName());
        transactionManager.getJpaPropertyMap()
                .forEach((s, o) -> log.info("ethan test transactionManager - {} : {}", s, o));

        // feed
        Feed feed = Feed.builder()
                .title("testTitle")
                .build();

        feedRepository.save(feed);
    }

    @Transactional
    @Test
    public void test2() throws Exception {
        log.info("ethan test : {}", productEntityManagerFactory.getPersistenceUnitName());
        log.info("ethan test : {}", productEntityManagerFactory.getPersistenceUnitInfo().getSharedCacheMode());
        log.info("ethan test : {}", productEntityManager.getProperties());

        productEntityManagerFactory.getJpaPropertyMap()
                .forEach((s, o) -> log.info("ethan test jpa : {} - {}", s, o));
        productEntityManagerFactory.getJpaVendorAdapter().getJpaPropertyMap()
                .forEach((s, o) -> log.info("ethan test jpa vendor - {} : {}", s, o));

        HikariDataSource dataSource = (HikariDataSource) productEntityManagerFactory.getDataSource();
        log.info("ethan test hikariCP - {}", dataSource.getMaxLifetime());

        JpaTransactionManager transactionManager = (JpaTransactionManager) productTransactionManager;
        log.info("ethan test transactionManager - {}", transactionManager.getPersistenceUnitName());
        transactionManager.getJpaPropertyMap()
                .forEach((s, o) -> log.info("ethan test transactionManager - {} : {}", s, o));


        // product
        List<Product> products = productRepository.findAll();
        assertThat(products).isNotEmpty();
        products.forEach(product -> log.info("{}", product));
    }

    /*
    Todo: 하나의 transaction에서 하나의 connection밖에 만들지 못하는듯..?
    하나의 dataSource는 하나의 JpaTransactionManager에서만 사용 가능하다
    어떤 JpaTransactionManager가 이미 dataSource를 사용하고 있으면 binding 불가능
org.springframework.transaction.IllegalTransactionStateException: Pre-bound JDBC Connection found! JpaTransactionManager does not support running within DataSourceTransactionManager if told to manage the DataSource itself. It is recommended to use a single JpaTransactionManager for all transactions on a single DataSource, no matter whether JPA or JDBC access.

	at org.springframework.orm.jpa.JpaTransactionManager.doBegin(JpaTransactionManager.java:359)
	at org.springframework.transaction.support.AbstractPlatformTransactionManager.getTransaction(AbstractPlatformTransactionManager.java:373)
	at org.springframework.transaction.interceptor.TransactionAspectSupport.createTransactionIfNecessary(TransactionAspectSupport.java:461)
	at org.springframework.transaction.interceptor.TransactionAspectSupport.invokeWithinTransaction(TransactionAspectSupport.java:277)
	at org.springframework.transaction.interceptor.TransactionInterceptor.invoke(TransactionInterceptor.java:96)
	at org.springframework.aop.framework.ReflectiveMethodInvocation.proceed(ReflectiveMethodInvocation.java:179)
	at org.springframework.dao.support.PersistenceExceptionTranslationInterceptor.invoke(PersistenceExceptionTranslationInterceptor.java:136)
	at org.springframework.aop.framework.ReflectiveMethodInvocation.proceed(ReflectiveMethodInvocation.java:179)
	at org.springframework.data.jpa.repository.support.CrudMethodMetadataPostProcessor$CrudMethodMetadataPopulatingMethodInterceptor.invoke(CrudMethodMetadataPostProcessor.java:133)
	at org.springframework.aop.framework.ReflectiveMethodInvocation.proceed(ReflectiveMethodInvocation.java:179)
	at org.springframework.aop.interceptor.ExposeInvocationInterceptor.invoke(ExposeInvocationInterceptor.java:92)
	at org.springframework.aop.framework.ReflectiveMethodInvocation.proceed(ReflectiveMethodInvocation.java:179)
	at org.springframework.data.repository.core.support.SurroundingTransactionDetectorMethodInterceptor.invoke(SurroundingTransactionDetectorMethodInterceptor.java:57)
	at org.springframework.aop.framework.ReflectiveMethodInvocation.proceed(ReflectiveMethodInvocation.java:179)
	at org.springframework.aop.framework.JdkDynamicAopProxy.invoke(JdkDynamicAopProxy.java:213)
	at com.sun.proxy.$Proxy85.findAll(Unknown Source)
	at me.ethan.multidatasource.DbConnectionTest.test(DbConnectionTest.java:75)
	at sun.reflect.NativeMethodAccessorImpl.invoke0(Native Method)
	at sun.reflect.NativeMethodAccessorImpl.invoke(NativeMethodAccessorImpl.java:62)
	at sun.reflect.DelegatingMethodAccessorImpl.invoke(DelegatingMethodAccessorImpl.java:43)
	at java.lang.reflect.Method.invoke(Method.java:498)
	at org.junit.runners.model.FrameworkMethod$1.runReflectiveCall(FrameworkMethod.java:50)
	at org.junit.internal.runners.model.ReflectiveCallable.run(ReflectiveCallable.java:12)
	at org.junit.runners.model.FrameworkMethod.invokeExplosively(FrameworkMethod.java:47)
	at org.junit.internal.runners.statements.InvokeMethod.evaluate(InvokeMethod.java:17)
	at org.springframework.test.context.junit4.statements.RunBeforeTestMethodCallbacks.evaluate(RunBeforeTestMethodCallbacks.java:75)
	at org.springframework.test.context.junit4.statements.RunAfterTestMethodCallbacks.evaluate(RunAfterTestMethodCallbacks.java:86)
	at org.springframework.test.context.junit4.statements.SpringRepeat.evaluate(SpringRepeat.java:84)
	at org.junit.runners.ParentRunner.runLeaf(ParentRunner.java:325)
	at org.springframework.test.context.junit4.SpringJUnit4ClassRunner.runChild(SpringJUnit4ClassRunner.java:252)
	at org.springframework.test.context.junit4.SpringJUnit4ClassRunner.runChild(SpringJUnit4ClassRunner.java:94)
	at org.junit.runners.ParentRunner$3.run(ParentRunner.java:290)
	at org.junit.runners.ParentRunner$1.schedule(ParentRunner.java:71)
	at org.junit.runners.ParentRunner.runChildren(ParentRunner.java:288)
	at org.junit.runners.ParentRunner.access$000(ParentRunner.java:58)
	at org.junit.runners.ParentRunner$2.evaluate(ParentRunner.java:268)
	at org.springframework.test.context.junit4.statements.RunBeforeTestClassCallbacks.evaluate(RunBeforeTestClassCallbacks.java:61)
	at org.springframework.test.context.junit4.statements.RunAfterTestClassCallbacks.evaluate(RunAfterTestClassCallbacks.java:70)
	at org.junit.runners.ParentRunner.run(ParentRunner.java:363)
	at org.springframework.test.context.junit4.SpringJUnit4ClassRunner.run(SpringJUnit4ClassRunner.java:191)
	at org.junit.runner.JUnitCore.run(JUnitCore.java:137)
	at com.intellij.junit4.JUnit4IdeaTestRunner.startRunnerWithArgs(JUnit4IdeaTestRunner.java:68)
	at com.intellij.rt.execution.junit.IdeaTestRunner$Repeater.startRunnerWithArgs(IdeaTestRunner.java:47)
	at com.intellij.rt.execution.junit.JUnitStarter.prepareStreamsAndStart(JUnitStarter.java:242)
	at com.intellij.rt.execution.junit.JUnitStarter.main(JUnitStarter.java:70)
     */
}
