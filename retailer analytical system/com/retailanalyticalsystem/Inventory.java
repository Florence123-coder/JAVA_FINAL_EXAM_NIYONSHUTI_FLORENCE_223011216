package com.retailanalyticalsystem;

import java.time.LocalDateTime;

public class Inventory {
    private int inventoryId;
    private String attribute1;
    private String attribute2;
    private String attribute3;
    private LocalDateTime createdAt;

    public Inventory() {}

    public Inventory(int inventoryId, String attribute1, String attribute2, String attribute3, LocalDateTime createdAt) {
        this.inventoryId = inventoryId;
        this.attribute1 = attribute1;
        this.attribute2 = attribute2;
        this.attribute3 = attribute3;
        this.createdAt = createdAt;
    }

    public int getInventoryId() { return inventoryId; }
    public void setInventoryId(int inventoryId) { this.inventoryId = inventoryId; }
    public String getAttribute1() { return attribute1; }
    public void setAttribute1(String attribute1) { this.attribute1 = attribute1; }
    public String getAttribute2() { return attribute2; }
    public void setAttribute2(String attribute2) { this.attribute2 = attribute2; }
    public String getAttribute3() { return attribute3; }
    public void setAttribute3(String attribute3) { this.attribute3 = attribute3; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    @Override
    public String toString() {
        return "Inventory ID: " + inventoryId;
    }
}
