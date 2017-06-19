package com.lemp.server.akka.actor;

import akka.actor.ActorRef;
import akka.actor.Props;
import akka.testkit.TestKit;
import com.google.gson.Gson;
import com.lemp.packet.Datum;
import com.lemp.packet.ServerRequest;
import com.lemp.server.BaseTest;
import com.lemp.server.RequestFactory;
import com.lemp.server.akka.object.SessionRequest;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.mockito.internal.verification.VerificationModeFactory;
import scala.concurrent.duration.Duration;

import java.io.IOException;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

/**
 * Created by AyberkC on 09.06.2017.
 */
public class ServerRequestProcessorActorTest extends BaseTest {

    @BeforeClass
    public static void setup() {
        setUp();
    }

    @Test
    public void testTimeRequest() throws InterruptedException, IOException {
        new TestKit(system) {{
            authenticateUser(session, testUsername, testUserPassword);
            Props props = Props.create(ServerRequestProcessorActor.class);
            ActorRef actor = system.actorOf(props);

            actor.tell(new SessionRequest(RequestFactory.getServerRequest("id", ServerRequest.Type.time), session), ActorRef.noSender());
            TimeUnit.MILLISECONDS.sleep(500);
            ArgumentCaptor<String> argument = ArgumentCaptor.forClass(String.class);
            Mockito.verify(remote, VerificationModeFactory.atLeastOnce()).sendText(argument.capture());
            Datum serverResponse = new Gson().fromJson(argument.getValue(), Datum.class);
            Assert.assertNotNull(serverResponse.getSrp());
            Assert.assertEquals("id", serverResponse.getSrp().getId());
            Assert.assertEquals(TimeZone.getDefault().getRawOffset() / (3600 * 1000), serverResponse.getSrp().getO());
            Assert.assertTrue(serverResponse.getSrp().getTm() + 5000 > System.currentTimeMillis());
        }};
    }

    @Test
    public void testKnockKnockRequest() throws InterruptedException, IOException {
        new TestKit(system) {{
            authenticateUser(session, testUsername, testUserPassword);
            Props props = Props.create(ServerRequestProcessorActor.class);
            ActorRef actor = system.actorOf(props);

            actor.tell(new SessionRequest(RequestFactory.getServerRequest("id", ServerRequest.Type.knock_kncok), session), ActorRef.noSender());
            TimeUnit.MILLISECONDS.sleep(500);
            ArgumentCaptor<String> argument = ArgumentCaptor.forClass(String.class);
            Mockito.verify(remote, VerificationModeFactory.atLeastOnce()).sendText(argument.capture());
            Datum serverResponse = new Gson().fromJson(argument.getValue(), Datum.class);
            Assert.assertNotNull(serverResponse.getSrp());
            Assert.assertEquals("id", serverResponse.getSrp().getId());
        }};
    }

    @AfterClass
    public static void teardown() {
        TestKit.shutdownActorSystem(system, Duration.create(10, TimeUnit.SECONDS), true);
        system = null;
    }
}
