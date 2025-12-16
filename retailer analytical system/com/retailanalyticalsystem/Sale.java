package com.retailanalyticalsystem;

import java.time.LocalDateTime;

public class Sale {
    private int saleId;
    private String orderNumber;
    private LocalDateTime date;
    private String status;
    private double totalAmount;
    private String paymentMethod;
    private String notes;

    public Sale() {}

    public Sale(int saleId, String orderNumber, LocalDateTime date, String status,
                double totalAmount, String paymentMethod, String notes) {
        this.saleId = saleId;
        this.orderNumber = orderNumber;
        this.date = date;
        this.status = status;
        this.totalAmount = totalAmount;
        this.paymentMethod = paymentMethod;
        this.notes = notes;
    }

    public int getSaleId() { return saleId; }
    public void setSaleId(int saleId) { this.saleId = saleId; }
    public String getOrderNumber() { return orderNumber; }
    public void setOrderNumber(String orderNumber) { this.orderNumber = orderNumber; }
    public LocalDateTime getDate() { return date; }
    public void setDate(LocalDateTime date) { this.date = date; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public double getTotalAmount() { return totalAmount; }
    public void setTotalAmount(double totalAmount) { this.totalAmount = totalAmount; }
    public String getPaymentMethod() { return paymentMethod; }
    public void setPaymentMethod(String paymentMethod) { this.paymentMethod = paymentMethod; }
    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }

    @Override
    public String toString() {
        return "Sale #" + orderNumber + " - Total: " + totalAmount;
    }
}
