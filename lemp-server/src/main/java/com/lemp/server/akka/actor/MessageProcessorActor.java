package com.lemp.server.akka.actor;

import akka.actor.ActorRef;
import akka.actor.UntypedActor;
import akka.cluster.pubsub.DistributedPubSub;
import akka.cluster.pubsub.DistributedPubSubMediator;
import com.lemp.packet.Message;
import com.lemp.server.Application;

/**
 * Created by ayberkcansever on 20/01/17.
 */
public class MessageProcessorActor extends UntypedActor {

    private ActorRef mediator;

    public MessageProcessorActor() {
        mediator = DistributedPubSub.get(Application.actorSystem).mediator();
    }

    @Override
    public void onReceive(Object message) throws Throwable {

        if(message instanceof Message) {
            Message msg = (Message) message;
            mediator.tell(new DistributedPubSubMediator.Send("/user/" + msg.getR(), msg, false), ActorRef.noSender());
        }

    }

}
