package com.lemp.server.database;

import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.Row;
import com.datastax.driver.core.Session;
import com.google.common.base.Strings;


/**
 * Created by ayberkcansever on 19/01/17.
 */
public class DBHelper {

    private static Cluster cluster;
    private static Session session;

    public static void init(){
        cluster = Cluster.builder().addContactPoint("127.0.0.1").build();
        session = cluster.connect("lemp");
    }

    public static boolean isAuthenticated(String identity, String token) {
        if(Strings.isNullOrEmpty(token)) {
            return false;
        }
        String userToken = "";
        for (Row row : session.execute("select * from user where username = ?", identity)) {
            userToken = row.getString("password");
        }
        if(token.equals(userToken)) {
            return true;
        }
        return false;
    }

}
