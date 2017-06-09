package com.lemp.server;

import com.lemp.object.Authentication;
import com.lemp.object.Logout;
import com.lemp.object.User;
import com.lemp.packet.Request;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by AyberkC on 09.06.2017.
 */
public class RequestFactory {

    public static Request getAuthenticationReqeust(String id, String username, String password){
        Request authRequest = new Request();
        authRequest.setId(id);
        Authentication authentication = new Authentication();
        authentication.setI(username);
        authentication.setT(password);
        authRequest.setA(authentication);
        return authRequest;
    }

    public static Request getLogoutRequest(String id, String reason){
        Request request = new Request();
        request.setId(id);
        Logout logout = new Logout();
        logout.setR(reason);
        request.setLo(logout);
        return request;
    }

    public static Request getAddFolloweesRequest(String id,int size){
        Request request = new Request();
        request.setId(id);
        List<User> followeeList = new ArrayList<>(size);
        for(int i = 1; i < size + 1; i++) {
            User followee = new User();
            followee.setU("followee" + i);
            followee.setN("followee" + i + "nick");
            followeeList.add(followee);
        }
        request.setAf(followeeList);
        return request;
    }

    public static Request getRemoveFolloweeRequest(String id, List<String> followeeList){
        Request rfRequest = new Request();
        rfRequest.setId("rfId");
        rfRequest.setRf(followeeList);
        return rfRequest;
    }

    public static Request getUpdateFolloweeRequest(String id, List<User> userList){
        Request request = new Request();
        request.setId(id);
        request.setUf(userList);
        return request;
    }

}
