package com.lemp.object;

public class Get {

    public enum Type {
        privacy("pr");

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
    }

    private String o;

    public String getO() {
        return o;
    }

    public void setO(String o) {
        this.o = o;
    }
}
