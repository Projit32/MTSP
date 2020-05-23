package com.example.smith.mapdirect;

import com.google.gson.annotations.SerializedName;

public class ResponseMessage {
    @SerializedName("Msg")
    String Messages;

    public String getMessages() {
        return Messages;
    }

    public ResponseMessage(String messages) {

        Messages = messages;
    }
}
