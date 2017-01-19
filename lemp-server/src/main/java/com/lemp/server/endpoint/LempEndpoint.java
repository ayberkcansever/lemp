package com.lemp.server.endpoint;

import akka.actor.ActorRef;
import com.lemp.server.akka.LempRouters;
import com.lemp.server.akka.object.ClientMessage;
import org.glassfish.tyrus.core.wsadl.model.Endpoint;

import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;


/**
 * Created by ayberkcansever on 13/01/17.
 */
@ServerEndpoint(value = "/lemp")
public class LempEndpoint extends Endpoint {

    @OnOpen
    public void onOpen(Session session, EndpointConfig config) {
        System.out.println(session.getId() + " opened.");
    }

    @OnClose
    public void onClose(Session session, CloseReason reason) {
        System.out.println(session.getId() + " closed.");
    }

    @OnError
    public void onError(Session session, Throwable t) {
        t.printStackTrace();
        System.out.println(session.getId() + " error.");
    }

    @OnMessage
    public void onMessage(String message, Session session) {
        LempRouters.packetProcessorRouter.tell(new ClientMessage(message, session), ActorRef.noSender());
    }

}
