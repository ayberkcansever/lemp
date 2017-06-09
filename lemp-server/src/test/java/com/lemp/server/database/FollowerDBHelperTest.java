package com.lemp.server.database;

import com.lemp.server.BaseTest;
import com.lemp.server.cache.CacheHolder;
import com.lemp.server.database.dbo.Followee;
import com.lemp.server.database.dbo.Follower;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by AyberkC on 07.06.2017.
 */
public class FollowerDBHelperTest extends BaseTest {

    @BeforeClass
    public static void setup() throws Exception {
        AbstractDBHelper.init(new String[]{"127.0.0.1"}, "lemp_test");
        CacheHolder.init();
    }

    @Test
    public void insertGetDeleteFolloweeListTest() throws Exception {
        truncateFollowTables();
        insertFollowees("follower1", 3);

        List<Followee> insertedList = FollowerDBHelper.getInstance().getUsersFollowees("follower1");
        Assert.assertEquals(3, insertedList.size());
        Assert.assertEquals(1, FollowerDBHelper.getInstance().getUsersFollowers("followee1").size());
        Assert.assertEquals(1, FollowerDBHelper.getInstance().getUsersFollowers("followee2").size());
        Assert.assertEquals(1, FollowerDBHelper.getInstance().getUsersFollowers("followee3").size());

        FollowerDBHelper.getInstance().deleteUsersAllFollowees("follower1");
        List<Followee> list = FollowerDBHelper.getInstance().getUsersFollowees("follower1");
        Assert.assertEquals(0, list.size());
        Assert.assertEquals(0, FollowerDBHelper.getInstance().getUsersFollowers("followee1").size());
        Assert.assertEquals(0, FollowerDBHelper.getInstance().getUsersFollowers("followee2").size());
        Assert.assertEquals(0, FollowerDBHelper.getInstance().getUsersFollowers("followee3").size());
    }

    @Test
    public void insertGetDeleteFollowerTest() throws Exception {
        truncateFollowTables();
        Follower follower1 = new Follower("followee1", "follower1", "follower1nick");
        FollowerDBHelper.getInstance().insertFollower(follower1);
        Assert.assertEquals(1, FollowerDBHelper.getInstance().getUsersFollowers("followee1").size());

        FollowerDBHelper.getInstance().deleteUserFollower("followee1", "follower1");
        Assert.assertEquals(0, FollowerDBHelper.getInstance().getUsersFollowers("followee1").size());

        FollowerDBHelper.getInstance().insertFollower(follower1);
        Assert.assertEquals(1, FollowerDBHelper.getInstance().getUsersFollowers("followee1").size());

        FollowerDBHelper.getInstance().deleteUsersAllFollowers("followee1");
        Assert.assertEquals(0, FollowerDBHelper.getInstance().getUsersFollowers("followee1").size());
    }

    @Test
    public void insertGetDeleteFollowerListTest() throws Exception {
        truncateFollowTables();
        insertFollowers("followee1", 3);

        List<Follower> insertedList = FollowerDBHelper.getInstance().getUsersFollowers("followee1");
        Assert.assertEquals(3, insertedList.size());

        FollowerDBHelper.getInstance().deleteUsersAllFollowers("followee1");
        List<Follower> list = FollowerDBHelper.getInstance().getUsersFollowers("followee1");
        Assert.assertEquals(0, list.size());
    }

    @Test
    public void deleteUserFolloweeTest() throws Exception {
        truncateFollowTables();
        insertFollowees("follower1", 3);

        List<String> list = new ArrayList<>();
        list.add("followee1");
        FollowerDBHelper.getInstance().deleteUserFollowee("follower1", list);
        Assert.assertEquals(2, FollowerDBHelper.getInstance().getUsersFollowees("follower1").size());
        Assert.assertEquals(0, FollowerDBHelper.getInstance().getUsersFollowers("followee1").size());

        list = new ArrayList<>();
        list.add("followee2");
        FollowerDBHelper.getInstance().deleteUserFollowee("follower1", list);
        Assert.assertEquals(1, FollowerDBHelper.getInstance().getUsersFollowees("follower1").size());
        Assert.assertEquals(0, FollowerDBHelper.getInstance().getUsersFollowers("followee2").size());

        list = new ArrayList<>();
        list.add("followee3");
        FollowerDBHelper.getInstance().deleteUserFollowee("follower1", list);
        Assert.assertEquals(0, FollowerDBHelper.getInstance().getUsersFollowees("follower1").size());
        Assert.assertEquals(0, FollowerDBHelper.getInstance().getUsersFollowers("followee3").size());
    }

    @Test
    public void updateFolloweeTest() throws Exception {
        truncateFollowTables();
        insertFollowees("follower1", 3);

        FollowerDBHelper.getInstance().updateFollowee("follower1", "followee1", "newnick1");
        FollowerDBHelper.getInstance().updateFollowee("follower1", "followee2", "newnick2");
        FollowerDBHelper.getInstance().updateFollowee("follower1", "followee3", "newnick3");

        List<Followee> followeeList = FollowerDBHelper.getInstance().getUsersFollowees("follower1");
        Assert.assertEquals(3, followeeList.size());
        for(Followee followee : followeeList) {
            Assert.assertTrue(followee.getNick().startsWith("newnick"));
        }

        List<Follower> followerList = FollowerDBHelper.getInstance().getUsersFollowers("followee1");
        Assert.assertEquals(1, followerList.size());
        Assert.assertTrue(followerList.get(0).getNick().equals("newnick1"));
        followerList = FollowerDBHelper.getInstance().getUsersFollowers("followee2");
        Assert.assertEquals(1, followerList.size());
        Assert.assertTrue(followerList.get(0).getNick().equals("newnick2"));
        followerList = FollowerDBHelper.getInstance().getUsersFollowers("followee3");
        Assert.assertEquals(1, followerList.size());
        Assert.assertTrue(followerList.get(0).getNick().equals("newnick3"));
    }

    private void insertFollowees(String follower, int size) {
        List<Followee> followeeList = new ArrayList<>(size);
        for(int i = 1; i < size + 1; i++) {
            Followee followee = new Followee(follower, "followee" + i, "followee" + i + "nick");
            followeeList.add(followee);
        }
        FollowerDBHelper.getInstance().insertFolloweeList(follower, followeeList);
    }

    private void insertFollowers(String followee, int size) {
        List<Follower> followerList = new ArrayList<>(size);
        for(int i = 1; i < size + 1; i++) {
            Follower follower = new Follower(followee, "follower" + i, "follower" + i + "nick");
            followerList.add(follower);
        }
        FollowerDBHelper.getInstance().insertFollowerList(followee, followerList);
    }
}
