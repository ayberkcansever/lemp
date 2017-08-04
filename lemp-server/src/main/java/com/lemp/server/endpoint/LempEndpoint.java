package com.lemp.server.endpoint;

import akka.actor.ActorRef;
import akka.actor.PoisonPill;
import akka.cluster.pubsub.DistributedPubSub;
import akka.cluster.pubsub.DistributedPubSubMediator;
import com.lemp.server.Application;
import com.lemp.server.akka.LempRouters;
import com.lemp.server.akka.actor.ActorProperties;
import com.lemp.server.akka.object.ClientMessage;
import com.lemp.server.database.StateDBHelper;
import com.lemp.server.database.dbo.User;
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
        try {
            String identity = ((User) session.getUserProperties().get(ActorProperties.USER)).getUsername();
            DistributedPubSub.get(Application.getActorSystem()).mediator().tell(new DistributedPubSubMediator.Send("/user/" + identity, PoisonPill.getInstance(), false), ActorRef.noSender());
            StateDBHelper.getInstance().updateState(identity);
            System.out.println(session.getId() + " " + identity + " closed.");
        } catch (Exception e) {

        }
    }

    @OnError
    public void onError(Session session, Throwable t) {
        t.printStackTrace();
        System.out.println(session.getId() + " error.");
    }

    @OnMessage
    public void onMessage(String message, Session session) {
        LempRouters.getPacketProcessorRouter().tell(new ClientMessage(message, session), ActorRef.noSender());
    }

}
