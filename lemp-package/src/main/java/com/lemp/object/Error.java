package com.lemp.object;

/**
 * Created by ayberkcansever on 13/01/17.
 */
public class Error {

    public enum Type {
        not_found(400, "Item not found."),
        unauthorized(401, "Unauthorized."),
        forbidden(403, "Forbidden."),
        internal_server_error(500, "Internal server Error.");

        private int code;
        private String description;

        Type(int code, String description) {
            this.code = code;
            this.description = description;
        }

        public int getCode() {
            return code;
        }

        public void setCode(int code) {
            this.code = code;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }
    }

    private int c;
    private String d;

    public int getC() {
        return c;
    }

    public void setC(int c) {
        this.c = c;
    }

    public String getD() {
        return d;
    }

    public void setD(String d) {
        this.d = d;
    }


}
