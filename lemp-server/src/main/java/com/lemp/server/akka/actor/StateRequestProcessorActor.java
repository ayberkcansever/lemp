package com.lemp.server.akka.actor;

import akka.actor.UntypedActor;
import com.google.gson.Gson;
import com.lemp.object.State;
import com.lemp.packet.Datum;
import com.lemp.packet.Request;
import com.lemp.packet.Response;
import com.lemp.server.akka.object.SessionRequest;
import com.lemp.server.database.StateDBHelper;

import javax.websocket.Session;

/**
 * Created by ayberkcansever on 15/01/17.
 */
public class StateRequestProcessorActor extends UntypedActor {

    private Gson gson = new Gson();

    @Override
    public void onReceive(Object msg) throws Throwable {
        if(msg instanceof SessionRequest) {
            try {
                SessionRequest sessionRequest = (SessionRequest) msg;
                Request request = sessionRequest.getRequest();
                Session session = sessionRequest.getSession();

                String username = request.getS().getU();
                Response response = new Response();
                response.setId(((SessionRequest) msg).getRequest().getId());
                State state = new State();
                state.setU(username);
                state.setV(StateDBHelper.getInstance().getState(username));
                response.setS(state);
                
                session.getBasicRemote().sendText(gson.toJson(new Datum(response)));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

}
