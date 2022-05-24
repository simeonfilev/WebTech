package com.car.instant.messenger.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.car.instant.messenger.domain.entities.Message;
import com.car.instant.messenger.repository.MessageRepository;

@Service
public class MessageServiceImpl implements MessageService {

    private final MessageRepository messageRepository;

    @Autowired
    public MessageServiceImpl(MessageRepository messageRepository) {
        this.messageRepository = messageRepository;
    }

    @Override
    public List<Message> getAll() {
        return this.messageRepository.findAll();
    }

    @Override
    public Message getById(String id) {
        return this.messageRepository.findById(id).get();
    }

    @Override
    public void deleteById(String id) {
        this.messageRepository.deleteById(id);
    }

    @Override
    public Message add(Message message) {
        return this.messageRepository.saveAndFlush(message);
    }

    @Override
    public Message update(String id, Message modifiedMessage) {
        return this.messageRepository.findById(id)
                .map(message -> {
                    message.setSentAt(modifiedMessage.getSentAt());
                    message.setText(modifiedMessage.getText());
                    message.setConversation(modifiedMessage.getConversation());
                    return this.messageRepository.save(message);
                })
                .orElseGet(() -> {
                    modifiedMessage.setId(id);
                    return this.messageRepository.save(modifiedMessage);
                });
    }

}
