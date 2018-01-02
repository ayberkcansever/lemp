package com.lemp.server.akka.actor;

import akka.actor.ActorRef;
import akka.actor.Props;
import akka.testkit.TestKit;
import com.google.gson.Gson;
import com.lemp.packet.Datum;
import com.lemp.packet.Response;
import com.lemp.server.BaseTest;
import com.lemp.server.RequestFactory;
import com.lemp.server.akka.object.SessionRequest;
import com.lemp.server.database.UserDBHelper;
import com.lemp.server.database.dbo.User;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.Mockito;
import scala.concurrent.duration.Duration;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * Created by AyberkC on 09.06.2017.
 */
public class PrivacyRequestProcessorActorTest extends BaseTest {

    @BeforeClass
    public static void setup() {
        setUp();
    }

    @Test
    public void testAddRemoveGetPrivacy() throws InterruptedException, IOException {
        new TestKit(system) {{
            UserDBHelper.getInstance().insertUser(testUsername, testUserPassword, User.Type.user.getKey());
            TimeUnit.MILLISECONDS.sleep(500);
            authenticateUser(session, testUsername, testUserPassword);
            Props props = Props.create(PrivacyRequestProcessorActor.class);
            ActorRef actor = system.actorOf(props);

            actor.tell(new SessionRequest(RequestFactory.getAddPrivacyRequest("id", "testuser2"), session), ActorRef.noSender());
            TimeUnit.MILLISECONDS.sleep(500);
            Response response = new Response();
            response.setId("id");
            Mockito.verify(remote).sendText(new Gson().toJson(new Datum(response)));
            TimeUnit.MILLISECONDS.sleep(500);

            actor.tell(new SessionRequest(RequestFactory.getGetPrivacyRequest("id"), session), ActorRef.noSender());
            TimeUnit.MILLISECONDS.sleep(500);
            Set<String> privacyList = new HashSet<>();
            response = new Response("id");
            privacyList.add("testuser2");
            response.setPr(privacyList);
            Mockito.verify(remote).sendText(new Gson().toJson(new Datum(response)));
            TimeUnit.MILLISECONDS.sleep(500);

            actor.tell(new SessionRequest(RequestFactory.getRemovePrivacyRequest("id2", "testuser2"), session), ActorRef.noSender());
            TimeUnit.MILLISECONDS.sleep(500);
            response = new Response("id2");
            Mockito.verify(remote).sendText(new Gson().toJson(new Datum(response)));
            TimeUnit.MILLISECONDS.sleep(500);

            actor.tell(new SessionRequest(RequestFactory.getGetPrivacyRequest("id"), session), ActorRef.noSender());
            TimeUnit.MILLISECONDS.sleep(500);
            response = new Response("id");
            response.setPr(new HashSet<>());
            Mockito.verify(remote).sendText(new Gson().toJson(new Datum(response)));
            TimeUnit.MILLISECONDS.sleep(500);

            UserDBHelper.getInstance().deleteUser(testUsername);
        }};
    }

    @AfterClass
    public static void teardown() {
        TestKit.shutdownActorSystem(system, Duration.create(10, TimeUnit.SECONDS), true);
        system = null;
    }
}
