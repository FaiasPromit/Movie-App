package com.example.movieapp;

import java.util.ArrayList;

public class user_class {
    public String name,email,username,gender,uid;
    public ArrayList<String>watchlist;
    user_class() {
        watchlist = new ArrayList<>();
        uid = name = email = username = gender = null;
    }

    public user_class(String name, String email, String username, String gender, String uid) {
        this.name = name;
        this.email = email;
        this.username = username;
        this.gender = gender;
        this.uid = uid;
        this.watchlist = new ArrayList<>();
    }

    public user_class(String name, String email, String username, String gender, String uid, ArrayList<String> watchlist) {
        this.name = name;
        this.email = email;
        this.username = username;
        this.gender = gender;
        this.uid = uid;
        this.watchlist = watchlist;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getUsername() {
        return username;
    }

    public String getGender() {
        return gender;
    }

    public String getUid() {
        return uid;
    }

    public ArrayList<String> getWatchlist() {
        return watchlist;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public void setWatchlist(ArrayList<String> watchlist) {
        this.watchlist = watchlist;
    }
}
