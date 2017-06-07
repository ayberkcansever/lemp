package com.lemp.server.akka;

import akka.actor.ActorRef;
import akka.actor.Props;
import akka.routing.FromConfig;
import com.lemp.server.Application;
import com.lemp.server.akka.actor.*;

/**
 * Created by ayberkcansever on 15/01/17.
 */
public class LempRouters {

    public static ActorRef packetProcessorRouter;
    public static ActorRef authenticationRequestProcessorRouter;
    public static ActorRef logoutRequestProcessorRouter;
    public static ActorRef messageProcessorRouter;
    public static ActorRef offlineMessageInserterRouter;
    public static ActorRef followingRequestRouter;
    public static ActorRef stateRequestRouter;
    public static ActorRef personalRequestRouter;
    public static ActorRef privacyRequestRouter;
    public static ActorRef groupRequestRouter;
    public static ActorRef broadcastGroupRequestRouter;

    static {
        try {
            packetProcessorRouter = Application.actorSystem.actorOf(FromConfig.getInstance().props(Props.create(PacketProcessorActor.class)),
                    "packetProcessorRouter");
            logoutRequestProcessorRouter = Application.actorSystem.actorOf(FromConfig.getInstance().props(Props.create(LogoutRequestProcessorActor.class)),
                    "logoutRequestProcessorRouter");
            authenticationRequestProcessorRouter = Application.actorSystem.actorOf(FromConfig.getInstance().props(Props.create(AuthenticationRequestProcessorActor.class)),
                    "authenticationRequestProcessorRouter");
            messageProcessorRouter = Application.actorSystem.actorOf(FromConfig.getInstance().props(Props.create(MessageProcessorActor.class)),
                    "messageProcessorRouter");
            offlineMessageInserterRouter = Application.actorSystem.actorOf(FromConfig.getInstance().props(Props.create(OfflineMessageInserterActor.class)),
                    "offlineMessageInserterRouter");
            followingRequestRouter = Application.actorSystem.actorOf(FromConfig.getInstance().props(Props.create(FollowingRequestProcessorActor.class)),
                    "followingRequestRouter");
            stateRequestRouter = Application.actorSystem.actorOf(FromConfig.getInstance().props(Props.create(StateRequestProcessorActor.class)),
                    "stateRequestRouter");
            personalRequestRouter = Application.actorSystem.actorOf(FromConfig.getInstance().props(Props.create(PersonalRequestProcessorActor.class)),
                    "personalRequestRouter");
            privacyRequestRouter = Application.actorSystem.actorOf(FromConfig.getInstance().props(Props.create(PrivacyRequestProcessorActor.class)),
                    "privacyRequestRouter");
            groupRequestRouter = Application.actorSystem.actorOf(FromConfig.getInstance().props(Props.create(GroupRequestProcessorActor.class)),
                    "groupRequestRouter");
            broadcastGroupRequestRouter = Application.actorSystem.actorOf(FromConfig.getInstance().props(Props.create(BroadcastGroupRequestProcessorActor.class)),
                    "broadcastGroupRequestRouter");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
