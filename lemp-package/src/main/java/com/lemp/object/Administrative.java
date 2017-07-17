package com.lemp.object;

/**
 * Created by ayberkcansever on 13/07/2017.
 */
public class Administrative {

    public enum Command {
        create("cr"),
        delete("del");

        private String key;

        Command(String key) {
            this.key = key;
        }

        public String getKey() {
            return key;
        }

        public void setKey(String key) {
            this.key = key;
        }

        public static Command getByKey(String key){
            for(Command t : values()){
                if(t.getKey().equals(key)){
                    return t;
                }
            }
            return null;
        }
    }

    private String c;
    private String i;
    private String t;
    private int ty;

    public String getC() {
        return c;
    }

    public void setC(String c) {
        this.c = c;
    }

    public String getI() {
        return i;
    }

    public void setI(String i) {
        this.i = i;
    }

    public String getT() {
        return t;
    }

    public void setT(String t) {
        this.t = t;
    }

    public int getTy() {
        return ty;
    }

    public void setTy(int ty) {
        this.ty = ty;
    }
}
