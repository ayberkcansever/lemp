package com.lemp.server;

import akka.actor.ActorSystem;
import akka.actor.DeadLetter;
import akka.actor.Props;
import com.lemp.server.akka.ClusterListener;
import com.lemp.server.akka.actor.DeadLetterActor;
import com.lemp.server.cache.CacheHolder;
import com.lemp.server.database.AbstractDBHelper;
import com.lemp.server.endpoint.LempEndpoint;
import org.glassfish.tyrus.server.Server;

import javax.websocket.DeploymentException;

/**
 * Created by ayberkcansever on 13/01/17.
 */
public class Application {

    private static ActorSystem actorSystem;

    public static void main(String[] args) throws DeploymentException {
        AbstractDBHelper.init(new String[]{"127.0.0.1"}, 9042, "lemp");
        CacheHolder.init();

        Server server = new Server("localhost", 8025, "/", null, LempEndpoint.class);
        server.start();

        // Create an Akka system
        actorSystem = ActorSystem.create("ClusterSystem");

        // Create an actor that handles cluster domain events
        actorSystem.actorOf(Props.create(ClusterListener.class), "clusterListener");

        actorSystem.eventStream().subscribe(actorSystem.actorOf(Props.create(DeadLetterActor.class)), DeadLetter.class);
    }

    public static ActorSystem getActorSystem() {
        return actorSystem;
    }

    public static void setActorSystem(ActorSystem as) {
        actorSystem = as;
    }

}
