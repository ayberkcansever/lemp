package com.lemp.packet;

/**
 * Created by ayberkcansever on 13/01/17.
 */
public abstract class Packet {

    protected String id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

}
