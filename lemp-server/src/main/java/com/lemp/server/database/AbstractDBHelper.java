package com.lemp.server.database;

import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.Session;
import com.google.gson.Gson;

/**
 * Created by AyberkC on 06.06.2017.
 */
public abstract class AbstractDBHelper {

    protected static Cluster cluster;
    public static Session session;

    protected static Gson gson = new Gson();

    protected AbstractDBHelper() { }

    public static void init(String[] contactPoints, int port, String keyspace ){
        if(cluster == null) {
            Cluster.Builder clusterBuilder = Cluster.builder().withPort(port);
            for(String contactPoint : contactPoints) {
                clusterBuilder.addContactPoint(contactPoint);
            }
            cluster = clusterBuilder.build();
        }
        if(session == null) {
            session = cluster.connect(keyspace);
        }
    }

}
