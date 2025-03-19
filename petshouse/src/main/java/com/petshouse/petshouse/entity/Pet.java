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
    @Column(name = "pet_id")
    private Long petId;

    @Column(name = "pet_name", nullable = false, length = 50)
    private String petName;

    @Column(name = "pet_age", nullable = false)
    private int petAge;

    @Column(name = "pet_type", nullable = false, length = 50)
    private String petType;

    @Column(name = "pet_description", nullable = false, columnDefinition = "TEXT")
    private String petDescription;

    @Column(name = "pet_status", nullable = false, length = 20)
    private String petStatus = "available";

    @ManyToOne
    @JoinColumn(name = "petOwnerId", nullable = false)
    private User petOwner;

    @Column(name = "pet_photo_url", nullable = false, columnDefinition = "TEXT")
    private String petPhotoURL;
}
