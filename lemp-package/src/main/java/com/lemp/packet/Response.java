package com.lemp.packet;

import com.lemp.object.*;
import com.lemp.object.Error;

/**
 * Created by ayberkcansever on 13/01/17.
 */
public class Response {

    private String id;
    private int result;
    private State s;
    private PersonalInfo i;
    private Picture p;
    private Status st;
    private Group g;
    private Name n;
    private Broadcast br;
    private Error.Type e;

    public Response() {
    }

    public Response(Error.Type e) {
        this.e = e;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getResult() {
        return result;
    }

    public void setResult(int result) {
        this.result = result;
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

    public Broadcast getBr() {
        return br;
    }

    public void setBr(Broadcast br) {
        this.br = br;
    }

    public Error.Type getE() {
        return e;
    }

    public void setE(Error.Type e) {
        this.e = e;
    }
}
