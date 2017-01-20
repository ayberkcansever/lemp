package com.lemp.server.akka;

import akka.actor.ActorRef;
import akka.actor.Props;
import akka.routing.FromConfig;
import com.lemp.server.Application;
import com.lemp.server.akka.actor.AuthenticationRequestProcessorActor;
import com.lemp.server.akka.actor.MessageProcessorActor;
import com.lemp.server.akka.actor.OfflineMessageInserterActor;
import com.lemp.server.akka.actor.PacketProcessorActor;

/**
 * Created by ayberkcansever on 15/01/17.
 */
public class LempRouters {

    public static ActorRef packetProcessorRouter;
    public static ActorRef authenticationRequestProcessorRouter;
    public static ActorRef messageProcessorRouter;
    public static ActorRef offlineMessageInserterRouter;

    static {
        try {
            packetProcessorRouter = Application.actorSystem.actorOf(FromConfig.getInstance().props(Props.create(PacketProcessorActor.class)),
                    "packetProcessorRouter");
            authenticationRequestProcessorRouter = Application.actorSystem.actorOf(FromConfig.getInstance().props(Props.create(AuthenticationRequestProcessorActor.class)),
                    "authenticationRequestProcessorRouter");
            messageProcessorRouter = Application.actorSystem.actorOf(FromConfig.getInstance().props(Props.create(MessageProcessorActor.class)),
                    "messageProcessorRouter");
            offlineMessageInserterRouter = Application.actorSystem.actorOf(FromConfig.getInstance().props(Props.create(OfflineMessageInserterActor.class)),
                    "offlineMessageInserterRouter");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
