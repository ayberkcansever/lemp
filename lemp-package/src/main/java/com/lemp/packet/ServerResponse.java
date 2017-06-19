package com.lemp.packet;

/**
 * Created by ayberkcansever on 13/01/17.
 */
public class ServerResponse extends Packet {

    private String t;
    private Error e;
    private long tm;
    private int o;

    public ServerResponse(ServerRequest serverRequest) {
        this.id = serverRequest.getId();
    }

    public String getT() {
        return t;
    }

    public void setT(String t) {
        this.t = t;
    }

    public Error getE() {
        return e;
    }

    public void setE(Error e) {
        this.e = e;
    }

    public long getTm() {
        return tm;
    }

    public void setTm(long tm) {
        this.tm = tm;
    }

    public int getO() {
        return o;
    }

    public void setO(int o) {
        this.o = o;
    }
}
