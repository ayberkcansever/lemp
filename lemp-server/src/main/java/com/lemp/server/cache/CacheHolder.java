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

    private static Ignite ignite;
    private static IgniteCache<String, User> userCache;
    private static IgniteCache<String, List<Followee>> followeeCache;
    private static IgniteCache<String, List<Follower>> followerCache;

    private CacheHolder() { }

    public static void init() {
        if(ignite != null) {
            return;
        }
        ignite = Ignition.start("ignite-config.xml");
        userCache = ignite.getOrCreateCache("userCache");
        followeeCache = ignite.getOrCreateCache("followerCache");
        followerCache = ignite.getOrCreateCache("followeeCache");
    }

    public static IgniteCache<String, User> getUserCache() {
        return userCache;
    }

    public static IgniteCache<String, List<Followee>> getFolloweeCache() {
        return followeeCache;
    }

    public static IgniteCache<String, List<Follower>> getFollowerCache() {
        return followerCache;
    }
}
