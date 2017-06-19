package com.lemp.server.akka.actor;

import akka.actor.UntypedActor;
import com.google.gson.Gson;
import com.lemp.packet.Datum;
import com.lemp.packet.Request;
import com.lemp.packet.Response;
import com.lemp.server.akka.object.SessionRequest;

import javax.websocket.Session;

/**
 * Created by ayberkcansever on 15/01/17.
 */
public class PersonalRequestProcessorActor extends UntypedActor {

    private Gson gson = new Gson();

    @Override
    public void onReceive(Object msg) throws Throwable {
        if(msg instanceof SessionRequest) {
            try {
                SessionRequest sessionRequest = (SessionRequest) msg;
                Request request = sessionRequest.getRequest();
                Session session = sessionRequest.getSession();

                // todo: fill them
                // Personal Information Request
                if(request.getI() != null) {

                }
                // Picture Requests
                else if(request.getP() != null) {
                    if(request.getP().getT().equals(Request.Type.set.getKey())) {

                    } else if(request.getP().getT().equals(Request.Type.get.getKey())) {

                    }
                }
                // Status Requests
                else if(request.getSt() != null) {
                    if(request.getSt().getT().equals(Request.Type.set.getKey())) {

                    } else if(request.getSt().getT().equals(Request.Type.get.getKey())) {

                    }
                }

                Response response = new Response();
                session.getBasicRemote().sendText(gson.toJson(new Datum(response)));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

}
