package com.car.instant.messenger.service;


import com.car.instant.messenger.domain.models.service.UserServiceModel;
import com.car.instant.messenger.domain.entities.User;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.*;

public interface UserService extends UserDetailsService {
    boolean createUser(UserServiceModel userServiceModel);

    Set<UserServiceModel> getAll();

    boolean promoteUser(String id);

    boolean demoteUser(String id);

    boolean activateUser(User user);

    boolean alreadyExistByEmail(String email);

    boolean alreadyExistByUsername(String username);

    boolean deleteByUsername(String username, Authentication authentication);

    User findByUsername(String username);

    User findById(String id);

    LinkedHashSet<User> getAllUsers();

    boolean changePassword(User user, String oldPassword, String newPassword);

}
