package com.suyh.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.Date;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class Person {
    private String id;
    private String name;

    // 说是属性值为null 时不进行序列化
//    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String sex;

    /**
     * 注解：@JsonIgnore 这个注解放在属性上面，则set 将不会解析，但是get 将会被序列化
     * 原来当@JsonIgnore 存在时，@JsonFormat 注解将不允许存在
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss.SSS")
//    @JsonIgnore
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

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public Date getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }
}
