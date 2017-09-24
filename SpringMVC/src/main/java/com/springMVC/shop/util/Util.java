package com.springMVC.shop.util;

import javax.servlet.http.HttpServletRequest;
import com.springMVC.shop.com.springMVC.shop.model.CartInfo;

public class Util {
	
	// Products in 	Cart, stored in session
	public static CartInfo getCartInSession(HttpServletRequest request) {
		
		// get Cart from Session
		CartInfo cartInfo = (CartInfo) request.getSession().getAttribute("myCart");
		
		// if null, create it
		if (cartInfo == null) {
			cartInfo = new CartInfo();
			
			// and store to Session
			request.getSession().setAttribute("myCart", cartInfo);
		}
		
		return cartInfo;
	}
	
	public static void removeCartInSession(HttpServletRequest request){
		request.getSession().removeAttribute("myCart");
	}
	
	public static void storeLastOrderedCartInSession(HttpServletRequest request, CartInfo cartInfo) {
		request.getSession().setAttribute("lastOrderedCart", cartInfo);
	}
	
	public static void getLastOrderedCartInSession(HttpServletRequest request) {
		request.getSession().getAttribute("lastOrderedCart");
	}

}
