package com.marketplace.core.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "_user")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class User {
    @Id
    @GeneratedValue
    private long id;

    @Column(unique=true, nullable=false)
    private String email;

    private String password;

    @Column(nullable=false)
    private String provider;

    @Column(nullable=false)
    private boolean enabled;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "user_role", joinColumns = { @JoinColumn(name = "user_id") }, inverseJoinColumns = { @JoinColumn(name = "role_id") })
    @Builder.Default
    private Set<Role> roles = new HashSet<>();

    @OneToOne(fetch = FetchType.EAGER)
    private Buyer buyer;
}
