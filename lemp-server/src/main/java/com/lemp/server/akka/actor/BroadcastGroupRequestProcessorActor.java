package com.lemp.server.akka.actor;

import com.google.gson.Gson;
import com.lemp.object.Broadcast;
import com.lemp.object.Error;
import com.lemp.object.Name;
import com.lemp.object.Picture;
import com.lemp.packet.Datum;
import com.lemp.packet.Request;
import com.lemp.packet.Response;
import com.lemp.server.akka.object.SessionRequest;
import com.lemp.server.database.BroadcastDBHelper;
import com.lemp.server.database.dbo.User;

import javax.websocket.Session;

/**
 * Created by ayberkcansever on 15/01/17.
 */
public class BroadcastGroupRequestProcessorActor extends LempActor {

    private Gson gson = new Gson();

    @Override
    public void onReceive(Object msg) throws Throwable {
        if(msg instanceof SessionRequest) {
            try {
                SessionRequest sessionRequest = (SessionRequest) msg;
                Request request = sessionRequest.getRequest();
                Session session = sessionRequest.getSession();
                String username = ((User) session.getUserProperties().get(ActorProperties.USER)).getUsername();
                Response response = new Response(request.getId());

                // Create Group Request
                if(request.getBr() != null && request.getBr().getT().equals(Broadcast.RequestType.create.getKey())) {
                    Broadcast broadcast = request.getBr();
                    broadcast.setC(System.currentTimeMillis());
                    broadcast.setO(username);
                    BroadcastDBHelper.getInstance().createBroadcast(broadcast);
                }
                // Add user to group Request
                else if(request.getBr() != null && request.getBr().getT().equals(Broadcast.RequestType.add.getKey())) {
                    Broadcast broadcast = BroadcastDBHelper.getInstance().loadBroadcast(request.getBr().getI());
                    if(broadcast != null) {
                        if(username.equals(broadcast.getO())) {
                            for(String newMember : request.getBr().getM()) {
                                BroadcastDBHelper.getInstance().addMemberToBroadcast(broadcast.getI(), newMember);
                            }
                        } else {
                            response.setE(Error.Type.forbidden);
                        }
                    } else {
                        response.setE(Error.Type.not_found);
                    }
                }
                // Banish user from group Request
                else if(request.getBr() != null && request.getBr().getT().equals(Broadcast.RequestType.banish.getKey())) {
                    Broadcast broadcast = BroadcastDBHelper.getInstance().loadBroadcast(request.getBr().getI());
                    if(broadcast != null) {
                        if(username.equals(broadcast.getO())) {
                            for(String newMember : request.getBr().getM()) {
                                BroadcastDBHelper.getInstance().removeMemberFromBroadcast(broadcast.getI(), newMember);
                            }
                        } else {
                            response.setE(Error.Type.forbidden);
                        }
                    } else {
                        response.setE(Error.Type.not_found);
                    }
                }
                // Terminate group Request
                else if(request.getBr() != null && request.getBr().getT().equals(Broadcast.RequestType.terminate.getKey())) {
                    Broadcast broadcast = BroadcastDBHelper.getInstance().loadBroadcast(request.getBr().getI());
                    if(broadcast != null) {
                        if(username.equals(broadcast.getO())) {
                            BroadcastDBHelper.getInstance().deleteBroadcast(broadcast.getI());
                        } else {
                            response.setE(Error.Type.forbidden);
                        }
                    } else {
                        response.setE(Error.Type.not_found);
                    }
                }
                // Group Information Request
                else if(request.getBr() != null && request.getBr().getT().equals(Broadcast.RequestType.information.getKey())) {
                    Broadcast broadcast = BroadcastDBHelper.getInstance().loadBroadcast(request.getBr().getI());
                    if(broadcast != null) {
                        if(username.equals(broadcast.getO())) {
                            response.setBr(broadcast);
                        } else {
                            response.setE(Error.Type.forbidden);
                        }
                    } else {
                        response.setE(Error.Type.not_found);
                    }
                }
                // Name Request
                else if(request.getN() != null) {
                    Broadcast broadcast = BroadcastDBHelper.getInstance().loadBroadcast(request.getN().getB());
                    if(broadcast != null) {
                        if(username.equals(broadcast.getO())) {
                            if(request.getN().getT().equals(Request.Type.set.getKey())) {
                                BroadcastDBHelper.getInstance().updateBroadcastName(broadcast.getI(), request.getN().getN());
                            } else if(request.getN().getT().equals(Request.Type.get.getKey())) {
                                Name name = new Name();
                                name.setB(broadcast.getI());
                                name.setN(broadcast.getN());
                                response.setN(name);
                            }
                        } else {
                            response.setE(Error.Type.forbidden);
                        }
                    } else {
                        response.setE(Error.Type.not_found);
                    }
                }
                // Pic Request
                else if(request.getP() != null) {
                    Broadcast broadcast = BroadcastDBHelper.getInstance().loadBroadcast(request.getP().getB());
                    if(broadcast != null) {
                        if(username.equals(broadcast.getO())) {
                            if(request.getP().getT().equals(Request.Type.set.getKey())) {
                                BroadcastDBHelper.getInstance().updateBroadcastPic(broadcast.getI(), request.getP().getV());
                            } else if(request.getP().getT().equals(Request.Type.get.getKey())) {
                                Picture picture = new Picture();
                                picture.setB(broadcast.getI());
                                picture.setV(broadcast.getP());
                                response.setP(picture);
                            }
                        } else {
                            response.setE(Error.Type.forbidden);
                        }
                    } else {
                        response.setE(Error.Type.not_found);
                    }
                }
                session.getBasicRemote().sendText(gson.toJson(new Datum(response)));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

}
