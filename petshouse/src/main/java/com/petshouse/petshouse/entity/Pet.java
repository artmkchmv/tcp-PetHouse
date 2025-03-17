package com.petshouse.petshouse.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "pets")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Pet {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long petId;

    @Column(nullable = false, length = 50)
    private String petName;

    @Column(nullable = false)
    private int petAge;

    @Column(nullable = false, length = 50)
    private String petType;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String petDescription;

    @Column(nullable = false, length = 20)
    private String petStatus = "available";

    @ManyToOne
    @JoinColumn(name = "petOwnerId", nullable = false)
    private User petOwner;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String petPhotoURL;
}
