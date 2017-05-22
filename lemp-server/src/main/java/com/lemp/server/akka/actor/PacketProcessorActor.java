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
                    // Authentication Request
                    if(datum.getRq().getA() != null) {
                        LempRouters.authenticationRequestProcessorRouter.tell(new SessionRequest(datum.getRq(), session), ActorRef.noSender());
                    }
                    // Following Request
                    else if(datum.getRq().getF() != null
                            || datum.getRq().getAf() != null
                            || datum.getRq().getRf() != null
                            || datum.getRq().getUf() != null) {
                        LempRouters.followingRequestRouter.tell(new SessionRequest(datum.getRq(), session), ActorRef.noSender());
                    }
                    // State Request
                    else if(datum.getRq().getS() != null){
                        LempRouters.stateRequestRouter.tell(new SessionRequest(datum.getRq(), session), ActorRef.noSender());
                    }
                    // Personal Request
                    else if(datum.getRq().getI() != null
                            || datum.getRq().getP() != null
                            || datum.getRq().getSt() != null) {
                        LempRouters.personalRequestRouter.tell(new SessionRequest(datum.getRq(), session), ActorRef.noSender());
                    }
                    // Privacy Request
                    else if(datum.getRq().getB() != null
                            || datum.getRq().getUb() != null) {
                        LempRouters.privacyRequestRouter.tell(new SessionRequest(datum.getRq(), session), ActorRef.noSender());
                    }
                    // Group Request
                    else if(datum.getRq().getG() != null
                            || (datum.getRq().getN() != null && datum.getRq().getN().getI() != null)
                            || datum.getRq().getMt() != null) {
                        LempRouters.groupRequestRouter.tell(new SessionRequest(datum.getRq(), session), ActorRef.noSender());
                    }
                    // Broadcast Group Request
                    else if(datum.getRq().getBr() != null
                            || (datum.getRq().getN() != null && datum.getRq().getN().getB() != null)) {
                        LempRouters.broadcastGroupRequestRouter.tell(new SessionRequest(datum.getRq(), session), ActorRef.noSender());
                    }
                } else if(datum.getM() != null) {
                    // override the sender with session identity
                    datum.getM().setS((String) session.getUserProperties().get("identity"));
                    LempRouters.messageProcessorRouter.tell(datum.getM(), ActorRef.noSender());
                }
                System.out.println(message + " received from session " + session.getId());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

}
