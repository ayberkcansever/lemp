package com.lemp.packet;

/**
 * Created by ayberkcansever on 13/01/17.
 */
public class Message extends Packet {

    private String s;
    private String r;
    private String t;
    private String w;

    public String getS() {
        return s;
    }

    public void setS(String s) {
        this.s = s;
    }

    public String getR() {
        return r;
    }

    public void setR(String r) {
        this.r = r;
    }

    public String getT() {
        return t;
    }

    public void setT(String t) {
        this.t = t;
    }

    public String getW() {
        return w;
    }

    public void setW(String w) {
        this.w = w;
    }
}
