package com.petshouse.petshouse.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "favorites")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Favorite {
    @Id
    @ManyToOne
    @JoinColumn(name = "favUserId", nullable = false)
    private User user;

    @Id
    @ManyToOne
    @JoinColumn(name = "favPetId", nullable = false)
    private Pet pet;
}
