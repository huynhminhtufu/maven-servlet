package com.mrhmt.dto;

import javax.xml.bind.annotation.*;
import java.io.Serializable;

@XmlRootElement(name="student")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name="student", propOrder = {
        "last",
        "middle",
        "first",
        "sex",
        "password",
        "address",
        "status"
})
public class StudentDTO implements Serializable {
    @XmlAttribute
    private String id;
    @XmlAttribute(name="class")
    private String sClass;
    @XmlElement(name="firstname")
    private String first;
    @XmlElement(name="middlename")
    private String middle;
    @XmlElement(name="lastname")
    private String last;
    @XmlElement
    private int sex;
    @XmlElement
    private String password;
    @XmlElement
    private String address;
    @XmlElement
    private String status;

    public StudentDTO() {
    }

    public StudentDTO(String id, String sClass, String first, String middle, String last, int sex, String password, String address, String status) {
        this.id = id;
        this.sClass = sClass;
        this.first = first;
        this.middle = middle;
        this.last = last;
        this.sex = sex;
        this.password = password;
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

    public int getSex() {
        return sex;
    }

    public void setSex(int sex) {
        this.sex = sex;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
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
