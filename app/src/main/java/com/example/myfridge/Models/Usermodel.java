package com.example.myfridge.Models;

public class Usermodel {
    private String userName;
    private String email;
    private String password;

    public Usermodel() {
    }

    public Usermodel(String _userName, String _email, String _password) {
        this.userName = _userName;
        this.email = _email;
        this.password = _password;
    }

    public String getUserName() {
        return userName;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }
}
