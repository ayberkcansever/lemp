package com.lemp.server.akka.actor;

import akka.actor.ActorRef;
import akka.actor.DeadLetter;
import akka.actor.UntypedActor;
import com.lemp.packet.Message;
import com.lemp.server.akka.LempRouters;

/**
 * Created by ayberkcansever on 20/01/17.
 */
public class DeadLetterActor extends UntypedActor {
    @Override
    public void onReceive(Object message) throws Throwable {
        if(message instanceof DeadLetter) {
            DeadLetter deadLetter = (DeadLetter) message;
            if(deadLetter.message() instanceof Message) {
                LempRouters.offlineMessageInserterRouter.tell(deadLetter.message(), ActorRef.noSender());
            }
        }
    }
}
