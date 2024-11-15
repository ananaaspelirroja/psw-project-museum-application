package it.project.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "exhibition")
@Getter
@Setter
@EqualsAndHashCode
@ToString
public class Exhibition {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "name", unique = true, nullable = false, length = 50)
    private String name;

    @Column(name = "description", nullable = true, length = 200)
    private String description;

    @OneToMany(mappedBy = "exhibition", cascade = CascadeType.ALL)
    @JsonIgnore // Ignora la serializzazione per evitare cicli infiniti
    @ToString.Exclude // Evita riferimenti ciclici in toString
    private List<Ticket> tickets = new ArrayList<>();;
}
