package com.lemp.packet;

/**
 * Created by ayberkcansever on 13/01/17.
 */
public class Information extends Packet {

    private String li;
    private String lo;

    public String getLi() {
        return li;
    }

    public void setLi(String li) {
        this.li = li;
    }

    public String getLo() {
        return lo;
    }

    public void setLo(String lo) {
        this.lo = lo;
    }
}
