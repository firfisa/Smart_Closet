package com.example.smart_closet.domain;

public class Administrator {

    public int id;
    public String account;
    public String password;

    public Administrator() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Administrator(int id, String account, String password) {
        this.id = id;
        this.account = account;
        this.password = password;
    }
}
