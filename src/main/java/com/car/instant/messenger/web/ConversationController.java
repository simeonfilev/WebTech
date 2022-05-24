package com.car.instant.messenger.web;

import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.car.instant.messenger.domain.entities.Conversation;
import com.car.instant.messenger.service.ConversationService;

@Controller
@RequestMapping("conversations")
public class ConversationController extends BaseController {
    private final ConversationService conversationService;

    @Autowired
    public ConversationController(ConversationService conversationService) {
        this.conversationService = conversationService;
    }

    @GetMapping
    Set<Conversation> getAllForUser(Authentication authentication) {
        return conversationService.getAllForUser(authentication);
    }

    @PostMapping
    Conversation createConversation(@RequestBody Conversation conversation, Authentication authentication) {
        return conversationService.add(conversation);
    }

    @GetMapping("/{id}")
    Conversation getSingleConversation(@PathVariable String id) {
        return this.conversationService.getById(id);
    }

    @PutMapping("/{id}")
    Conversation modifyConversation(@RequestBody Conversation modifiedConversation, @PathVariable String id) {
        return this.conversationService.update(id, modifiedConversation);
    }

    @DeleteMapping("/{id}")
    void deleteConversation(@PathVariable String id) {
        this.conversationService.deleteById(id);
    }

}
