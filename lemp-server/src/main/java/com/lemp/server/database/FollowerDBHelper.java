package com.lemp.server.database;

import com.datastax.driver.core.PreparedStatement;
import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Row;
import com.lemp.server.cache.CacheHolder;
import com.lemp.server.database.dbo.Followee;
import com.lemp.server.database.dbo.Follower;
import org.apache.ignite.cache.CacheEntryProcessor;

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

    private PreparedStatement insertFollower;
    private PreparedStatement insertFollowee;
    private PreparedStatement loadUserFollowers;
    private PreparedStatement loadUserFollowees;
    private PreparedStatement deleteUserFollowers;
    private PreparedStatement deleteUserFollower;
    private PreparedStatement deleteUserFollowees;
    private PreparedStatement deleteUserFollowee;
    private PreparedStatement updateFollowee;
    private PreparedStatement updateFollower;

    private FollowerDBHelper() {
        insertFollower = session.prepare("insert into follower (followee, follower, nick) values (?,?,?)");
        insertFollowee = session.prepare("insert into followee (follower, followee, nick) values (?,?,?)");
        loadUserFollowers = session.prepare("select * from follower where followee = ?");
        loadUserFollowees = session.prepare("select * from followee where follower = ?");
        deleteUserFollowers = session.prepare("delete from follower where followee = ?");
        deleteUserFollower = session.prepare("delete from follower where followee = ? and follower = ?");
        deleteUserFollowees = session.prepare("delete from followee where follower = ?");
        deleteUserFollowee = session.prepare("delete from followee where follower = ? and followee = ?");
        updateFollowee = session.prepare("update followee set nick = ? where follower = ? and followee = ?");
        updateFollower = session.prepare("update follower set nick = ? where followee = ? and follower = ?");
    }

    public static FollowerDBHelper getInstance() {
        return instance;
    }

    public List<Followee> insertFolloweeList(String follower, List<Followee> followeeList) {
        List<Followee> insertedFollowees = new ArrayList<>();
        for(Followee followee : followeeList) {
            session.execute(insertFollowee.bind(follower, followee.getFollowee(), followee.getNick()));
            insertedFollowees.add(followee);
        }
        Lock lock = CacheHolder.getFolloweeCache().lock(follower);
        try {
            lock.lock();
            List<Followee> existingFolloweeList = CacheHolder.getFolloweeCache().get(follower);
            if(existingFolloweeList == null) {
                existingFolloweeList = new ArrayList<>();
            }
            // first of all remove the existing followees if exists
            for(Followee followee : insertedFollowees) {
                existingFolloweeList.remove(followee);
            }
            existingFolloweeList.addAll(insertedFollowees);
            CacheHolder.getFolloweeCache().put(follower, existingFolloweeList);
        } catch (Exception e) {
            throw e;
        } finally {
            try {
                lock.unlock();
            } catch (Exception e) {

            }
        }
        for(Followee followee : insertedFollowees) {
            insertFollower(new Follower(followee.getFollowee(), follower, followee.getNick()));
        }
        return insertedFollowees;
    }

    public void insertFollower(Follower follower) {
        session.execute(insertFollower.bind(follower.getFollowee(), follower.getFollower(), follower.getNick()));
        Lock lock = CacheHolder.getFollowerCache().lock(follower.getFollowee());
        try {
            lock.lock();
            List<Follower> existingFollowerList = CacheHolder.getFollowerCache().get(follower.getFollowee());
            if(existingFollowerList == null) {
                existingFollowerList = new ArrayList<>();
            }
            // firstly remove the follower if it already exists
            existingFollowerList.remove(follower);
            existingFollowerList.add(follower);
            CacheHolder.getFollowerCache().put(follower.getFollowee(), existingFollowerList);
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
        List<Follower> insertedFollowers = new ArrayList<>();
        for(Follower follower : followerList) {
            session.execute(insertFollower.bind(followee, follower.getFollower(), follower.getNick()));
            insertedFollowers.add(follower);
        }
        Lock lock = CacheHolder.getFollowerCache().lock(followee);
        try {
            lock.lock();
            List<Follower> existingFollowerList = CacheHolder.getFollowerCache().get(followee);
            if(existingFollowerList == null) {
                existingFollowerList = new ArrayList<>();
            }
            // first of all remove the existing followers if exists
            for(Follower follower : insertedFollowers) {
                existingFollowerList.remove(follower);
            }
            existingFollowerList.addAll(followerList);
            CacheHolder.getFollowerCache().put(followee, existingFollowerList);
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
        List<Followee> followeeList = CacheHolder.getFolloweeCache().get(follower);
        if(followeeList == null) {
            followeeList = new ArrayList<>();
            ResultSet resultSet = session.execute(loadUserFollowees.bind(follower));
            Iterator<Row> rows = resultSet.iterator();
            while (rows.hasNext()) {
                Row r = rows.next();
                String f = r.getString("followee");
                String n = r.getString("nick");
                Followee followee = new Followee(follower, f, n);
                followeeList.add(followee);
            }
            CacheHolder.getFolloweeCache().put(follower, followeeList);
        }
        return followeeList;
    }

    public List<Follower> getUsersFollowers(String followee) {
        List<Follower> followerList = CacheHolder.getFollowerCache().get(followee);
        if(followerList == null) {
            followerList = new ArrayList<>();
            ResultSet resultSet = session.execute(loadUserFollowers.bind(followee));
            Iterator<Row> rows = resultSet.iterator();
            while (rows.hasNext()) {
                Row r = rows.next();
                String f = r.getString("follower");
                String n = r.getString("nick");
                Follower follower = new Follower(followee, f, n);
                followerList.add(follower);
            }
            CacheHolder.getFollowerCache().put(followee, followerList);
        }
        return followerList;
    }

    public void deleteUsersAllFollowees(String follower) {
        List<Followee> followeeList = getUsersFollowees(follower);
        session.execute(deleteUserFollowees.bind(follower));
        CacheHolder.getFolloweeCache().remove(follower);
        for(Followee followee : followeeList) {
            deleteUserFollower(followee.getFollowee(), followee.getFollower());
        }
    }

    public void deleteUserFollowee(String follower, List<String> followees) {
        for (String followee : followees) {
            session.execute(deleteUserFollowee.bind(follower, followee));
        }
        Lock lock = CacheHolder.getFolloweeCache().lock(follower);
        try {
            lock.lock();
            List<Followee> existingFolloweeList = CacheHolder.getFolloweeCache().get(follower);
            if (existingFolloweeList != null) {
                for (String followee : followees) {
                    existingFolloweeList.remove(new Followee(follower, followee, ""));
                }
                CacheHolder.getFolloweeCache().put(follower, existingFolloweeList);
            }
        } catch (Exception e) {
            throw e;
        } finally {
            try {
                lock.unlock();
            } catch (Exception e) {

            }
        }
        for (String followee : followees) {
            deleteUserFollower(followee, follower);
        }
    }

    public void deleteUserFollower(String followee, String follower) {
        session.execute(deleteUserFollower.bind(followee, follower));
        Lock lockR = CacheHolder.getFollowerCache().lock(followee);
        try {
            lockR.lock();
            List<Follower> existingFollowerList = CacheHolder.getFollowerCache().get(followee);
            if(existingFollowerList != null) {
                existingFollowerList.remove(new Follower(followee, follower, ""));
                CacheHolder.getFollowerCache().put(followee, existingFollowerList);
            }
        } catch (Exception e) {
            throw e;
        } finally {
            try {
                lockR.unlock();
            } catch (Exception e) {

            }
        }
    }

    public void deleteUsersAllFollowers(String followee) {
        session.execute(deleteUserFollowers.bind(followee));
        CacheHolder.getFollowerCache().remove(followee);
    }

    public void updateFollowee(String follower, String followee, String newNick) {
        session.execute(updateFollowee.bind(newNick, follower, followee));
        CacheHolder.getFolloweeCache().invoke(follower, (CacheEntryProcessor<String, List<Followee>, Object>) (mutableEntry, objects) -> {
            List<Followee> existingList = mutableEntry.getValue();
            if(existingList != null) {
                String followee1 = (String) objects[0];
                String newNick1 = (String) objects[1];
                for (Followee followeeObj : existingList) {
                    if (followee1.equals(followeeObj.getFollowee())) {
                        followeeObj.setNick(newNick1);
                        break;
                    }
                }
            } else {
                existingList = new ArrayList<>();
                existingList.add(new Followee(follower, followee, newNick));
            }
            mutableEntry.setValue(existingList);
            return null;
        }, followee, newNick);

        updateFollower(follower, followee, newNick);
    }

    public void updateFollower(String follower, String followee, String newNick) {
        session.execute(updateFollower.bind(newNick, followee, follower));
        CacheHolder.getFollowerCache().invoke(followee, (CacheEntryProcessor<String, List<Follower>, Object>) (mutableEntry, objects) -> {
            List<Follower> existingList = mutableEntry.getValue();
            if(existingList != null) {
                String follower1 = (String) objects[0];
                String newNick1 = (String) objects[1];
                for (Follower followerObj : existingList) {
                    if (follower1.equals(followerObj.getFollower())) {
                        followerObj.setNick(newNick1);
                        break;
                    }
                }
            } else {
                existingList = new ArrayList<>();
                existingList.add(new Follower(followee, follower, newNick));
            }
            mutableEntry.setValue(existingList);
            return null;
        }, follower, newNick);
    }
}
