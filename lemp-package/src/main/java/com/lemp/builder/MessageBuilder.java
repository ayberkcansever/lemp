package com.lemp.builder;

import com.lemp.packet.Message;

public class MessageBuilder {

    private String id;
    private String sender;
    private String receiver;
    private Message.Type type;
    private String content;
    private String persistency;
    private String serverReceipt;
    private String peerReceipt;

    public MessageBuilder(String id, String sender, String receiver) {
        this.id = id;
        this.sender = sender;
        this.receiver = receiver;
    }

    public MessageBuilder setType(Message.Type type) {
        this.type = type;
        return this;
    }

    public MessageBuilder setContent(String content) {
        this.content = content;
        return this;
    }

    public MessageBuilder setPersistency(String persistency) {
        this.persistency = persistency;
        return this;
    }

    public MessageBuilder setServerReceipt(String serverReceipt) {
        this.serverReceipt = serverReceipt;
        return this;
    }

    public MessageBuilder setPeerReceipt(String peerReceipt) {
        this.peerReceipt = peerReceipt;
        return this;
    }

    public Message build(){
        Message message = new Message();
        message.setId(this.id);
        message.setS(this.sender);
        message.setR(this.receiver);
        message.setT(this.type.getKey());
        message.setC(this.content);
        message.setP(this.persistency);
        message.setSc(this.serverReceipt);
        message.setPc(this.peerReceipt);
        message.setSt(System.currentTimeMillis());
        return message;
    }
}
