package com.lemp.server.database;

import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.Row;
import com.datastax.driver.core.Session;
import com.google.common.base.Strings;
import com.google.gson.Gson;
import com.lemp.packet.Message;

import java.util.Date;


/**
 * Created by ayberkcansever on 19/01/17.
 */
public class DBHelper {

    private static Cluster cluster;
    private static Session session;

    private static Gson gson = new Gson();

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

    public static void insertOfflineMessage(Message message) {
        session.execute("insert into offline (receiver, sender, id, message, sent_time) values(?, ?, ?, ?, ?)",
                message.getR(), message.getS(), message.getId(), gson.toJson(message), new Date());
    }

}
