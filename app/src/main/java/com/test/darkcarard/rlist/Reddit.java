package com.test.darkcarard.rlist;

import java.io.Serializable;
import java.util.Arrays;

/**
 * Created by darkcarard on 19/02/17.
 */

public class Reddit implements Serializable{
    private static final String URL = "https://www.reddit.com";

    private String name;
    private String description;
    private long suscribers;
    private String creationDate;
    private String image;
    private String imageHeader;
    private int[] imageSize;
    private String category;
    private String url;
    private String language;
    private boolean over18;

    public Reddit(String description, long suscribers, String creationDate, String image, int[] imageSize,
                  String category, String url, String language, boolean over18, String imageHeader) {
        this.description = description;
        this.suscribers = suscribers;
        this.creationDate = creationDate;
        this.image = image;
        this.imageSize = imageSize;
        this.category = category;
        this.url = url;
        this.language = language;
        this.over18 = over18;
        this.imageHeader = imageHeader;
        this.name = url;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public long getSuscribers() {
        return suscribers;
    }

    public void setSuscribers(long suscribers) {
        this.suscribers = suscribers;
    }

    public String getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(String creationDate) {
        this.creationDate = creationDate;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public int[] getImageSize() {
        return imageSize;
    }

    public void setImageSize(int[] imageSize) {
        this.imageSize = imageSize;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getUrl() {
        return URL+url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public boolean isOver18() {
        return over18;
    }

    public void setOver18(boolean over18) {
        this.over18 = over18;
    }

    public String getImageHeader() {
        return imageHeader;
    }

    public void setImageHeader(String imageHeader) {
        this.imageHeader = imageHeader;
    }

    @Override
    public String toString() {
        return "Reddit{" +
                "description='" + description + '\'' +
                ", suscribers=" + suscribers +
                ", creationDate='" + creationDate + '\'' +
                ", image='" + image + '\'' +
                ", imageSize=" + Arrays.toString(imageSize) +
                ", category='" + category + '\'' +
                ", url='" + url + '\'' +
                ", language='" + language + '\'' +
                ", over18=" + over18 +
                '}';
    }
}
