package com.example.smith.mapdirect;

import io.realm.RealmObject;

public class LoginCredentials extends RealmObject {
    String UserName;
    String Password;

    public String getUserName() {
        return UserName;
    }

    public void setUserName(String userName) {
        UserName = userName;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        Password = password;
    }
}
