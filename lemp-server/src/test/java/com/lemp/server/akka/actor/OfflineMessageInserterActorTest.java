package com.lemp.server.akka.actor;

import akka.actor.ActorRef;
import akka.actor.Props;
import akka.testkit.TestKit;
import com.lemp.packet.Message;
import com.lemp.server.BaseTest;
import com.lemp.server.database.OfflineMessageDBHelper;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import scala.concurrent.duration.Duration;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Created by AyberkC on 09.06.2017.
 */
public class OfflineMessageInserterActorTest extends BaseTest {

    @BeforeClass
    public static void setup() {
        setUp();
    }

    @Test
    public void testOfflineMessage() throws InterruptedException, IOException {
        new TestKit(system) {{
            authenticateUser(session, testUsername, testUserPassword);
            Props props = Props.create(OfflineMessageInserterActor.class);
            ActorRef actor = system.actorOf(props);

            Message message = new Message();
            message.setId("id");
            message.setS("sender");
            message.setR("receiver");
            message.setSt(System.currentTimeMillis());
            message.setT(Message.Type.text.getKey());
            message.setC("Hello");
            actor.tell(message, ActorRef.noSender());
            TimeUnit.MILLISECONDS.sleep(500);

            List<Message> messageList = OfflineMessageDBHelper.getInstance().getOfflineMessages("receiver", true);
            Assert.assertEquals(1, messageList.size());

            message.setP(Message.PersistencyType.persistent.getKey());
            actor.tell(message, ActorRef.noSender());
            TimeUnit.MILLISECONDS.sleep(500);
            messageList = OfflineMessageDBHelper.getInstance().getOfflineMessages("receiver", true);
            Assert.assertEquals(1, messageList.size());

            message.setP(Message.PersistencyType.non_persistent.getKey());
            actor.tell(message, ActorRef.noSender());
            TimeUnit.MILLISECONDS.sleep(500);
            messageList = OfflineMessageDBHelper.getInstance().getOfflineMessages("receiver", true);
            Assert.assertEquals(0, messageList.size());
        }};
    }

    @AfterClass
    public static void teardown() {
        TestKit.shutdownActorSystem(system, Duration.create(10, TimeUnit.SECONDS), true);
        system = null;
    }
}
