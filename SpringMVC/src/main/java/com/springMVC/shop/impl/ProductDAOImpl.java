package com.springMVC.shop.impl;

import java.util.Date;

import org.hibernate.Criteria;
import org.hibernate.Hibernate;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.hibernate.type.TrueFalseType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.springMVC.shop.dao.ProductDAO;
import com.springMVC.shop.entity.Product;
import com.springMVC.shop.model.PaginationResult;
import com.springMVC.shop.model.ProductInfo;

// Transactional for Hibernate
@Transactional
public class ProductDAOImpl implements ProductDAO {

	@Autowired
	private SessionFactory sessionFactory;
	
	public Product findProduct(String code) {
        Session session = sessionFactory.openSession();
        Product product = session.get(Product.class, code);
        Hibernate.initialize(product.getCode());
		session.close();
        return product;
    }

    public ProductInfo findProductInfo(String code) {
        Product product = this.findProduct(code);
        
        if (product == null) {
			return null;
		}
       
    	return new ProductInfo(product.getCode(), product.getName(), product.getPrice());
    }

    public PaginationResult<ProductInfo> queryProduct(int page, int maxResult,
    		int maxNavigationPage, String likeName) {
        String sql = "select new " + ProductInfo.class.getName() //
        		+ "(p.code, p.name, p.price) " + "from " //
        		+ Product.class.getName() + " p";
    	
        if (likeName != null && likeName.length() > 0) {
			sql += " Where lower(p.name) like :likeName";
		}
        
        sql += " order by p.createDate desc";
        
        Session session = sessionFactory.getCurrentSession();
        
        Query query = session.createQuery(sql);
     
        if (likeName != null && likeName.length() > 0) {
            query.setParameter("likeName", "%" + likeName.toLowerCase() + "%");
        }
        return new PaginationResult<ProductInfo>(query, page, maxResult, maxNavigationPage);
    }

    public void save(ProductInfo productInfo) {
    	String code = productInfo.getCode();
    	
    	Product product = null;
    	
    	boolean isNew = false;
    	if (code != null) {
			product = this.findProduct(code);
		}
    	
    	if (product == null) {
			isNew = true;
			product = new Product();
			product.setCreateDate(new Date());
		}
    	
    	product.setCode(code);
    	product.setName(productInfo.getName());
    	product.setPrice(productInfo.getPrice());
    	
    	if (productInfo.getFileData() != null) {
			byte[] image = productInfo.getFileData().getBytes();
			
			if (image != null && image.length > 0) {
				product.setImage(image);
			}
		}
    	
    	if (isNew) {
			this.sessionFactory.getCurrentSession().persist(product);
		}
    	
    	// if error in DB, exception will be thrown out immadiately
    	this.sessionFactory.getCurrentSession().flush();
    
    }

	public PaginationResult<ProductInfo> queryProduct(int page, int maxResult,
			int maxNavigationPage) {
		return queryProduct(page, maxResult, maxNavigationPage);
	}
}
