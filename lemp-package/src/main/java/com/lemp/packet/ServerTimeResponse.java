package com.lemp.packet;

/**
 * Created by ayberkcansever on 13/01/17.
 */
public class ServerTimeResponse extends ServerResponse {

    private long tm;

    public long getTm() {
        return tm;
    }

    public void setTm(long tm) {
        this.tm = tm;
    }
}
