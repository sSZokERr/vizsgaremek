package com.example.vizsgaremek_android;

public class PostImages {
    private int userid;
    private String url;
    private int imageType;
    private int projectId;

    public int getProject() {
        return projectId;
    }

    public void setProject(int project) {
        this.projectId = project;
    }

    public void setImageType(int imageType) {
        this.imageType = imageType;
    }

    public int getImageType() {
        return imageType;
    }

    public PostImages(int userid, String url, int imageType, int project) {
        this.userid = userid;
        this.url = url;
        this.imageType = imageType;
        this.projectId = project;
    }

    public void setUserid(int userid) {
        this.userid = userid;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getUserid() {
        return userid;
    }

    public String getUrl() {
        return url;
    }
}
