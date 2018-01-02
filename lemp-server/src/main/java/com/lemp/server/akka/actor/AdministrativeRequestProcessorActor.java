package com.lemp.server.akka.actor;

import com.google.gson.Gson;
import com.lemp.object.Administrative;
import com.lemp.object.Error;
import com.lemp.packet.Datum;
import com.lemp.packet.Request;
import com.lemp.packet.Response;
import com.lemp.server.akka.object.SessionRequest;
import com.lemp.server.database.UserDBHelper;
import com.lemp.server.database.dbo.User;

import javax.websocket.Session;

/**
 * Created by ayberkcansever on 15/01/17.
 */
public class AdministrativeRequestProcessorActor extends LempActor {

    private Gson gson = new Gson();

    @Override
    public void onReceive(Object msg) throws Throwable {
        if(msg instanceof SessionRequest) {
            SessionRequest sessionRequest = (SessionRequest) msg;
            Request request = sessionRequest.getRequest();
            Session session = sessionRequest.getSession();
            User requesterUser = (User) session.getUserProperties().get(ActorProperties.USER);
            if(User.Type.admin.getKey() != requesterUser.getUserType()) {
                session.getBasicRemote().sendText(gson.toJson(new Datum(new Response(Error.Type.forbidden))));
            } else {
                Administrative administrative = request.getAd();
                try {
                    switch (Administrative.Command.getByKey(request.getAd().getC())) {
                        case create:
                            String username = administrative.getI();
                            String password = administrative.getT();
                            int userType = administrative.getTy();
                            UserDBHelper.getInstance().insertUser(username, password, userType);
                            session.getBasicRemote().sendText(gson.toJson(new Datum(new Response(request.getId()))));
                            break;
                        case delete:
                            UserDBHelper.getInstance().deleteUser(administrative.getI());
                            session.getBasicRemote().sendText(gson.toJson(new Datum(new Response(request.getId()))));
                            break;
                        default:
                            session.getBasicRemote().sendText(gson.toJson(new Datum(new Response(request.getId(), Error.Type.bad_request))));
                            break;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    session.getBasicRemote().sendText(gson.toJson(new Datum(new Response(request.getId(), Error.Type.internal_server_error))));
                }
            }
        }

    }

}
