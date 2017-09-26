package com.springMVC.shop.controller;

import com.springMVC.shop.com.springMVC.shop.model.CartInfo;
import com.springMVC.shop.com.springMVC.shop.model.CustomerInfo;
import com.springMVC.shop.com.springMVC.shop.model.PaginationResult;
import com.springMVC.shop.com.springMVC.shop.model.ProductInfo;
import com.springMVC.shop.dao.OrderDAO;
import com.springMVC.shop.dao.ProductDAO;
import com.springMVC.shop.entity.Product;
import com.springMVC.shop.util.Utils;
import com.springMVC.shop.validator.CustomerInfoValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by Ryan Thebloez on 9/26/2017.
 */
@Controller
//enable Hibernate Controller
@Transactional
// Need to use redirect attributes
@EnableWebMvc
public class MainController {

    @Autowired
    private OrderDAO orderDAO;

    @Autowired
    private ProductDAO productDAO;

    @Autowired
    private CustomerInfoValidator customerInfoValidator;

    @InitBinder
    public void myInitBinder(WebDataBinder dataBinder){
        Object target = dataBinder.getTarget();
        if (target == null) {
            return;
        }
        System.out.println("Target = " + target);

        // For Cart Form
        if (target.getClass() == CartInfo.class) {

        }
        // for Customer form
        else if (target.getClass() == CustomerInfo.class) {
            dataBinder.setValidator(customerInfoValidator);
        }
    }

    @RequestMapping("/403")
    public String accessDenied(){
        return "/403";
    }

    @RequestMapping("/")
    public String home(){
        return "index";
    }

    // Product List page
    @RequestMapping({"/productList"})
    public String listProductHandler(Model model,//
            @RequestParam(value = "name", defaultValue = "") String likeName,//
            @RequestParam(value = "page", defaultValue = "1") int page){

        final int maxResult = 5;
        final int maxNavigationPage = 10;

        PaginationResult<ProductInfo> result = productDAO.queryProduct(page,//
                 maxResult,maxNavigationPage, likeName);

        model.addAttribute("paginationProducts", result);

        return "productList";
    }

    @RequestMapping({"/buyProduct"})
    public String listProductHandler(HttpServletRequest request, Model model,
                                     @RequestParam(value = "code", defaultValue = "") String code) {
        Product product = null;
        if (code != null && code.length() > 0) {
            product = productDAO.findProduct(code);
        }

        if (code != null){
            // cart into Stored in session
            CartInfo cartInfo = Utils.getCartInSession(request);

            ProductInfo productInfo = new ProductInfo(product);

            cartInfo.addProduct(productInfo, 1);
        }
        // Redirect to shoppingCart page
        return "redirect:/shoppingCart";
    }

    @RequestMapping({"/shoppingCartRemoveProduct"})
    public String removeProductHandler(HttpServletRequest request, Model model,//
                     @RequestParam(value = "code", defaultValue = "") String code){
        Product product = null;
        if (code != null && code.length() > 0) {
            product = productDAO.findProduct(code);
        }
        if (code != null) {

            // Cart Info stored in Session
            CartInfo cartInfo = Utils.getCartInSession(request);

            ProductInfo productInfo = new ProductInfo(product);

            cartInfo.removeProduct(productInfo);
        }

        // redirect to shoppingCart Page
        return "redirect:/shoppingCart";
    }

    // POST : Update quantity of product in cart.
    @RequestMapping(value = {"/shoppingCart"}, method = RequestMethod.POST)
    public String shoppingCartUpdateQty(HttpServletRequest request, //
                 Model model, @ModelAttribute("cartForm") CartInfo cartForm){

        CartInfo cartInfo = Utils.getCartInSession(request);
        cartInfo.updateQuantity(cartForm);

        // redirect to shoppingCart page.
        return "redirect:/shoppingCart";
    }

    // GET : show cart
    @RequestMapping(value = {"/shoppingCart"}, method = RequestMethod.GET)
    public String shoppingCartHandler(HttpServletRequest request, Model model){
        CartInfo myCart = Utils.getCartInSession(request);

        model.addAttribute("cartForm", myCart);

        return "shoppingCart";
    }

    // GET : enter customer information
    @RequestMapping(value = {"shoppingCartCustomer"}, method = RequestMethod.GET)
    public String shoppingCartCustomerForm(HttpServletRequest request, Model model){

        CartInfo cartInfo = Utils.getCartInSession(request);

        // cart is empty
        if (cartInfo.isEmpty()) {

            //redirect to shoppingCart page
            return "redirect:/shoppingCart";
        }

        CustomerInfo customerInfo = cartInfo.getCustomerInfo();
        if (customerInfo == null) {
            customerInfo = new CustomerInfo();
        }

        model.addAttribute("customerForm", customerInfo);

        return "shoppingCartCustomer";
    }

    // POST : Save Customer information
    @RequestMapping(value = {"/shoppingCartCustomer"}, method = RequestMethod.POST)
    public String shoppingCartCustomerSave(HttpServletRequest request, Model model, //
                                           @ModelAttribute("customerForm") @Validated CustomerInfo customerForm,
                                           BindingResult result, final RedirectAttributes redirectAttributes){
        // if has error
        if (result.hasErrors()) {
            customerForm.setValid(false);

            // forward re-enter customer info
            return "shoppingCartCustomer";
        }
        customerForm.setValid(true);

        CartInfo cartInfo = Utils.getCartInSession(request);

        cartInfo.setCustomerInfo(customerForm);

        // return to confirmation page
        return "redirect:/shoppingCartConfirmation";
    }

    // GET : Review cart to confirm
    @RequestMapping(value = {"/shoppingCartConfirmation"}, method = RequestMethod.GET)
    public String shoppingCartConfirmationReview(HttpServletRequest request, Model model){
        CartInfo cartInfo = Utils.getCartInSession(request);

        // Cart have no order
        if (cartInfo.isEmpty()) {
            // redirect to shoppingCart page
            return "redirect:/shoppongCart";
        } else if (!cartInfo.isValidCustomer()) {
            // enter customer info
            return "redirect:/shoppingCartCustomer";
        }

        return "shoppingCartConfirmation";
    }

    // POST : Send Cart (save)
    @RequestMapping(value = {"/shoppingCartConfirmation"}, method = RequestMethod.POST)
    // avoid UnexpectedRollbackException
    @Transactional(propagation = Propagation.NEVER)
    public String shoppingCartConfirmationSave(HttpServletRequest request, Model model){

        CartInfo cartInfo = Utils.getCartInSession(request);

        // cart have no products
        if (cartInfo.isEmpty()) {
            // redirect to shoppingCart Page
            return "redirect:/shoppingCart";
        } else if (!cartInfo.isValidCustomer()) {
            // enter to customerInfo
            return "redirect:/shoppingCartCustomer";
        }

        try {
            orderDAO.saveOrder(cartInfo);
        } catch (Exception e){
            // need : Propagation.NEVER?
            return "shoppingCartConfirmation";
        }

        // remove cart in session
        Utils.removeCartInSession(request);

        // store last ordered cart to session
        Utils.storeLastOrderedCartInSession(request,cartInfo);

        // redirect to successful page
        return "redirect:/shoppingCartFinalize";
    }


}
