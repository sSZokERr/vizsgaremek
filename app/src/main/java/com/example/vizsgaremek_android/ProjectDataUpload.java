package com.example.vizsgaremek_android;

public class ProjectDataUpload {
    private int userid;
    private String projectTitle;
    private String projectData;

    public void setUserId(int userid) {
        this.userid = userid;
    }

    public void setProjectTitle(String projectTitle) {
        this.projectTitle = projectTitle;
    }

    public void setProjectData(String projectData) {
        this.projectData = projectData;
    }

    public int getUserId() {
        return userid;
    }

    public String getProjectTitle() {
        return projectTitle;
    }

    public String getProjectData() {
        return projectData;
    }

    public ProjectDataUpload(int userid, String projectTitle, String projectData) {
        this.userid = userid;
        this.projectTitle = projectTitle;
        this.projectData = projectData;
    }
}
