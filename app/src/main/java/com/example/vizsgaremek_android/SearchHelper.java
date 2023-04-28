package com.example.vizsgaremek_android;

public class SearchHelper {
    private int userid;
    private String url;
    private int imageType;
    private String firstName;
    private String lastName;

    public void setUserid(int userid) {
        this.userid = userid;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setImageType(int imageType) {
        this.imageType = imageType;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public int getUserid() {
        return userid;
    }

    public String getUrl() {
        return url;
    }

    public int getImageType() {
        return imageType;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public SearchHelper(int userid, String url, int imageType, String firstName, String lastName) {
        this.userid = userid;
        this.url = url;
        this.imageType = imageType;
        this.firstName = firstName;
        this.lastName = lastName;
    }
}
