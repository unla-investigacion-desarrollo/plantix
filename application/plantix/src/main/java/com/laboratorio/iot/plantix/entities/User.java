package com.laboratorio.iot.plantix.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.*;

@Entity
@Table(name = "users")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class User implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String email;
    private String password;
    private LocalDateTime registrationDate;
    private long dni;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
        name = "user_role",
        joinColumns = @JoinColumn(name = "user_id"),
        inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private List<Role> roles;

    /*
    extiendo esta clase con los dos metodos extra de abajo para que spring security
    tenga una manera de validar (autenticar y autorizar) usuarios
     */

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Set<GrantedAuthority> authorities = new HashSet<>();
        for (Role role : roles) {
            if(role == null || role.getRole().isBlank()) continue;

            authorities.add(new SimpleGrantedAuthority("ROLE_" + role.getRole()));

            for (Permission permission : role.getPermissions()) {
                if(permission == null || permission.getPermission().isBlank()) continue;

                authorities.add(new SimpleGrantedAuthority(permission.getPermission()));
            }
        }
        return authorities;
    }

    @Override
    public String getUsername() {
        return email;
    }
}
