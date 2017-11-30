package com.comcare.comcare_user.Models;


public class JobModel {
    String km, name, problem1, time, jobId, status;

    public JobModel() {
        this.km = "";
        this.name = "";
        this.problem1 = "";
        this.time = "";
        this.jobId = "";
        this.status = "";
    }

    public JobModel(String km, String name, String problem1, String time, String jobId, String status) {
        this.km = km;
        this.name = name;
        this.problem1 = problem1;
        this.time = time;
        this.jobId = jobId;
        this.status = status;
    }

    public String getKm() {
        return km;
    }

    public void setKm(String km) {
        this.km = km;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getproblem1() {
        return problem1;
    }

    public void setproblem1(String problem1) {
        this.problem1 = problem1;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getJobId() {
        return jobId;
    }

    public void setJobId(String jobId) {
        this.jobId = jobId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}

