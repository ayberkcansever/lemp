package com.lemp.server;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import com.google.gson.Gson;
import com.lemp.packet.Request;
import com.lemp.packet.Response;
import com.lemp.server.akka.actor.AuthenticationRequestProcessorActor;
import com.lemp.server.akka.object.SessionRequest;
import com.lemp.server.database.AbstractDBHelper;
import org.mockito.Mockito;

import javax.websocket.RemoteEndpoint;
import javax.websocket.Session;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

/**
 * Created by AyberkC on 09.06.2017.
 */
public abstract class BaseTest {

    public static ActorSystem system;

    protected static final String testUsername = "testuser1";
    protected static final String testUserPassword = "1";

    protected void authenticateUser(Session session, RemoteEndpoint.Basic remote,
                                  String username, String password) throws InterruptedException, IOException {
        Props authProps = Props.create(AuthenticationRequestProcessorActor.class);
        ActorRef authActor = system.actorOf(authProps);
        Request authRequest = RequestFactory.getAuthenticationReqeust("id", username, password);
        authActor.tell(new SessionRequest(authRequest, session), ActorRef.noSender());
        TimeUnit.SECONDS.sleep(1);
        Response response = new Response();
        response.setId(authRequest.getId());
        response.setResult(1);
        Mockito.verify(remote).sendText(new Gson().toJson(response));
    }

    protected void truncateFollowTables(){
        AbstractDBHelper.session.execute("truncate followee");
        AbstractDBHelper.session.execute("truncate follower");
    }

}
