package com.lemp.server.akka.actor;

import akka.actor.ActorRef;
import akka.actor.UntypedActor;
import akka.cluster.pubsub.DistributedPubSub;
import akka.cluster.pubsub.DistributedPubSubMediator;
import com.google.gson.Gson;
import com.lemp.packet.Message;

import javax.websocket.Session;

/**
 * Created by ayberkcansever on 20/01/17.
 */
public class SessionActor extends UntypedActor {

    private Gson gson = new Gson();
    private Session session;

    public SessionActor(Session session) {
        this.session = session;
        ActorRef mediator = DistributedPubSub.get(getContext().system()).mediator();
        mediator.tell(new DistributedPubSubMediator.Put(getSelf()), getSelf());
    }

    @Override
    public void onReceive(Object message) throws Throwable {
        if(message instanceof Message) {
            Message msg = (Message) message;
            if("text".equals(msg.getT())) {
                session.getBasicRemote().sendText(gson.toJson(message));
            }
        }

    }

    @Override
    public void preStart() {
        System.out.println(session.getUserProperties().get("identity") + " starting...");
    }

    @Override
    public void postStop() {
        System.out.println(session.getUserProperties().get("identity") + " stopped...");
    }
}
