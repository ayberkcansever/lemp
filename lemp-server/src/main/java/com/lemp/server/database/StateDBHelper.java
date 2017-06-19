package com.lemp.server.database;

import com.datastax.driver.core.PreparedStatement;
import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Row;
import com.lemp.server.cache.CacheHolder;

import java.util.Iterator;

/**
 * Created by AyberkC on 13.06.2017.
 */
public class StateDBHelper extends AbstractDBHelper {

    private static StateDBHelper instance;

    static {
        try {
            instance = new StateDBHelper();
        } catch(Exception e){
            e.printStackTrace();
        }
    }

    private PreparedStatement stateInsertPs;
    private PreparedStatement stateSelectPs;

    private StateDBHelper() {
        stateInsertPs = session.prepare("insert into state (username, last_offline_time) values(?, ?)");
        stateSelectPs = session.prepare("select * from state where username = ?");
    }

    public static StateDBHelper getInstance() {
        return instance;
    }

    public long updateState(String username) {
        long lastOfflineTime = System.currentTimeMillis();
        session.execute(stateInsertPs.bind(username, lastOfflineTime));
        CacheHolder.getStateCache().put(username, lastOfflineTime);
        return lastOfflineTime;
    }

    public long getState(String username) {
        Long lastOfflineTime = CacheHolder.getStateCache().get(username);
        if(lastOfflineTime == null) {
            ResultSet resultSet = session.execute(stateSelectPs.bind(username));
            Iterator<Row> rows = resultSet.iterator();
            while (rows.hasNext()) {
                Row r = rows.next();
                lastOfflineTime = r.getLong("last_offline_time");
                CacheHolder.getStateCache().put(username, lastOfflineTime);
            }
        }
        return lastOfflineTime == null ? -1 : lastOfflineTime;
    }
}
