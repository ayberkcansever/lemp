package com.lemp.server.akka.actor;

import com.google.gson.Gson;
import com.lemp.object.Group;
import com.lemp.packet.Datum;
import com.lemp.packet.Request;
import com.lemp.packet.Response;
import com.lemp.server.akka.object.SessionRequest;
import com.lemp.server.database.dbo.User;

import javax.websocket.Session;

/**
 * Created by ayberkcansever on 15/01/17.
 */
public class GroupRequestProcessorActor extends LempActor {

    private Gson gson = new Gson();

    /*@Override
    public void onReceive(Object msg) throws Throwable {
        if(msg instanceof SessionRequest) {
            try {
                SessionRequest sessionRequest = (SessionRequest) msg;
                Request request = sessionRequest.getRequest();
                Session session = sessionRequest.getSession();
                String username = ((User) session.getUserProperties().get(ActorProperties.USER)).getUsername();

                // todo: fill them
                // Create Group Request
                switch(Group.RequestType.getByKey(request.getG().getT())) {
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

                session.getBasicRemote().sendText(gson.toJson(new Datum(new Response())));
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
                        SessionRequest sessionRequest = msg;
                        Request request = sessionRequest.getRequest();
                        Session session = sessionRequest.getSession();
                        String username = ((User) session.getUserProperties().get(ActorProperties.USER)).getUsername();

                        // todo: fill them
                        // Create Group Request
                        switch(Group.RequestType.getByKey(request.getG().getT())) {
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

                        session.getBasicRemote().sendText(gson.toJson(new Datum(new Response())));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                })
                .build();
    }

}
