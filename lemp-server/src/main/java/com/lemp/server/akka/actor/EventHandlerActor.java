package com.lemp.server.akka.actor;

import com.lemp.packet.Information;
import com.lemp.server.akka.event.LoginEvent;
import com.lemp.server.akka.event.LogoutEvent;
import com.lemp.server.database.FollowerDBHelper;
import com.lemp.server.database.PrivacyDBHelper;
import com.lemp.server.database.dbo.Follower;

import java.util.Set;

public class EventHandlerActor extends LempActor {

    @Override
    public void onReceive(Object msg) throws Throwable {

        if(msg instanceof LoginEvent) {
            LoginEvent loginEvent = (LoginEvent) msg;
            String loginUser = loginEvent.getUsername();
            Set<Follower> followers = FollowerDBHelper.getInstance().getUsersFollowers(loginUser);
            Information information = new Information();
            information.setLi(loginUser);
            followers.parallelStream().forEach(follower -> {
                if(!PrivacyDBHelper.getInstance().getPrivacySet(loginUser).contains(follower.getFollower())) {
                    sendPacket(follower.getFollower(), information);
                }
            });
        } else if(msg instanceof LogoutEvent) {
            LogoutEvent logoutEvent = (LogoutEvent) msg;
            String logoutUser = logoutEvent.getUsername();
            Set<Follower> followers = FollowerDBHelper.getInstance().getUsersFollowers(logoutUser);
            Information information = new Information();
            information.setLo(logoutUser);
            followers.parallelStream().forEach(follower -> {
                if(!PrivacyDBHelper.getInstance().getPrivacySet(logoutUser).contains(follower.getFollower())) {
                    sendPacket(follower.getFollower(), information);
                }
            });
        }

    }
}
