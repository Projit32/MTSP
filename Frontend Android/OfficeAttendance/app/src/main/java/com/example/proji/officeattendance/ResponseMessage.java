package com.example.proji.officeattendance;

import com.google.gson.annotations.SerializedName;

public class ResponseMessage {
    @SerializedName("Msg")
    String Messages;
    @SerializedName("code")
    int code;

    public ResponseMessage(String messages, int code) {
        Messages = messages;
        this.code = code;
    }

    public String getMessages() {
        return Messages;
    }

    public int getCode() {
        return code;
    }
}
