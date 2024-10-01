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
@Table(name = "order")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private int id;

    @Column(name = "order_time", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date orderTime;

    @Column(name = "total_amount", nullable = false)
    private int totalAmount;

    @ManyToOne(optional = false)
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "order")
    private List<OrderTicket> orderTickets;




}
