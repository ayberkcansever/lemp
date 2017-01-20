package com.lemp.server.akka.actor;

import akka.actor.UntypedActor;
import com.lemp.packet.Message;
import com.lemp.server.database.DBHelper;

/**
 * Created by ayberkcansever on 21/01/17.
 */
public class OfflineMessageInserterActor extends UntypedActor {

    @Override
    public void onReceive(Object message) throws Throwable {
        if(message instanceof Message) {
            Message msg = (Message) message;
            DBHelper.insertOfflineMessage(msg);
        }
    }
}
