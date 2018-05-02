package com.lemp.server.akka.actor;

import com.google.gson.Gson;
import com.lemp.packet.Datum;
import com.lemp.packet.ServerRequest;
import com.lemp.packet.ServerResponse;
import com.lemp.server.akka.object.SessionRequest;

import javax.websocket.Session;
import java.util.TimeZone;

/**
 * Created by ayberkcansever on 15/01/17.
 */
public class ServerRequestProcessorActor extends LempActor {

    private Gson gson = new Gson();

    /*@Override
    public void onReceive(Object msg) throws Throwable {
        if(msg instanceof SessionRequest) {
            try {
                SessionRequest sessionRequest = (SessionRequest) msg;
                ServerRequest serverRequest = sessionRequest.getServerRequest();
                Session session = sessionRequest.getSession();

                switch (ServerRequest.Type.getByKey(serverRequest.getT())) {
                    case time:
                        ServerResponse serverTimeResponse = new ServerResponse(serverRequest);
                        serverTimeResponse.setT(ServerRequest.Type.time.getKey());
                        serverTimeResponse.setTm(System.currentTimeMillis());
                        serverTimeResponse.setO(TimeZone.getDefault().getRawOffset() / (3600 * 1000));
                        session.getBasicRemote().sendText(gson.toJson(new Datum(serverTimeResponse)));
                        break;
                    case knock_kncok:
                        ServerResponse serverResponse = new ServerResponse(serverRequest);
                        serverResponse.setT(ServerRequest.Type.knock_kncok.getKey());
                        session.getBasicRemote().sendText(gson.toJson(new Datum(serverResponse)));
                        break;
                    default:
                        break;
                }

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
                        ServerRequest serverRequest = sessionRequest.getServerRequest();
                        Session session = sessionRequest.getSession();

                        switch (ServerRequest.Type.getByKey(serverRequest.getT())) {
                            case time:
                                ServerResponse serverTimeResponse = new ServerResponse(serverRequest);
                                serverTimeResponse.setT(ServerRequest.Type.time.getKey());
                                serverTimeResponse.setTm(System.currentTimeMillis());
                                serverTimeResponse.setO(TimeZone.getDefault().getRawOffset() / (3600 * 1000));
                                session.getBasicRemote().sendText(gson.toJson(new Datum(serverTimeResponse)));
                                break;
                            case knock_kncok:
                                ServerResponse serverResponse = new ServerResponse(serverRequest);
                                serverResponse.setT(ServerRequest.Type.knock_kncok.getKey());
                                session.getBasicRemote().sendText(gson.toJson(new Datum(serverResponse)));
                                break;
                            default:
                                break;
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                })
                .build();
    }

}
