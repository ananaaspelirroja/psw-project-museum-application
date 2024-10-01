package it.project.entity;

import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@EqualsAndHashCode
@ToString
@Entity
@Table(name = "item")

public class Item {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private int id;

    @Column(name = "name", nullable = true, length = 50)
    private String name;

    @Column(name = "bar_code", nullable = true, length = 70)
    private String barCode;

    @Column(name = "description", nullable = true, length = 500)
    private String description;

    @Column(name = "price", nullable = true)
    private float price;

    @Column(name = "quantity", nullable = true)
    private int quantity;

    @Column(name = "version", nullable = false)
    private long version;

    @ManyToOne(optional = false)
    @JoinColumn(name = "category_id", nullable = false) // Use a dedicated column for the foreign key
    private Category category;

}
