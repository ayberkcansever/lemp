package com.lemp.server.cache;

import com.lemp.server.database.dbo.Followee;
import com.lemp.server.database.dbo.Follower;
import com.lemp.server.database.dbo.User;
import org.apache.ignite.Ignite;
import org.apache.ignite.IgniteCache;
import org.apache.ignite.Ignition;

import java.util.Set;

/**
 * Created by AyberkC on 06.06.2017.
 */
public class CacheHolder {

    private static Ignite ignite;
    private static IgniteCache<String, User> userCache;
    private static IgniteCache<String, Set<Followee>> followeeCache;
    private static IgniteCache<String, Set<Follower>> followerCache;
    private static IgniteCache<String, Long> stateCache;
    private static IgniteCache<String, Set<String>> privacyCache;

    private CacheHolder() { }

    public static void init() {
        if(ignite != null) {
            return;
        }
        ignite = Ignition.start("ignite-config.xml");
        userCache = ignite.getOrCreateCache("userCache");
        followeeCache = ignite.getOrCreateCache("followerCache");
        followerCache = ignite.getOrCreateCache("followeeCache");
        stateCache = ignite.getOrCreateCache("stateCache");
        privacyCache = ignite.getOrCreateCache("privacyCache");
    }

    public static IgniteCache<String, User> getUserCache() {
        return userCache;
    }

    public static IgniteCache<String, Set<Followee>> getFolloweeCache() {
        return followeeCache;
    }

    public static IgniteCache<String, Set<Follower>> getFollowerCache() {
        return followerCache;
    }

    public static IgniteCache<String, Long> getStateCache() {
        return stateCache;
    }

    public static IgniteCache<String, Set<String>> getPrivacyCache() {
        return privacyCache;
    }
}
