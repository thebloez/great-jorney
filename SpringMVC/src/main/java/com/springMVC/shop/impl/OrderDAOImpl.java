package com.springMVC.shop.impl;

import com.springMVC.shop.com.springMVC.shop.model.*;
import com.springMVC.shop.dao.OrderDAO;
import com.springMVC.shop.dao.ProductDAO;
import com.springMVC.shop.entity.Order;
import com.springMVC.shop.entity.OrderDetail;
import com.springMVC.shop.entity.Product;
import org.hibernate.Hibernate;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@Transactional
public class OrderDAOImpl implements OrderDAO {

    @Autowired
    private SessionFactory sessionFactory;

    @Autowired
    private ProductDAO productDAO;

    private int getMaxOrderNum(){
        String sql = "select max(o.orderNum) from " + Order.class.getName() + " o ";
        Session session = sessionFactory.getCurrentSession();
        Query query = session.createQuery(sql);
        Integer value = (Integer) query.uniqueResult();
        if (value == null) {
            return 0;
        }
        return value;
    }

    public void saveOrder(CartInfo cartInfo) {
        Session session = sessionFactory.getCurrentSession();

        int orderNum = this.getMaxOrderNum() + 1;
        Order order = new Order();

        CustomerInfo customerInfo = cartInfo.getCustomerInfo();
        order.setId(UUID.randomUUID().toString());
        order.setOrderNum(orderNum);
        order.setOrderDate(new Date());
        order.setCustomerName(customerInfo.getName());
        order.setCustomerEmail(customerInfo.getEmail());
        order.setCustomerAddress(customerInfo.getAddress());
        order.setCustomerPhone(customerInfo.getPhone());

        session.persist(order);

        List<CartLineInfo> list = cartInfo.getCartLines();

        for (CartLineInfo cart : list) {
            OrderDetail detail = new OrderDetail();
            detail.setId(UUID.randomUUID().toString());
            detail.setOrder(order);
            detail.setAmount(cart.getAmount());
            detail.setQuantity(cart.getQuantity());
            detail.setPrice(cart.getProductInfo().getPrice());

            String code = cart.getProductInfo().getCode();
            Product product = this.productDAO.findProduct(code);
            detail.setProduct(product);

            session.persist(detail);
        }
        // set OrderNum for Report
        cartInfo.setOrderNum(orderNum);
    }

    //page 1,2,..
    public PaginationResult<OrderInfo> listOrderInfo(int page, int maxResult, int maxNavigationPage) {
        String sql = "Select new " + OrderInfo.class.getName()//
                + "(ord.id, ord.orderDate, ord.orderNum, ord.amount,"
                + "ord.customerName, ord.customerAddress, ord.customerName, ord.customerPhone)" + "from "
                + Order.class.getName() + " ord "//
                + " order by ord.orderNum desc";

        Session session = this.sessionFactory.getCurrentSession();

        Query query = session.createQuery(sql);

        return new PaginationResult<OrderInfo>(query, page, maxResult, maxNavigationPage);
    }

    public Order findOrder(String orderId){
        Session session = sessionFactory.getCurrentSession();
        Order order = session.get(Order.class, orderId);
        Hibernate.initialize(order.getId());

        session.close();

        return order;
    }

    public OrderInfo getOrderInfo(String orderId) {
        Order order = this.findOrder(orderId);
        if (order == null) {
            return null;
        }

        return new OrderInfo(order.getId(),//
                 order.getOrderDate(), order.getOrderNum(), order.getAmount(), //
                 order.getCustomerName(), order.getCustomerAddress(), //
                 order.getCustomerEmail(), order.getCustomerPhone());
    }

    public List<OrderDetailInfo> listOrderDetailInfos(String orderId) {
        String sql = "Select new " + OrderDetailInfo.class.getName() //
            + "(d.id, d.product.code, d.product.name, d.quanity, d.price, d.amount"//
            + " from " + OrderDetail.class.getName() + " d "//
            + " where d.order.id =:orderId ";

        Session session = this.sessionFactory.getCurrentSession();
        Query query = session.createQuery(sql);
        query.setParameter("orderId", orderId);

        return query.list();
    }
}
