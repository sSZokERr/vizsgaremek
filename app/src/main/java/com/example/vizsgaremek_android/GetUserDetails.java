package com.example.vizsgaremek_android;

public class GetUserDetails {
    private int id;
    private String email;
    private String password;
    private String firstName;
    private String lastName;
    private int projectCount;
    private String studies;
    private String occupation;
    private String workExperience;
    private String aboutMe;

    public GetUserDetails(int id, String email, String password, String firstName, String lastName, int projectCount, String studies, String occupation, String workExperience, String aboutMe) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.projectCount = projectCount;
        this.studies = studies;
        this.occupation = occupation;
        this.workExperience = workExperience;
        this.aboutMe = aboutMe;
    }

    public int getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }
    public String getFirstName() {
        return firstName;
    }
    public String getLastName() {
        return lastName;
    }
    public int getProjectCount() {
        return projectCount;
    }
    public String getStudies() {
        return studies;
    }

    public String getOccupation() {
        return occupation;
    }

    public String getWorkExperience() {
        return workExperience;
    }

    public String getAboutMe() {
        return aboutMe;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setProjectCount(int projectCount) {
        this.projectCount = projectCount;
    }

    public void setStudies(String studies) {
        this.studies = studies;
    }

    public void setOccupation(String occupation) {
        this.occupation = occupation;
    }

    public void setWorkExperience(String workExperience) {
        this.workExperience = workExperience;
    }

    public void setAboutMe(String aboutMe) {
        this.aboutMe = aboutMe;
    }
}
