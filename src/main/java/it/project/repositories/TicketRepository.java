package it.project.repositories;

import it.project.entity.Ticket;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TicketRepository extends JpaRepository<Ticket, Integer> {

    boolean existsByName(String name);

    List<Ticket> findByNameContaining(String name);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    Optional<Ticket> findById(Integer id);

    List<Ticket> findByExhibitionId(int exhibitionId);

    List<Ticket> findAll();
}
