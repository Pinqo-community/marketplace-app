package com.marketplace.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Buyer {
    @Id
    @GeneratedValue
    private long id;

    @Column(nullable = false)
    private String firstName;

    @Column(nullable = false)
    private String lastName;

    private String avatarUrl;

    @OneToOne(fetch = FetchType.EAGER)
    private Address address;
}
