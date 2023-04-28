package com.example.vizsgaremek_android;

public class GetProjectsData {
    private int userId;
    private String projectTitle;
    private String projectData;
    private int projectId;

    public GetProjectsData(int userid, String projectTitle, String projectData, int projectId) {
        this.userId = userid;
        this.projectTitle = projectTitle;
        this.projectData = projectData;
        this.projectId = projectId;
    }

    public int getUserid() {
        return userId;
    }

    public String getProjectTitle() {
        return projectTitle;
    }

    public String getProjectData() {
        return projectData;
    }

    public int getProjectId() {
        return projectId;
    }

    public void setUserid(int userid) {
        this.userId = userid;
    }

    public void setProjectTitle(String projectTitle) {
        this.projectTitle = projectTitle;
    }

    public void setProjectData(String projectData) {
        this.projectData = projectData;
    }

    public void setProjectId(int projectId) {
        this.projectId = projectId;
    }
}
