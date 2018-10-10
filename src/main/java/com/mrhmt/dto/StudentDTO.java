package com.mrhmt.dto;

import java.io.Serializable;

public class StudentDTO implements Serializable {
    private String id;
    private String sClass;
    private String first;
    private String middle;
    private String last;
    private boolean sex;
    private String address;
    private String status;

    public StudentDTO(String id, String sClass, String first, String middle, String last, boolean sex, String address, String status) {
        this.id = id;
        this.sClass = sClass;
        this.first = first;
        this.middle = middle;
        this.last = last;
        this.sex = sex;
        this.address = address;
        this.status = status;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getsClass() {
        return sClass;
    }

    public void setsClass(String sClass) {
        this.sClass = sClass;
    }

    public String getFirst() {
        return first;
    }

    public void setFirst(String first) {
        this.first = first;
    }

    public String getMiddle() {
        return middle;
    }

    public void setMiddle(String middle) {
        this.middle = middle;
    }

    public String getLast() {
        return last;
    }

    public void setLast(String last) {
        this.last = last;
    }

    public boolean isSex() {
        return sex;
    }

    public void setSex(boolean sex) {
        this.sex = sex;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
