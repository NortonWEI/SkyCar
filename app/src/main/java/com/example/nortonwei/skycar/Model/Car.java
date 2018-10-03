package com.example.nortonwei.skycar.Model;

public class Car {
    String id;
    String name;
    String desc;
    String imgUrl;
    float basePrice;
    float audPrice;

    public Car(String id, String name, String desc, String imgUrl, float basePrice, float audPrice) {
        this.id = id;
        this.name = name;
        this.desc = desc;
        this.imgUrl = imgUrl;
        this.basePrice = basePrice;
        this.audPrice = audPrice;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDesc() {
        return desc;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public float getBasePrice() {
        return basePrice;
    }

    public float getAudPrice() {
        return audPrice;
    }
}
