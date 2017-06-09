package com.lemp.server.akka.actor;

import akka.actor.ActorSystem;
import akka.testkit.TestKit;
import com.lemp.server.BaseTest;
import com.lemp.server.cache.CacheHolder;
import com.lemp.server.database.AbstractDBHelper;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import scala.concurrent.duration.Duration;

import javax.websocket.RemoteEndpoint;
import javax.websocket.Session;
import java.io.IOException;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

/**
 * Created by AyberkC on 09.06.2017.
 */
public class AuthenticationRequestProcessorActorTest extends BaseTest {

    @BeforeClass
    public static void setup() {
        AbstractDBHelper.init(new String[]{"127.0.0.1"}, "lemp_test");
        CacheHolder.init();
        if(system == null) {
            system = ActorSystem.create();
        }
    }

    @Test
    public void testAuthentication() throws InterruptedException, IOException {
        new TestKit(system) {{
            Session session = mock(Session.class);
            given(session.getUserProperties()).willReturn(new HashMap<>());
            RemoteEndpoint.Basic remote = mock(RemoteEndpoint.Basic.class);
            given(session.getBasicRemote()).willReturn(remote);
            authenticateUser(session, remote, testUsername, testUserPassword);
        }};
    }

    @AfterClass
    public static void teardown() {
        TestKit.shutdownActorSystem(system, Duration.create(10, TimeUnit.SECONDS), true);
        system = null;
    }
}
