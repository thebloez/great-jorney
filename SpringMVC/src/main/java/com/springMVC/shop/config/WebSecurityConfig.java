package com.springMVC.shop.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

import com.springMVC.shop.authentication.MyDBAuthenticationService;

/**
 * Created by Ryan Thebloez on 9/15/2017.
 */
@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    MyDBAuthenticationService myDBAuthenticationService;

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception{
        
    	// for user in database
        auth.userDetailsService(myDBAuthenticationService);
    }

    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable();

        // the pages requires login as EMPLOYEE or MANAGER
        // if no login, it will redirect to /login page
        http.authorizeRequests().antMatchers("/orderList","/order","accountInfo")
                .access("anyRole('ROLE_EMPLOYEE', 'ROLE_MANAGER')");

        // for MANAGER only
        http.authorizeRequests().antMatchers("/product").access("hasRole('ROLE_MANAGER')");

        // when the user has logged in as XX
        // but access a page requires role YY
        http.authorizeRequests().and().exceptionHandling().accessDeniedPage("/403");

        //config for login form
        http.authorizeRequests().and().formLogin()
                // submit URL of login page
                .loginProcessingUrl("j_spring_security_check") // submit URL
                .loginPage("/login")
                .defaultSuccessUrl("/accountInfo")
                .failureUrl("/login?error=true")
                .usernameParameter("userName")
                .passwordParameter("password")
                // config for logout page
                // (Go home to homepage)
                .and().logout().logoutUrl("/logout").logoutSuccessUrl("/");
    }




}
