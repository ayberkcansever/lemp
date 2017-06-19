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

    private static ActorRef packetProcessorRouter;
    private static ActorRef authenticationRequestProcessorRouter;
    private static ActorRef logoutRequestProcessorRouter;
    private static ActorRef messageProcessorRouter;
    private static ActorRef offlineMessageInserterRouter;
    private static ActorRef followingRequestRouter;
    private static ActorRef stateRequestRouter;
    private static ActorRef personalRequestRouter;
    private static ActorRef privacyRequestRouter;
    private static ActorRef groupRequestRouter;
    private static ActorRef broadcastGroupRequestRouter;
    private static ActorRef serverRequestProcessorRouter;

    private LempRouters() { }

    static {
        try {
            packetProcessorRouter = Application.getActorSystem().actorOf(FromConfig.getInstance().props(Props.create(PacketProcessorActor.class)),
                    "packetProcessorRouter");
            logoutRequestProcessorRouter = Application.getActorSystem().actorOf(FromConfig.getInstance().props(Props.create(LogoutRequestProcessorActor.class)),
                    "logoutRequestProcessorRouter");
            authenticationRequestProcessorRouter = Application.getActorSystem().actorOf(FromConfig.getInstance().props(Props.create(AuthenticationRequestProcessorActor.class)),
                    "authenticationRequestProcessorRouter");
            messageProcessorRouter = Application.getActorSystem().actorOf(FromConfig.getInstance().props(Props.create(MessageProcessorActor.class)),
                    "messageProcessorRouter");
            offlineMessageInserterRouter = Application.getActorSystem().actorOf(FromConfig.getInstance().props(Props.create(OfflineMessageInserterActor.class)),
                    "offlineMessageInserterRouter");
            followingRequestRouter = Application.getActorSystem().actorOf(FromConfig.getInstance().props(Props.create(FollowingRequestProcessorActor.class)),
                    "followingRequestRouter");
            stateRequestRouter = Application.getActorSystem().actorOf(FromConfig.getInstance().props(Props.create(StateRequestProcessorActor.class)),
                    "stateRequestRouter");
            personalRequestRouter = Application.getActorSystem().actorOf(FromConfig.getInstance().props(Props.create(PersonalRequestProcessorActor.class)),
                    "personalRequestRouter");
            privacyRequestRouter = Application.getActorSystem().actorOf(FromConfig.getInstance().props(Props.create(PrivacyRequestProcessorActor.class)),
                    "privacyRequestRouter");
            groupRequestRouter = Application.getActorSystem().actorOf(FromConfig.getInstance().props(Props.create(GroupRequestProcessorActor.class)),
                    "groupRequestRouter");
            broadcastGroupRequestRouter = Application.getActorSystem().actorOf(FromConfig.getInstance().props(Props.create(BroadcastGroupRequestProcessorActor.class)),
                    "broadcastGroupRequestRouter");
            serverRequestProcessorRouter = Application.getActorSystem().actorOf(FromConfig.getInstance().props(Props.create(ServerRequestProcessorActor.class)),
                    "serverRequestProcessorRouter");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static ActorRef getPacketProcessorRouter() {
        return packetProcessorRouter;
    }

    public static ActorRef getAuthenticationRequestProcessorRouter() {
        return authenticationRequestProcessorRouter;
    }

    public static ActorRef getLogoutRequestProcessorRouter() {
        return logoutRequestProcessorRouter;
    }

    public static ActorRef getMessageProcessorRouter() {
        return messageProcessorRouter;
    }

    public static ActorRef getOfflineMessageInserterRouter() {
        return offlineMessageInserterRouter;
    }

    public static ActorRef getFollowingRequestRouter() {
        return followingRequestRouter;
    }

    public static ActorRef getStateRequestRouter() {
        return stateRequestRouter;
    }

    public static ActorRef getPersonalRequestRouter() {
        return personalRequestRouter;
    }

    public static ActorRef getPrivacyRequestRouter() {
        return privacyRequestRouter;
    }

    public static ActorRef getGroupRequestRouter() {
        return groupRequestRouter;
    }

    public static ActorRef getBroadcastGroupRequestRouter() {
        return broadcastGroupRequestRouter;
    }

    public static ActorRef getServerRequestProcessorRouter() {
        return serverRequestProcessorRouter;
    }
}
