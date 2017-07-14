package com.lemp.server.database;

import com.lemp.server.cache.CacheHolder;
import com.lemp.server.database.dbo.User;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.concurrent.TimeUnit;

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
    public void insertDeleteUserTest() throws InterruptedException {
        UserDBHelper.getInstance().insertUser("dummyUser", "1", 1);
        User insertedUser = UserDBHelper.getInstance().getUser("dummyUser");
        Assert.assertEquals(insertedUser.getUsername(), "dummyUser");
        Assert.assertEquals(insertedUser.getPassword(), "1");
        Assert.assertEquals(insertedUser.getUserType(), 1);
        UserDBHelper.getInstance().deleteUser("dummyUser");
        Assert.assertNull(UserDBHelper.getInstance().isAuthenticatedUser("dummyUser", "1"));
    }

    @Test
    public void isAuthenticatedTest() {
        Assert.assertNotNull(UserDBHelper.getInstance().isAuthenticatedUser("testuser1", "1"));
        Assert.assertNull(UserDBHelper.getInstance().isAuthenticatedUser("testuser1", "0"));
    }

}
