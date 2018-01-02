package com.lemp.server.akka.actor;

import akka.actor.ActorRef;
import akka.actor.InvalidActorNameException;
import akka.actor.PoisonPill;
import akka.actor.Props;
import akka.cluster.pubsub.DistributedPubSub;
import akka.cluster.pubsub.DistributedPubSubMediator;
import com.google.gson.Gson;
import com.lemp.object.Authentication;
import com.lemp.packet.Datum;
import com.lemp.packet.Request;
import com.lemp.packet.Response;
import com.lemp.server.Application;
import com.lemp.server.akka.object.SessionRequest;
import com.lemp.server.database.UserDBHelper;
import com.lemp.server.database.dbo.User;

import javax.websocket.Session;
import java.util.concurrent.TimeUnit;

/**
 * Created by ayberkcansever on 15/01/17.
 */
public class AuthenticationRequestProcessorActor extends LempActor {

    private Gson gson = new Gson();

    @Override
    public void onReceive(Object msg) throws Throwable {
        if(msg instanceof SessionRequest) {
            try {
                SessionRequest sessionRequest = (SessionRequest) msg;
                Request request = sessionRequest.getRequest();
                Session session = sessionRequest.getSession();
                Authentication authentication = request.getA();
                String identity = authentication.getI();
                String token = authentication.getT();

                Response response = new Response();
                response.setId(request.getId());
                User user = UserDBHelper.getInstance().isAuthenticatedUser(identity, token);
                response.setResult(user != null ? 1 : 0);
                if(response.getResult() == 1) {
                    session.getUserProperties().put(ActorProperties.USER, user);
                    try {
                        getContext().system().actorOf(Props.create(SessionActor.class, session), identity);
                    } catch (InvalidActorNameException ex) {
                        DistributedPubSub.get(Application.getActorSystem()).mediator().tell(new DistributedPubSubMediator.Send("/user/" + identity, PoisonPill.getInstance(), false), ActorRef.noSender());
                        TimeUnit.SECONDS.sleep(2);
                        getContext().system().actorOf(Props.create(SessionActor.class, session), identity);
                    }
                }
                session.getBasicRemote().sendText(gson.toJson(new Datum(response)));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

}
