package com.lemp.server.endpoint;

import com.google.gson.Gson;
import com.lemp.packet.Datum;
import org.glassfish.tyrus.core.wsadl.model.Endpoint;

import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;


/**
 * Created by ayberkcansever on 13/01/17.
 */
@ServerEndpoint(value = "/lemp")
public class LempEndpoint extends Endpoint {

    @OnOpen
    public void onOpen(Session session, EndpointConfig config) {
        System.out.println(session.getId() + " opened.");
    }

    @OnClose
    public void onClose(Session session, CloseReason reason) {
        System.out.println(session.getId() + " closed.");
    }

    @OnError
    public void onError(Session session, Throwable t) {
        System.out.println(session.getId() + " error.");
    }

    @OnMessage
    public void onMessage(String message, Session session) {
        try {
            Gson gson = new Gson();
            Datum datum = gson.fromJson(message, Datum.class);
            System.out.println(message + " received from session " + session.getId());
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
