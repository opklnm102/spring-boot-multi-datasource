package me.ethan.multidatasource.feed;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by ethan.kim on 2018. 3. 3..
 */
@Service
public class FeedManager implements IFeedManager {

    @Autowired
    private FeedRepository feedRepository;

    @Override
    public void test() {
        feedRepository.findAll();
    }
}
