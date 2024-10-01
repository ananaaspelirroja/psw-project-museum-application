package it.project.entity;

import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import java.util.Date;
import java.util.List;


@Getter
@Setter
@EqualsAndHashCode
@ToString
@Entity
@Table(name = "ticket")

public class Ticket {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private int id;

    @Column(name = "name", nullable = true, length = 50)
    private String name;

    @Column(name = "description", nullable = true, length = 500)
    private String description;

    @Column(name = "price", nullable = true)
    private float price;

    @Column(name = "quantity", nullable = true)
    private int quantity;

    @Column(name = "start_time", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date startTime;

    @Column(name = "end_time", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date endTime;

    @Column(name = "version", nullable = false)
    private long version;

    @ManyToOne(optional = false)
    @JoinColumn(name = "exhibition_id", nullable = false)
    private Exhibition exhibition;

    @OneToMany(mappedBy = "ticket")
    private List<OrderTicket> orderTickets;

}
