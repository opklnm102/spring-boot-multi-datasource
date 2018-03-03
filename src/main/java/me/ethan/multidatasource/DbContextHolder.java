package me.ethan.multidatasource;

import org.springframework.util.Assert;

/**
 * Created by ethan.kim on 2018. 3. 3..
 */
public class DbContextHolder {

    private static final ThreadLocal<DbType> contextHolder = new ThreadLocal<>();

    public static  void setRouting(DbType routing) {
        Assert.notNull(routing, "DbType cannot be null");
        contextHolder.set(routing);
    }

    public static DbType getDbRoutong() {
        return contextHolder.get();  // Todo: 없을때의 방지는...? default datasource가 해준다
    }

    public static void clearDbRouting() {
        contextHolder.remove();
    }
}
