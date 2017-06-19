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
import com.lemp.server.akka.object.SessionRequest;
import com.lemp.server.database.StateDBHelper;
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
            authenticateUser(session, testUsername, testUserPassword);
            Props props = Props.create(StateRequestProcessorActor.class);
            ActorRef actor = system.actorOf(props);

            long lastOfflineTime = StateDBHelper.getInstance().updateState("testuser2");
            actor.tell(new SessionRequest(RequestFactory.getStateRequest("id", "testuser2"), session), ActorRef.noSender());
            TimeUnit.MILLISECONDS.sleep(500);
            Response response = new Response();
            response.setId("id");
            State state = new State();
            state.setU("testuser2");
            state.setV(lastOfflineTime);
            response.setS(state);
            Mockito.verify(remote).sendText(new Gson().toJson(new Datum(response)));
        }};
    }

    @AfterClass
    public static void teardown() {
        TestKit.shutdownActorSystem(system, Duration.create(10, TimeUnit.SECONDS), true);
        system = null;
    }
}
