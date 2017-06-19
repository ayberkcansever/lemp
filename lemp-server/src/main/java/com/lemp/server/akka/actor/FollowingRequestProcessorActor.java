package com.lemp.server.akka.actor;

import akka.actor.UntypedActor;
import com.google.gson.Gson;
import com.lemp.object.Error;
import com.lemp.object.User;
import com.lemp.packet.Datum;
import com.lemp.packet.Request;
import com.lemp.packet.Response;
import com.lemp.server.akka.object.SessionRequest;
import com.lemp.server.database.FollowerDBHelper;
import com.lemp.server.database.dbo.Followee;

import javax.websocket.Session;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by ayberkcansever on 15/01/17.
 */
public class FollowingRequestProcessorActor extends UntypedActor {

    private Gson gson = new Gson();

    @Override
    public void onReceive(Object msg) throws Throwable {
        if(msg instanceof SessionRequest) {
            SessionRequest sessionRequest = (SessionRequest) msg;
            Request request = sessionRequest.getRequest();
            Session session = sessionRequest.getSession();
            String username = (String) session.getUserProperties().get(ActorProperties.IDENTITY_KEY);
            try {
                // Add new following
                if(request.getAf() != null) {
                    List<com.lemp.object.User> userList = request.getAf();
                    List<Followee> followeeList = userList.stream()
                            .map(u -> new Followee(username, u.getU(), u.getN())).collect(Collectors.toList());
                    FollowerDBHelper.getInstance().insertFolloweeList(username, followeeList);
                }
                // Remove following
                else if(request.getRf() != null) {
                    FollowerDBHelper.getInstance().deleteUserFollowee(username, request.getRf());
                }
                // Update following
                else if(request.getUf() != null) {
                    for(User user : request.getUf()) {
                        FollowerDBHelper.getInstance().updateFollowee(username, user.getU(), user.getN());
                    }
                }
                session.getBasicRemote().sendText(gson.toJson(new Datum(new Response())));
            } catch (Exception e) {
                e.printStackTrace();
                Response response = new Response(Error.Type.internal_server_error);
                session.getBasicRemote().sendText(gson.toJson(new Datum(response)));
            }
        }

    }

}
