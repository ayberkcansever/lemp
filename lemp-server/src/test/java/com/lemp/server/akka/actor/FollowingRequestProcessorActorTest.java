package com.lemp.server.akka.actor;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.testkit.TestKit;
import com.google.gson.Gson;
import com.lemp.object.User;
import com.lemp.packet.Request;
import com.lemp.packet.Response;
import com.lemp.server.BaseTest;
import com.lemp.server.RequestFactory;
import com.lemp.server.akka.object.SessionRequest;
import com.lemp.server.cache.CacheHolder;
import com.lemp.server.database.AbstractDBHelper;
import com.lemp.server.database.FollowerDBHelper;
import com.lemp.server.database.dbo.Followee;
import com.lemp.server.database.dbo.Follower;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.Mockito;
import scala.concurrent.duration.Duration;

import javax.websocket.RemoteEndpoint;
import javax.websocket.Session;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

/**
 * Created by AyberkC on 07.06.2017.
 */
public class FollowingRequestProcessorActorTest extends BaseTest {

    private static Session session;
    private static RemoteEndpoint.Basic remote;

    @BeforeClass
    public static void setup() {
        AbstractDBHelper.init(new String[]{"127.0.0.1"}, "lemp_test");
        CacheHolder.init();
        if(system == null) {
            system = ActorSystem.create();
        }
        session = mock(Session.class);
        given(session.getUserProperties()).willReturn(new HashMap<>());
        remote = mock(RemoteEndpoint.Basic.class);
        given(session.getBasicRemote()).willReturn(remote);
    }

    @Test
    public void testLogoutRequestProcessorActor() throws InterruptedException, IOException {
        new TestKit(system) {{
            Props props = Props.create(LogoutRequestProcessorActor.class);
            ActorRef actor = system.actorOf(props);
            Request request = RequestFactory.getLogoutRequest("id", "i'm bored...");
            actor.tell(new SessionRequest(request, session), ActorRef.noSender());
            TimeUnit.SECONDS.sleep(1);
            Mockito.verify(session).close();
        }};
    }

    @Test
    public void testRegisterAndRemoveFollowees() throws InterruptedException, IOException {
        new TestKit(system) {{
            truncateFollowTables();
            authenticateUser(session, remote, testUsername, testUserPassword);

            Props props = Props.create(FollowingRequestProcessorActor.class);
            ActorRef actor = system.actorOf(props);
            Request request = RequestFactory.getAddFolloweesRequest("id", 2);
            actor.tell(new SessionRequest(request, session), ActorRef.noSender());
            TimeUnit.SECONDS.sleep(1);
            Assert.assertEquals(2, FollowerDBHelper.getInstance().getUsersFollowees(testUsername).size());
            Assert.assertEquals(1, FollowerDBHelper.getInstance().getUsersFollowers("followee1").size());
            Assert.assertEquals(1, FollowerDBHelper.getInstance().getUsersFollowers("followee2").size());
            Mockito.verify(remote).sendText(new Gson().toJson(new Response()));

            List<String> rfList = new ArrayList<>();
            rfList.add("followee1");
            actor.tell(new SessionRequest(RequestFactory.getRemoveFolloweeRequest("id", rfList), session), ActorRef.noSender());
            TimeUnit.SECONDS.sleep(1);
            Assert.assertEquals(1, FollowerDBHelper.getInstance().getUsersFollowees(testUsername).size());
            Assert.assertEquals(0, FollowerDBHelper.getInstance().getUsersFollowers("followee1").size());

            rfList.clear();
            rfList.add("followee2");
            actor.tell(new SessionRequest(RequestFactory.getRemoveFolloweeRequest("id", rfList), session), ActorRef.noSender());
            TimeUnit.SECONDS.sleep(1);
            Assert.assertEquals(0, FollowerDBHelper.getInstance().getUsersFollowees(testUsername).size());
            Assert.assertEquals(0, FollowerDBHelper.getInstance().getUsersFollowers("followee1").size());
        }};
    }

    @Test
    public void testUpdateFollowee() throws InterruptedException, IOException {
        new TestKit(system) {{
            truncateFollowTables();
            authenticateUser(session, remote, testUsername, testUserPassword);

            Props props = Props.create(FollowingRequestProcessorActor.class);
            ActorRef actor = system.actorOf(props);
            Request request = RequestFactory.getAddFolloweesRequest("id", 2);
            actor.tell(new SessionRequest(request, session), ActorRef.noSender());
            TimeUnit.SECONDS.sleep(1);

            List<User> userList = new ArrayList<>();
            userList.add(new User("followee1", "newnick1"));
            userList.add(new User("followee2", "newnick2"));
            Request ufRequest = RequestFactory.getUpdateFolloweeRequest("id", userList);
            actor.tell(new SessionRequest(ufRequest, session), ActorRef.noSender());
            TimeUnit.SECONDS.sleep(1);

            List<Followee> followeeList = FollowerDBHelper.getInstance().getUsersFollowees(testUsername);
            Assert.assertEquals(2, followeeList.size());
            Assert.assertTrue(followeeList.get(0).getNick().equals("newnick1"));
            Assert.assertTrue(followeeList.get(1).getNick().equals("newnick2"));

            List<Follower> followerList = FollowerDBHelper.getInstance().getUsersFollowers("followee1");
            Assert.assertEquals(1, followerList.size());
            Assert.assertTrue(followerList.get(0).getNick().equals("newnick1"));

            followerList = FollowerDBHelper.getInstance().getUsersFollowers("followee2");
            Assert.assertEquals(1, followerList.size());
            Assert.assertTrue(followerList.get(0).getNick().equals("newnick2"));
        }};
    }


    @AfterClass
    public static void teardown() {
        TestKit.shutdownActorSystem(system, Duration.create(10, TimeUnit.SECONDS), true);
        system = null;
    }

}
