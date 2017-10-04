package com.lemp.server.akka.actor;

import akka.actor.ActorRef;
import akka.testkit.TestKit;
import com.google.gson.Gson;
import com.lemp.builder.MessageBuilder;
import com.lemp.packet.Datum;
import com.lemp.packet.Message;
import com.lemp.server.BaseTest;
import com.lemp.server.akka.object.ClientMessage;
import com.lemp.server.database.PrivacyDBHelper;
import com.lemp.server.database.UserDBHelper;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.internal.verification.VerificationModeFactory;
import scala.concurrent.duration.Duration;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

/**
 * Created by AyberkC on 09.06.2017.
 */
public class MessageProcessorActorTest extends BaseTest {

    @BeforeClass
    public static void setup() {
        setUp();
    }

    @Test
    public void testMessage() throws InterruptedException, IOException {
        new TestKit(system) {{
            truncatePrivacyTable();
            UserDBHelper.getInstance().insertUser(testUsername, testUserPassword, 0);
            UserDBHelper.getInstance().insertUser(testUsername2, testUserPassword2, 0);
            authenticateUser(session, testUsername, testUserPassword);
            authenticateUser(session2, testUsername2, testUserPassword2);
            TimeUnit.MILLISECONDS.sleep(100);

            Message message = new MessageBuilder("id", testUsername2, testUsername)
                                    .setType(Message.Type.text)
                                    .setContent("Selam")
                                    .setPeerReceipt("0")
                                    .setServerReceipt("0")
                                    .setPersistency("0").build();

            packetProcessorActor.tell(new ClientMessage(new Gson().toJson(new Datum(message)), session2), ActorRef.noSender());
            TimeUnit.MILLISECONDS.sleep(500);
            Mockito.verify(remote).sendText(new Gson().toJson(new Datum(message)));

        }};
    }

    @Test
    public void testPrivacy() throws InterruptedException, IOException {
        new TestKit(system) {{
            testMessage();
            PrivacyDBHelper.getInstance().addPrivacy(testUsername, testUsername2);
            TimeUnit.MILLISECONDS.sleep(200);
            Message message = new MessageBuilder("id", testUsername2, testUsername)
                    .setType(Message.Type.text)
                    .setContent("Selam")
                    .setPeerReceipt("0")
                    .setServerReceipt("0")
                    .setPersistency("0").build();

            packetProcessorActor.tell(new ClientMessage(new Gson().toJson(new Datum(message)), session2), ActorRef.noSender());
            TimeUnit.MILLISECONDS.sleep(200);
            Mockito.verify(remote, VerificationModeFactory.times(0)).sendText(new Gson().toJson(new Datum(message)));
            TimeUnit.MILLISECONDS.sleep(200);

            PrivacyDBHelper.getInstance().removePrivacy(testUsername, testUsername2);
            TimeUnit.MILLISECONDS.sleep(200);
            packetProcessorActor.tell(new ClientMessage(new Gson().toJson(new Datum(message)), session2), ActorRef.noSender());
            TimeUnit.MILLISECONDS.sleep(200);
            Mockito.verify(remote).sendText(new Gson().toJson(new Datum(message)));
        }};
    }

    @AfterClass
    public static void teardown() {
        TestKit.shutdownActorSystem(system, Duration.create(10, TimeUnit.SECONDS), true);
        system = null;
    }
}
