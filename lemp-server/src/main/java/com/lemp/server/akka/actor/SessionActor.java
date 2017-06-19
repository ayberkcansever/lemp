package com.lemp.server.akka.actor;

import akka.actor.ActorRef;
import akka.actor.UntypedActor;
import akka.cluster.pubsub.DistributedPubSub;
import akka.cluster.pubsub.DistributedPubSubMediator;
import com.google.gson.Gson;
import com.lemp.packet.Datum;
import com.lemp.packet.Message;
import com.lemp.server.database.OfflineMessageDBHelper;

import javax.websocket.Session;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;

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
    public void onReceive(Object msg) throws Throwable {
        if(msg instanceof Message) {
            session.getBasicRemote().sendText(gson.toJson(new Datum((Message) msg)));
        }
    }

    @Override
    public void preStart() {
        System.out.println(session.getUserProperties().get(ActorProperties.IDENTITY_KEY) + " starting...");
        deliverOfflineMessages();
    }

    @Override
    public void postStop() {
        System.out.println(session.getUserProperties().get(ActorProperties.IDENTITY_KEY) + " stopped...");
    }

    private void deliverOfflineMessages() {
        try {
            List<String> offlineMessages = OfflineMessageDBHelper.getInstance()
                                                                 .getOfflineMessagesAsString((String) session.getUserProperties().get(ActorProperties.IDENTITY_KEY), true);
            Iterator<String> iterator = offlineMessages.iterator();
            while(iterator.hasNext()) {
                String message = iterator.next();
                try {
                    session.getBasicRemote().sendText(message);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
