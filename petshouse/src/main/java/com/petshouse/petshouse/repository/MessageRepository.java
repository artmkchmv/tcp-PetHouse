package com.petshouse.petshouse.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.petshouse.petshouse.entity.Message;

@Repository
public interface MessageRepository extends JpaRepository<Message, Long> {

    List<Message> findBySender_IdOrReceiver_Id(Long userId, Long userId2);
}
