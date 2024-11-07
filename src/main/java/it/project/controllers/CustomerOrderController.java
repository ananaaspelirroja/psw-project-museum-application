package it.project.controllers;

import it.project.entity.CustomerOrder;
import it.project.entity.User;
import it.project.services.CustomerOrderService;
import it.project.services.UserService;
import it.project.utils.ResultMessage;
import it.project.utils.exceptions.OrderNotFoundException;
import it.project.utils.exceptions.UserNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/orders")
public class CustomerOrderController {

    @Autowired
    private CustomerOrderService customerOrderService;

    @Autowired
    private UserService userService;

    @PostMapping
    @ResponseStatus(code = HttpStatus.OK)
    public ResponseEntity<?> createOrder(@RequestBody @Valid CustomerOrder customerOrder, Authentication authentication) {
        try {
            CustomerOrder savedOrder = customerOrderService.addOrder(customerOrder, authentication);
            return new ResponseEntity<>(new ResultMessage("Order created successfully!", savedOrder.getId()), HttpStatus.OK);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Failed to create order!", e);
        }
    }


    @GetMapping("/user-orders")
    public ResponseEntity<?> getOrdersByUser(Authentication authentication) {
        try {
            List<CustomerOrder> orders = customerOrderService.getOrdersByUser(authentication);
            return new ResponseEntity<>(orders, HttpStatus.OK);
        } catch (UserNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User not found!", e);
        }
    }


    @GetMapping("/user-orders/{startDate}/{endDate}")
    public ResponseEntity<?> getOrdersInPeriod(
            @PathVariable("startDate") @DateTimeFormat(pattern = "dd-MM-yyyy HH:mm") LocalDateTime start,
            @PathVariable("endDate") @DateTimeFormat(pattern = "dd-MM-yyyy HH:mm") LocalDateTime end,
            Authentication authentication) {
        try {
            List<CustomerOrder> result = customerOrderService.getOrdersByUserInPeriod(authentication, start, end);
            if (result.isEmpty()) {
                return new ResponseEntity<>(new ResultMessage("No results!"), HttpStatus.OK);
            }
            return new ResponseEntity<>(result, HttpStatus.OK);
        } catch (UserNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User not found!", e);
        } catch (OrderNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Order not found!", e);
        }
    }

}

