package com.lemp.object;

import java.util.List;

/**
 * Created by ayberkcansever on 13/01/17.
 */
public class Group {

    public enum RequestType {
        create("c"),
        add("a"),
        banish("b"),
        leave("l"),
        terminate("t"),
        information("i"),
        add_admin("aad"),
        get_admins("ad");

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

        public static RequestType getByKey(String key){
            for(RequestType t : values()){
                if(t.getKey().equals(key)){
                    return t;
                }
            }
            return null;
        }
    }

    private String t;
    private String i;
    private String n;
    private String p;
    private List<String> m;
    private List<String> ad;
    private long c;
    private long mt;

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

    public long getMt() {
        return mt;
    }

    public void setMt(long mt) {
        this.mt = mt;
    }

    public List<String> getAd() {
        return ad;
    }

    public void setAd(List<String> ad) {
        this.ad = ad;
    }
}
