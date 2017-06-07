package com.lemp.server.database.dbo;

import org.apache.ignite.binary.BinaryObjectException;
import org.apache.ignite.binary.BinaryReader;
import org.apache.ignite.binary.BinaryWriter;
import org.apache.ignite.binary.Binarylizable;

/**
 * Created by AyberkC on 06.06.2017.
 */
public class User implements Binarylizable {

    private String username;
    private String password;

    public User() {
    }

    public User(String username, String password) {
        this.username = username;
        this.password = password;
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

    public void writeBinary(BinaryWriter binaryWriter) throws BinaryObjectException {
        binaryWriter.writeString("username", username);
        binaryWriter.writeString("password", password);
    }

    public void readBinary(BinaryReader binaryReader) throws BinaryObjectException {
        this.username = binaryReader.readString("username");
        this.password = binaryReader.readString("password");
    }
}
