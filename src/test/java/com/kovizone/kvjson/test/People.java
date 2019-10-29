package com.kovizone.kvjson.test;

import com.kovizone.kvjson.annotation.DateFormat;

import java.util.Date;
import java.util.List;
import java.util.Map;

public class People {


    private Date date;
    private Integer id;
    private String name;
    private Boolean flag;
    private Integer age;
    private String work;
    private Character sex;
    private List<People> child;
    private People didi;

    public People getDidi() {
        return didi;
    }

    public void setDidi(People didi) {
        this.didi = didi;
    }

    @Override
    public String toString() {
        return "People{" +
                "date=" + date +
                ", id=" + id +
                ", name='" + name + '\'' +
                ", flag=" + flag +
                ", age=" + age +
                ", work='" + work + '\'' +
                ", sex=" + sex +
                ", child=" + child +
                '}';
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Boolean getFlag() {
        return flag;
    }

    public void setFlag(Boolean flag) {
        this.flag = flag;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public String getWork() {
        return work;
    }

    public void setWork(String work) {
        this.work = work;
    }

    public Character getSex() {
        return sex;
    }

    public void setSex(Character sex) {
        this.sex = sex;
    }

    public List getChild() {
        return child;
    }

    public void setChild(List child) {
        this.child = child;
    }
}
