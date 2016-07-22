package com.soulaimenk.hotdeals.wrappers;

/**
 * Created by Soulaimen on 19/07/2016.
 */
public class UserType {
    private String uid;
    private String type;

    public UserType() {
    }

    public UserType(String uid, String type) {
        this.uid = uid;
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
}
