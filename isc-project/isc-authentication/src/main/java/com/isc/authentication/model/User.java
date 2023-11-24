package com.isc.authentication.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.isc.authentication.model.enums.UserTypeEnum;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.domain.Persistable;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;


@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Data
public class User implements UserDetails, Persistable<Long> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    @JsonIgnore
    private String password;

    @Column(unique = true)
    private String uuid;

    @Column(unique = true)
    private String username;

    private String name;

    private String surname;

    @Column(unique = true, nullable = false)
    private Long registryId;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private UserTypeEnum usertype;

    @ManyToMany(cascade = CascadeType.MERGE, fetch = FetchType.EAGER)
    @JoinTable(
            name = "user_role",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "authority_id"))
    private Set<Role> authorities = new HashSet<>();

    @Column(unique = true)
    private int phoneNumber;
    @Column(unique = true)
    private String email;

    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime dateOfCreation;


    @Column(nullable = false)
    private boolean enabled;

    @Column(nullable = false)
    private boolean accountNonLocked;

    @Column(nullable = false)
    private boolean accountNonExpired;

    @Column(nullable = false)
    private boolean credentialsNonExpired;

    @Override
    public boolean isNew() {
        return Objects.isNull(id);
    }

    @Override
    public String getUsername() {
        return email;
    }

}
