package com.springMVC.shop.dao;

import com.springMVC.shop.entity.Account;

/**
 * Created by Ryan Thebloez on 9/17/2017.
 */
public interface AccountDAO {

    public Account findAccount(String userName);
}
