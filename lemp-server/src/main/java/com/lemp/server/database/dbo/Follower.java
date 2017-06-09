package com.lemp.server.database.dbo;

import org.apache.ignite.binary.BinaryObjectException;
import org.apache.ignite.binary.BinaryReader;
import org.apache.ignite.binary.BinaryWriter;
import org.apache.ignite.binary.Binarylizable;

/**
 * Created by AyberkC on 07.06.2017.
 */
public class Follower implements Binarylizable {

    private String followee;
    private String follower;
    private String nick;

    public Follower() {
    }

    public Follower(String followee, String follower, String nick) {
        this.follower = follower;
        this.followee = followee;
        this.nick = nick;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Follower follower1 = (Follower) o;

        if (followee != null ? !followee.equals(follower1.followee) : follower1.followee != null) return false;
        return follower != null ? follower.equals(follower1.follower) : follower1.follower == null;
    }

    @Override
    public int hashCode() {
        int result = followee != null ? followee.hashCode() : 0;
        result = 31 * result + (follower != null ? follower.hashCode() : 0);
        return result;
    }
}
