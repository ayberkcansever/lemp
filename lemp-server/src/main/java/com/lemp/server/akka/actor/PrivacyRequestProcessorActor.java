package com.lemp.server.akka.actor;

import com.google.gson.Gson;
import com.lemp.packet.Datum;
import com.lemp.packet.Request;
import com.lemp.packet.Response;
import com.lemp.server.akka.object.SessionRequest;
import com.lemp.server.database.PrivacyDBHelper;
import com.lemp.server.database.dbo.User;

import javax.websocket.Session;

/**
 * Created by ayberkcansever on 15/01/17.
 */
public class PrivacyRequestProcessorActor extends LempActor {

    private Gson gson = new Gson();

    /*@Override
    public void onReceive(Object msg) throws Throwable {
        if(msg instanceof SessionRequest) {
            try {
                SessionRequest sessionRequest = (SessionRequest) msg;
                Request request = sessionRequest.getRequest();
                Session session = sessionRequest.getSession();
                String username = ((User) session.getUserProperties().get(ActorProperties.USER)).getUsername();
                Response response = new Response(request.getId());

                // Block Request
                if(request.getB() != null) {
                    String bannedUsername = request.getB().getUsername();
                    PrivacyDBHelper.getInstance().addPrivacy(username, bannedUsername);
                }
                // Unblock Request
                else if(request.getUb() != null) {
                    String unblockedUsername = request.getUb().getUsername();
                    PrivacyDBHelper.getInstance().removePrivacy(username, unblockedUsername);
                } else if(request.getGet() != null) {
                    response.setPr(PrivacyDBHelper.getInstance().getPrivacySet(username));
                }

                session.getBasicRemote().sendText(gson.toJson(new Datum(response)));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }*/

    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(SessionRequest.class, msg -> {
                    try {
                        SessionRequest sessionRequest = (SessionRequest) msg;
                        Request request = sessionRequest.getRequest();
                        Session session = sessionRequest.getSession();
                        String username = ((User) session.getUserProperties().get(ActorProperties.USER)).getUsername();
                        Response response = new Response(request.getId());

                        // Block Request
                        if(request.getB() != null) {
                            String bannedUsername = request.getB().getUsername();
                            PrivacyDBHelper.getInstance().addPrivacy(username, bannedUsername);
                        }
                        // Unblock Request
                        else if(request.getUb() != null) {
                            String unblockedUsername = request.getUb().getUsername();
                            PrivacyDBHelper.getInstance().removePrivacy(username, unblockedUsername);
                        } else if(request.getGet() != null) {
                            response.setPr(PrivacyDBHelper.getInstance().getPrivacySet(username));
                        }

                        session.getBasicRemote().sendText(gson.toJson(new Datum(response)));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                })
                .build();
    }

}
