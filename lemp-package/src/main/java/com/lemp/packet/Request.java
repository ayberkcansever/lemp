package com.lemp.packet;

import com.lemp.object.*;

import java.util.List;

/**
 * Created by ayberkcansever on 13/01/17.
 */
public class Request extends Packet{

    public enum Type {
        set("s"),
        get("g");

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

    private String r;
    private Authentication a;
    private Logout lo;
    private List<User> af;
    private List<User> uf;
    private List<String> rf;
    private State s;
    private PersonalInfo i;
    private Picture p;
    private Status st;
    private Privacy b;
    private Privacy ub;
    private Group g;
    private Name n;
    private Mute mt;
    private Unmute umt;
    private Broadcast br;
    private Administrative ad;

    public String getR() {
        return r;
    }

    public void setR(String r) {
        this.r = r;
    }

    public Authentication getA() {
        return a;
    }

    public void setA(Authentication a) {
        this.a = a;
    }

    public Logout getLo() {
        return lo;
    }

    public void setLo(Logout lo) {
        this.lo = lo;
    }

    public List<User> getAf() {
        return af;
    }

    public void setAf(List<User> af) {
        this.af = af;
    }

    public List<User> getUf() {
        return uf;
    }

    public void setUf(List<User> uf) {
        this.uf = uf;
    }

    public List<String> getRf() {
        return rf;
    }

    public void setRf(List<String> rf) {
        this.rf = rf;
    }

    public State getS() {
        return s;
    }

    public void setS(State s) {
        this.s = s;
    }

    public PersonalInfo getI() {
        return i;
    }

    public void setI(PersonalInfo i) {
        this.i = i;
    }

    public Picture getP() {
        return p;
    }

    public void setP(Picture p) {
        this.p = p;
    }

    public Status getSt() {
        return st;
    }

    public void setSt(Status st) {
        this.st = st;
    }

    public Privacy getB() {
        return b;
    }

    public void setB(Privacy b) {
        this.b = b;
    }

    public Group getG() {
        return g;
    }

    public void setG(Group g) {
        this.g = g;
    }

    public Name getN() {
        return n;
    }

    public void setN(Name n) {
        this.n = n;
    }

    public Mute getMt() {
        return mt;
    }

    public void setMt(Mute mt) {
        this.mt = mt;
    }

    public Broadcast getBr() {
        return br;
    }

    public void setBr(Broadcast br) {
        this.br = br;
    }

    public Privacy getUb() {
        return ub;
    }

    public void setUb(Privacy ub) {
        this.ub = ub;
    }

    public Unmute getUmt() {
        return umt;
    }

    public void setUmt(Unmute umt) {
        this.umt = umt;
    }

    public Administrative getAd() {
        return ad;
    }

    public void setAd(Administrative ad) {
        this.ad = ad;
    }
}
