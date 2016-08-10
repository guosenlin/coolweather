package com.gsl.coolweather.app.model;

/**
 * Created by guosenlin on 16-8-10.
 */

public class County {
    private int id;              //primary key
    private String name;         //name of county in China
    private String code;         //code of county in China
    private int cityId;          //foreign key for city

    public County(){}

    public County(int id, String name, String code, int provinceId) {
        this.id = id;
        this.name = name;
        this.code = code;
        this.cityId = provinceId;
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

    public int getCityId() {
        return cityId;
    }

    public void setCityId(int cityId) {
        this.cityId = cityId;
    }
}
