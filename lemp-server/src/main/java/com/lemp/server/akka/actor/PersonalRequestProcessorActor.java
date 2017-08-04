package com.lemp.server.akka.actor;

import akka.actor.UntypedActor;
import com.google.gson.Gson;
import com.lemp.object.Error;
import com.lemp.object.Picture;
import com.lemp.object.Status;
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
public class PersonalRequestProcessorActor extends UntypedActor {

    private Gson gson = new Gson();

    @Override
    public void onReceive(Object msg) throws Throwable {
        if(msg instanceof SessionRequest) {
            try {
                SessionRequest sessionRequest = (SessionRequest) msg;
                Request request = sessionRequest.getRequest();
                Session session = sessionRequest.getSession();
                String username = ((User) session.getUserProperties().get(ActorProperties.USER)).getUsername();
                Response response = new Response(request.getId());

                // Personal Information Request
                if(request.getI() != null) {

                }
                // Picture Requests
                else if(request.getP() != null) {
                    if(request.getP().getT().equals(Request.Type.set.getKey())) {
                        String picUrl = request.getP().getV();
                        UserDBHelper.getInstance().updateUserPicUrl(username, picUrl);
                    } else if(request.getP().getT().equals(Request.Type.get.getKey())) {
                        String requestedUsername = request.getP().getU();
                        User requestedUser = UserDBHelper.getInstance().getUser(requestedUsername);
                        if(requestedUser != null) {
                            Picture picture = new Picture();
                            picture.setU(requestedUsername);
                            picture.setV(requestedUser.getPicUrl());
                            response.setP(picture);
                        } else {
                            response.setE(Error.Type.not_found);
                        }
                    }
                }
                // Status Requests
                else if(request.getSt() != null) {
                    if(request.getSt().getT().equals(Request.Type.set.getKey())) {
                        String status = request.getSt().getS();
                        UserDBHelper.getInstance().updateUserStatus(username, status);
                    } else if(request.getSt().getT().equals(Request.Type.get.getKey())) {
                        String requestedUsername = request.getSt().getU();
                        User requestedUser = UserDBHelper.getInstance().getUser(requestedUsername);
                        if(requestedUser != null) {
                            Status status = new Status();
                            status.setU(requestedUsername);
                            status.setS(requestedUser.getStatus());
                            response.setSt(status);
                        } else {
                            response.setE(Error.Type.not_found);
                        }
                    }
                }

                session.getBasicRemote().sendText(gson.toJson(new Datum(response)));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

}
