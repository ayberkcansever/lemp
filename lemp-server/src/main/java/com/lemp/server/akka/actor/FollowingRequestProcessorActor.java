package com.lemp.server.akka.actor;

import akka.actor.UntypedActor;
import com.google.gson.Gson;
import com.lemp.packet.Request;
import com.lemp.packet.Response;
import com.lemp.server.akka.object.SessionRequest;

import javax.websocket.Session;

/**
 * Created by ayberkcansever on 15/01/17.
 */
public class FollowingRequestProcessorActor extends UntypedActor {

    private Gson gson = new Gson();

    @Override
    public void onReceive(Object msg) throws Throwable {
        if(msg instanceof SessionRequest) {
            try {
                SessionRequest sessionRequest = (SessionRequest) msg;
                Request request = sessionRequest.getRequest();
                Session session = sessionRequest.getSession();

                // todo: fill them
                // Register user's followings
                if(request.getF() != null) {

                }
                // Add new following
                else if(request.getAf() != null) {

                }
                // Remove following
                else if(request.getRf() != null) {

                }
                // Update following
                else if(request.getUf() != null) {

                }

                Response response = new Response();
                session.getBasicRemote().sendText(gson.toJson(response));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

}
