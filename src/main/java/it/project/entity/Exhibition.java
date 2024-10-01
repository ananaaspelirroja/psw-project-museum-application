package it.project.entity;

import jakarta.persistence.*;
import lombok.*;

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

    @Column(name = "name", nullable = true, length = 50)
    private String name;

    @Column(name = "description", nullable = true, length = 200)
    private String description;

    @OneToMany(mappedBy = "exhibition", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Ticket> tickets;
}
