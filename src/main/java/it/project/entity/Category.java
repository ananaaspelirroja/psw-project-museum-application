package it.project.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "category")
@Getter
@Setter
@EqualsAndHashCode
@ToString


public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "name", nullable = true, length = 50)
    private String name;

    @Column(name = "description", nullable = true, length = 200)
    private String description;
}
