package com.car.instant.messenger.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.car.instant.messenger.domain.entities.Message;

@Repository
public interface MessageRepository extends JpaRepository<Message, String> {

    Optional<Message> findById(String id);

}
