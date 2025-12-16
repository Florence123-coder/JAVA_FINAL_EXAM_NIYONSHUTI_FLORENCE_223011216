package com.retailanalyticalsystem;

import java.time.LocalDateTime;

public class Product {
    private int productId;
    private String name;
    private String description;
    private String category;
    private double priceOrValue;
    private String status;
    private LocalDateTime createdAt;

    public Product() {}

    public Product(int productId, String name, String description, String category,
                   double priceOrValue, String status, LocalDateTime createdAt) {
        this.productId = productId;
        this.name = name;
        this.description = description;
        this.category = category;
        this.priceOrValue = priceOrValue;
        this.status = status;
        this.createdAt = createdAt;
    }

    // Getters & Setters
    public int getProductId() { return productId; }
    public void setProductId(int productId) { this.productId = productId; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }
    public double getPriceOrValue() { return priceOrValue; }
    public void setPriceOrValue(double priceOrValue) { this.priceOrValue = priceOrValue; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    @Override
    public String toString() {
        return "Product: " + name + " (" + category + ")";
    }
}
