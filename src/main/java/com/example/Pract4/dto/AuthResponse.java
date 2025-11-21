package com.example.Pract4.dto;

import com.example.Pract4.entity.User;

import java.time.LocalDateTime;
import java.util.Set;

public class AuthResponse {
    
    private Long id;
    private String username;
    private String email;
    private String firstName;
    private String lastName;
    private User.SubscriptionType subscriptionType;
    private Set<User.Role> roles;
    private LocalDateTime createdAt;
    private String message;
    private boolean success;
    
    // Constructors
    public AuthResponse() {}
    
    public AuthResponse(User user, String message, boolean success) {
        this.id = user.getId();
        this.username = user.getUsername();
        this.email = user.getEmail();
        this.firstName = user.getFirstName();
        this.lastName = user.getLastName();
        this.subscriptionType = user.getSubscriptionType();
        this.roles = user.getRoles();
        this.createdAt = user.getCreatedAt();
        this.message = message;
        this.success = success;
    }
    
    public AuthResponse(String message, boolean success) {
        this.message = message;
        this.success = success;
    }
    
    // Getters and Setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public String getUsername() {
        return username;
    }
    
    public void setUsername(String username) {
        this.username = username;
    }
    
    public String getEmail() {
        return email;
    }
    
    public void setEmail(String email) {
        this.email = email;
    }
    
    public String getFirstName() {
        return firstName;
    }
    
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }
    
    public String getLastName() {
        return lastName;
    }
    
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
    
    public User.SubscriptionType getSubscriptionType() {
        return subscriptionType;
    }
    
    public void setSubscriptionType(User.SubscriptionType subscriptionType) {
        this.subscriptionType = subscriptionType;
    }
    
    public Set<User.Role> getRoles() {
        return roles;
    }
    
    public void setRoles(Set<User.Role> roles) {
        this.roles = roles;
    }
    
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
    
    public String getMessage() {
        return message;
    }
    
    public void setMessage(String message) {
        this.message = message;
    }
    
    public boolean isSuccess() {
        return success;
    }
    
    public void setSuccess(boolean success) {
        this.success = success;
    }
}
