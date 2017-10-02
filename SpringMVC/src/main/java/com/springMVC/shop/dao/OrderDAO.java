package com.springMVC.shop.dao;

import com.springMVC.shop.model.CartInfo;
import com.springMVC.shop.model.OrderDetailInfo;
import com.springMVC.shop.model.OrderInfo;
import com.springMVC.shop.model.PaginationResult;

import java.util.List;

/**
 * Created by Ryan Thebloez on 9/17/2017.
 */
public interface OrderDAO {

    public void saveOrder(CartInfo cartInfo);

    public PaginationResult<OrderInfo> listOrderInfo(int page, int maxResult, int maxNavigationPage);

    public OrderInfo getOrderInfo(String orderId);

    public List<OrderDetailInfo> listOrderDetailInfos(String orderId);
}
