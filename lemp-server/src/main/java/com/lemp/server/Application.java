package com.lemp.server;

import akka.actor.ActorSystem;
import akka.actor.Props;
import com.lemp.server.akka.ClusterListener;
import com.lemp.server.endpoint.LempEndpoint;
import org.glassfish.tyrus.server.Server;

import javax.websocket.DeploymentException;

/**
 * Created by ayberkcansever on 13/01/17.
 */
public class Application {


    public static void main(String[] args) throws DeploymentException {
        Server server = new Server("localhost", 8025, "/", null, LempEndpoint.class);
        server.start();

        // Create an Akka system
        ActorSystem system = ActorSystem.create("ClusterSystem");

        // Create an actor that handles cluster domain events
        system.actorOf(Props.create(ClusterListener.class), "clusterListener");

    }

}
