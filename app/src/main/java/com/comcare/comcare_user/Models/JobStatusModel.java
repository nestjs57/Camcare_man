package com.comcare.comcare_user.Models;

public class JobStatusModel {

    private String name, date, status, type, time, man_id, order_id;

    public JobStatusModel() {
        this.name = "";
        this.date = "";
        this.status = "";
        this.type = "";
        this.time = "";
        this.man_id = "";
        this.order_id = "";
    }

    public JobStatusModel(String name, String date, String status, String type, String time, String man_id, String order_id) {
        this.name = name;
        this.date = date;
        this.status = status;
        this.type = type;
        this.time = time;
        this.man_id = man_id;
        this.order_id = order_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getMan_id() {
        return man_id;
    }

    public void setMan_id(String Man_id) {
        this.man_id = man_id;
    }

    public String getOrder_id() {
        return order_id;
    }

    public void setOrder_id(String order_id) {
        this.order_id = order_id;
    }
}

