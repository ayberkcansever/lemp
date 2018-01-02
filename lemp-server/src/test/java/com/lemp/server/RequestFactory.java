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

    public static Request getSetPicUrlRequest(String id, String picUrl) {
        Request request = new Request();
        request.setId(id);
        Picture picture = new Picture();
        picture.setT(Request.Type.set.getKey());
        picture.setV(picUrl);
        request.setP(picture);
        return request;
    }

    public static Request getGetPicUrlRequest(String id, String username) {
        Request request = new Request();
        request.setId(id);
        Picture picture = new Picture();
        picture.setT(Request.Type.get.getKey());
        picture.setU(username);
        request.setP(picture);
        return request;
    }

    public static Request getSetStatusRequest(String id, String status) {
        Request request = new Request();
        request.setId(id);
        Status statusObj = new Status();
        statusObj.setT(Request.Type.set.getKey());
        statusObj.setS(status);
        request.setSt(statusObj);
        return request;
    }

    public static Request getGetStatusRequest(String id, String username) {
        Request request = new Request();
        request.setId(id);
        Status statusObj = new Status();
        statusObj.setT(Request.Type.get.getKey());
        statusObj.setU(username);
        request.setSt(statusObj);
        return request;
    }

    public static Request getAddPrivacyRequest(String id, String bannedUsername) {
        Request request = new Request();
        request.setId(id);
        Privacy privacyObj = new Privacy();
        privacyObj.setUsername(bannedUsername);
        request.setB(privacyObj);
        return request;
    }

    public static Request getRemovePrivacyRequest(String id, String bannedUsername) {
        Request request = new Request();
        request.setId(id);
        Privacy privacyObj = new Privacy();
        privacyObj.setUsername(bannedUsername);
        request.setUb(privacyObj);
        return request;
    }

    public static Request getGetPrivacyRequest(String id) {
        Request request = new Request();
        request.setId(id);
        Get getObj = new Get();
        getObj.setO("pr");
        request.setGet(getObj);
        return request;
    }
}
