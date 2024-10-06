package it.project.services;

import it.project.entity.Ticket;
import it.project.repositories.TicketRepository;
import it.project.utils.exceptions.DuplicateTicketNameException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TicketService {

    @Autowired
    private TicketRepository ticketRepository;

    @Transactional
    public int addTicket(Ticket ticket) throws DuplicateTicketNameException {
        if(ticketRepository.existsByName(ticket.getName())){
            throw new DuplicateTicketNameException();
        }
        return ticketRepository.save(ticket).getId();
    }
}
