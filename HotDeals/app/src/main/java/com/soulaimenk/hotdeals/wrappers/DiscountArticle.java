package com.soulaimenk.hotdeals.wrappers;

import java.util.Date;

/**
 * Created by Soulaimen on 02/08/2016.
 */
public class DiscountArticle {

    private String uid;
    private String title;
    private String imageBase64;
    private String description;
    private Date date;
    private double oldPrice;
    private int percentage;
    private int periodInHours;
    private String latitude;
    private String longitude;
    private int likes;

    public DiscountArticle() {
    }

    public DiscountArticle(String title, String imageBase64, String description, Date date, double oldPrice, int percentage, int periodInHours, String latitude, String longitude) {
        this.title = title;
        this.imageBase64 = imageBase64;
        this.description = description;
        this.date = date;
        this.oldPrice = oldPrice;
        this.percentage = percentage;
        this.periodInHours = periodInHours;
        this.latitude = latitude;
        this.longitude = longitude;
        this.likes = 0;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImageBase64() {
        return imageBase64;
    }

    public void setImageBase64(String imageBase64) {
        this.imageBase64 = imageBase64;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public double getOldPrice() {
        return oldPrice;
    }

    public void setOldPrice(double oldPrice) {
        this.oldPrice = oldPrice;
    }

    public int getPercentage() {
        return percentage;
    }

    public void setPercentage(int percentage) {
        this.percentage = percentage;
    }

    public int getPeriodInHours() {
        return periodInHours;
    }

    public void setPeriodInHours(int periodInHours) {
        this.periodInHours = periodInHours;
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

    public int getLikes() {
        return likes;
    }

    public void setLikes(int likes) {
        this.likes = likes;
    }
}
