package com.lemp.server.cache;

import com.lemp.server.database.dbo.Followee;
import com.lemp.server.database.dbo.Follower;
import com.lemp.server.database.dbo.User;
import org.apache.ignite.Ignite;
import org.apache.ignite.IgniteCache;
import org.apache.ignite.Ignition;

import java.util.List;

/**
 * Created by AyberkC on 06.06.2017.
 */
public class CacheHolder {

    public static Ignite ignite;
    public static IgniteCache<String, User> userCache;
    public static IgniteCache<String, List<Followee>> followeeCache;
    public static IgniteCache<String, List<Follower>> followerCache;

    public static void init(){
        ignite = Ignition.start("ignite-config.xml");
        userCache = ignite.getOrCreateCache("userCache");
        followeeCache = ignite.getOrCreateCache("followerCache");
        followerCache = ignite.getOrCreateCache("followeeCache");
    }

}
