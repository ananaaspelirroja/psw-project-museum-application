package it.project.controllers;

import it.project.entity.CustomerOrder;
import it.project.entity.User;
import it.project.services.CustomerOrderService;
import it.project.services.UserService;
import it.project.utils.ResultMessage;
import it.project.utils.exceptions.OrderNotFoundException;
import it.project.utils.exceptions.QuantityUnavailableException;
import it.project.utils.exceptions.UserNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/my-tickets")
@CrossOrigin(origins = "http://localhost:4200")
public class CustomerOrderController {

    @Autowired
    private CustomerOrderService customerOrderService;

    @PostMapping
    public ResponseEntity<?> addToCart(
            @RequestParam("ticketId") int ticketId,
            @RequestParam("quantity") int quantity,
            Authentication authentication) {
        try {
            CustomerOrder cart = customerOrderService.addToCart(ticketId, quantity, authentication);
            return new ResponseEntity<>(new ResultMessage("Item added to cart!", cart.getId()), HttpStatus.OK);
        } catch (QuantityUnavailableException e) {
            return new ResponseEntity<>(new ResultMessage(e.getMessage()), HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping
    public ResponseEntity<?> createOrder(@RequestBody Map<String, Object> payload, Authentication authentication) {
        try {
            List<Map<String, Integer>> items = (List<Map<String, Integer>>) payload.get("items");
            int totalAmount = (int) payload.get("totalAmount");

            CustomerOrder order = customerOrderService.createOrder(items, totalAmount, authentication);
            return new ResponseEntity<>(new ResultMessage("Order created successfully!", order.getId()), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(new ResultMessage("Failed to create order!"), HttpStatus.BAD_REQUEST);
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

