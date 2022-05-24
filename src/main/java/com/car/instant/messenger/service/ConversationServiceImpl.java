package com.car.instant.messenger.service;

import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import com.car.instant.messenger.domain.entities.Conversation;
import com.car.instant.messenger.domain.entities.User;
import com.car.instant.messenger.repository.ConversationRepository;
import com.car.instant.messenger.repository.UserRepository;

@Service
public class ConversationServiceImpl implements ConversationService {

    private final ConversationRepository conversationRepository;
    private final UserRepository userRepository;

    @Autowired
    public ConversationServiceImpl(ConversationRepository conversationRepository,
            UserRepository userRepository) {
        this.conversationRepository = conversationRepository;
        this.userRepository = userRepository;
    }


    @Override
    public Set<Conversation> getAllForUser(Authentication authentication) {
        User user = this.userRepository.findByUsername(authentication.getName()).get();
        return user.getConversations();
    }

    @Override
    public Conversation add(Conversation conversation) {
        return this.conversationRepository.saveAndFlush(conversation);
    }

    @Override
    public Conversation getById(String id) {
        return this.conversationRepository.findById(id).get();
    }

    @Override
    public Conversation update(String id, Conversation modifiedConversation) {
        return this.conversationRepository.findById(id)
                .map(conversation -> {
                    conversation.setUsers(modifiedConversation.getUsers());
                    conversation.setCreatedAt(modifiedConversation.getCreatedAt());
                    conversation.setName(modifiedConversation.getName());
                    conversation.setMessages(modifiedConversation.getMessages());
                    return this.conversationRepository.save(conversation);
                })
                .orElseGet(() -> {
                    modifiedConversation.setId(id);
                    return this.conversationRepository.save(modifiedConversation);
                });
    }

    @Override
    public void deleteById(String id) {
        this.conversationRepository.deleteById(id);
    }

}
