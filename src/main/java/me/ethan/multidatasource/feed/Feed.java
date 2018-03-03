package me.ethan.multidatasource.feed;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * Created by ethan.kim on 2018. 3. 3..
 */
@Entity
@Table(name = "feed", schema = "TEST_FEED")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString
@EqualsAndHashCode(of = "feedId")
public class Feed {

    @Id
    @Column(name = "feed_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Getter
    private Long feedId;

    @Getter
    @Setter
    @Column(name = "title")
    private String title;

    @Builder
    public Feed(String title) {
        this.title = title;
    }
}

