package com.noon.a7pets;

public class Products {

    private String name;
    private String price;
    private int image;

    public Products(int image, String name, String price) {
        this.name = name;
        this.price = price;
        this.image = image;
    }

    public Products() {
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public void setImage(int image) {
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public String getPrice() {
        return price;
    }

    public int getImage() {
        return image;
    }
}
