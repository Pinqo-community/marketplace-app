package com.marketplace.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Vendor {
    @Id
    @GeneratedValue
    private long id;

    @Column(nullable = false)
    private String siret;

    @Column(nullable = false)
    private String companyName;

    private String description;

    private String logoUrl;

    @OneToOne(fetch = FetchType.EAGER)
    private Address address;
}
