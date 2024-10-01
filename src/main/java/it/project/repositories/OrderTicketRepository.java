package it.project.repositories;

import it.project.entity.Exhibition;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderTicketRepository extends JpaRepository<Exhibition, Integer> {
}
