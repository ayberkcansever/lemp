package com.lemp.server.akka.actor;

import akka.actor.ActorRef;
import akka.actor.Props;
import akka.testkit.TestKit;
import com.google.gson.Gson;
import com.lemp.object.Picture;
import com.lemp.object.Status;
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
import java.util.concurrent.TimeUnit;

/**
 * Created by AyberkC on 09.06.2017.
 */
public class PersonalRequestProcessorActorTest extends BaseTest {

    @BeforeClass
    public static void setup() {
        setUp();
    }

    @Test
    public void testSetGetPicUrl() throws InterruptedException, IOException {
        new TestKit(system) {{
            UserDBHelper.getInstance().insertUser(testUsername, testUserPassword, User.Type.user.getKey());
            TimeUnit.MILLISECONDS.sleep(500);
            authenticateUser(session, testUsername, testUserPassword);
            Props props = Props.create(PersonalRequestProcessorActor.class);
            ActorRef actor = system.actorOf(props);

            actor.tell(new SessionRequest(RequestFactory.getSetPicUrlRequest("id", "picUrl"), session), ActorRef.noSender());
            TimeUnit.MILLISECONDS.sleep(500);
            Response response = new Response();
            response.setId("id");
            Mockito.verify(remote).sendText(new Gson().toJson(new Datum(response)));

            actor.tell(new SessionRequest(RequestFactory.getGetPicUrlRequest("id", testUsername), session), ActorRef.noSender());
            TimeUnit.MILLISECONDS.sleep(500);
            Picture picture = new Picture();
            picture.setU(testUsername);
            picture.setV("picUrl");
            response.setP(picture);
            Mockito.verify(remote).sendText(new Gson().toJson(new Datum(response)));
            UserDBHelper.getInstance().deleteUser(testUsername);
        }};
    }

    @Test
    public void testSetGetStatus() throws InterruptedException, IOException {
        new TestKit(system) {{
            UserDBHelper.getInstance().insertUser(testUsername, testUserPassword, User.Type.user.getKey());
            TimeUnit.MILLISECONDS.sleep(500);
            authenticateUser(session, testUsername, testUserPassword);
            Props props = Props.create(PersonalRequestProcessorActor.class);
            ActorRef actor = system.actorOf(props);

            actor.tell(new SessionRequest(RequestFactory.getSetStatusRequest("id", "status"), session), ActorRef.noSender());
            TimeUnit.MILLISECONDS.sleep(500);
            Response response = new Response();
            response.setId("id");
            Mockito.verify(remote).sendText(new Gson().toJson(new Datum(response)));

            actor.tell(new SessionRequest(RequestFactory.getGetStatusRequest("id", testUsername), session), ActorRef.noSender());
            TimeUnit.MILLISECONDS.sleep(500);
            Status status = new Status();
            status.setU(testUsername);
            status.setS("status");
            response.setSt(status);
            Mockito.verify(remote).sendText(new Gson().toJson(new Datum(response)));
            UserDBHelper.getInstance().deleteUser(testUsername);
        }};
    }

    @AfterClass
    public static void teardown() {
        TestKit.shutdownActorSystem(system, Duration.create(10, TimeUnit.SECONDS), true);
        system = null;
    }
}
