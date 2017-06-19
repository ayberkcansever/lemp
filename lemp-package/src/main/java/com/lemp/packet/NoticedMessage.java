package com.lemp.packet;

import com.lemp.object.Notify;

/**
 * Created by AyberkC on 23.05.2017.
 */
public class NoticedMessage extends Message {

    private Notify n;

    public NoticedMessage(Message message) {
        setR(message.getS());
        Notify nf = new Notify();
        nf.setId(message.getId());
        this.n = nf;
    }

    public Notify getN() {
        return n;
    }

    public void setN(Notify n) {
        this.n = n;
    }
}
