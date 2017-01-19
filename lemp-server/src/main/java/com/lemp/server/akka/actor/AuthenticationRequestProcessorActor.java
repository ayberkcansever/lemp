package com.lemp.server.akka.actor;

import akka.actor.UntypedActor;
import com.google.gson.Gson;
import com.lemp.object.Authentication;
import com.lemp.packet.Request;
import com.lemp.packet.Response;
import com.lemp.server.akka.object.SessionRequest;
import com.lemp.server.database.DBHelper;

import javax.websocket.Session;

/**
 * Created by ayberkcansever on 15/01/17.
 */
public class AuthenticationRequestProcessorActor extends UntypedActor {

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
                response.setResult(DBHelper.isAuthenticated(identity, token) ? 1 : 0);
                session.getBasicRemote().sendText(gson.toJson(response));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

}
