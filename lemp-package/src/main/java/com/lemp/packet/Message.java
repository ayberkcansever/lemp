package com.lemp.packet;

/**
 * Created by ayberkcansever on 13/01/17.
 */
public class Message extends Packet {

    public enum Type {
        text("t"),
        photo("p"),
        video("v"),
        audio("a"),
        location("l");

        private String key;

        Type(String key) {
            this.key = key;
        }

        public String getKey() {
            return key;
        }

        public void setKey(String key) {
            this.key = key;
        }
    }

    private String s;
    private String r;
    private String t;
    private String c;

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

    public String getC() {
        return c;
    }

    public void setC(String c) {
        this.c = c;
    }
}
