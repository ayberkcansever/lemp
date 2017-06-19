package com.lemp.packet;

import com.lemp.object.Delivery;

/**
 * Created by AyberkC on 23.05.2017.
 */
public class DeliveredMessage extends Message {

    private Delivery d;

    public DeliveredMessage(Message message) {
        setR(message.getS());
        Delivery dm = new Delivery();
        dm.setId(message.getId());
        this.d = dm;
    }

    public Delivery getD() {
        return d;
    }

    public void setD(Delivery d) {
        this.d = d;
    }
}
