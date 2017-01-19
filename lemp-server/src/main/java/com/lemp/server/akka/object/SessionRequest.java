package com.lemp.server.akka.object;

import com.lemp.packet.Request;

import javax.websocket.Session;

/**
 * Created by ayberkcansever on 15/01/17.
 */
public class SessionRequest {

    private Request request;
    private Session session;

    public SessionRequest(Request request, Session session) {
        this.request = request;
        this.session = session;
    }

    public Request getRequest() {
        return request;
    }

    public void setRequest(Request request) {
        this.request = request;
    }

    public Session getSession() {
        return session;
    }

    public void setSession(Session session) {
        this.session = session;
    }
}
