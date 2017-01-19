package com.lemp.server.akka.object;

import javax.websocket.Session;

/**
 * Created by ayberkcansever on 15/01/17.
 */
public class ClientMessage {

    private String message;
    private Session session;

    public ClientMessage(String message, Session session) {
        this.message = message;
        this.session = session;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Session getSession() {
        return session;
    }

    public void setSession(Session session) {
        this.session = session;
    }
}
