package com.gsl.coolweather.app.model;

/**
 * Created by guosenlin on 16-8-10.
 */

public class City {
    private int id;             //primary key
    private String name;         //name of city in China
    private String code;        //code of city in China
    private int provinceId;     //foreign key for province

    public City(){}

    public City(int id, String name, String code, int provinceId) {
        this.id = id;
        this.name = name;
        this.code = code;
        this.provinceId = provinceId;
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

    public int getProvinceId() {
        return provinceId;
    }

    public void setProvinceId(int provinceId) {
        this.provinceId = provinceId;
    }
}
