package com.lemp.server.akka.actor;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.cluster.pubsub.DistributedPubSub;
import akka.cluster.pubsub.DistributedPubSubMediator;
import com.lemp.packet.Packet;
import com.lemp.server.Application;

public abstract class LempActor extends AbstractActor {

    protected ActorRef mediator;

    public LempActor() {
        mediator = DistributedPubSub.get(Application.getActorSystem()).mediator();
    }

    protected void sendPacket(String username, Packet packet) {
        mediator.tell(new DistributedPubSubMediator.Send("/user/" + username, packet, false), ActorRef.noSender());
    }
}
