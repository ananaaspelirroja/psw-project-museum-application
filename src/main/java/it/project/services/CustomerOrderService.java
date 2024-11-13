package it.project.services;

import it.project.entity.CustomerOrder;
import it.project.entity.OrderTicket;
import it.project.entity.Ticket;
import it.project.entity.User;
import it.project.repositories.CustomerOrderRepository;
import it.project.repositories.OrderTicketRepository;
import it.project.repositories.TicketRepository;
import it.project.repositories.UserRepository;
import it.project.utils.exceptions.OrderNotFoundException;
import it.project.utils.exceptions.QuantityUnavailableException;
import it.project.utils.exceptions.UserNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Service
public class CustomerOrderService {

    @Autowired
    private CustomerOrderRepository customerOrderRepository;

    @Autowired
    private OrderTicketRepository orderTicketRepository;

    @Autowired
    private TicketRepository ticketRepository;

    @Autowired
    private UserRepository userRepository;

    @Transactional
    public CustomerOrder addToCart(int ticketId, int quantity, Authentication authentication) throws QuantityUnavailableException {
        User user = userRepository.findByUsername(authentication.getName())
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        CustomerOrder cart = customerOrderRepository.findByUserAndConfirmedFalse(user)
                .orElseGet(() -> createNewCart(user));

        Ticket ticket = ticketRepository.findById(ticketId)
                .orElseThrow(() -> new RuntimeException("Ticket not found"));

        OrderTicket orderTicket = cart.getOrderTickets().stream()
                .filter(ot -> ot.getTicket().getId() == ticketId)
                .findFirst()
                .orElseGet(() -> {
                    OrderTicket newOrderTicket = new OrderTicket();
                    newOrderTicket.setCustomerOrder(cart);
                    newOrderTicket.setTicket(ticket);
                    newOrderTicket.setQuantity(0);
                    cart.getOrderTickets().add(newOrderTicket);
                    return newOrderTicket;
                });

        if (ticket.getQuantity() < quantity) {
            throw new QuantityUnavailableException("Not enough tickets available");
        }

        orderTicket.setQuantity(orderTicket.getQuantity() + quantity);
        updateTotalAmount(cart);

        return customerOrderRepository.save(cart);
    }

    private CustomerOrder createNewCart(User user) {
        CustomerOrder cart = new CustomerOrder();
        cart.setUser(user);
        cart.setTotalAmount(0);
        cart.setConfirmed(false);
        return customerOrderRepository.save(cart);
    }

    private void updateTotalAmount(CustomerOrder cart) {
        double total = cart.getOrderTickets().stream()
                .mapToDouble(ot -> ot.getQuantity() * ot.getTicket().getPrice())
                .sum();
        cart.setTotalAmount((int) total);
    }

    @Transactional
    public CustomerOrder createOrder(List<Map<String, Integer>> items, int totalAmount, Authentication authentication) throws QuantityUnavailableException {
        User user = userRepository.findByUsername(authentication.getName())
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        CustomerOrder order = new CustomerOrder();
        order.setUser(user);
        order.setTotalAmount(totalAmount);
        order.setConfirmed(true);

        for (Map<String, Integer> item : items) {
            int ticketId = item.get("ticketId");
            int quantity = item.get("quantity");

            Ticket ticket = ticketRepository.findById(ticketId)
                    .orElseThrow(() -> new RuntimeException("Ticket not found"));

            if (ticket.getQuantity() < quantity) {
                throw new QuantityUnavailableException("Not enough tickets available for ticket ID: " + ticketId);
            }

            OrderTicket orderTicket = new OrderTicket();
            orderTicket.setCustomerOrder(order);
            orderTicket.setTicket(ticket);
            orderTicket.setQuantity(quantity);
            order.getOrderTickets().add(orderTicket);

            ticket.setQuantity(ticket.getQuantity() - quantity);
            ticketRepository.save(ticket);
        }

        return customerOrderRepository.save(order);
    }


    @Transactional(readOnly = true)
    public List<CustomerOrder> getOrdersByUser(Authentication authentication) throws UserNotFoundException {
        User user = userRepository.findByUsername(authentication.getName())
                .orElseThrow(() -> new UserNotFoundException("User not found"));
        return customerOrderRepository.findByUser(user);
    }

    @Transactional(readOnly = true)
    public List<CustomerOrder> getOrdersByUserInPeriod(Authentication authentication, LocalDateTime start, LocalDateTime end) throws UserNotFoundException, OrderNotFoundException {
        if (start.isAfter(end)) {
            throw new OrderNotFoundException("Invalid date range: Start date is after end date.");
        }
        User user = userRepository.findByUsername(authentication.getName())
                .orElseThrow(() -> new UserNotFoundException("User not found"));
        List<CustomerOrder> orders = customerOrderRepository.findByUserAndOrderDateBetween(start, end, user);
        if (orders.isEmpty()) {
            throw new OrderNotFoundException("No orders found for the specified period");
        }
        return orders;
    }

    @Transactional(readOnly = true)
    public List<CustomerOrder> getAllOrders() {
        return customerOrderRepository.findAll();
    }
}
