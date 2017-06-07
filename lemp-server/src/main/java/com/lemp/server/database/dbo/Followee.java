package com.lemp.server.database.dbo;

import org.apache.ignite.binary.BinaryObjectException;
import org.apache.ignite.binary.BinaryReader;
import org.apache.ignite.binary.BinaryWriter;
import org.apache.ignite.binary.Binarylizable;

/**
 * Created by AyberkC on 07.06.2017.
 */
public class Followee implements Binarylizable {

    private String follower;
    private String followee;
    private String nick;

    public Followee() {
    }

    public Followee(String follower, String followee, String nick) {
        this.followee = followee;
        this.follower = follower;
        this.nick = nick;
    }

    public Followee(String follower, com.lemp.object.User user) {
        this.follower = follower;
        this.followee = user.getU();
        this.nick = user.getN();
    }

    public String getFollower() {
        return follower;
    }

    public void setFollower(String follower) {
        this.follower = follower;
    }

    public String getFollowee() {
        return followee;
    }

    public void setFollowee(String followee) {
        this.followee = followee;
    }

    public String getNick() {
        return nick;
    }

    public void setNick(String nick) {
        this.nick = nick;
    }

    public void writeBinary(BinaryWriter binaryWriter) throws BinaryObjectException {
        binaryWriter.writeString("follower", follower);
        binaryWriter.writeString("followee", followee);
        binaryWriter.writeString("nick", nick);
    }

    public void readBinary(BinaryReader binaryReader) throws BinaryObjectException {
        this.follower = binaryReader.readString("follower");
        this.followee = binaryReader.readString("followee");
        this.nick = binaryReader.readString("nick");
    }
}
