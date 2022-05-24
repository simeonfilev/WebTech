package com.car.instant.messenger.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.car.instant.messenger.domain.entities.Conversation;

@Repository
public interface ConversationRepository extends JpaRepository<Conversation, String> {

}
