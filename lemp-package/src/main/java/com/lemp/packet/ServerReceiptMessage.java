package com.lemp.packet;

import com.lemp.object.ServerReceipt;

/**
 * Created by AyberkC on 23.05.2017.
 */
public class ServerReceiptMessage extends Message{

    private ServerReceipt sr;

    public ServerReceiptMessage(Message message) {
        setR(message.getS());
        ServerReceipt src = new ServerReceipt();
        src.setId(message.getId());
        this.sr = src;
    }

    public ServerReceipt getSr() {
        return sr;
    }

    public void setSr(ServerReceipt sr) {
        this.sr = sr;
    }
}
