package com.petshouse.petshouse.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.petshouse.petshouse.entity.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    
    User findByLogin(String login);
}
