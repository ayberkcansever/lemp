package com.lemp.server.akka.actor;

import akka.actor.ActorRef;
import akka.actor.DeadLetter;
import com.lemp.object.State;
import com.lemp.packet.Message;
import com.lemp.packet.Request;
import com.lemp.packet.Response;
import com.lemp.server.akka.LempRouters;
import com.lemp.server.database.StateDBHelper;

/**
 * Created by ayberkcansever on 20/01/17.
 */
public class DeadLetterActor extends LempActor {

    @Override
    public void onReceive(Object message) throws Throwable {
        if(message instanceof DeadLetter) {
            DeadLetter deadLetter = (DeadLetter) message;
            if(deadLetter.message() instanceof Message) {
                LempRouters.getOfflineMessageInserterRouter().tell(deadLetter.message(), ActorRef.noSender());
            } else if(deadLetter.message() instanceof Request) {
                Request req = (Request) deadLetter.message();
                if(req.getS() != null) {
                    String username = req.getS().getU();
                    Response response = new Response();
                    response.setId(req.getId());
                    response.setR(req.getR());
                    State state = new State();
                    state.setU(username);
                    state.setV(StateDBHelper.getInstance().getState(username));
                    response.setS(state);
                    LempRouters.getMessageProcessorRouter().tell(response, ActorRef.noSender());
                }
            }
        }
    }

}
