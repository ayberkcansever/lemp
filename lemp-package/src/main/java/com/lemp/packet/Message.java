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

    public enum ExpectType {
        server_receipt_expected("1"),
        server_receipt_not_expected("0"),
        delivery_message_expected("1"),
        delivery_message_not_expected("0");

        private String key;

        ExpectType(String key) {
            this.key = key;
        }

        public String getKey() {
            return key;
        }

        public void setKey(String key) {
            this.key = key;
        }
    }

    public enum PersistencyType {
        persistent("1"),
        non_persistent("0");

        private String key;

        PersistencyType(String key) {
            this.key = key;
        }

        public String getKey() {
            return key;
        }

        public void setKey(String key) {
            this.key = key;
        }
    }

    protected String s;
    protected String r;
    protected String t;
    protected String c;
    protected Long st;
    protected String sc = "1";
    protected String pc = "1";
    protected String p = "1";

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

    public Long getSt() {
        return st;
    }

    public void setSt(Long st) {
        this.st = st;
    }

    public String getSc() {
        return sc;
    }

    public void setSc(String sc) {
        this.sc = sc;
    }

    public String getPc() {
        return pc;
    }

    public void setPc(String pc) {
        this.pc = pc;
    }

    public String getP() {
        return p;
    }

    public void setP(String p) {
        this.p = p;
    }
}
