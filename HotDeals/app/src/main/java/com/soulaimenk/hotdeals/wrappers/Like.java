package com.soulaimenk.hotdeals.wrappers;

/**
 * Created by Soulaimen on 02/08/2016.
 */
public class Like {
    String type;
    String uid;

    public Like(String type, String uid) {
        this.type = type;
        this.uid = uid;
    }

    public Like() {
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }
}
