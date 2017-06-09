package com.lemp.server.akka.actor;

import akka.testkit.TestKit;
import com.lemp.server.BaseTest;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import scala.concurrent.duration.Duration;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

/**
 * Created by AyberkC on 09.06.2017.
 */
public class AuthenticationRequestProcessorActorTest extends BaseTest {

    @BeforeClass
    public static void setup() {
        setUp();
    }

    @Test
    public void testAuthentication() throws InterruptedException, IOException {
        new TestKit(system) {{
            authenticateUser(session, testUsername, testUserPassword);
            TimeUnit.SECONDS.sleep(1);
            logoutUser(session);
        }};
    }

    @AfterClass
    public static void teardown() {
        TestKit.shutdownActorSystem(system, Duration.create(10, TimeUnit.SECONDS), true);
        system = null;
    }
}
