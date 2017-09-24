package com.springMVC.shop.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Id;
import javax.persistence.UniqueConstraint;
import java.io.Serializable;
import java.util.Date;

/**
 * Created by Ryan Thebloez on 9/15/2017.
 */
@Entity
@Table(name = "orders", uniqueConstraints = {@UniqueConstraint(columnNames = "Order_Num")})
public class Order implements Serializable{

    private static final long serialVersionUID = -2576670215015463100L;

    private String Id;
    private Date orderDate;
    private int orderNum;
    private double amount;

    private String customerName, customerAddress, customerEmail, customerPhone;

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    @Id
    @Column(name = "ID", length = 2, nullable = false)
    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    @Column(name = "Order_Date", nullable = false)
    public Date getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(Date orderDate) {
        this.orderDate = orderDate;
    }

    @Column(name = "Order_Num", nullable = false, length = 5)
    public int getOrderNum() {
        return orderNum;
    }

    public void setOrderNum(int orderNum) {
        this.orderNum = orderNum;
    }

    @Column(name = "Amount", nullable = false)
    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    @Column(name = "Customer_Name", nullable = false, length = 255)
    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    @Column(name = "Customer_Address", nullable = false)
    public String getCustomerAddress() {
        return customerAddress;
    }

    public void setCustomerAddress(String customerAddress) {
        this.customerAddress = customerAddress;
    }

    @Column(name = "Customer_Email", nullable = false, length = 128)
    public String getCustomerEmail() {
        return customerEmail;
    }

    public void setCustomerEmail(String customerEmail) {
        this.customerEmail = customerEmail;
    }

    @Column(name = "Customer_Phone", nullable = false, length = 128)
    public String getCustomerPhone() {
        return customerPhone;
    }

    public void setCustomerPhone(String customerPhone) {
        this.customerPhone = customerPhone;
    }
}
