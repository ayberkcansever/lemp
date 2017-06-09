package com.lemp.server.database;

import com.datastax.driver.core.PreparedStatement;
import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Row;
import com.lemp.packet.Datum;
import com.lemp.packet.Message;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by AyberkC on 06.06.2017.
 */
public class OfflineMessageDBHelper extends AbstractDBHelper {

    private static OfflineMessageDBHelper instance;

    static {
        try {
            instance = new OfflineMessageDBHelper();
        } catch(Exception e){
            e.printStackTrace();
        }
    }

    private PreparedStatement offlineInsertPs;
    private PreparedStatement offlineSelectPs;
    private PreparedStatement offlineDeletePs;

    private OfflineMessageDBHelper() {
        offlineInsertPs = session.prepare("insert into offline (receiver, sender, id, message, sent_time) values(?, ?, ?, ?, ?)");
        offlineSelectPs = session.prepare("select * from offline where receiver = ?");
        offlineDeletePs = session.prepare("delete from offline where receiver = ?");
    }

    public static OfflineMessageDBHelper getInstance() {
        return instance;
    }

    public void insertOfflineMessage(Message message) {
        session.execute(offlineInsertPs.bind(message.getR(), message.getS(), message.getId(), gson.toJson(message), new Date()));
    }

    public List<String> getOfflineMessagesAsString(String receiver, boolean delete) {
        List<String> list = new ArrayList<>();
        ResultSet rs = session.execute(offlineSelectPs.bind(receiver));
        for(Row row : rs) {
            list.add(row.getString("message"));
        }
        if(delete) {
            deleteOfflineMessages(receiver);
        }
        return list;
    }

    public List<Message> getOfflineMessages(String receiver, boolean delete) {
        List<Message> list = new ArrayList<>();
        for(String msgStr : getOfflineMessagesAsString(receiver, delete)) {
            Datum datum = gson.fromJson(msgStr, Datum.class);
            list.add(datum.getM());
        }
        return list;
    }

    public void deleteOfflineMessages(String receiver) {
        session.execute(offlineDeletePs.bind(receiver));
    }
}
