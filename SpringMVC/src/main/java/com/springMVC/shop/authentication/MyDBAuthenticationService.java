package com.springMVC.shop.authentication;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.springMVC.shop.dao.AccountDAO;
import com.springMVC.shop.entity.Account;

@Service
public class MyDBAuthenticationService implements UserDetailsService {
	
	@Autowired
	private AccountDAO accountDAO;
	
	public UserDetails loadUserByUsername(String username)
			throws UsernameNotFoundException {
		
		Account account = accountDAO.findAccount(username);
		System.out.println("Account : " + account);
		
		if (account == null) {
			throw new UsernameNotFoundException("User "//
					+ username + " was not found in Database");
		}
		
		// Employee, Manager, ...
		String role = account.getUserRole();
		
		List<GrantedAuthority> granList = new ArrayList<GrantedAuthority>();
		
		// ROLE_EMPLOYEE, ROLE_MANAGER
		GrantedAuthority authority = new SimpleGrantedAuthority("ROLE_" + role);
		
		granList.add(authority);
		
		boolean enabled = account.isActive();
		boolean accountNonExpired = true;
		boolean credentialNonExpired = true;
		boolean accountNonLocked = true;
		
		UserDetails userDetails = (UserDetails) new User(account.getUserName(), //
				account.getPassword(), //
				enabled, accountNonExpired, //
				credentialNonExpired, accountNonLocked, granList);
		
		return userDetails;
	}

}
