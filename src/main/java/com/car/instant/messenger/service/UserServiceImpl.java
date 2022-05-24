package com.car.instant.messenger.service;

import com.car.instant.messenger.domain.entities.UserRole;
import com.car.instant.messenger.domain.models.service.UserServiceModel;
import com.car.instant.messenger.repository.RoleRepository;
import com.car.instant.messenger.repository.UserRepository;
import com.car.instant.messenger.repository.MessageRepository;
import com.car.instant.messenger.domain.entities.User;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    private final RoleRepository roleRepository;

    private final MessageRepository messageRepository;

    private final ModelMapper modelMapper;

    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, RoleRepository roleRepository, MessageRepository messageRepository, ModelMapper modelMapper, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.messageRepository = messageRepository;
        this.modelMapper = modelMapper;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    private Set<UserRole> getAuthorities(String authority) {
        Set<UserRole> userAuthorities = new HashSet<>();

        userAuthorities.add(this.roleRepository.getByAuthority(authority));

        return userAuthorities;
    }


    private String getUserAuthority(String userId) {
        if (!this.userRepository.findById(userId).isPresent()) {
            return null;
        }
        if (!this.userRepository.findById(userId).get().getAuthorities().stream().findFirst().isPresent()) {
            return null;
        }
        return this.userRepository.findById(userId).get().getAuthorities().stream().findFirst().get().getAuthority();
    }

    @Override
    public boolean createUser(UserServiceModel userServiceModel) {
        User userEntity = this.modelMapper.map(userServiceModel, User.class);
        userEntity.setPassword(this.bCryptPasswordEncoder.encode(userEntity.getPassword()));
        if (this.userRepository.findAll().isEmpty()) {
            userEntity.setAuthorities(this.getAuthorities("ADMIN"));
        } else {
            userEntity.setAuthorities(this.getAuthorities("USER"));
        }

        try {
            userEntity.setEnabled(true);
            this.userRepository.save(userEntity);
        } catch (Exception ignored) {
            return false;
        }

        return true;
    }

    @Override
    public Set<UserServiceModel> getAll() {
        return this.userRepository
                .findAll()
                .stream()
                .map(x -> this.modelMapper.map(x, UserServiceModel.class))
                .collect(Collectors.toUnmodifiableSet());
    }

    @Override
    public boolean promoteUser(String id) {
        User user = this.userRepository
                .findByUsername(id)
                .orElse(null);

        if (user == null) return false;

        String userAuthority = this.getUserAuthority(user.getId());

        if (userAuthority == null) {
            user.setAuthorities(this.getAuthorities("USER"));
            this.userRepository.save(user);
            return true;
        }

        switch (userAuthority) {
            case "USER":
                user.setAuthorities(this.getAuthorities("MODERATOR"));
                break;
            case "MODERATOR":
                user.setAuthorities(this.getAuthorities("ADMIN"));
                break;
            default:
                throw new IllegalArgumentException("There is no role, higher than ADMIN");
        }

        this.userRepository.save(user);
        return true;

    }

    @Override
    public boolean demoteUser(String id) {
        User user = this.userRepository
                .findByUsername(id)
                .orElse(null);

        if (user == null) return false;

        String userAuthority = this.getUserAuthority(user.getId());
        if (userAuthority == null) {
            user.setAuthorities(this.getAuthorities("USER"));
            this.userRepository.save(user);
            return true;
        }


        switch (userAuthority) {
            case "ADMIN":
                user.setAuthorities(this.getAuthorities("MODERATOR"));
                break;
            case "MODERATOR":
                user.setAuthorities(this.getAuthorities("USER"));
                break;
            default:
                throw new IllegalArgumentException("There is no role, lower than USER");
        }

        this.userRepository.save(user);
        return true;
    }

    @Override
    public boolean activateUser(User user) {
        if (user != null && !user.isEnabled()) {
            user.setEnabled(true);
            this.userRepository.saveAndFlush(user);
            return true;
        }
        return false;
    }

    @Override
    public boolean alreadyExistByEmail(String email) {
        return this.userRepository.findByEmail(email).isPresent();
    }

    @Override
    public boolean alreadyExistByUsername(String username) {
        return this.userRepository.findByUsername(username).isPresent();
    }

    @Override
    @Transactional
    public boolean deleteByUsername(String username, Authentication authentication) {
        User deleting = this.userRepository.findByUsername(authentication.getName()).get();
        User toBeDeleted = this.userRepository.findByUsername(username).get();
        try {
            if (this.getUserAuthority(deleting.getId()).equals("ADMIN") && !this.getUserAuthority(toBeDeleted.getId()).equals("ADMIN")) {
                this.userRepository.deleteUserInRoles(toBeDeleted.getId());
                this.userRepository.deleteUserInUsers(toBeDeleted.getId());
                this.userRepository.flush();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public User findByUsername(String username) {
        var temp = this.userRepository.findByUsername(username);
        if (temp.isPresent()) {
            return temp.get();
        }
        return null;
    }

    @Override
    public User findById(String id) {
        if (this.userRepository.findById(id).isPresent()) {
            return this.userRepository.findById(id).get();
        }
        return null;

    }

    @Override
    public LinkedHashSet<User> getAllUsers() {
        return this.userRepository.getAllUsers();
    }

    @Override
    public boolean changePassword(User user, String oldPassword, String newPassword) {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String existingPassword = oldPassword;
        String dbPassword = user.getPassword();

        if (passwordEncoder.matches(existingPassword, dbPassword)) {
            if (newPassword.length() >= 6) {
                user.setPassword(passwordEncoder.encode(newPassword));
                this.userRepository.saveAndFlush(user);
                return true;
            }
        }
        return false;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = this.userRepository
                .findByUsername(username)
                .orElse(null);

        if (user == null) throw new UsernameNotFoundException("No such user.");
        return user;
    }

}
