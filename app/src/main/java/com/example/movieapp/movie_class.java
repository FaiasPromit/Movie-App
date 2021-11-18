package com.example.movieapp;

public class movie_class {
    public String image_url,userid,description,title,uid;
    public  movie_class() { }

    public movie_class(String image_url, String userid, String description, String title, String uid) {
        this.image_url = image_url;
        this.userid = userid;
        this.description = description;
        this.title = title;
        this.uid = uid;
    }

    public String getImage_url() {
        return image_url;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }
}
