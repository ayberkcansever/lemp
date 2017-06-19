package com.lemp.server;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import com.google.gson.Gson;
import com.lemp.packet.Datum;
import com.lemp.packet.Request;
import com.lemp.packet.Response;
import com.lemp.server.akka.actor.AuthenticationRequestProcessorActor;
import com.lemp.server.akka.actor.LogoutRequestProcessorActor;
import com.lemp.server.akka.object.SessionRequest;
import com.lemp.server.cache.CacheHolder;
import com.lemp.server.database.AbstractDBHelper;
import org.mockito.Mockito;
import org.mockito.internal.verification.VerificationModeFactory;

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
public abstract class BaseTest {

    public static ActorSystem system;

    protected static final String testUsername = "testuser1";
    protected static final String testUserPassword = "1";

    protected static Session session;
    protected static RemoteEndpoint.Basic remote;

    protected static void setUp(){
        AbstractDBHelper.init(new String[]{"127.0.0.1"}, "lemp_test");
        CacheHolder.init();
        if(system == null) {
            system = ActorSystem.create();
        }
        if(session == null) {
            session = mock(Session.class);
            given(session.getUserProperties()).willReturn(new HashMap<>());
            remote = mock(RemoteEndpoint.Basic.class);
            given(session.getBasicRemote()).willReturn(remote);
        }
    }

    protected void authenticateUser(Session session, String username, String password) throws InterruptedException, IOException {
        Props authProps = Props.create(AuthenticationRequestProcessorActor.class);
        ActorRef authActor = system.actorOf(authProps);
        Request authRequest = RequestFactory.getAuthenticationReqeust("id", username, password);
        authActor.tell(new SessionRequest(authRequest, session), ActorRef.noSender());
        TimeUnit.SECONDS.sleep(1);
        Response response = new Response();
        response.setId(authRequest.getId());
        response.setResult(1);
        Mockito.verify(session.getBasicRemote(), VerificationModeFactory.atLeastOnce()).sendText(new Gson().toJson(new Datum(response)));
    }

    protected void logoutUser(Session session) throws InterruptedException, IOException {
        Props props = Props.create(LogoutRequestProcessorActor.class);
        ActorRef actor = system.actorOf(props);
        Request request = RequestFactory.getLogoutRequest("id", "i'm bored...");
        actor.tell(new SessionRequest(request, session), ActorRef.noSender());
        TimeUnit.SECONDS.sleep(1);
        Mockito.verify(session, VerificationModeFactory.atLeastOnce()).close();
    }

    protected void truncateFollowTables(){
        AbstractDBHelper.session.execute("truncate followee");
        AbstractDBHelper.session.execute("truncate follower");
    }

}
