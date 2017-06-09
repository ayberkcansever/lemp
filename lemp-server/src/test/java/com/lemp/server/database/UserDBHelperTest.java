package com.lemp.server.database;

import com.lemp.server.cache.CacheHolder;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * Created by AyberkC on 07.06.2017.
 */
public class UserDBHelperTest {

    @BeforeClass
    public static void setup() throws Exception {
        AbstractDBHelper.init(new String[]{"127.0.0.1"}, "lemp_test");
        CacheHolder.init();
    }

    @Test
    public void getUserTest() throws Exception {
        Assert.assertNotNull(UserDBHelper.getInstance().getUser("testuser1"));
    }

    @Test
    public void isAuthenticatedTest() {
        Assert.assertTrue(UserDBHelper.getInstance().isAuthenticated("testuser1", "1"));
        Assert.assertFalse(UserDBHelper.getInstance().isAuthenticated("testuser1", "0"));
    }

}
