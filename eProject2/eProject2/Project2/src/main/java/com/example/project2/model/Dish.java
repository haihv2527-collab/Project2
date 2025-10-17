package com.example.project2.model;

public class Dish {
    private int dishId;
    private String dishName;
    private double price;
    private String category;
    private String description;
    private String imageUrl;
    private boolean available;

    public Dish(int dishId, String dishName, double price, String category,
                String description, String imageUrl, boolean available) {
        this.dishId = dishId;
        this.dishName = dishName;
        this.price = price;
        this.category = category;
        this.description = description;
        this.imageUrl = imageUrl;
        this.available = available;
    }

    // Getters & Setters
    public int getDishId() { return dishId; }
    public void setDishId(int dishId) { this.dishId = dishId; }

    public String getDishName() { return dishName; }
    public void setDishName(String dishName) { this.dishName = dishName; }

    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getImageUrl() { return imageUrl; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }

    public boolean isAvailable() { return available; }
    public void setAvailable(boolean available) { this.available = available; }

    public String getFormattedPrice() {
        return String.format("%,.0fđ", price);
    }

    @Override
    public String toString() {
        return dishName + " - " + getFormattedPrice();
    }
}