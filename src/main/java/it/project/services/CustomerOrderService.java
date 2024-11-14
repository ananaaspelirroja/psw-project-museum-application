package it.project.services;

import it.project.entity.CustomerOrder;
import it.project.entity.OrderTicket;
import it.project.entity.Ticket;
import it.project.entity.User;
import it.project.repositories.CustomerOrderRepository;
import it.project.repositories.OrderTicketRepository;
import it.project.repositories.TicketRepository;
import it.project.repositories.UserRepository;
import it.project.utils.exceptions.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

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


    @Transactional(rollbackFor = {QuantityUnavailableException.class, TicketUnavailableException.class})
    public CustomerOrder addToCart(int ticketId, int quantity, Authentication authentication) throws QuantityUnavailableException {
        String userCode = authentication.getName();  // Usa il nome o un altro campo ID dal token

        // Cerca l'utente nella tua tabella User
        Optional<User> userOpt = userRepository.findByCode(userCode);

        User user;
        if (userOpt.isPresent()) {
            user = userOpt.get();
        } else {
            // Se l'utente non esiste, creane uno nuovo
            user = new User();
            user.setCode(userCode); // Imposta ID o altri campi rilevanti
            user.setUsername(authentication.getName());
            userRepository.save(user);
        }

        CustomerOrder cart = customerOrderRepository.findByUserAndConfirmedFalse(user)
                .orElseGet(() -> createNewCart(user)); // Trova o crea un ordine non confermato (carrello)

        Ticket ticket = ticketRepository.findById(ticketId)
                .orElseThrow(() -> new TicketUnavailableException());

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


    @Transactional(rollbackFor = {QuantityUnavailableException.class, TicketUnavailableException.class, InvalidOrderException.class})
    public CustomerOrder createOrder(Authentication authentication) throws QuantityUnavailableException, InvalidOrderException {
        User user = userRepository.findByUsername(authentication.getName())
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        // Recupera il carrello esistente
        CustomerOrder cart = customerOrderRepository.findByUserAndConfirmedFalse(user)
                .orElseThrow(() -> new RuntimeException("Carrello non trovato"));

        if(cart.getOrderTickets().isEmpty()){
            throw new InvalidOrderException("Quantità non valida per l'ordine. Ogni articolo deve avere una quantità maggiore di 0.");
        }

        // Verifica che ogni OrderTicket abbia una quantità maggiore di 0
        for (OrderTicket orderTicket : cart.getOrderTickets()) {
            if (orderTicket.getQuantity() <= 0) {
                throw new InvalidOrderException("Quantità non valida per l'ordine. Ogni articolo deve avere una quantità maggiore di 0.");
            }
        }

        // Verifica la disponibilità per ogni item nel carrello
        for (OrderTicket orderTicket : cart.getOrderTickets()) {
            Ticket ticket = orderTicket.getTicket();

            // Verifica se il biglietto è disponibile
            if (ticket == null) {
                throw new TicketUnavailableException("Il biglietto con ID " + orderTicket.getTicket().getId() + " non è più disponibile.");
            }

            int requestedQuantity = orderTicket.getQuantity();

            if (ticket.getQuantity() < requestedQuantity) {
                throw new QuantityUnavailableException("Quantità insufficiente per il biglietto con ID: " + ticket.getId());
            }

            // Aggiorna la quantità disponibile del biglietto
            ticket.setQuantity(ticket.getQuantity() - requestedQuantity);
            ticketRepository.save(ticket);
        }

        // Conferma il carrello come ordine
        cart.setConfirmed(true);
        return customerOrderRepository.save(cart);
    }


    @Transactional
    public void clearCart(Authentication authentication) {
        String userCode = authentication.getName();

        // Cerca l'utente nella tua tabella User
        User user = userRepository.findByCode(userCode)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        // Trova o crea il carrello non confermato dell'utente
        CustomerOrder cart = customerOrderRepository.findByUserAndConfirmedFalse(user)
                .orElseGet(() -> createNewCart(user));  // Usa createNewCart per creare un carrello vuoto

        // Rimuovi tutti gli OrderTicket associati al carrello
        cart.getOrderTickets().clear();

        // Aggiorna il totale del carrello a zero
        cart.setTotalAmount(0);

        // Salva il carrello aggiornato
        customerOrderRepository.save(cart);
    }


    @Transactional(readOnly = true)
    public List<CustomerOrder> getOrdersByUser(Authentication authentication) throws UserNotFoundException {
        String username = authentication.getName();

        // Stampa per verificare l'username
        System.out.println("Tentativo di recuperare ordini per l'utente con codice: " + username);

        User user = userRepository.findByCode(username)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        // Stampa per confermare che l'utente è stato trovato
        System.out.println("Utente trovato: " + user);

        List<CustomerOrder> orders = customerOrderRepository.findByUserAndConfirmedTrue(user);

        // Stampa il numero di ordini trovati per l'utente
        System.out.println("Numero di ordini trovati per l'utente " + username + ": " + orders.size());

        // Stampa i dettagli di ogni ordine (opzionale)
        for (CustomerOrder order : orders) {
            System.out.println("Dettagli ordine: " + order);
        }

        return orders;
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
        return customerOrderRepository.findByConfirmedTrue();
    }
}
