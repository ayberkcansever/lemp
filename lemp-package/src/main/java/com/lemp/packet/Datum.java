package com.lemp.packet;

/**
 * Created by ayberkcansever on 13/01/17.
 */
public class Datum {

    private Request rq;
    private ServerRequest srq;
    private Information i;
    private Message m;
    private Response rp;
    private ServerResponse srp;

    public Datum(Response rp) {
        this.rp = rp;
    }

    public Datum(ServerResponse srp) {
        this.srp = srp;
    }

    public Datum(Information i) {
        this.i = i;
    }

    public Datum(Message m) {
        this.m = m;
    }

    public Datum(Request rq) {
        this.rq = rq;
    }

    public Request getRq() {
        return rq;
    }

    public void setRq(Request rq) {
        this.rq = rq;
    }

    public Information getI() {
        return i;
    }

    public void setI(Information i) {
        this.i = i;
    }

    public Message getM() {
        return m;
    }

    public void setM(Message m) {
        this.m = m;
    }

    public Response getRp() {
        return rp;
    }

    public void setRp(Response rp) {
        this.rp = rp;
    }

    public ServerRequest getSrq() {
        return srq;
    }

    public void setSrq(ServerRequest srq) {
        this.srq = srq;
    }

    public ServerResponse getSrp() {
        return srp;
    }

    public void setSrp(ServerResponse srp) {
        this.srp = srp;
    }
}
