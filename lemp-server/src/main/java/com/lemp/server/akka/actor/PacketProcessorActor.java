package com.lemp.server.akka.actor;

import akka.actor.ActorRef;
import akka.actor.UntypedActor;
import com.google.gson.Gson;
import com.lemp.object.Error;
import com.lemp.packet.Datum;
import com.lemp.packet.Message;
import com.lemp.packet.Response;
import com.lemp.packet.ServerReceiptMessage;
import com.lemp.server.akka.LempRouters;
import com.lemp.server.akka.object.ClientMessage;
import com.lemp.server.akka.object.SessionRequest;
import com.lemp.server.database.dbo.User;

import javax.websocket.Session;

/**
 * Created by ayberkcansever on 15/01/17.
 */
public class PacketProcessorActor extends LempActor {

    private Gson gson = new Gson();

    @Override
    public void onReceive(Object msg) throws Throwable {
        if(msg instanceof ClientMessage) {
            try {
                ClientMessage clientMessage = (ClientMessage) msg;
                String message = clientMessage.getMessage();
                Session session = clientMessage.getSession();

                Datum datum = gson.fromJson(message, Datum.class);

                if((session.getUserProperties().get(ActorProperties.USER)) == null) {
                    if(datum.getRq() != null && datum.getRq().getA() != null) {
                        // continue if identity is null && an authentication request came
                    } else {
                        Response errorResponse = new Response();
                        errorResponse.setE(Error.Type.unauthorized);
                        session.getBasicRemote().sendText(gson.toJson(new Datum(errorResponse)));
                        return;
                    }
                }

                if(datum.getRq() != null) {
                    // Authentication Request
                    if(datum.getRq().getA() != null) {
                        LempRouters.getAuthenticationRequestProcessorRouter().tell(new SessionRequest(datum.getRq(), session), ActorRef.noSender());
                        return;
                    }
                    // Logout Request
                    else if(datum.getRq().getLo() != null) {
                        LempRouters.getLogoutRequestProcessorRouter().tell(new SessionRequest(datum.getRq(), session), ActorRef.noSender());
                    }
                    // Following Request
                    else if(datum.getRq().getAf() != null
                            || datum.getRq().getRf() != null
                            || datum.getRq().getUf() != null) {
                        LempRouters.getFollowingRequestRouter().tell(new SessionRequest(datum.getRq(), session), ActorRef.noSender());
                    }
                    // State Request
                    else if(datum.getRq().getS() != null) {
                        datum.getRq().setR(((User) session.getUserProperties().get(ActorProperties.USER)).getUsername());
                        LempRouters.getMessageProcessorRouter().tell(datum.getRq(), ActorRef.noSender());
                    }
                    // Personal Request
                    else if(datum.getRq().getI() != null
                            || datum.getRq().getP() != null
                            || datum.getRq().getSt() != null) {
                        LempRouters.getPersonalRequestRouter().tell(new SessionRequest(datum.getRq(), session), ActorRef.noSender());
                    }
                    // Privacy Request
                    else if(datum.getRq().getB() != null
                            || datum.getRq().getUb() != null) {
                        LempRouters.getPrivacyRequestRouter().tell(new SessionRequest(datum.getRq(), session), ActorRef.noSender());
                    }
                    // Group Request
                    else if(datum.getRq().getG() != null
                            || (datum.getRq().getN() != null && datum.getRq().getN().getI() != null)
                            || datum.getRq().getMt() != null) {
                        LempRouters.getGroupRequestRouter().tell(new SessionRequest(datum.getRq(), session), ActorRef.noSender());
                    }
                    // Broadcast Group Request
                    else if(datum.getRq().getBr() != null
                            || (datum.getRq().getN() != null && datum.getRq().getN().getB() != null)) {
                        LempRouters.getBroadcastGroupRequestRouter().tell(new SessionRequest(datum.getRq(), session), ActorRef.noSender());
                    }
                    // Administrative Request
                    else if(datum.getRq().getAd() != null) {
                        LempRouters.getAdministrativeRequestProcessorRouter().tell(new SessionRequest(datum.getRq(), session), ActorRef.noSender());
                    }
                } else if(datum.getM() != null) {
                    // override the sender with session identity
                    datum.getM().setS(((User) session.getUserProperties().get(ActorProperties.USER)).getUsername());
                    if(datum.getM().getSt() == null) {
                        datum.getM().setSt(System.currentTimeMillis());
                    }
                    // sent server receipt message to the sender
                    if(Message.ExpectType.server_receipt_expected.getKey().equals(datum.getM().getSc())) {
                        ServerReceiptMessage srm = new ServerReceiptMessage(datum.getM());
                        LempRouters.getMessageProcessorRouter().tell(srm, ActorRef.noSender());
                    }
                    LempRouters.getMessageProcessorRouter().tell(datum.getM(), ActorRef.noSender());
                } else if(datum.getSrq() != null) {
                    LempRouters.getServerRequestProcessorRouter().tell(new SessionRequest(datum.getSrq(), session), ActorRef.noSender());
                }
                System.out.println(message + " received from session " + session.getId());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

}
