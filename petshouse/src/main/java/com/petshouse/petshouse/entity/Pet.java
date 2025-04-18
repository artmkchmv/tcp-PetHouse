package com.petshouse.petshouse.entity;

import com.petshouse.petshouse.enums.PetStatus;
import com.petshouse.petshouse.enums.PetType;

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

    @Enumerated(EnumType.STRING)
    @Column(name = "pet_type", nullable = false, length = 50)
    private PetType petType;

    @Column(name = "pet_description", nullable = false, columnDefinition = "TEXT")
    private String petDescription;

    @Enumerated(EnumType.STRING)
    @Column(name = "pet_status", nullable = false, length = 20)
    private PetStatus petStatus = PetStatus.AVAILABLE;

    @ManyToOne
    @JoinColumn(name = "pet_owner_id", nullable = false)
    private User petOwner;

    @Column(name = "pet_photo_url", nullable = false, columnDefinition = "TEXT")
    private String petPhotoURL;
}
