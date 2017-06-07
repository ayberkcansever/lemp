package com.lemp.server.akka.actor;

import akka.actor.UntypedActor;
import com.google.gson.Gson;
import com.lemp.object.Error;
import com.lemp.packet.Request;
import com.lemp.packet.Response;
import com.lemp.server.akka.object.SessionRequest;
import com.lemp.server.database.FollowerDBHelper;
import com.lemp.server.database.dbo.Followee;
import com.lemp.server.database.dbo.Follower;

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
            String username = (String) session.getUserProperties().get("identity");
            try {
                // todo: fill them
                // Register user's followees
                if(request.getF() != null) {
                    List<com.lemp.object.User> userList = request.getF();
                    List<Followee> followeeList = userList.stream()
                            .map(u -> new Followee(username, u.getU(), u.getN())).collect(Collectors.toList());
                    List<Followee> insertedFolloweeList = FollowerDBHelper.getInstance().insertFolloweeList(username, followeeList);
                    for(Followee followee : insertedFolloweeList) {
                        FollowerDBHelper.getInstance().insertFollower(new Follower(followee.getFollowee(), followee.getFollower(), followee.getNick()));
                    }
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
                Response response = new Response(Error.Type.internal_server_error);
                session.getBasicRemote().sendText(gson.toJson(response));
            }
        }

    }

}
