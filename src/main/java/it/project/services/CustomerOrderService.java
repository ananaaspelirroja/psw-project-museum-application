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
    public CustomerOrder addOrder(CustomerOrder customerOrder, Authentication authentication) throws QuantityUnavailableException {
        User user = userRepository.findByUsername(authentication.getName())
                .orElseThrow(() -> new UserNotFoundException("User not found"));
        customerOrder.setUser(user);

        CustomerOrder result = customerOrderRepository.save(customerOrder);
        for (OrderTicket orderTicket : result.getOrderTickets()) {
            orderTicket.setCustomerOrder(result);

            Ticket ticket = orderTicket.getTicket();
            int newQuantity = ticket.getQuantity() - orderTicket.getQuantity();
            if (newQuantity < 0) {
                throw new QuantityUnavailableException("Not enough tickets available");
            }
            ticket.setQuantity(newQuantity);
            ticketRepository.save(ticket); // Update ticket quantity
            orderTicketRepository.save(orderTicket); // Save each order ticket
        }
        return result;
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
        List<CustomerOrder> orders = customerOrderRepository.findByUserAndOrderDateBetween(user, start, end);
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
