package it.project.controllers;

import it.project.entity.CustomerOrder;
import it.project.entity.User;
import it.project.services.CustomerOrderService;
import it.project.services.UserService;
import it.project.utils.ResultMessage;
import it.project.utils.exceptions.OrderNotFoundException;
import it.project.utils.exceptions.QuantityUnavailableException;
import it.project.utils.exceptions.TicketUnavailableException;
import it.project.utils.exceptions.UserNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "http://localhost:4200")
public class CustomerOrderController {

    @Autowired
    private CustomerOrderService customerOrderService;

    @PostMapping("/tickets/add-to-cart")
    @PreAuthorize("hasRole('ROLE_ROLE_USER')")
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

    @PostMapping("/tickets/create-order")
    @PreAuthorize("hasRole('ROLE_ROLE_USER')")
    public ResponseEntity<ResultMessage> createOrder(Authentication authentication) {
            if (authentication == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ResultMessage("User is not authenticated"));
            }
        try {
            // Crea l'ordine confermando il carrello esistente
            CustomerOrder order = customerOrderService.createOrder(authentication);

            // Restituisce una risposta di successo con l'ID dell'ordine
            return ResponseEntity.ok(new ResultMessage("Order created successfully!", order.getId()));

        } catch (UserNotFoundException e) {
            // Gestione dell'eccezione per utente non trovato
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ResultMessage("User not found!"));

        } catch (TicketUnavailableException e) {
            // Gestione dell'eccezione per biglietto non disponibile
            return ResponseEntity.status(HttpStatus.GONE)
                    .body(new ResultMessage("One or more tickets are no longer available."));

        } catch (QuantityUnavailableException e) {
            // Gestione dell'eccezione per quantità insufficiente di biglietti
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(new ResultMessage(e.getMessage()));

        } catch (RuntimeException e) {
            // Gestione per carrello non trovato o altri errori generali
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ResultMessage("Failed to create order: " + e.getMessage()));
        }
    }

    @PostMapping("/tickets/clear")
    public ResponseEntity<Void> clearCart(Authentication authentication) {
        customerOrderService.clearCart(authentication);
        return ResponseEntity.noContent().build();
    }



    @GetMapping("/my-tickets")
    @PreAuthorize("hasRole('ROLE_ROLE_USER')")
    public ResponseEntity<?> getOrdersByUser(Authentication authentication) {
        try {
            List<CustomerOrder> orders = customerOrderService.getOrdersByUser(authentication);
            return new ResponseEntity<>(orders, HttpStatus.OK);
        } catch (UserNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User not found!", e);
        }
    }

    @GetMapping("/my-tickets/{startDate}/{endDate}")
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

    @GetMapping("/orders/all")
    @PreAuthorize("hasRole('ROLE_ROLE_ADMIN')")
    public ResponseEntity<?> getAllOrders() {
        List<CustomerOrder> orders = customerOrderService.getAllOrders();
        return new ResponseEntity<>(orders, HttpStatus.OK);
    }

    @GetMapping("/orders/{userCode}")
    public List<CustomerOrder> getOrdersByUser(@PathVariable String userCode) {
        return customerOrderService.getOrdersByUserCode(userCode);
    }
}

