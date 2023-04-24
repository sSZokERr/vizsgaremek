package com.example.vizsgaremek_android;

public class ProjectDataUpload {
    private int userId;
    private String projectTitle;
    private String projectData;

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public void setProjectTitle(String projectTitle) {
        this.projectTitle = projectTitle;
    }

    public void setProjectData(String projectData) {
        this.projectData = projectData;
    }

    public int getUserId() {
        return userId;
    }

    public String getProjectTitle() {
        return projectTitle;
    }

    public String getProjectData() {
        return projectData;
    }

    public ProjectDataUpload(int userId, String projectTitle, String projectData) {
        this.userId = userId;
        this.projectTitle = projectTitle;
        this.projectData = projectData;
    }
}
