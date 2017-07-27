package com.lemp.server.database.dbo;

import lombok.Data;
import org.apache.ignite.binary.BinaryObjectException;
import org.apache.ignite.binary.BinaryReader;
import org.apache.ignite.binary.BinaryWriter;
import org.apache.ignite.binary.Binarylizable;

/**
 * Created by AyberkC on 06.06.2017.
 */
@Data
public class User implements Binarylizable {

    public enum Type {
        user(0),
        admin(1);

        private int key;

        Type(int key) {
            this.key = key;
        }

        public int getKey() {
            return key;
        }

        public void setKey(int key) {
            this.key = key;
        }

        public static Type getByKey(int key) {
            for (Type t : values()) {
                if (t.getKey() == key) {
                    return t;
                }
            }
            return null;
        }
    }

    private String username;
    private String password;
    private int userType;
    private String picUrl;
    private String status;

    public User() {
    }

    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public User(String username, String password, int userType) {
        this.username = username;
        this.password = password;
        this.userType = userType;
    }

    public User(String username, String password, int userType, String picUrl, String status) {
        this.username = username;
        this.password = password;
        this.userType = userType;
        this.picUrl = picUrl;
        this.status = status;
    }

    public void writeBinary(BinaryWriter binaryWriter) throws BinaryObjectException {
        binaryWriter.writeString("username", username);
        binaryWriter.writeString("password", password);
        binaryWriter.writeInt("userType", userType);
        binaryWriter.writeString("picUrl", picUrl);
        binaryWriter.writeString("status", status);
    }

    public void readBinary(BinaryReader binaryReader) throws BinaryObjectException {
        this.username = binaryReader.readString("username");
        this.password = binaryReader.readString("password");
        this.userType = binaryReader.readInt("userType");
        this.picUrl = binaryReader.readString("picUrl");
        this.status = binaryReader.readString("status");
    }
}
