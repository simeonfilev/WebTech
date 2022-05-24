package com.car.instant.messenger.service;

import java.util.List;
import java.util.Set;

import org.springframework.security.core.Authentication;

import com.car.instant.messenger.domain.entities.Conversation;

public interface ConversationService {

    Set<Conversation> getAllForUser(Authentication authentication);

    Conversation add(Conversation conversation);

    Conversation getById(String id);

    Conversation update(String id, Conversation modifiedConversation);

    void deleteById(String id);
}
