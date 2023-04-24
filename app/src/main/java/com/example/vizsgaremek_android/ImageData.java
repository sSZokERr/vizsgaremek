package com.example.vizsgaremek_android;


import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "image")
public class ImageData {
    @PrimaryKey(autoGenerate = true)
    public int primary;
    public int id;
    public int imageType;
    public int project;

    public int positionInProject;
    public String imageUrl;

    public ImageData(int primary, int id, int imageType, int project, int positionInProject, String imageUrl) {
        this.primary = primary;
        this.id = id;
        this.imageType = imageType;
        this.project = project;
        this.positionInProject = positionInProject;
        this.imageUrl = imageUrl;
    }

    public int getPrimary() {
        return primary;
    }

    public int getId() {
        return id;
    }

    public int getImageType() {
        return imageType;
    }

    public int getProject() {
        return project;
    }

    public int getPositionInProject() {
        return positionInProject;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setPrimary(int primary) {
        this.primary = primary;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setImageType(int imageType) {
        this.imageType = imageType;
    }

    public void setProject(int project) {
        this.project = project;
    }

    public void setPositionInProject(int positionInProject) {
        this.positionInProject = positionInProject;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}