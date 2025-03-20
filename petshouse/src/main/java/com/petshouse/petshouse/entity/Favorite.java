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
    @EmbeddedId
    private FavoriteId id;

    @ManyToOne
    @MapsId("user")
    @JoinColumn(name = "favUserId", nullable = false)
    private User user;

    @ManyToOne
    @MapsId("pet")
    @JoinColumn(name = "favPetId", nullable = false)
    private Pet pet;
}
