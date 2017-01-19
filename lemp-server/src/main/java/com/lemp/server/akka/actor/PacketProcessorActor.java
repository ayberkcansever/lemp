package com.lemp.server.akka.actor;

import akka.actor.ActorRef;
import akka.actor.UntypedActor;
import com.google.gson.Gson;
import com.lemp.packet.Datum;
import com.lemp.server.akka.LempRouters;
import com.lemp.server.akka.object.ClientMessage;
import com.lemp.server.akka.object.SessionRequest;

import javax.websocket.Session;

/**
 * Created by ayberkcansever on 15/01/17.
 */
public class PacketProcessorActor extends UntypedActor {

    private Gson gson = new Gson();

    @Override
    public void onReceive(Object msg) throws Throwable {
        if(msg instanceof ClientMessage) {
            try {
                ClientMessage clientMessage = (ClientMessage) msg;
                String message = clientMessage.getMessage();
                Session session = clientMessage.getSession();
                Datum datum = gson.fromJson(message, Datum.class);
                if(datum.getRq() != null) {
                    if(datum.getRq().getA() != null) {
                        LempRouters.authenticationRequestProcessorRouter.tell(new SessionRequest(datum.getRq(), session), ActorRef.noSender());
                    }

                }


                System.out.println(message + " received from session " + session.getId());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

}
