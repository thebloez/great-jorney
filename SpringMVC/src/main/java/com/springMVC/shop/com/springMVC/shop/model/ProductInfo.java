package com.springMVC.shop.com.springMVC.shop.model;

import org.springframework.web.multipart.commons.CommonsMultipartFile;

/**
 * Created by Ryan Thebloez on 9/15/2017.
 */
public class ProductInfo {
    private String code, name;
    private double price;

    private boolean newProduct = false;

    // Upload file
    private CommonsMultipartFile fileData;

    public ProductInfo() {
    }

    public ProductInfo(String code, String name, double price) {
        this.code = code;
        this.name = name;
        this.price = price;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }
    
    public void setFileData(CommonsMultipartFile fileData){
        this.fileData = fileData;
    }

    public CommonsMultipartFile getFileData() {
		return fileData;
	}

	public boolean isNewProduct(){
        return newProduct;
    }

    public void setNewProduct(boolean newProduct){
        this.newProduct = newProduct;
    }


}
