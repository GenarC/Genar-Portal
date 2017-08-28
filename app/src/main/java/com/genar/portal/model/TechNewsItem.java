package com.genar.portal.model;

public class TechNewsItem {

    private String name;
    private String url;
    private String color;

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public TechNewsItem(String name, String url, String color) {
        this.name = name;
        this.url = url;
        this.color = color;
    }
}
