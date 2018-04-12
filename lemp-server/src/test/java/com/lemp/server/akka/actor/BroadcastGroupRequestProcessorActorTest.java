package com.lemp.server.akka.actor;

import akka.actor.ActorRef;
import akka.actor.Props;
import akka.testkit.TestKit;
import com.google.gson.Gson;
import com.lemp.object.Broadcast;
import com.lemp.object.Error;
import com.lemp.object.Name;
import com.lemp.object.Picture;
import com.lemp.packet.Datum;
import com.lemp.packet.Request;
import com.lemp.packet.Response;
import com.lemp.server.BaseTest;
import com.lemp.server.RequestFactory;
import com.lemp.server.akka.object.SessionRequest;
import com.lemp.server.database.BroadcastDBHelper;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.Mockito;
import scala.concurrent.duration.Duration;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Created by AyberkC on 07.06.2017.
 */
public class BroadcastGroupRequestProcessorActorTest extends BaseTest {

    @BeforeClass
    public static void setup() {
        setUp();
    }

    @Test
    public void testBroadcastRequest() throws InterruptedException, IOException {
        new TestKit(system) {{
            truncateBroadcastTables();
            authenticateUser(session, testUsername, testUserPassword);
            authenticateUser(session2, testUsername2, testUserPassword2);

            Props props = Props.create(BroadcastGroupRequestProcessorActor.class);
            ActorRef actor = system.actorOf(props);

            Request createRequest = RequestFactory.getCreateBroadcastRequest("id", 3);
            actor.tell(new SessionRequest(createRequest, session), ActorRef.noSender());
            TimeUnit.SECONDS.sleep(1);

            Broadcast loadedBroadcast = BroadcastDBHelper.getInstance().loadBroadcast(createRequest.getBr().getI());

            Assert.assertTrue(loadedBroadcast.getI().equals(createRequest.getBr().getI()));
            Assert.assertTrue(loadedBroadcast.getN().equals(createRequest.getBr().getN()));
            Assert.assertTrue(loadedBroadcast.getP().equals(createRequest.getBr().getP()));
            Assert.assertTrue(loadedBroadcast.getO().equals(testUsername));
            Assert.assertTrue(loadedBroadcast.getM().size() == createRequest.getBr().getM().size());
            Mockito.verify(remote).sendText(new Gson().toJson(new Datum(new Response("id"))));

            // test info
            Request infoRequest = RequestFactory.getInformationBroadcastRequest("id", createRequest.getBr().getI());
            actor.tell(new SessionRequest(infoRequest, session), ActorRef.noSender());
            TimeUnit.SECONDS.sleep(1);
            Response infoResponse = new Response("id");
            infoResponse.setBr(loadedBroadcast);
            Mockito.verify(remote).sendText(new Gson().toJson(new Datum(infoResponse)));

            // test update name, pic
            Request updateNameRequest = RequestFactory.getBroadcastNameRequest("id", Request.Type.set, createRequest.getBr().getI(), "newName");
            Request updatePicRequest = RequestFactory.getBroadcastPicRequest("id", Request.Type.set, createRequest.getBr().getI(), "newPic");
            actor.tell(new SessionRequest(updateNameRequest, session), ActorRef.noSender());
            TimeUnit.SECONDS.sleep(1);
            actor.tell(new SessionRequest(updatePicRequest, session), ActorRef.noSender());
            TimeUnit.SECONDS.sleep(1);
            actor.tell(new SessionRequest(infoRequest, session), ActorRef.noSender());
            TimeUnit.SECONDS.sleep(1);
            loadedBroadcast = BroadcastDBHelper.getInstance().loadBroadcast(createRequest.getBr().getI());
            Assert.assertTrue(loadedBroadcast.getN().equals("newName"));
            Assert.assertTrue(loadedBroadcast.getP().equals("newPic"));

            // test get name
            Request getNameRequest = RequestFactory.getBroadcastNameRequest("id", Request.Type.get, createRequest.getBr().getI());
            actor.tell(new SessionRequest(getNameRequest, session), ActorRef.noSender());
            TimeUnit.SECONDS.sleep(1);
            Response getNameResponse = new Response("id");
            Name name = new Name();
            name.setB(createRequest.getBr().getI());
            name.setN("newName");
            getNameResponse.setN(name);
            Mockito.verify(remote).sendText(new Gson().toJson(new Datum(getNameResponse)));

            // test get pic
            Request getPicRequest = RequestFactory.getBroadcastPicRequest("id", Request.Type.get, createRequest.getBr().getI());
            actor.tell(new SessionRequest(getPicRequest, session), ActorRef.noSender());
            TimeUnit.SECONDS.sleep(1);
            Response getPicResponse = new Response("id");
            Picture picture = new Picture();
            picture.setB(createRequest.getBr().getI());
            picture.setV("newPic");
            getPicResponse.setP(picture);
            Mockito.verify(remote).sendText(new Gson().toJson(new Datum(getPicResponse)));

            Request terminateRequest = RequestFactory.getTerminateBroadcastRequest("id", createRequest.getBr().getI());
            Request terminateRequestDummy = RequestFactory.getTerminateBroadcastRequest("id", "dummy");

            // test not found
            actor.tell(new SessionRequest(terminateRequestDummy, session), ActorRef.noSender());
            TimeUnit.SECONDS.sleep(1);
            Response notFoundResponse = new Response();
            notFoundResponse.setId("id");
            notFoundResponse.setE(Error.Type.not_found);
            Mockito.verify(remote).sendText(new Gson().toJson(new Datum(notFoundResponse)));

            // test forbidden
            actor.tell(new SessionRequest(terminateRequest, session2), ActorRef.noSender());
            TimeUnit.SECONDS.sleep(1);
            Response forbiddenResponse = new Response();
            forbiddenResponse.setId("id");
            forbiddenResponse.setE(Error.Type.forbidden);
            Mockito.verify(remote2).sendText(new Gson().toJson(new Datum(forbiddenResponse)));

            // test add member
            List<String> members = new ArrayList<>();
            members.add("testuser20");
            members.add("testuser21");
            members.add("testuser22");
            Request addMemberRequest = RequestFactory.getAddMembersToBroadcastRequest("id", createRequest.getBr().getI(), members);
            actor.tell(new SessionRequest(addMemberRequest, session), ActorRef.noSender());
            TimeUnit.SECONDS.sleep(1);
            loadedBroadcast = BroadcastDBHelper.getInstance().loadBroadcast(createRequest.getBr().getI());
            Assert.assertTrue(loadedBroadcast.getM().size() == 6);

            // test remove member
            Request removeMemberRequest = RequestFactory.getRemoveMembersFromBroadcastRequest("id", createRequest.getBr().getI(), members);
            actor.tell(new SessionRequest(removeMemberRequest, session), ActorRef.noSender());
            TimeUnit.SECONDS.sleep(1);
            loadedBroadcast = BroadcastDBHelper.getInstance().loadBroadcast(createRequest.getBr().getI());
            Assert.assertTrue(loadedBroadcast.getM().size() == 3);

            // test delete
            actor.tell(new SessionRequest(terminateRequest, session), ActorRef.noSender());
            TimeUnit.SECONDS.sleep(1);

            loadedBroadcast = BroadcastDBHelper.getInstance().loadBroadcast(createRequest.getBr().getI());
            Assert.assertTrue(loadedBroadcast == null);

            logoutUser(session);
        }};
    }

    @AfterClass
    public static void teardown() {
        TestKit.shutdownActorSystem(system, Duration.create(10, TimeUnit.SECONDS), true);
        system = null;
    }

}
