package me.ethan.multidatasource;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/*
http://www.baeldung.com/spring-data-jpa-multiple-databases
https://sonegy.wordpress.com/2013/05/15/spring-data-jpa-multiple-datasource-%EC%82%AC%EC%9A%A9/
http://egloos.zum.com/kwon37xi/v/5364167
https://spring.io/blog/2007/01/23/dynamic-datasource-routing/
 */
@SpringBootApplication
public class MultiDatasourceApplication {

	public static void main(String[] args) {
		SpringApplication.run(MultiDatasourceApplication.class, args);
	}
}
