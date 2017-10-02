package com.springMVC.shop.dao;

import com.springMVC.shop.entity.Product;
import com.springMVC.shop.model.PaginationResult;
import com.springMVC.shop.model.ProductInfo;

/**
 * Created by Ryan Thebloez on 9/17/2017.
 */
public interface ProductDAO {

    Product findProduct(String Code);

    ProductInfo findProductInfo(String code);

    PaginationResult<ProductInfo> queryProduct(int page, int maxResult,
                                                      int maxNavigationPage, String likeName);
    
    PaginationResult<ProductInfo> queryProduct(int page, int maxResult,
            int maxNavigationPage);
    
    void save(ProductInfo productInfo);
}
