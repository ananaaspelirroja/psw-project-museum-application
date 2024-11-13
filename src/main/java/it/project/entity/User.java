package it.project.entity;

import it.project.utils.UserRole;
import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

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

    @Column(name = "code", unique = true, nullable = false)
    private String code;

    @Column(name = "username", unique = true, nullable = false, length = 50)
    private String username;

    @Column(name = "first_name", nullable = true, length = 50)
    private String firstName;

    @Column(name = "last_name", nullable = true, length = 50)
    private String lastName;

    @Column(name = "email", unique = true, nullable = true, length = 70)
    private String email;

    @Column(name = "password", nullable = true, length = 70)
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = true)
    private UserRole role;

    @OneToMany(mappedBy = "user", cascade = CascadeType.MERGE)
    private List<CustomerOrder> purchases = new ArrayList<>();;

    public String fullName() {
        return firstName + " " + lastName;
    }

    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singletonList(new SimpleGrantedAuthority(this.role.getName()));
    }
}
