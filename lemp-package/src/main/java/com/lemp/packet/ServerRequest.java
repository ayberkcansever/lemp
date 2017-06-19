package com.lemp.packet;

/**
 * Created by ayberkcansever on 13/01/17.
 */
public class ServerRequest extends Packet {

    public enum Type {
        time("t"),
        knock_kncok("k");

        private String key;

        Type(String key) {
            this.key = key;
        }

        public String getKey() {
            return key;
        }

        public void setKey(String key) {
            this.key = key;
        }

        public static Type getByKey(String key){
            for(Type t : values()){
                if(t.getKey().equals(key)){
                    return t;
                }
            }
            return null;
        }

    }

    private String t;

    public String getT() {
        return t;
    }

    public void setT(String t) {
        this.t = t;
    }
}
