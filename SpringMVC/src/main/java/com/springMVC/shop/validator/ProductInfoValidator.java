package com.springMVC.shop.validator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import com.springMVC.shop.dao.ProductDAO;
import com.springMVC.shop.entity.Product;
import com.springMVC.shop.model.ProductInfo;

// @Component = as a bean
@Component
public class ProductInfoValidator implements Validator {
	
	@Autowired
	private ProductDAO productDAO;

	// this Validator support ProductInfo class
	public boolean supports(Class<?> clazz) {
		return clazz == ProductInfo.class;
	}

	public void validate(Object target, Errors errors) {
		
		ProductInfo productInfo = (ProductInfo) target;
		
		// Check the fields of ProductInfo class.
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "code", "NotEmpty.productForm.code");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "name", "NotEmpty.productForm.name");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "price", "NotEmpty.productForm.price");
        
        String code = productInfo.getCode();
        if (code != null && code.length() > 0) {
			if (code.matches("\\s+")) {
				errors.rejectValue("code", "Pattern.productForm.code");
			} else if (productInfo.isNewProduct()) {
				Product product = productDAO.findProduct(code);
				if (product != null) {
					errors.rejectValue("code", "Duplicate.productForm.code");
				}
			}
		}	
	}
}
