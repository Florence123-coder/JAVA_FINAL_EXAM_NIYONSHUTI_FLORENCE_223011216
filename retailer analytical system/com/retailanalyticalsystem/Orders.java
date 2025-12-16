package com.retailanalyticalsystem;

import java.util.Date;

public class Orders {

    private int orderID;
    private int customerID;
    private int productID;
    private int quantity;
    private double totalPrice;
    private Date orderDate;

    // ===== Constructor =====
    public Orders(int orderID, int customerID, int productID, int quantity, double totalPrice, Date orderDate) {
        this.orderID = orderID;
        this.customerID = customerID;
        this.productID = productID;
        this.quantity = quantity;
        this.totalPrice = totalPrice;
        this.orderDate = orderDate;
    }

    // ===== Getters and Setters =====
    public int getOrderID() {
        return orderID;
    }

    public void setOrderID(int orderID) {
        this.orderID = orderID;
    }

    public int getCustomerID() {
        return customerID;
    }

    public void setCustomerID(int customerID) {
        this.customerID = customerID;
    }

    public int getProductID() {
        return productID;
    }

    public void setProductID(int productID) {
        this.productID = productID;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public Date getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(Date orderDate) {
        this.orderDate = orderDate;
    }

    @Override
    public String toString() {
        return "Order{" +
                "orderID=" + orderID +
                ", customerID=" + customerID +
                ", productID=" + productID +
                ", quantity=" + quantity +
                ", totalPrice=" + totalPrice +
                ", orderDate=" + orderDate +
                '}';
    }
}
