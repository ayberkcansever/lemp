package com.lemp.server.database;

import com.lemp.server.BaseTest;
import com.lemp.server.cache.CacheHolder;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * Created by AyberkC on 13.06.2017.
 */
public class PrivacyDBHelperTest extends BaseTest {

    @BeforeClass
    public static void setup() throws Exception {
        AbstractDBHelper.init(new String[]{"127.0.0.1"}, 9042, "lemp_test");
        CacheHolder.init();
    }

    @Test
    public void addRemovePrivacyTest() throws Exception {
        String bannedUsername = "testuser2";
        PrivacyDBHelper.getInstance().addPrivacy(testUsername, bannedUsername);
        Assert.assertTrue(PrivacyDBHelper.getInstance().getPrivacySet(testUsername).contains(bannedUsername));

        PrivacyDBHelper.getInstance().removePrivacy(testUsername, "testuser2");
        Assert.assertTrue(!PrivacyDBHelper.getInstance().getPrivacySet(testUsername).contains(bannedUsername));
    }
}
