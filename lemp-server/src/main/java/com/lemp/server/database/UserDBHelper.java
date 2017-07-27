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

    private PreparedStatement insertUserPs;
    private PreparedStatement updateUserPicUrlPs;
    private PreparedStatement updateUserStatusPs;
    private PreparedStatement deleteUserPs;
    private PreparedStatement loadUserPs;

    private UserDBHelper() {
        insertUserPs = session.prepare("insert into user (username, password, user_type) values(?, ?, ?)");
        updateUserPicUrlPs = session.prepare("update user set pic_url = ? where username = ?");
        updateUserStatusPs = session.prepare("update user set status = ? where username = ?");
        deleteUserPs = session.prepare("delete from user where username = ?");
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
                int t = r.getInt("user_type");
                String pu = r.getString("pic_url");
                String s = r.getString("status");
                user = new User(u, p, t, pu, s);
                CacheHolder.getUserCache().put(username, user);
                break;
           }
        }
        return user;
    }

    public void insertUser(String username, String password, int userType) {
        session.execute(insertUserPs.bind(username, password, userType));
        CacheHolder.getUserCache().put(username, new User(username, password, userType));
    }

    public void updateUserPicUrl(String username, String picUrl) {
        session.execute(updateUserPicUrlPs.bind(picUrl, username));
        User user = getUser(username);
        user.setPicUrl(picUrl);
        CacheHolder.getUserCache().put(username, user);
    }
    public void updateUserStatus(String username, String status) {
        session.execute(updateUserStatusPs.bind(status, username));
        User user = getUser(username);
        user.setStatus(status);
        CacheHolder.getUserCache().put(username, user);
    }

    public void deleteUser(String username) {
        session.execute(deleteUserPs.bind(username));
        CacheHolder.getUserCache().remove(username);
    }

    public User isAuthenticatedUser(String identity, String token) {
        if(Strings.isNullOrEmpty(token)) {
            return null;
        }
        User user = getUser(identity);
        if(user == null) {
            return null;
        }
        if(token.equals(user.getPassword())) {
            return user;
        }
        return null;
    }
}
