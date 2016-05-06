package com.example.eliane.moore.model;

/**
 * Created by NgocTri on 11/7/2015.
 */
public class Product {
    private int id;
    private String name;
    private int price;
    private String description;
    private String music;

    public Product(int id, String name, String description,String music) {
        this.id = id;
        this.name = name;
        //this.price = price;
        this.description = description;
        this.music=music;
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

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getDescription() {
        return description;
    }
    public String getMusic() {
        return music;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
