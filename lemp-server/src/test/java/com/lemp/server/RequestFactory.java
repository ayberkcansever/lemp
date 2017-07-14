package com.lemp.server;

import com.lemp.object.*;
import com.lemp.packet.Request;
import com.lemp.packet.ServerRequest;

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

    public static Request getAddFolloweesRequest(String id, int size){
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

    public static Request getStateRequest(String id, String username) {
        Request request = new Request();
        request.setId(id);
        State state = new State();
        state.setU(username);
        request.setS(state);
        return request;
    }

    public static ServerRequest getServerRequest(String id, ServerRequest.Type type) {
        ServerRequest serverRequest = new ServerRequest();
        serverRequest.setId(id);
        serverRequest.setT(type.getKey());
        return serverRequest;
    }

    public static Request getCreateUserRequest(String id, String username, String password) {
        Request request = new Request();
        request.setId(id);
        Administrative administrative = new Administrative();
        administrative.setC(Administrative.Command.create.getKey());
        administrative.setI(username);
        administrative.setT(password);
        request.setAd(administrative);
        return request;
    }

    public static Request getDeleteUserRequest(String id, String username) {
        Request request = new Request();
        request.setId(id);
        Administrative administrative = new Administrative();
        administrative.setC(Administrative.Command.delete.getKey());
        administrative.setI(username);
        request.setAd(administrative);
        return request;
    }

}
