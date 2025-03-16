package com.shopme.restcontroller;

import javax.servlet.http.HttpServletRequest;

import com.shopme.entity.Customer;
import com.shopme.exception.CustomerNotFoundException;
import com.shopme.exception.OrderNotFoundException;
import com.shopme.service.CustomerService;
import com.shopme.service.OrderService;
import com.shopme.util.OrderReturnRequest;
import com.shopme.util.OrderReturnResponse;
import com.shopme.util.Utility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class OrderRestController {

    @Autowired
    private OrderService orderService;
    @Autowired
    private CustomerService customerService;

    @PostMapping("/orders/return")
    public ResponseEntity<?> handleOrderReturnRequest(@RequestBody OrderReturnRequest returnRequest,
                                                      HttpServletRequest servletRequest) {
        System.out.println("Order ID: " + returnRequest.getOrderId());
        System.out.println("Reason: " + returnRequest.getReason());
        System.out.println("Note: " + returnRequest.getNote());

        Customer customer = null;

        try {
            customer = getAuthenticatedCustomer(servletRequest);
        }
        catch (CustomerNotFoundException ex) {
            return new ResponseEntity<>("Authentication required", HttpStatus.BAD_REQUEST);
        }

        try {
            orderService.setOrderReturnRequested(returnRequest, customer);
        }
        catch (OrderNotFoundException ex) {
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(new OrderReturnResponse(returnRequest.getOrderId()), HttpStatus.OK);
    }

    private Customer getAuthenticatedCustomer(HttpServletRequest request)
            throws CustomerNotFoundException {
        String email = Utility.getEmailOfAuthenticatedCustomer(request);
        if (email == null) {
            throw new CustomerNotFoundException("No authenticated customer");
        }

        return customerService.getCustomerByEmail(email);
    }
}