package com.lemp.server.database;

import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.Session;
import com.google.gson.Gson;

/**
 * Created by AyberkC on 06.06.2017.
 */
public abstract class AbstractDBHelper {

    protected static Cluster cluster;
    protected static Session session;

    protected static Gson gson = new Gson();

    public static void init(){
        cluster = Cluster.builder().addContactPoint("127.0.0.1").build();
        session = cluster.connect("lemp");
    }

}
