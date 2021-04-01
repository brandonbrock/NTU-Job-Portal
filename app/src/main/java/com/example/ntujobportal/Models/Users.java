package com.example.ntujobportal.Models;

public class Users {

    String email, password;
    int isAdmin;

    public Users() {

    }

    public Users(String email, String password, int isAdmin) {
        this.email = email;
        this.password = password;
        this.isAdmin = isAdmin;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getisAdmin() {
        return isAdmin;
    }

    public void setisAdmin(int isAdmin) {
        isAdmin = isAdmin;
    }
}
