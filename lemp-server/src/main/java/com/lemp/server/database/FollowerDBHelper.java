package com.lemp.server.database;

import com.datastax.driver.core.PreparedStatement;
import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Row;
import com.lemp.server.cache.CacheHolder;
import com.lemp.server.database.dbo.Followee;
import com.lemp.server.database.dbo.Follower;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.locks.Lock;

/**
 * Created by AyberkC on 06.06.2017.
 */
public class FollowerDBHelper extends AbstractDBHelper {

    private static FollowerDBHelper instance;

    static {
        try {
            instance = new FollowerDBHelper();
        } catch(Exception e){
            e.printStackTrace();
        }
    }

    private static PreparedStatement insertFollower;
    private static PreparedStatement insertFollowee;
    private static PreparedStatement loadUserFollowers;
    private static PreparedStatement loadUserFollowees;

    private FollowerDBHelper() {
        insertFollower = session.prepare("insert into follower (followee, follower, nick) values (?,?,?)");
        insertFollowee = session.prepare("insert into followee (follower, followee, nick) values (?,?,?)");
        loadUserFollowers = session.prepare("select * from follower where followee = ?");
        loadUserFollowees = session.prepare("select * from followee where follower = ?");
    }

    public static FollowerDBHelper getInstance() {
        return instance;
    }

    public List<Followee> insertFolloweeList(String follower, List<Followee> followeeList) {
        List<Followee> insertedFollowees = new ArrayList<>();
        for(Followee followee : followeeList) {
            if(UserDBHelper.getInstance().getUser(followee.getFollowee()) != null) {
                session.execute(insertFollowee.bind(follower, followee.getFollowee(), followee.getNick()));
                insertedFollowees.add(followee);
            }
        }
        Lock lock = CacheHolder.followeeCache.lock(follower);
        try {
            lock.lock();
            List<Followee> existingFolloweeList = CacheHolder.followeeCache.get(follower);
            if(existingFolloweeList == null) {
                existingFolloweeList = new ArrayList<>();
            }
            existingFolloweeList.addAll(insertedFollowees);
            CacheHolder.followeeCache.put(follower, existingFolloweeList);
        } catch (Exception e) {
            throw e;
        } finally {
            try {
                lock.unlock();
            } catch (Exception e) {

            }
        }
        return insertedFollowees;
    }

    public void insertFollower(Follower follower) {
        session.execute(insertFollower.bind(follower.getFollowee(), follower.getFollower(), follower.getNick()));
        Lock lock = CacheHolder.followerCache.lock(follower.getFollowee());
        try {
            lock.lock();
            List<Follower> existingFollowerList = CacheHolder.followerCache.get(follower.getFollowee());
            if(existingFollowerList == null) {
                existingFollowerList = new ArrayList<>();
            }
            existingFollowerList.add(follower);
            CacheHolder.followerCache.put(follower.getFollowee(), existingFollowerList);
        } catch (Exception e) {
            throw e;
        } finally {
            try {
                lock.unlock();
            } catch (Exception e) {

            }
        }
    }

    public void insertFollowerList(String followee, List<Follower> followerList) {
        for(Follower follower : followerList) {
            session.execute(insertFollower.bind(followee, follower.getFollower(), follower.getNick()));
        }
        Lock lock = CacheHolder.followerCache.lock(followee);
        try {
            lock.lock();
            List<Follower> existingFollowerList = CacheHolder.followerCache.get(followee);
            if(existingFollowerList == null) {
                existingFollowerList = new ArrayList<>();
            }
            existingFollowerList.addAll(followerList);
            CacheHolder.followerCache.put(followee, existingFollowerList);
        } catch (Exception e) {
            throw e;
        } finally {
            try {
                lock.unlock();
            } catch (Exception e) {

            }
        }
    }

    public List<Followee> getUsersFollowees(String follower) {
        List<Followee> followeeList = CacheHolder.followeeCache.get(follower);
        if(followeeList == null) {
            ResultSet resultSet = session.execute(loadUserFollowees.bind(follower));
            Iterator<Row> rows = resultSet.iterator();
            while (rows.hasNext()) {
                Row r = rows.next();
                String f = r.getString("followee");
                String n = r.getString("nick");
                Followee followee = new Followee(follower, f, n);
                followeeList.add(followee);
            }
            CacheHolder.followeeCache.put(follower, followeeList);
        }
        return followeeList;
    }

    public List<Follower> getUsersFollowers(String followee) {
        List<Follower> followerList = CacheHolder.followerCache.get(followee);
        if(followerList == null) {
            ResultSet resultSet = session.execute(loadUserFollowers.bind(followee));
            Iterator<Row> rows = resultSet.iterator();
            while (rows.hasNext()) {
                Row r = rows.next();
                String f = r.getString("follower");
                String n = r.getString("nick");
                Follower follower = new Follower(followee, f, n);
                followerList.add(follower);
            }
            CacheHolder.followerCache.put(followee, followerList);
        }
        return followerList;
    }

}
