package com.lemp.server.database;

import com.datastax.driver.core.PreparedStatement;
import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Row;
import com.lemp.server.cache.CacheHolder;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 * Created by AyberkC on 13.06.2017.
 */
public class PrivacyDBHelper extends AbstractDBHelper {

    private static PrivacyDBHelper instance;

    static {
        try {
            instance = new PrivacyDBHelper();
        } catch(Exception e){
            e.printStackTrace();
        }
    }

    private PreparedStatement privacyAddPs;
    private PreparedStatement privacyRemovePs;
    private PreparedStatement privacySelectPs;

    private PrivacyDBHelper() {
        privacyAddPs = session.prepare("insert into privacy (username, banned_username) values(?, ?)");
        privacyRemovePs = session.prepare("delete from privacy where username = ? and banned_username = ?");
        privacySelectPs = session.prepare("select banned_username from privacy where username = ?");
    }

    public static PrivacyDBHelper getInstance() {
        return instance;
    }

    public Set<String> getPrivacySet(String username) {
        Set<String> privacySet = CacheHolder.getPrivacyCache().get(username);
        if(privacySet == null) {
            privacySet = new HashSet<>();
            ResultSet resultSet = session.execute(privacySelectPs.bind(username));
            Iterator<Row> rows = resultSet.iterator();
            while (rows.hasNext()) {
                Row r = rows.next();
                String b = r.getString("banned_username");
                privacySet.add(b);
            }
            CacheHolder.getPrivacyCache().put(username, privacySet);
        }
        return privacySet;
    }

    public void addPrivacy(String username, String bannedUsername) {
        session.execute(privacyAddPs.bind(username, bannedUsername));
        Set<String> privacySet = getPrivacySet(username);
        privacySet.add(bannedUsername);
        CacheHolder.getPrivacyCache().put(username, privacySet);
    }

    public void removePrivacy(String username, String bannedUsername) {
        session.execute(privacyRemovePs.bind(username, bannedUsername));
        Set<String> privacySet = getPrivacySet(username);
        privacySet.remove(bannedUsername);
        CacheHolder.getPrivacyCache().put(username, privacySet);
    }

}
