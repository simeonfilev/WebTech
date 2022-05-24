package com.car.instant.messenger.service;

import java.util.List;

import com.car.instant.messenger.domain.entities.Message;

public interface MessageService {

    List<Message> getAll();

    Message getById(String id);

    void deleteById(String id);

    Message add(Message message);

    Message update(String id, Message modifiedMessage);
}
