package com.petshouse.petshouse.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.petshouse.petshouse.entity.Message;

@Repository
public interface MessageRepository extends JpaRepository<Message, Long> {

    @Query("SELECT m FROM Message m WHERE " +
           "(m.sender.id = :userId1 AND m.receiver.id = :userId2) OR " +
           "(m.sender.id = :userId2 AND m.receiver.id = :userId1) " +
           "ORDER BY m.messageTimeStamp ASC")
    List<Message> findConversationBetweenUsers(@Param("userId1") Long userId1, @Param("userId2") Long userId2);

    @Query("SELECT m FROM Message m WHERE m.sender.id = :userId OR m.receiver.id = :userId ORDER BY m.messageTimeStamp ASC")
    List<Message> findAllByUserId(@Param("userId") Long userId);

    @Query("""
        SELECT 
            CASE WHEN m.sender.id = :userId THEN m.receiver.id ELSE m.sender.id END AS otherUserId,
            CASE WHEN m.sender.id = :userId THEN m.receiver.login ELSE m.sender.login END AS otherUserLogin,
            m.messageText AS lastMessageText,
            m.messageTimeStamp AS lastMessageTimestamp
        FROM Message m
        WHERE m.sender.id = :userId OR m.receiver.id = :userId
        AND m.messageTimeStamp = (
            SELECT MAX(m2.messageTimeStamp)
            FROM Message m2
            WHERE 
                (m2.sender.id = m.sender.id AND m2.receiver.id = m.receiver.id)
                OR
                (m2.sender.id = m.receiver.id AND m2.receiver.id = m.sender.id)
        )
        ORDER BY m.messageTimeStamp DESC
        """)
    List<Object[]> findDialogsByUserId(@Param("userId") Long userId);
}
