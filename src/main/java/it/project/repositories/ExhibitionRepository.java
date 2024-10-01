package it.project.repositories;

import it.project.entity.Exhibition;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository

public interface ExhibitionRepository extends JpaRepository<Exhibition, Integer> {

    Exhibition findByName(String name);
    Exhibition findById(int id);

    boolean existByName(String name);
    boolean existById(int id);


}
