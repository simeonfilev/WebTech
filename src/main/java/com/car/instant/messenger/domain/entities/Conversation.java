package com.car.instant.messenger.domain.entities;

import java.sql.Timestamp;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "conversation")
public class Conversation extends BaseEntity {

    private String name;

    private Timestamp createdAt;

    private Set<User> users;

    private Set<Message> messages;

    public Conversation() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    @ManyToMany(mappedBy = "conversations")
    public Set<User> getUsers() {
        return users;
    }

    public void setUsers(Set<User> cars) {
        this.users = cars;
    }

    @OneToMany(mappedBy = "conversation", targetEntity = Message.class)
    @Column(name = "converastion_messages", nullable = true)
    public Set<Message> getMessages() {
        return messages;
    }

    public void setMessages(Set<Message> messages) {
        this.messages = messages;
    }
}
