package com.example.kioskmainpage.Utilities;

public class Senior_MenuItem {

    private String name;
    private String price;
    private int image;

    public Senior_MenuItem(String name, String price, int image) {
        this.name = name;
        this.price = price;
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price+"원";
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }
}
