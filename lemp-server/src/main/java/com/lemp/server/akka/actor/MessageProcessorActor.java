package com.lemp.server.akka.actor;

import com.lemp.packet.Message;
import com.lemp.packet.Request;
import com.lemp.packet.Response;
import com.lemp.server.database.PrivacyDBHelper;

/**
 * Created by ayberkcansever on 20/01/17.
 */
public class MessageProcessorActor extends LempActor {

    /*@Override
    public void onReceive(Object message) throws Throwable {

        if(message instanceof Message) {
            Message msg = (Message) message;
            // block the message if receiver is banned the sender
            if(PrivacyDBHelper.getInstance().getPrivacySet(msg.getR()).contains(msg.getS())) {
                return;
            }
            sendPacket(msg.getR(), msg);
        } else if(message instanceof Request) {
            Request req = (Request) message;
            if(req.getS() != null) {
                String username = req.getS().getU();
                sendPacket(username, req);
            }
        } else if(message instanceof Response) {
            Response response = (Response) message;
            String receiver = response.getR();
            sendPacket(receiver, response);
        }

    }*/

    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(Message.class, msg -> {
                    // block the message if receiver is banned the sender
                    if(PrivacyDBHelper.getInstance().getPrivacySet(msg.getR()).contains(msg.getS())) {
                        return;
                    }
                    sendPacket(msg.getR(), msg);
                })
                .match(Request.class, req -> {
                    if(req.getS() != null) {
                        String username = req.getS().getU();
                        sendPacket(username, req);
                    }
                })
                .match(Response.class, response -> {
                    String receiver = response.getR();
                    sendPacket(receiver, response);
                })
                .build();
    }

}
