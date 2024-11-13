package it.project.repositories;

import it.project.entity.CustomerOrder;

import it.project.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface CustomerOrderRepository extends JpaRepository<CustomerOrder, Integer> {

    List<CustomerOrder> findByUser(User user);

    @Query("select o from CustomerOrder o where o.orderTime > ?1 and o.orderTime < ?2 and o.user = ?3")
    List<CustomerOrder> findByUserAndOrderDateBetween(LocalDateTime startDate, LocalDateTime endDate, User user);

    Optional<CustomerOrder> findByUserAndConfirmedFalse(User user);
}

