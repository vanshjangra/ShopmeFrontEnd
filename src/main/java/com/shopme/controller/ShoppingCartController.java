package com.shopme.controller;

import com.shopme.entity.Address;
import com.shopme.entity.CartItem;
import com.shopme.entity.Customer;
import com.shopme.entity.ShippingRate;
import com.shopme.service.AddressService;
import com.shopme.service.CustomerService;
import com.shopme.service.ShippingRateService;
import com.shopme.service.ShoppingCartService;
import com.shopme.util.Utility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
public class ShoppingCartController {
    @Autowired
    private ShoppingCartService cartService;

    @Autowired
    private CustomerService customerService;

    @Autowired
    private AddressService addressService;

    @Autowired
    private ShippingRateService shipService;

    @GetMapping("/cart")
    public String viewCart(Model model, HttpServletRequest request){
        Customer customer = getAuthenticatedCustomer(request);
        List<CartItem> cartItems = cartService.listCartItems(customer);

        float estimatedTotal = 0.0f;

        for(CartItem item : cartItems){
            estimatedTotal += item.getSubtotal();
        }

        Address defaultAddress = addressService.getDefaultAddress(customer);
        ShippingRate shippingRate = null;
        boolean usePrimaryAddressAsDefault = false;

        if (defaultAddress != null){
            shippingRate = shipService.getShippingRateForAddress(defaultAddress);
        }
        else {
            usePrimaryAddressAsDefault = true;
            shippingRate = shipService.getShippingRateForCustomer(customer);
        }

        model.addAttribute("usePrimaryAddressAsDefault", usePrimaryAddressAsDefault);
        model.addAttribute("shippingSupported", shippingRate != null);
        model.addAttribute("cartItems", cartItems);
        model.addAttribute("estimatedTotal", estimatedTotal);

        return "cart/shopping_cart";
    }

    private Customer getAuthenticatedCustomer(HttpServletRequest request) {
        String email = Utility.getEmailOfAuthenticatedCustomer(request);
        return customerService.getCustomerByEmail(email);
    }
}
