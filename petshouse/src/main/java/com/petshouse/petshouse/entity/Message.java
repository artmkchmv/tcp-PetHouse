package com.petshouse.petshouse.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "Messages")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Message {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long messageId;

    @ManyToOne
    @JoinColumn(name = "messageSenderId", nullable = false)
    private User sender;

    @ManyToOne
    @JoinColumn(name = "messageReceiverId", nullable = false)
    private User receiver;

    @ManyToOne
    @JoinColumn(name = "messagePetId", nullable = false)
    private Pet pet;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String messageText;

    @Column(nullable = false)
    private LocalDateTime messageTimeStamp = LocalDateTime.now();
}
