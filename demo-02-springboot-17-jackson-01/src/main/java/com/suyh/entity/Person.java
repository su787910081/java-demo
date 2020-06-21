package com.suyh.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.Date;

public class Person {
    private String id;
    private String name;
    private String sex;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss.SSS")
    private Date createdDate;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss.SSS")
    private Date updateDate;

    public Person() {
    }

    public Person(String id, String name, String sex) {
        this.id = id;
        this.name = name;
        this.sex = sex;
    }

    @Override
    public String toString() {
        return "Person{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", sex='" + sex + '\'' +
                '}';
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

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    @JsonIgnore
    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public Date getUpdateDate() {
        return updateDate;
    }

    @JsonIgnore
    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }
}
