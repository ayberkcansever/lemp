package com.lemp.server.database;

import com.datastax.driver.core.PreparedStatement;
import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Row;
import com.google.common.base.Strings;
import com.lemp.server.cache.CacheHolder;
import com.lemp.server.database.dbo.User;

import java.util.Iterator;

/**
 * Created by AyberkC on 06.06.2017.
 */
public class UserDBHelper extends AbstractDBHelper {

    private static UserDBHelper instance;

    static {
        try {
            instance = new UserDBHelper();
        } catch(Exception e){
            e.printStackTrace();
        }
    }

    private PreparedStatement loadUserPs;

    private UserDBHelper() {
        loadUserPs = session.prepare("select * from user where username = ?");
    }

    public static UserDBHelper getInstance() {
        return instance;
    }

    public User getUser(String username) {
        User user = CacheHolder.getUserCache().get(username);
        if(user == null) {
            ResultSet resultSet = session.execute(loadUserPs.bind(username));
            Iterator<Row> rows = resultSet.iterator();
            while (rows.hasNext()) {
                Row r = rows.next();
                String u = r.getString("username");
                String p = r.getString("password");
                user = new User(u, p);
                CacheHolder.getUserCache().put(username, user);
                break;
           }
        }
        return user;
    }

    public boolean isAuthenticated(String identity, String token) {
        if(Strings.isNullOrEmpty(token)) {
            return false;
        }
        User user = getUser(identity);
        if(user == null) {
            return false;
        }
        if(token.equals(user.getPassword())) {
            return true;
        }
        return false;
    }
}
