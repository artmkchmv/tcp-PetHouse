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
    @Column(name = "message_id")
    private Long messageId;

    @ManyToOne
    @JoinColumn(name = "message_sender_id", nullable = false)
    private User sender;

    @ManyToOne
    @JoinColumn(name = "message_receiver_id", nullable = false)
    private User receiver;

    @ManyToOne
    @JoinColumn(name = "message_pet_id", nullable = false)
    private Pet pet;

    @Column(name = "message_text", nullable = false, columnDefinition = "TEXT")
    private String messageText;

    @Column(name = "message_timestamp", nullable = false)
    private LocalDateTime messageTimeStamp = LocalDateTime.now();
}
