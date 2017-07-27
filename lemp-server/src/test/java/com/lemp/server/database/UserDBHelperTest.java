package com.lemp.server.database;

import com.lemp.server.BaseTest;
import com.lemp.server.cache.CacheHolder;
import com.lemp.server.database.dbo.User;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * Created by AyberkC on 07.06.2017.
 */
public class UserDBHelperTest extends BaseTest {

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
    public void updateUserPicUrlTest() throws InterruptedException {
        UserDBHelper.getInstance().insertUser(testUsername, "1", 1);
        UserDBHelper.getInstance().updateUserPicUrl(testUsername, "picUrl");
        User user = UserDBHelper.getInstance().getUser(testUsername);
        Assert.assertEquals(user.getPicUrl(), "picUrl");
        UserDBHelper.getInstance().deleteUser(testUsername);
    }

    @Test
    public void updateUserStatusTest() throws InterruptedException {
        UserDBHelper.getInstance().insertUser(testUsername, "1", 1);
        UserDBHelper.getInstance().updateUserStatus(testUsername, "status");
        User user = UserDBHelper.getInstance().getUser(testUsername);
        Assert.assertEquals(user.getStatus(), "status");
        UserDBHelper.getInstance().deleteUser(testUsername);
    }

    @Test
    public void isAuthenticatedTest() {
        Assert.assertNotNull(UserDBHelper.getInstance().isAuthenticatedUser("testuser1", "1"));
        Assert.assertNull(UserDBHelper.getInstance().isAuthenticatedUser("testuser1", "0"));
    }

}
