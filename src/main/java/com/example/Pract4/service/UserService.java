package com.example.Pract4.service;

import com.example.Pract4.entity.User;
import com.example.Pract4.model.UserModel;
import com.example.Pract4.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class UserService {
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private PasswordEncoder passwordEncoder;
    
    public List<UserModel> getAllUsers() {
        return userRepository.findAll().stream()
                .map(UserModel::new)
                .collect(Collectors.toList());
    }
    
    public Optional<UserModel> getUserById(Long id) {
        return userRepository.findById(id)
                .map(UserModel::new);
    }
    
    public Optional<UserModel> getUserByUsername(String username) {
        return userRepository.findByUsername(username)
                .map(UserModel::new);
    }
    
    public Optional<UserModel> getUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .map(UserModel::new);
    }
    
    public UserModel createUser(UserModel userModel) {
        User user = new User();
        user.setUsername(userModel.getUsername());
        user.setEmail(userModel.getEmail());
        user.setPassword(passwordEncoder.encode(userModel.getPassword()));
        user.setFirstName(userModel.getFirstName());
        user.setLastName(userModel.getLastName());
        user.setSubscriptionType(userModel.getSubscriptionType() != null ? 
                userModel.getSubscriptionType() : User.SubscriptionType.FREE);
        user.setIsActive(true);
        user.getRoles().add(User.Role.USER);
        
        User savedUser = userRepository.save(user);
        return new UserModel(savedUser);
    }
    
    public Optional<UserModel> updateUser(Long id, UserModel userModel) {
        return userRepository.findById(id)
                .map(user -> {
                    user.setFirstName(userModel.getFirstName());
                    user.setLastName(userModel.getLastName());
                    user.setEmail(userModel.getEmail());
                    if (userModel.getPassword() != null && !userModel.getPassword().isEmpty()) {
                        user.setPassword(passwordEncoder.encode(userModel.getPassword()));
                    }
                    user.setSubscriptionType(userModel.getSubscriptionType());
                    user.setIsActive(userModel.getIsActive());
                    return userRepository.save(user);
                })
                .map(UserModel::new);
    }
    
    public boolean deleteUser(Long id) {
        if (userRepository.existsById(id)) {
            userRepository.deleteById(id);
            return true;
        }
        return false;
    }
    
    public List<UserModel> getActiveUsers() {
        return userRepository.findActiveUsers().stream()
                .map(UserModel::new)
                .collect(Collectors.toList());
    }
    
    public List<UserModel> getUsersBySubscriptionType(User.SubscriptionType subscriptionType) {
        return userRepository.findBySubscriptionType(subscriptionType).stream()
                .map(UserModel::new)
                .collect(Collectors.toList());
    }
    
    public List<UserModel> searchUsersByName(String name) {
        return userRepository.findByNameContaining(name).stream()
                .map(UserModel::new)
                .collect(Collectors.toList());
    }
    
    public boolean existsByUsername(String username) {
        return userRepository.existsByUsername(username);
    }
    
    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }
    
    public UserModel changeSubscriptionType(Long userId, User.SubscriptionType subscriptionType) {
        return userRepository.findById(userId)
                .map(user -> {
                    user.setSubscriptionType(subscriptionType);
                    return userRepository.save(user);
                })
                .map(UserModel::new)
                .orElse(null);
    }
}

