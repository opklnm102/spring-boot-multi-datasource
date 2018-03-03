package me.ethan.multidatasource.product;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

import me.ethan.multidatasource.DbRoute;
import me.ethan.multidatasource.DbType;

/**
 * Created by ethan.kim on 2018. 2. 19..
 */
/*
service에서는 여러 repository를 사용하기 때문에
repository에서 dataSource를 결정할 정보가 들어가야 할꺼 같은데...
aop로 하려니 interface라서 읽지를 못한다
 */
@DbRoute(value = DbType.PRODUCT)
public interface ProductRepository extends JpaRepository<Product, Long> {

    @DbRoute(value = DbType.PRODUCT)
    Optional<Product> findByProductId(long productId);
}
