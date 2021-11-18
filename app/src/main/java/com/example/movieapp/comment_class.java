package com.example.movieapp;

public class comment_class {
    public String body, movie_id, user_id;
    public String id;
    public boolean visible;

    public comment_class(String body, String movie_id, String user_id, String id, boolean visible) {
        this.body = body;
        this.movie_id = movie_id;
        this.user_id = user_id;
        this.id = id;
        this.visible = visible;
    }
    public comment_class(){}
    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getMovie_id() {
        return movie_id;
    }

    public void setMovie_id(String movie_id) {
        this.movie_id = movie_id;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public boolean isVisible() {
        return visible;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }
}
