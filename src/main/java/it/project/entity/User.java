package it.project.entity;


import it.project.utils.UserRole;
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
@Table(name = "user")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Column(name = "firstName", nullable = true, length = 50)
    private String firstName;

    @Column(name = "lastName", nullable = true, length = 50)
    private String lastName;

    @Column(name = "email", unique = true, nullable = true, length = 70)
    private String email;

    @Column(name = "password", nullable = true, length = 70)
    private String password;

    @Column(name = "role")
    private UserRole role;

    private String fullName(){
        return firstName + " " + lastName;
    }


}
