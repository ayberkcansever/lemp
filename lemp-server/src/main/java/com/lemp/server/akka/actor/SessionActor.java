package com.lemp.server.akka.actor;

import akka.actor.ActorRef;
import akka.cluster.pubsub.DistributedPubSub;
import akka.cluster.pubsub.DistributedPubSubMediator;
import com.google.gson.Gson;
import com.lemp.object.State;
import com.lemp.packet.*;
import com.lemp.server.akka.LempRouters;
import com.lemp.server.akka.event.LoginEvent;
import com.lemp.server.akka.event.LogoutEvent;
import com.lemp.server.database.OfflineMessageDBHelper;
import com.lemp.server.database.dbo.User;

import javax.websocket.Session;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;

/**
 * Created by ayberkcansever on 20/01/17.
 */
public class SessionActor extends LempActor {

    private Gson gson = new Gson();
    private Session session;
    private User user;
    
    public SessionActor(Session session) {
        this.session = session;
        user = ((User) session.getUserProperties().get(ActorProperties.USER));
        ActorRef mediator = DistributedPubSub.get(getContext().system()).mediator();
        mediator.tell(new DistributedPubSubMediator.Put(getSelf()), getSelf());
    }

    @Override
    public void onReceive(Object msg) throws Throwable {
        if(msg instanceof Message) {
            session.getBasicRemote().sendText(gson.toJson(new Datum((Message) msg)));
        } else if(msg instanceof Request) {
            Request req = (Request) msg;
            if(req.getS() != null) {
                Response response = new Response();
                response.setId(req.getId());
                response.setR(req.getR());
                State state = new State();
                state.setU(((User) session.getUserProperties().get(ActorProperties.USER)).getUsername());
                state.setV(0);
                response.setS(state);
                LempRouters.getMessageProcessorRouter().tell(response, ActorRef.noSender());
            }
        } else if(msg instanceof Response) {
            Response response = (Response) msg;
            if(response.getS() != null) {
                session.getBasicRemote().sendText(gson.toJson(new Datum(response)));
            }
        } else if(msg instanceof Information) {
            session.getBasicRemote().sendText(gson.toJson(new Datum((Information) msg)));
        }
    }

    @Override
    public void preStart() {
        System.out.println(user.getUsername() + " starting...");
        deliverOfflineMessages();
        LempRouters.getEventHandlerRouter().tell(new LoginEvent(user.getUsername()), ActorRef.noSender());
    }

    @Override
    public void postStop() {
        System.out.println(user.getUsername() + " stopped...");
        LempRouters.getEventHandlerRouter().tell(new LogoutEvent(user.getUsername()), ActorRef.noSender());
    }

    private void deliverOfflineMessages() {
        try {
            List<String> offlineMessages = OfflineMessageDBHelper.getInstance()
                                                                 .getOfflineMessagesAsString(((User) session.getUserProperties().get(ActorProperties.USER)).getUsername(), true);
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
