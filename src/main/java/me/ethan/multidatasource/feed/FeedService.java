package me.ethan.multidatasource.feed;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import me.ethan.multidatasource.DbRoute;
import me.ethan.multidatasource.DbType;

/**
 * Created by ethan.kim on 2018. 3. 3..
 */
@Service
@DbRoute(value = DbType.FEED)  // 이건 동작한다
public class FeedService {

    @Autowired
    private FeedRepository feedRepository;

    public void test() {
        feedRepository.findAll();
    }
}
