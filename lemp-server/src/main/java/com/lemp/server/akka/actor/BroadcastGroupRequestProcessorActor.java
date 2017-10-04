package com.lemp.server.akka.actor;

import akka.actor.UntypedActor;
import com.google.gson.Gson;
import com.lemp.object.Broadcast;
import com.lemp.packet.Datum;
import com.lemp.packet.Request;
import com.lemp.packet.Response;
import com.lemp.server.akka.object.SessionRequest;

import javax.websocket.Session;

/**
 * Created by ayberkcansever on 15/01/17.
 */
public class BroadcastGroupRequestProcessorActor extends LempActor {

    private Gson gson = new Gson();

    @Override
    public void onReceive(Object msg) throws Throwable {
        if(msg instanceof SessionRequest) {
            try {
                SessionRequest sessionRequest = (SessionRequest) msg;
                Request request = sessionRequest.getRequest();
                Session session = sessionRequest.getSession();

                Response response = new Response();

                // Create Group Request
                if(request.getBr().getT().equals(Broadcast.RequestType.create.getKey())) {

                }
                // Add user to group Request
                else if(request.getBr().getT().equals(Broadcast.RequestType.add.getKey())) {

                }
                // Banish user from group Request
                else if(request.getBr().getT().equals(Broadcast.RequestType.banish.getKey())) {

                }
                // Terminate group Request
                else if(request.getBr().getT().equals(Broadcast.RequestType.terminate.getKey())) {

                }
                // Group Information Request
                else if(request.getBr().getT().equals(Broadcast.RequestType.information.getKey())) {

                }
                // Name Request
                else if(request.getN() != null) {
                    if(request.getN().getT().equals(Request.Type.set.getKey())) {

                    } else if(request.getN().getT().equals(Request.Type.get.getKey())) {

                    }
                }

                session.getBasicRemote().sendText(gson.toJson(new Datum(response)));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

}
