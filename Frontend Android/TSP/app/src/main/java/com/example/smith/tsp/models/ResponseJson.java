package com.example.smith.tsp.models;

import com.google.gson.annotations.SerializedName;

public class ResponseJson {

    @SerializedName("Msg")
    String message;
    @SerializedName("code")
    int code;

    public ResponseJson(String message, int code) {
        this.message = message;
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public int getCode() {
        return code;
    }
}
