package com.example.vizsgaremek_android;

public class UpdateProfileDetails {
    public int userid;
    public String updateStudies;
    public String updateOccupation;
    public String updateWorkExperience;
    public String updateAboutMe;

    public void setUserid(int userid) {
        this.userid = userid;
    }

    public void setUpdateStudies(String updateStudies) {
        this.updateStudies = updateStudies;
    }

    public void setUpdateOccupation(String updateOccupation) {
        this.updateOccupation = updateOccupation;
    }

    public void setUpdateWorkExperience(String updateWorkExperience) {
        this.updateWorkExperience = updateWorkExperience;
    }

    public void setUpdateAboutMe(String updateAboutMe) {
        this.updateAboutMe = updateAboutMe;
    }

    public int getUserid() {
        return userid;
    }

    public String getUpdateStudies() {
        return updateStudies;
    }

    public String getUpdateOccupation() {
        return updateOccupation;
    }

    public String getUpdateWorkExperience() {
        return updateWorkExperience;
    }

    public String getUpdateAboutMe() {
        return updateAboutMe;
    }

    public UpdateProfileDetails(int userid, String updateStudies, String updateOccupation, String updateWorkExperience, String updateAboutMe) {
        this.userid = userid;
        this.updateStudies = updateStudies;
        this.updateOccupation = updateOccupation;
        this.updateWorkExperience = updateWorkExperience;
        this.updateAboutMe = updateAboutMe;
    }
}
