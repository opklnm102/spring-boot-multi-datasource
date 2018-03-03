package me.ethan.multidatasource;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

import lombok.extern.slf4j.Slf4j;

/**
 * Created by ethan.kim on 2018. 3. 3..
 */
@Aspect
@Component
@Slf4j
public class DbRoutingAspect {

    @Around(value = "@within(dbRoute) || @annotation(dbRoute)")
    public Object setDatasource(ProceedingJoinPoint pjp, DbRoute dbRoute) throws Throwable {

        log.info("ethan test aop {}", dbRoute);

        if (dbRoute != null) {  // method level
            DbContextHolder.setRouting(dbRoute.value());
        } else {
            // class level
            DbRoute classAnnotation = (DbRoute) pjp.getSignature().getDeclaringType().getAnnotation(DbRoute.class);

            if (classAnnotation != null) {
                log.info("ethan test use class annotation when method annotation is not exist  " + classAnnotation.value());
                DbContextHolder.setRouting(classAnnotation.value());
            } else {
                // default
                DbContextHolder.setRouting(DbType.FEED);
            }
        }

        return pjp.proceed();
    }

    @Around(value = "execution(public * ((@DbRoute *)+).*(..)) && within(@DbRoute *)")
    public Object setDatasource2(ProceedingJoinPoint pjp) throws Throwable {

        MethodSignature signature = (MethodSignature) pjp.getSignature();
        Method method = signature.getMethod();
        DbRoute dbRoute = method.getAnnotation(DbRoute.class);

        log.info("ethan test aop {}", dbRoute);

        if (dbRoute != null) {  // method level
            DbContextHolder.setRouting(dbRoute.value());
        } else {
            // class level
            DbRoute classAnnotation = (DbRoute) pjp.getSignature().getDeclaringType().getAnnotation(DbRoute.class);
//                pjp.getSignature().


            log.info("ethan test {}", classAnnotation);

            if (classAnnotation != null) {
                log.info("ethan test use class annotation when method annotation is not exist  " + classAnnotation.value());
                DbContextHolder.setRouting(classAnnotation.value());
            } else {
                // default
                DbContextHolder.setRouting(DbType.FEED);
            }
        }

        return pjp.proceed();
    }
}
