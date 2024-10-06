package it.project.controllers;

import it.project.entity.Ticket;
import it.project.utils.exceptions.DuplicateTicketNameException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import it.project.services.TicketService;
import org.springframework.http.ResponseEntity;
import javax.validation.Valid;

@RestController
@RequestMapping("/tickets")

public class TicketController {

    private TicketService ticketService;

    @PostMapping
    public ResponseEntity<?> createTicket(@RequestBody @Valid Ticket ticket) {
        try {
            int createdTicket = ticketService.addTicket(ticket);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdTicket); // Pass the Ticket object
        } catch (DuplicateTicketNameException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
