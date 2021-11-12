package com.company.socialadapter.dto.builder;

import com.google.gson.annotations.Expose;

public class Response {
    @Expose
    private int code;

    @Expose
    private String message;

    @Expose
    private Object data;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
    private Response(Builder builder){
        this.code = builder.code;
        this.message = builder.message;
        this.data = builder.data;
    }

    public static final class Builder{
        //requirement params
        private int code;

        //optional params
        private String message = "message send from server";
        private Object data = new Object();
        public Builder(int code){
            this.code = code;
        }
        public Builder buildMessage(String message){
            this.message = message;
            return this;
        }
        public Builder buildData(Object data){
            this.data = data;
            return this;
        }
        public Response build(){
            return new Response(this);
        }
    }
}
