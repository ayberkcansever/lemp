package com.lemp.object;

import java.util.List;

/**
 * Created by ayberkcansever on 13/01/17.
 */
public class Broadcast {

    public enum RequestType {
        create("c"),
        add("a"),
        banish("b"),
        terminate("t"),
        information("i");

        private String key;

        RequestType(String key) {
            this.key = key;
        }

        public String getKey() {
            return key;
        }

        public void setKey(String key) {
            this.key = key;
        }
    }

    private String t;
    private String i;
    private String n;
    private String p;
    private List<String> m;
    private long c;
    private String o;

    public Broadcast() {
    }

    public Broadcast(String i, String n, String p, long c, String o) {
        this.i = i;
        this.n = n;
        this.p = p;
        this.c = c;
        this.o = o;
    }

    public String getT() {
        return t;
    }

    public void setT(String t) {
        this.t = t;
    }

    public String getI() {
        return i;
    }

    public void setI(String i) {
        this.i = i;
    }

    public String getN() {
        return n;
    }

    public void setN(String n) {
        this.n = n;
    }

    public String getP() {
        return p;
    }

    public void setP(String p) {
        this.p = p;
    }

    public List<String> getM() {
        return m;
    }

    public void setM(List<String> m) {
        this.m = m;
    }

    public long getC() {
        return c;
    }

    public void setC(long c) {
        this.c = c;
    }

    public String getO() {
        return o;
    }

    public void setO(String o) {
        this.o = o;
    }
}
