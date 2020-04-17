package com.caffeine.dreamlifeassociation;

public class RequestModel {

    String id, name, gender, problem, group, quantity, number, hospital, location, date;

    public RequestModel() {}

    public RequestModel(String id, String name, String gender, String problem, String group, String quantity, String number, String hospital, String location, String date) {
        this.id = id;
        this.name = name;
        this.gender = gender;
        this.problem = problem;
        this.group = group;
        this.quantity = quantity;
        this.number = number;
        this.hospital = hospital;
        this.location = location;
        this.date = date;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getProblem() {
        return problem;
    }

    public void setProblem(String problem) {
        this.problem = problem;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getHospital() {
        return hospital;
    }

    public void setHospital(String hospital) {
        this.hospital = hospital;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
