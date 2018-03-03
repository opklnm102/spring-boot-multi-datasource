package me.ethan.multidatasource;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by ethan.kim on 2018. 3. 3..
 */
@Target({ElementType.METHOD, ElementType.TYPE, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited  // 상속됨
public @interface DbRoute {

    /**
     * dataSource type
     *
     * @return
     */
    DbType value() default DbType.FEED;
}
