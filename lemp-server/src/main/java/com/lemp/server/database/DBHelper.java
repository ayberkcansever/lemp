package com.lemp.server.database;

import com.datastax.driver.core.*;
import com.google.common.base.Strings;
import com.google.gson.Gson;
import com.lemp.packet.Datum;
import com.lemp.packet.Message;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


/**
 * Created by ayberkcansever on 19/01/17.
 */
public class DBHelper {

    private static Cluster cluster;
    private static Session session;

    private static Gson gson = new Gson();
    private static PreparedStatement offlineInsertPs;
    private static PreparedStatement offlineSelectPs;
    private static PreparedStatement offlineDeletePs;

    public static void init(){
        cluster = Cluster.builder().addContactPoint("127.0.0.1").build();
        session = cluster.connect("lemp");
        offlineInsertPs = session.prepare("insert into offline (receiver, sender, id, message, sent_time) values(?, ?, ?, ?, ?)");
        offlineSelectPs = session.prepare("select * from offline where receiver = ?");
        offlineDeletePs = session.prepare("delete from offline where receiver = ?");
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
        BoundStatement bound = offlineInsertPs.bind(message.getR(), message.getS(), message.getId(), gson.toJson(message), new Date());
        session.execute(bound);
    }

    public static List<String> getOfflineMessagesAsString(String receiver, boolean delete) {
        List<String> list = new ArrayList<String>();
        BoundStatement bound = offlineSelectPs.bind(receiver);
        ResultSet rs = session.execute(bound);
        for(Row row : rs) {
            list.add(row.getString("message"));
        }
        if(delete) {
            deleteOfflineMessages(receiver);
        }
        return list;
    }

    public static List<Message> getOfflineMessages(String receiver, boolean delete) {
        List<Message> list = new ArrayList<Message>();
        for(String msgStr : getOfflineMessagesAsString(receiver, delete)) {
            Datum datum = gson.fromJson(msgStr, Datum.class);
            list.add(datum.getM());
        }
        return list;
    }

    public static void deleteOfflineMessages(String receiver) {
        BoundStatement boundDel = offlineDeletePs.bind(receiver);
        session.execute(boundDel);
    }
}
