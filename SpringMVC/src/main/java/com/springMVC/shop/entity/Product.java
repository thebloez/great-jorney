package com.springMVC.shop.entity;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by Ryan Thebloez on 9/15/2017.
 */

@Entity
@Table(name = "products")
public class Product {

    private static final long serialVersionUID = -1000119078147252957L;
    private String code, name;
    private double price;
    private byte[] image;

    // for sort
    private Date createDate;

    @Temporal(TemporalType.DATE.TIMESTAMP)
    @Column(name = "Create_Date", nullable = false)
    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    @Id
    @Column(name = "Code", nullable = false, length = 5)
    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    @Column(name = "Name", nullable = false, length = 50)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Column(name = "Price", nullable = false)
    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    @Lob
    @Column(name = "Image", length = Integer.MAX_VALUE, nullable = true)
    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }
}
