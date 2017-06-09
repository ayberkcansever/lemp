package com.lemp.server.akka.actor;

import akka.actor.UntypedActor;
import com.google.gson.Gson;
import com.lemp.object.Group;
import com.lemp.packet.Request;
import com.lemp.packet.Response;
import com.lemp.server.akka.object.SessionRequest;

import javax.websocket.Session;

/**
 * Created by ayberkcansever on 15/01/17.
 */
public class GroupRequestProcessorActor extends UntypedActor {

    private Gson gson = new Gson();

    @Override
    public void onReceive(Object msg) throws Throwable {
        if(msg instanceof SessionRequest) {
            try {
                SessionRequest sessionRequest = (SessionRequest) msg;
                Request request = sessionRequest.getRequest();
                Session session = sessionRequest.getSession();

                // todo: fill them
                // Create Group Request
                switch(Group.RequestType.valueOf(request.getG().getT())) {
                    case create:
                        break;
                    case add:
                        break;
                    case banish:
                        break;
                    case leave:
                        break;
                    case terminate:
                        break;
                    case information:
                        break;
                    case add_admin:
                        break;
                    case get_admins:
                        break;
                    default:
                        break;
                }

                // Name Request
                if(request.getN() != null) {
                    if(request.getN().getT().equals(Request.Type.set.getKey())) {

                    } else if(request.getN().getT().equals(Request.Type.get.getKey())) {

                    }
                }
                // Mute Request
                else if(request.getMt() != null) {

                }
                // Unmute Request
                else if(request.getUmt() != null) {

                }

                Response response = new Response();
                session.getBasicRemote().sendText(gson.toJson(response));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

}
