package me.ethan.multidatasource.product;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by ethan.kim on 2018. 2. 19..
 */
public interface ProductRepository extends JpaRepository<Product, Long> {

}
