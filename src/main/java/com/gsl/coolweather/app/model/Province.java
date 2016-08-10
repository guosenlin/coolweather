package com.gsl.coolweather.app.model;

/**
 * Created by guosenlin on 16-8-10.
 */

public class Province {
    private int id;             //primary key
    private String name;         //name of province in China
    private String code;        //code of province in China

    public Province(){}

    public Province(int id, String name, String code) {
        this.id = id;
        this.name = name;
        this.code = code;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
