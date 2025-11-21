package com.example.Pract4.model;

import com.example.Pract4.entity.User;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.Set;

public class UserModel {
    
    private Long id;
    
    @NotBlank
    @Size(max = 50)
    private String username;
    
    @NotBlank
    @Size(max = 100)
    @Email
    private String email;
    
    @NotBlank
    @Size(max = 100)
    private String password;
    
    @Size(max = 100)
    private String firstName;
    
    @Size(max = 100)
    private String lastName;
    
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private User.SubscriptionType subscriptionType;
    private Boolean isActive;
    private Set<User.Role> roles;
    
    // Constructors
    public UserModel() {}
    
    public UserModel(User user) {
        this.id = user.getId();
        this.username = user.getUsername();
        this.email = user.getEmail();
        this.firstName = user.getFirstName();
        this.lastName = user.getLastName();
        this.createdAt = user.getCreatedAt();
        this.updatedAt = user.getUpdatedAt();
        this.subscriptionType = user.getSubscriptionType();
        this.isActive = user.getIsActive();
        this.roles = user.getRoles();
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
    
    public String getPassword() {
        return password;
    }
    
    public void setPassword(String password) {
        this.password = password;
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
    
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
    
    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
    
    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
    
    public User.SubscriptionType getSubscriptionType() {
        return subscriptionType;
    }
    
    public void setSubscriptionType(User.SubscriptionType subscriptionType) {
        this.subscriptionType = subscriptionType;
    }
    
    public Boolean getIsActive() {
        return isActive;
    }
    
    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }
    
    public Set<User.Role> getRoles() {
        return roles;
    }
    
    public void setRoles(Set<User.Role> roles) {
        this.roles = roles;
    }
}

