package com.marketplace.entity;

import com.marketplace.model.RoleType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Role {
    @Id
    @GeneratedValue
    private long id;

    @Column(unique=true, nullable=false)
    @Enumerated(EnumType.STRING)
    private RoleType name;

    public Role(RoleType name) {
        this.name = name;
    }
}
