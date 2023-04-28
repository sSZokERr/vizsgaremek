package com.example.vizsgaremek_android;

public class PictureUpload {
    private String base64String;
    private int id;
    private int imageType;
    private int position;
    private int project;

    public PictureUpload(String base64String, int id, int imageType, int position, int project) {
        this.base64String = base64String;
        this.id = id;
        this.imageType = imageType;
        this.position = position;
        this.project = project;
    }

    public void setImageType(int imageType) {
        this.imageType = imageType;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public void setProject(int project) {
        this.project = project;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setBase64String(String base64String) {
        this.base64String = base64String;
    }

    public int getId() {
        return id;
    }

    public String getBase64String() {
        return base64String;
    }

    public int getImageType() {
        return imageType;
    }

    public int getPosition() {
        return position;
    }

    public int getProject() {
        return project;
    }
}
