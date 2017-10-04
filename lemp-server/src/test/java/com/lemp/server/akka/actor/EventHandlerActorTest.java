package com.lemp.server.akka.actor;

import akka.testkit.TestKit;
import com.google.gson.Gson;
import com.lemp.packet.Datum;
import com.lemp.packet.Information;
import com.lemp.server.BaseTest;
import com.lemp.server.database.FollowerDBHelper;
import com.lemp.server.database.UserDBHelper;
import com.lemp.server.database.dbo.Follower;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.Mockito;
import scala.concurrent.duration.Duration;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

/**
 * Created by AyberkC on 09.06.2017.
 */
public class EventHandlerActorTest extends BaseTest {

    @BeforeClass
    public static void setup() {
        setUp();
    }

    @Test
    public void testLoginLogoutInformation() throws InterruptedException, IOException {
        new TestKit(system) {{
            truncateFollowTables();
            truncatePrivacyTable();
            UserDBHelper.getInstance().insertUser(testUsername, testUserPassword, 0);
            UserDBHelper.getInstance().insertUser("testuser2", "1", 0);
            authenticateUser(session, testUsername, testUserPassword);

            FollowerDBHelper.getInstance().insertFollower(new Follower(testUsername2, testUsername, testUsername2));
            authenticateUser(session2, testUsername2, testUserPassword2);

            Information information = new Information();
            information.setLi(testUsername2);
            Mockito.verify(remote).sendText(new Gson().toJson(new Datum(information)));

            TimeUnit.MILLISECONDS.sleep(500);
            logoutUser(session2);
            information = new Information();
            information.setLo(testUsername2);
            Mockito.verify(remote).sendText(new Gson().toJson(new Datum(information)));
        }};
    }

    @AfterClass
    public static void teardown() {
        TestKit.shutdownActorSystem(system, Duration.create(10, TimeUnit.SECONDS), true);
        system = null;
    }
}
