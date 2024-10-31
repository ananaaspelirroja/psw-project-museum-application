package it.project.repositories;

import it.project.entity.Ticket;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TicketRepository extends JpaRepository<Ticket, Integer> {


    boolean existsByName(String name);

    List<Ticket> findByNameContaining(String name);

    Optional<Ticket> findById(Integer id);

    Page<Ticket> findAll(Pageable pageable);

    List<Ticket> findByExhibitionId(int exhibitionId);
}
