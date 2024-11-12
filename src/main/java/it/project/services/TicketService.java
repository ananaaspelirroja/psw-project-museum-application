package it.project.services;

import it.project.entity.Ticket;
import it.project.repositories.TicketRepository;
import it.project.utils.exceptions.DuplicateTicketNameException;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.ArrayList;
import java.util.List;


@Service
public class TicketService {

    @Autowired
    private TicketRepository ticketRepository;

    @Transactional(readOnly = false)
    public int addTicket(Ticket ticket) throws DuplicateTicketNameException {
        if(ticketRepository.existsByName(ticket.getName())){
            throw new DuplicateTicketNameException();
        }
        return ticketRepository.save(ticket).getId();
    }

    @Transactional(readOnly = true)
    public List<Ticket> showTicketsByName(String ticketName) {
        return ticketRepository.findByNameContaining(ticketName);
    }

    @Transactional(readOnly = true)
    public Ticket showTicketById(Integer ticketId) {
        return ticketRepository.findById(ticketId).orElse(null);
    }


    @Transactional(readOnly = true)
    public List<Ticket> showAllTickets() {
        return ticketRepository.findAll();
    }


    @Transactional(readOnly = true)
    public List<Ticket> findTicketsByExhibitionId(int exhibitionId) {
        return ticketRepository.findByExhibitionId(exhibitionId);
    }

    @Transactional
    public Ticket updateTicketQuantity(int ticketId, int quantity) {
        Ticket ticket = ticketRepository.findById(ticketId)
                .orElseThrow(() -> new IllegalArgumentException("Ticket not found"));
        ticket.setQuantity(quantity);
        return ticketRepository.save(ticket);
    }

    @Transactional
    public void deleteTicket(int ticketId) {
        ticketRepository.deleteById(ticketId);
    }
}
