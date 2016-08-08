package com.soulaimenk.hotdeals.wrappers;

import java.util.HashMap;

/**
 * Created by Soulaimen on 19/07/2016.
 */


public class UserType {
    private String uid;
    private String email;
    private String type;
    private HashMap<String, Favorite> favorites;
    private HashMap<String, Like> likes;

    public UserType() {
    }

    public UserType(String uid, String email, String type) {
        this.uid = uid;
        this.email = email;
        this.type = type;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public HashMap<String, Favorite> getFavorites() {
        return favorites;
    }

    public void setFavorites(HashMap<String, Favorite> favorites) {
        this.favorites = favorites;
    }

    public HashMap<String, Like> getLikes() {
        return likes;
    }

    public void setLikes(HashMap<String, Like> likes) {
        this.likes = likes;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
