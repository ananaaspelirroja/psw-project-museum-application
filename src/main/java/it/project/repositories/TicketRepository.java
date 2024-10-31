package it.project.repositories;

import it.project.entity.Ticket;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TicketRepository extends JpaRepository<Ticket, Integer> {

    // Metodo per verificare l'esistenza di un ticket con un nome specifico
    boolean existsByName(String name);

    // Metodo per cercare i ticket che contengono una stringa specifica nel nome
    List<Ticket> findByNameContaining(String name);

    // Metodo per cercare un ticket per ID
    Optional<Ticket> findById(Integer id);

    // Metodo per cercare tutti i ticket con paginazione
    Page<Ticket> findAll(Pageable pageable);
}
