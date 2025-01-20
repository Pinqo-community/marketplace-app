package com.marketplace.auth.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.*;

import java.util.Date;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class InvalidRefreshToken {
    @Id
    @GeneratedValue
    private long id;

    @Column(nullable = false, unique = true)
    private String token;

    private Date expiryDate;
}
