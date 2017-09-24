package com.springMVC.shop.entity;

import javax.persistence.*;

/**
 * Created by Ryan Thebloez on 9/15/2017.
 */
public class OrderDetail {

    private static final long serialVersionUID = 7550745928843183535L;

    private String id;
    private Order order;

    private Product product;
    private int quanity;
    private double price, amount;

    @Id
    @Column(name = "ID", nullable = false, length = 50)
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ORDER_ID", nullable = false)
    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "PRODUCT_ID", nullable = false)
    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    @Column(name = "Quanity")
    public int getQuanity() {
        return quanity;
    }

    public void setQuantity(int quantity) {
        this.quanity = quantity;
    }

    @Column(name = "Price", nullable = false)
    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    @Column(name = "Amount", nullable = false)
    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }
}
