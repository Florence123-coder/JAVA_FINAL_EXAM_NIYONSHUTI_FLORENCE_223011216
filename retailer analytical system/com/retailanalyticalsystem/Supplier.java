package com.retailanalyticalsystem;

import java.time.LocalDateTime;

public class Supplier {
    private int supplierId;
    private String attribute1;
    private String attribute2;
    private String attribute3;
    private LocalDateTime createdAt;

    public Supplier() {}

    public Supplier(int supplierId, String attribute1, String attribute2, String attribute3, LocalDateTime createdAt) {
        this.supplierId = supplierId;
        this.attribute1 = attribute1;
        this.attribute2 = attribute2;
        this.attribute3 = attribute3;
        this.createdAt = createdAt;
    }

    public int getSupplierId() { return supplierId; }
    public void setSupplierId(int supplierId) { this.supplierId = supplierId; }
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
        return "Supplier ID: " + supplierId;
    }
}
