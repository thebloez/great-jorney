package com.springMVC.shop.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * Created by Ryan Thebloez on 9/15/2017.
 */
@Entity
@Table(name = "accounts")
public class Account implements Serializable{

    private static final long serialVersionUID = -2054386655979281969L;

    public static final String ROLE_MANAGER = "MANAGER";
    public static final String ROLE_EMPLOYEE = "EMPLOYEE";

    private String userName, password, userRole;
    private boolean active;

    @Id
    @Column(name = "User_Name", length = 20, nullable = false)
    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    @Column(name = "Password", length = 20, nullable = false)
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Column(name = "User_Role", length = 20, nullable = false)
    public String getUserRole() {
        return userRole;
    }

    public void setUserRole(String userRole) {
        this.userRole = userRole;
    }

    @Column(name = "Active", length = 1, nullable = false)
    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    @Override
    public String toString(){
        return "["+ this.userName + "," +
                "" + this.password + "," +
                "" + this.userRole + "]";
    }
}
