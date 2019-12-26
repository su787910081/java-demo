package com.suyh;

import com.alibaba.fastjson.annotation.JSONField;

import java.util.Date;
import java.util.List;

public class Menu {
    // 这里的menuName 与json 文件中id 属性对应
    @JSONField(name = "id")
    private String menuName;
    private String resourceName;
    private Integer order;
    // 主要用作格式化成字符串时使用，从json 文件中读的时候似乎没有影响
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private Date date;
    List<Menu> items;

    public String getMenuName() {
        return menuName;
    }

    public void setMenuName(String menuName) {
        this.menuName = menuName;
    }

    public String getResourceName() {
        return resourceName;
    }

    public void setResourceName(String resourceName) {
        this.resourceName = resourceName;
    }

    public Integer getOrder() {
        return order;
    }

    public void setOrder(Integer order) {
        this.order = order;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public List<Menu> getItems() {
        return items;
    }

    public void setItems(List<Menu> items) {
        this.items = items;
    }

    @Override
    public String toString() {
        return "com.suyh.Menu{" +
                "menuName='" + menuName + '\'' +
                ", resourceName='" + resourceName + '\'' +
                ", order=" + order +
                ", date=" + date +
                ", items=" + items +
                '}';
    }
}