package com.lemp.server.database;

import com.lemp.object.Broadcast;
import com.lemp.server.BaseTest;
import com.lemp.server.cache.CacheHolder;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by AyberkC on 13.06.2017.
 */
public class BroadcastDBHelperTest extends BaseTest {

    @BeforeClass
    public static void setup() throws Exception {
        AbstractDBHelper.init(new String[]{"127.0.0.1"}, 9042, "lemp_test");
        CacheHolder.init();
    }

    @Test
    public void createDeleteBroadcastTest() throws Exception {
        truncateBroadcastTables();

        String broadcastId = "test_br_".concat(String.valueOf(System.currentTimeMillis()));
        Broadcast broadcast = createTestBroadcast(broadcastId);

        Broadcast loadedBroadcast = BroadcastDBHelper.getInstance().loadBroadcast(broadcastId);
        Assert.assertTrue(loadedBroadcast.getI().equals(broadcast.getI()));
        Assert.assertTrue(loadedBroadcast.getN().equals(broadcast.getN()));
        Assert.assertTrue(loadedBroadcast.getP().equals(broadcast.getP()));
        Assert.assertTrue(loadedBroadcast.getO().equals(broadcast.getO()));
        Assert.assertTrue(loadedBroadcast.getC() == broadcast.getC());
        Assert.assertTrue(loadedBroadcast.getM().size() == broadcast.getM().size());

        BroadcastDBHelper.getInstance().deleteBroadcast(broadcastId);
        Assert.assertTrue(BroadcastDBHelper.getInstance().loadBroadcast(broadcastId) == null);
    }

    @Test
    public void addRemoveBroadcastTest() throws Exception {
        truncateBroadcastTables();
        String broadcastId = "test_br_".concat(String.valueOf(System.currentTimeMillis()));
        createTestBroadcast(broadcastId);

        BroadcastDBHelper.getInstance().addMemberToBroadcast(broadcastId, "testusername5");
        Broadcast loadedBroadcast = BroadcastDBHelper.getInstance().loadBroadcast(broadcastId);
        Assert.assertTrue(loadedBroadcast.getM().size() == 4);

        BroadcastDBHelper.getInstance().addMemberToBroadcast(broadcastId, "testusername6");
        loadedBroadcast = BroadcastDBHelper.getInstance().loadBroadcast(broadcastId);
        Assert.assertTrue(loadedBroadcast.getM().size() == 5);

        BroadcastDBHelper.getInstance().removeMemberFromBroadcast(broadcastId, "testusername6");
        loadedBroadcast = BroadcastDBHelper.getInstance().loadBroadcast(broadcastId);
        Assert.assertTrue(loadedBroadcast.getM().size() == 4);

        BroadcastDBHelper.getInstance().removeMemberFromBroadcast(broadcastId, "testusername5");
        loadedBroadcast = BroadcastDBHelper.getInstance().loadBroadcast(broadcastId);
        Assert.assertTrue(loadedBroadcast.getM().size() == 3);

    }

    @Test
    public void updateBroadcastTest() throws Exception {
        truncateBroadcastTables();
        String broadcastId = "test_br_".concat(String.valueOf(System.currentTimeMillis()));
        createTestBroadcast(broadcastId);

        String newName = "newName";
        String newPic = "newPic";

        BroadcastDBHelper.getInstance().updateBroadcastName(broadcastId, newName);
        BroadcastDBHelper.getInstance().updateBroadcastPic(broadcastId, newPic);
        Broadcast loadedBroadcast = BroadcastDBHelper.getInstance().loadBroadcast(broadcastId);
        Assert.assertTrue(loadedBroadcast.getN().equals(newName));
        Assert.assertTrue(loadedBroadcast.getP().equals(newPic));
    }

    private Broadcast createTestBroadcast(String broadcastId) {
        Broadcast broadcast = new Broadcast(broadcastId, "name", "pic", System.currentTimeMillis(), testUsername);
        List<String> members = new ArrayList<>();
        members.add("testusername2");
        members.add("testusername3");
        members.add("testusername4");
        broadcast.setM(members);
        BroadcastDBHelper.getInstance().createBroadcast(broadcast);
        return broadcast;
    }

}
