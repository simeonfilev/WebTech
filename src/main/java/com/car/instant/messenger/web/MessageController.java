package com.car.instant.messenger.web;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.car.instant.messenger.domain.entities.Message;
import com.car.instant.messenger.service.MessageService;

@RestController
@RequestMapping("messages")
public class MessageController extends BaseController {
    private final MessageService messageService;

    @Autowired
    public MessageController(MessageService messageService) {
        this.messageService = messageService;
    }

    @GetMapping
    List<Message> getAllMessages() {
        return messageService.getAll();
    }

    @PostMapping
    Message addMessage(@RequestBody Message message) {
        return messageService.add(message);
    }

    @GetMapping("/{id}")
    Message getSingleMessage(@PathVariable String id) {
        return this.messageService.getById(id);
    }

    @PutMapping("/{id}")
    Message modifyMessage(@RequestBody Message modifiedMessage, @PathVariable String id) {
        return this.messageService.update(id, modifiedMessage);
    }

    @DeleteMapping("/{id}")
    void deleteMessage(@PathVariable String id) {
        this.messageService.deleteById(id);
    }

}
