package com.lemp.server.akka.actor;

import akka.actor.UntypedActor;
import com.lemp.packet.Message;
import com.lemp.server.database.OfflineMessageDBHelper;

/**
 * Created by ayberkcansever on 21/01/17.
 */
public class OfflineMessageInserterActor extends LempActor {

    @Override
    public void onReceive(Object message) throws Throwable {
        if(message instanceof Message) {
            Message msg = (Message) message;
            if(Message.PersistencyType.persistent.getKey().equals(msg.getP())) {
                OfflineMessageDBHelper.getInstance().insertOfflineMessage(msg);
            }
        }
    }
}
