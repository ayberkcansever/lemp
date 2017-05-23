package com.lemp.packet;

/**
 * Created by ayberkcansever on 13/01/17.
 */
public class ServerResponse extends Packet {

    private String t;
    private Error e;

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
}
