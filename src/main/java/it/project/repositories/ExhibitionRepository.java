package it.project.repositories;

import it.project.entity.Exhibition;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ExhibitionRepository extends JpaRepository<Exhibition, Integer> { // JpaRepository fornisce automaticamente i metodi CRUD

    @Query("SELECT e FROM Exhibition e WHERE e.name LIKE %?1%")
    List<Exhibition> findByNameLike(String name);

}
