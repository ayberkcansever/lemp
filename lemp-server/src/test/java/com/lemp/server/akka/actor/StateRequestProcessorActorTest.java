package com.lemp.server.akka.actor;

import akka.actor.ActorRef;
import akka.actor.Props;
import akka.testkit.TestKit;
import com.google.gson.Gson;
import com.lemp.object.State;
import com.lemp.packet.Datum;
import com.lemp.packet.Response;
import com.lemp.server.BaseTest;
import com.lemp.server.RequestFactory;
import com.lemp.server.akka.object.ClientMessage;
import com.lemp.server.database.StateDBHelper;
import com.lemp.server.database.UserDBHelper;
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
public class StateRequestProcessorActorTest extends BaseTest {

    @BeforeClass
    public static void setup() {
        setUp();
    }

    @Test
    public void testState() throws InterruptedException, IOException {
        new TestKit(system) {{
            UserDBHelper.getInstance().insertUser(testUsername, testUserPassword, 0);
            UserDBHelper.getInstance().insertUser("testuser2", "1", 0);
            authenticateUser(session, testUsername, testUserPassword);
            Props props = Props.create(PacketProcessorActor.class);
            ActorRef actor = system.actorOf(props);
            Props msgProps = Props.create(MessageProcessorActor.class);
            ActorRef messageProcessorActor = system.actorOf(msgProps);

            long lastOfflineTime = StateDBHelper.getInstance().updateState("testuser2");
            actor.tell(new ClientMessage(new Gson().toJson(new Datum(RequestFactory.getStateRequest("id", "testuser2"))), session), ActorRef.noSender());
            TimeUnit.MILLISECONDS.sleep(500);
            Response response = new Response();
            response.setId("id");
            response.setR(testUsername);
            State state = new State();
            state.setU("testuser2");
            state.setV(lastOfflineTime);
            response.setS(state);
            Mockito.verify(remote).sendText(new Gson().toJson(new Datum(response)));
            TimeUnit.SECONDS.sleep(2);

            authenticateUser(session2, "testuser2", "1");
            actor.tell(new ClientMessage(new Gson().toJson(new Datum(RequestFactory.getStateRequest("id", "testuser2"))), session), ActorRef.noSender());
            state.setV(0);
            response.setS(state);
            TimeUnit.SECONDS.sleep(2);
            Mockito.verify(remote).sendText(new Gson().toJson(new Datum(response)));
        }};
    }

    @AfterClass
    public static void teardown() {
        TestKit.shutdownActorSystem(system, Duration.create(10, TimeUnit.SECONDS), true);
        system = null;
    }
}
