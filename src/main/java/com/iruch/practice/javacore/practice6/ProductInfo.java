package com.iruch.practice.javacore.practice6;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductInfo {

    private final String name;
    private String description;
    private double price;
    private double rating;

    public ProductInfo(String name, String description, double price, double rating) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.rating = rating;
    }

    @Override
    public String toString() {
        return "ProductInfo{" +
                "name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", price=" + price +
                ", rating=" + rating +
                '}';
    }
}
