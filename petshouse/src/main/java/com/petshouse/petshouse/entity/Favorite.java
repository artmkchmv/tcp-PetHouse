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
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "fav_user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "fav_pet_id", nullable = false)
    private Pet pet;
}