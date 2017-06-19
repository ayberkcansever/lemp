package com.lemp.server.akka.actor;

import akka.actor.ActorRef;
import akka.actor.Props;
import akka.testkit.TestKit;
import com.google.gson.Gson;
import com.lemp.object.User;
import com.lemp.packet.Request;
import com.lemp.packet.Response;
import com.lemp.server.BaseTest;
import com.lemp.server.RequestFactory;
import com.lemp.server.akka.object.SessionRequest;
import com.lemp.server.database.FollowerDBHelper;
import com.lemp.server.database.dbo.Followee;
import com.lemp.server.database.dbo.Follower;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.Mockito;
import scala.concurrent.duration.Duration;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * Created by AyberkC on 07.06.2017.
 */
public class FollowingRequestProcessorActorTest extends BaseTest {

    @BeforeClass
    public static void setup() {
        setUp();
    }

    @Test
    public void testRegisterAndRemoveFollowees() throws InterruptedException, IOException {
        new TestKit(system) {{
            truncateFollowTables();
            authenticateUser(session, testUsername, testUserPassword);

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
            logoutUser(session);
        }};
    }

    @Test
    public void testUpdateFollowee() throws InterruptedException, IOException {
        new TestKit(system) {{
            truncateFollowTables();
            authenticateUser(session, testUsername, testUserPassword);

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

            Set<Followee> followeeList = FollowerDBHelper.getInstance().getUsersFollowees(testUsername);
            Assert.assertEquals(2, followeeList.size());
            List<Followee> list = new ArrayList<>(followeeList);
            Collections.sort(list, Comparator.comparing(Followee::getNick));
            Assert.assertTrue(new ArrayList<>(followeeList).get(1).getNick().equals("newnick1"));
            Assert.assertTrue(new ArrayList<>(followeeList).get(0).getNick().equals("newnick2"));

            Set<Follower> followerList = FollowerDBHelper.getInstance().getUsersFollowers("followee1");
            Assert.assertEquals(1, followerList.size());
            Assert.assertTrue(new ArrayList<>(followerList).get(0).getNick().equals("newnick1"));

            followerList = FollowerDBHelper.getInstance().getUsersFollowers("followee2");
            Assert.assertEquals(1, followerList.size());
            Assert.assertTrue(new ArrayList<>(followerList).get(0).getNick().equals("newnick2"));

            FollowerDBHelper.getInstance().deleteUsersAllFollowees("testuser1");
            logoutUser(session);
        }};
    }

    @AfterClass
    public static void teardown() {
        TestKit.shutdownActorSystem(system, Duration.create(10, TimeUnit.SECONDS), true);
        system = null;
    }

}
