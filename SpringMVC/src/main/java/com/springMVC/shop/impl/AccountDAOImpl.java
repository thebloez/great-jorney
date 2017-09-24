package com.springMVC.shop.impl;

import org.hibernate.Criteria;
import org.hibernate.Hibernate;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import com.springMVC.shop.dao.AccountDAO;
import com.springMVC.shop.entity.Account;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;

/**
 * Created by Ryan Thebloez on 9/17/2017.
 */
// Transactional Hibernate
@Transactional
public class AccountDAOImpl implements AccountDAO {

    @Autowired
    private SessionFactory sessionFactory;

    public Account findAccount(String userName) {

        Session session = sessionFactory.openSession();
//        Criteria criteria = session.createCriteria(Account.class);
        Account account = session.get(Account.class, userName);
        Hibernate.initialize(account.getUserName());
        session.close();
        return account;
    }
}
