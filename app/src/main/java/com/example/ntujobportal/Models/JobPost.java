package com.example.ntujobportal.Models;

public class JobPost {
private String JobTitle;
private String JobType;
private String JobSalary;
private String JobDegree;
private String JobLocation;
private String JobDeadline;
private String JobWebsite;

    public JobPost(){

    }

    public String getJobTitle() {
        return JobTitle;
    }

    public void setJobTitle(String jobTitle) {
        JobTitle = jobTitle;
    }

    public String getJobType() {
        return JobType;
    }

    public void setJobType(String jobType) {
        JobType = jobType;
    }

    public String getJobSalary() {
        return JobSalary;
    }

    public void setJobSalary(String jobSalary) {
        JobSalary = jobSalary;
    }

    public String getJobDegree() {
        return JobDegree;
    }

    public void setJobDegree(String jobDegree) {
        JobDegree = jobDegree;
    }

    public String getJobLocation() {
        return JobLocation;
    }

    public void setJobLocation(String jobLocation) {
        JobLocation = jobLocation;
    }

    public String getJobDeadline() {
        return JobDeadline;
    }

    public void setJobDeadline(String jobDeadline) {
        JobDeadline = jobDeadline;
    }

    public String getJobWebsite() {
        return JobWebsite;
    }

    public void setJobWebsite(String jobWebsite) {
        JobWebsite = jobWebsite;
    }
}
