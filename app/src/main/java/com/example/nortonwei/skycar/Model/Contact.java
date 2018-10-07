package com.example.nortonwei.skycar.Model;

public class Contact {
    String id;
    String name;
    String mobile;

    public Contact(String id, String name, String mobile) {
        this.id = id;
        this.name = name;
        this.mobile = mobile;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getMobile() {
        return mobile;
    }
}
