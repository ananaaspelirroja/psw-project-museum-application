package it.project.controllers;

import it.project.entity.Ticket;
import it.project.utils.ResultMessage;
import it.project.utils.exceptions.DuplicateTicketNameException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import it.project.services.TicketService;
import org.springframework.http.ResponseEntity;
import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/tickets")
public class TicketController {

    private TicketService ticketService;


    @PostMapping
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
    public List<Ticket> showAllTickets() {
        return ticketService.showAllTickets();
    }

    @GetMapping("/paged")
    public ResponseEntity showAllTickets(@RequestParam(value = "pageNumber", defaultValue = "0") int pageNumber, @RequestParam(value = "pageSize", defaultValue = "10") int pageSize, @RequestParam(value = "sortBy", defaultValue = "id") String sortBy) {
        List<Ticket> result = ticketService.showAllTickets(pageNumber, pageSize, sortBy);
        if ( result.size() <= 0 ) {
            return new ResponseEntity<>(new ResultMessage("No results!"), HttpStatus.OK);
        }
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

}

