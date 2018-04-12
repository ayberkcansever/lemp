package com.lemp.server.database;

import com.datastax.driver.core.PreparedStatement;
import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Row;
import com.lemp.object.Broadcast;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;

/**
 * Created by AyberkC on 13.06.2017.
 */
public class BroadcastDBHelper extends AbstractDBHelper {

    private static BroadcastDBHelper instance;

    static {
        try {
            instance = new BroadcastDBHelper();
        } catch(Exception e){
            e.printStackTrace();
        }
    }

    private PreparedStatement insertBroadcastPs;
    private PreparedStatement addMemberToBroadcastPs;
    private PreparedStatement removeMemberFromBroadcastPs;
    private PreparedStatement deleteBroadcastPs;
    private PreparedStatement deleteBroadcastMembersPs;
    private PreparedStatement loadBroadcastPs;
    private PreparedStatement loadBroadcastMembersPs;
    private PreparedStatement updateBroadcastNamePs;
    private PreparedStatement updateBroadcastPicPs;

    private BroadcastDBHelper() {
        insertBroadcastPs = session.prepare("insert into broadcast (id, owner, name, pic, creation_time) values(?, ?, ?, ?, ?)");
        addMemberToBroadcastPs = session.prepare("insert into broadcast_member (broadcast_id, member) values(?, ?)");
        removeMemberFromBroadcastPs = session.prepare("delete from broadcast_member where broadcast_id = ? and member = ?");
        deleteBroadcastPs = session.prepare("delete from broadcast where id = ?");
        deleteBroadcastMembersPs = session.prepare("delete from broadcast_member where broadcast_id = ?");
        loadBroadcastPs = session.prepare("select * from broadcast where id = ?");
        loadBroadcastMembersPs = session.prepare("select * from broadcast_member where broadcast_id = ?");
        updateBroadcastNamePs = session.prepare("update broadcast set name = ? where id = ?");
        updateBroadcastPicPs = session.prepare("update broadcast set pic = ? where id = ?");
    }

    public static BroadcastDBHelper getInstance() {
        return instance;
    }

    public void createBroadcast(Broadcast broadcast) {
        session.execute(insertBroadcastPs.bind(broadcast.getI(), broadcast.getO(),
                broadcast.getN(), broadcast.getP(), new Date(broadcast.getC())));
        if(broadcast.getM() != null) {
            for (String member : broadcast.getM()) {
                addMemberToBroadcast(broadcast.getI(), member);
            }
        }
        // todo: cache operations
    }

    public void addMemberToBroadcast(String id, String member) {
        session.execute(addMemberToBroadcastPs.bind(id, member));
        // todo: cache operations
    }

    public void removeMemberFromBroadcast(String id, String member) {
        session.execute(removeMemberFromBroadcastPs.bind(id, member));
        // todo: cache operations
    }

    public void deleteBroadcast(String id) {
        session.execute(deleteBroadcastPs.bind(id));
        session.execute(deleteBroadcastMembersPs.bind(id));
        // todo: cache operations
    }

    public void updateBroadcastName(String id, String name) {
        session.execute(updateBroadcastNamePs.bind(name, id));
        // todo: cache operations
    }

    public void updateBroadcastPic(String id, String pic) {
        session.execute(updateBroadcastPicPs.bind(pic, id));
        // todo: cache operations
    }

    public Broadcast loadBroadcast(String id) {
        // todo: cache operations
        Broadcast broadcast = null;
        ResultSet resultSet = session.execute(loadBroadcastPs.bind(id));
        Iterator<Row> rows = resultSet.iterator();
        while (rows.hasNext()) {
            Row r = rows.next();
            String i = r.getString("id");
            String o = r.getString("owner");
            String n = r.getString("name");
            String p = r.getString("pic");
            Date c = r.getTimestamp("creation_time");
            broadcast = new Broadcast(i, n, p, c.getTime(), o);
            broadcast.setM(new ArrayList<>());
        }
        if(broadcast != null) {
            resultSet = session.execute(loadBroadcastMembersPs.bind(id));
            rows = resultSet.iterator();
            while (rows.hasNext()) {
                Row r = rows.next();
                String member = r.getString("member");
                broadcast.getM().add(member);
            }
        }
        return broadcast;
    }


}
