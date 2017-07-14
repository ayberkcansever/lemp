package com.lemp.server.database.dbo;

import org.apache.ignite.binary.BinaryObjectException;
import org.apache.ignite.binary.BinaryReader;
import org.apache.ignite.binary.BinaryWriter;
import org.apache.ignite.binary.Binarylizable;

/**
 * Created by AyberkC on 06.06.2017.
 */
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

        public static Type getByKey(int key){
            for(Type t : values()){
                if(t.getKey() == key) {
                    return t;
                }
            }
            return null;
        }
    }



    private String username;
    private String password;
    private int userType;

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

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getUserType() {
        return userType;
    }

    public void setUserType(int userType) {
        this.userType = userType;
    }

    public void writeBinary(BinaryWriter binaryWriter) throws BinaryObjectException {
        binaryWriter.writeString("username", username);
        binaryWriter.writeString("password", password);
        binaryWriter.writeInt("userType", userType);
    }

    public void readBinary(BinaryReader binaryReader) throws BinaryObjectException {
        this.username = binaryReader.readString("username");
        this.password = binaryReader.readString("password");
        this.userType = binaryReader.readInt("userType");
    }
}
