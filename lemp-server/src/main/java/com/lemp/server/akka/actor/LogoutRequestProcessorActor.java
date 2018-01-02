package com.lemp.server.akka.actor;

import akka.actor.ActorRef;
import akka.actor.PoisonPill;
import akka.cluster.pubsub.DistributedPubSub;
import akka.cluster.pubsub.DistributedPubSubMediator;
import com.lemp.server.akka.object.SessionRequest;
import com.lemp.server.database.dbo.User;

import javax.websocket.Session;

/**
 * Created by ayberkcansever on 15/01/17.
 */
public class LogoutRequestProcessorActor extends LempActor {

    private ActorRef mediator;

    public LogoutRequestProcessorActor() {
        mediator = DistributedPubSub.get(getContext().system()).mediator();
    }

    @Override
    public void onReceive(Object msg) throws Throwable {
        if(msg instanceof SessionRequest) {
            try {
                SessionRequest sessionRequest = (SessionRequest) msg;
                Session session = sessionRequest.getSession();
                String username = ((User) session.getUserProperties().get(ActorProperties.USER)).getUsername();
                mediator.tell(new DistributedPubSubMediator.Send("/user/" + username, PoisonPill.getInstance(), false), ActorRef.noSender());
                session.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

}
