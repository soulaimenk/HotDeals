package com.soulaimenk.hotdeals.wrappers;

/**
 * Created by Soulaimen on 20/07/2016.
 */
public class NewProduct {
    private String uid;
    private String title;
    private String imageBase64;
    private String description;
    private String date;
    private String latitude;
    private String longitude;

    public NewProduct() {
    }

    public NewProduct(String title, String description, String date, String latitude, String longitude, String imageBase64) {
        this.title = title;
        this.imageBase64 = imageBase64;
        this.description = description;
        this.date = date;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getImageBase64() {
        return imageBase64;
    }

    public void setImageBase64(String imageBase64) {
        this.imageBase64 = imageBase64;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }


}
