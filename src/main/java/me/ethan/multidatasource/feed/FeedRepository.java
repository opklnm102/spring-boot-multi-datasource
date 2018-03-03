package me.ethan.multidatasource.feed;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by ethan.kim on 2018. 3. 3..
 */
public interface FeedRepository extends JpaRepository<Feed, Long> {

}
