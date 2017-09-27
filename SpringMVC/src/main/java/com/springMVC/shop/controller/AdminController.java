package com.springMVC.shop.controller;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.multipart.support.ByteArrayMultipartFileEditor;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import com.springMVC.shop.dao.OrderDAO;
import com.springMVC.shop.dao.ProductDAO;
import com.springMVC.shop.model.ProductInfo;
import com.springMVC.shop.validator.ProductInfoValidator;


@Controller
// enable Hibernate Transaction
@Transactional
// need to use RedirectAttributes
@EnableWebMvc
public class AdminController {
	
	@Autowired
	private OrderDAO orderDao;
	
	@Autowired
	private ProductDAO productDAO;
	
	@Autowired
	private ProductInfoValidator productInfoValidator;
	
	// configurated in ApplicationContextConfig
	@Autowired
	private ResourceBundleMessageSource messageSource;	
	
	@InitBinder
	public void myInitBinder(WebDataBinder dataBinder){
		
		Object target = dataBinder.getTarget();
		if (target == null) {
			return;
		}
		System.out.println("Target : " + target);
		
		if (target.getClass() == ProductInfo.class) {
			dataBinder.setValidator(productInfoValidator);
			// for upload image
			dataBinder.registerCustomEditor(byte[].class, new ByteArrayMultipartFileEditor());
		}
	}
	
	// GET : Show Login Page
	@RequestMapping(value = { "/login" }, method = RequestMethod.GET)
	public String login(Model model){
		
		return "login";
	}
	
	@RequestMapping(value = { "/accountInfo" }, method = RequestMethod.GET)
	public String accountInfo(Model model){
		
		UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext()
				.getAuthentication().getPrincipal();
		
		System.out.println(userDetails.getPassword());
		System.out.println(userDetails.getUsername());
		System.out.println(userDetails.isEnabled());
		
		model.addAttribute("userDetails", userDetails);
		return "accountInfo";
	}
	
	
}
