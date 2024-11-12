package it.project.controllers;

import it.project.entity.Ticket;
import it.project.utils.ResultMessage;
import it.project.utils.exceptions.DuplicateTicketNameException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import it.project.services.TicketService;
import org.springframework.http.ResponseEntity;
import javax.validation.Valid;
import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("/api/tickets")
public class TicketController {

    @Autowired
    private TicketService ticketService;


    @PostMapping
    //@PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> createTicket(@RequestBody @Valid Ticket ticket) {
        try {
            int createdTicketId = ticketService.addTicket(ticket);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(new ResultMessage("Ticket created successfully", createdTicketId));
        } catch (DuplicateTicketNameException e) {
            return ResponseEntity.badRequest().body(new ResultMessage("Error: " + e.getMessage()));
        }
    }

    @GetMapping("/search/by_name")
    public ResponseEntity<?> findTicketByName(@RequestParam(required = false) String ticketName) {
        List<Ticket> result = ticketService.showTicketsByName(ticketName);
        if (result.isEmpty()) {
            return ResponseEntity.ok(new ResultMessage("No results!"));
        }
        return ResponseEntity.ok(result);
    }

    @GetMapping("/{ticket-id}")
    public ResponseEntity<?> findTicketById(@PathVariable("ticket-id") Integer ticketId) {
        Ticket ticket = ticketService.showTicketById(ticketId);
        if (ticket == null) {
            return ResponseEntity.ok(new ResultMessage("Ticket not found!"));
        }
        return ResponseEntity.ok(ticket);
    }

    @GetMapping("/by-exhibition-id/{exhibitionId}")
    public ResponseEntity<?> findTicketsByExhibitionId(@PathVariable int exhibitionId) {
        List<Ticket> tickets = ticketService.findTicketsByExhibitionId(exhibitionId);
        if (tickets.isEmpty()) {
            return ResponseEntity.ok(new ResultMessage("No tickets found for exhibition ID: " + exhibitionId));
        }
        return ResponseEntity.ok(tickets);
    }


    @GetMapping
    public ResponseEntity<?> showAllTickets() {
        try {
            List<Ticket> result = ticketService.showAllTickets();
            if (result.isEmpty()) {
                return ResponseEntity.ok(new ResultMessage("No results!"));
            }
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            e.printStackTrace(); // Log dell'errore nel server
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred while fetching tickets.");
        }
    }



    @PutMapping("/{ticket-id}/quantity")
    //@PreAuthorize("hasRole('admin')")
    public ResponseEntity<?> updateTicketQuantity(@PathVariable("ticket-id") int ticketId, @RequestParam int quantity) {
        try {
            Ticket updatedTicket = ticketService.updateTicketQuantity(ticketId, quantity);
            return ResponseEntity.ok(updatedTicket);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ResultMessage("Ticket not found"));
        }
    }

    @DeleteMapping("/{ticket-id}")
    //@PreAuthorize("hasRole('admin')")
    public ResponseEntity<?> deleteTicket(@PathVariable("ticket-id") int ticketId) {
        ticketService.deleteTicket(ticketId);
        return ResponseEntity.ok(new ResultMessage("Ticket deleted successfully"));
    }

}

