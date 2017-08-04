package com.lemp.server.database;

import com.lemp.server.BaseTest;
import com.lemp.server.cache.CacheHolder;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * Created by AyberkC on 13.06.2017.
 */
public class StateDBHelperTest extends BaseTest {

    @BeforeClass
    public static void setup() throws Exception {
        AbstractDBHelper.init(new String[]{"127.0.0.1"}, 9062, "lemp_test");
        CacheHolder.init();
    }

    @Test
    public void updateGetStateTest() throws Exception {
        long lastOfflineTime = StateDBHelper.getInstance().updateState(testUsername);
        Assert.assertEquals(lastOfflineTime, StateDBHelper.getInstance().getState(testUsername));
    }
}
