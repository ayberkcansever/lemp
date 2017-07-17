package com.lemp.server.akka.actor;

import akka.actor.ActorRef;
import akka.actor.Props;
import akka.testkit.TestKit;
import com.lemp.packet.Request;
import com.lemp.server.BaseTest;
import com.lemp.server.RequestFactory;
import com.lemp.server.akka.object.SessionRequest;
import com.lemp.server.database.UserDBHelper;
import com.lemp.server.database.dbo.User;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import scala.concurrent.duration.Duration;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

/**
 * Created by AyberkC on 09.06.2017.
 */
public class AdministrativeRequestProcessorActorTest extends BaseTest {

    @BeforeClass
    public static void setup() {
        setUp();
    }

    @Test
    public void testCreateDeleteUser() throws InterruptedException, IOException {
        new TestKit(system) {{
            UserDBHelper.getInstance().insertUser(testUsername, testUserPassword, User.Type.admin.getKey());
            TimeUnit.SECONDS.sleep(1);
            authenticateUser(session, testUsername, testUserPassword);
            Props props = Props.create(AdministrativeRequestProcessorActor.class);
            ActorRef actor = system.actorOf(props);
            TimeUnit.SECONDS.sleep(1);

            Request request = RequestFactory.getCreateUserRequest("id", "testuser2", "1");
            actor.tell(new SessionRequest(request, session), ActorRef.noSender());
            TimeUnit.MILLISECONDS.sleep(100);
            Assert.assertNotNull(UserDBHelper.getInstance().getUser("testuser2"));

            request = RequestFactory.getDeleteUserRequest("id", "testuser2");
            actor.tell(new SessionRequest(request, session), ActorRef.noSender());
            TimeUnit.MILLISECONDS.sleep(100);
            Assert.assertNull(UserDBHelper.getInstance().getUser("testuser2"));

            UserDBHelper.getInstance().deleteUser(testUsername);
            logoutUser(session);
        }};
    }

    @AfterClass
    public static void teardown() {
        TestKit.shutdownActorSystem(system, Duration.create(10, TimeUnit.SECONDS), true);
        system = null;
    }
}
