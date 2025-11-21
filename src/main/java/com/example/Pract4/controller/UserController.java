package com.example.Pract4.controller;

import com.example.Pract4.entity.User;
import com.example.Pract4.model.UserModel;
import com.example.Pract4.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@Tag(name = "User Management", description = "API для управления пользователями")
public class UserController {
    
    @Autowired
    private UserService userService;
    
    @GetMapping
    @Operation(summary = "Получить всех пользователей", description = "Возвращает список всех пользователей")
    public ResponseEntity<List<UserModel>> getAllUsers() {
        List<UserModel> users = userService.getAllUsers();
        return ResponseEntity.ok(users);
    }
    
    @GetMapping("/{id}")
    @Operation(summary = "Получить пользователя по ID", description = "Возвращает пользователя по указанному ID")
    public ResponseEntity<UserModel> getUserById(
            @Parameter(description = "ID пользователя") @PathVariable Long id) {
        return userService.getUserById(id)
                .map(user -> ResponseEntity.ok(user))
                .orElse(ResponseEntity.notFound().build());
    }
    
    @GetMapping("/username/{username}")
    @Operation(summary = "Получить пользователя по имени", description = "Возвращает пользователя по имени пользователя")
    public ResponseEntity<UserModel> getUserByUsername(
            @Parameter(description = "Имя пользователя") @PathVariable String username) {
        return userService.getUserByUsername(username)
                .map(user -> ResponseEntity.ok(user))
                .orElse(ResponseEntity.notFound().build());
    }
    
    @GetMapping("/email/{email}")
    @Operation(summary = "Получить пользователя по email", description = "Возвращает пользователя по email адресу")
    public ResponseEntity<UserModel> getUserByEmail(
            @Parameter(description = "Email пользователя") @PathVariable String email) {
        return userService.getUserByEmail(email)
                .map(user -> ResponseEntity.ok(user))
                .orElse(ResponseEntity.notFound().build());
    }
    
    @PostMapping
    @Operation(summary = "Создать нового пользователя", description = "Создает нового пользователя в системе")
    public ResponseEntity<UserModel> createUser(@Valid @RequestBody UserModel userModel) {
        if (userService.existsByUsername(userModel.getUsername())) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
        if (userService.existsByEmail(userModel.getEmail())) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
        
        UserModel createdUser = userService.createUser(userModel);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdUser);
    }
    
    @PutMapping("/{id}")
    @Operation(summary = "Обновить пользователя", description = "Обновляет информацию о пользователе")
    public ResponseEntity<UserModel> updateUser(
            @Parameter(description = "ID пользователя") @PathVariable Long id,
            @Valid @RequestBody UserModel userModel) {
        return userService.updateUser(id, userModel)
                .map(user -> ResponseEntity.ok(user))
                .orElse(ResponseEntity.notFound().build());
    }
    
    @DeleteMapping("/{id}")
    @Operation(summary = "Удалить пользователя", description = "Удаляет пользователя из системы")
    public ResponseEntity<Void> deleteUser(
            @Parameter(description = "ID пользователя") @PathVariable Long id) {
        if (userService.deleteUser(id)) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
    
    @GetMapping("/active")
    @Operation(summary = "Получить активных пользователей", description = "Возвращает список всех активных пользователей")
    public ResponseEntity<List<UserModel>> getActiveUsers() {
        List<UserModel> users = userService.getActiveUsers();
        return ResponseEntity.ok(users);
    }
    
    @GetMapping("/subscription/{subscriptionType}")
    @Operation(summary = "Получить пользователей по типу подписки", description = "Возвращает пользователей с определенным типом подписки")
    public ResponseEntity<List<UserModel>> getUsersBySubscriptionType(
            @Parameter(description = "Тип подписки") @PathVariable User.SubscriptionType subscriptionType) {
        List<UserModel> users = userService.getUsersBySubscriptionType(subscriptionType);
        return ResponseEntity.ok(users);
    }
    
    @GetMapping("/search")
    @Operation(summary = "Поиск пользователей по имени", description = "Ищет пользователей по имени или фамилии")
    public ResponseEntity<List<UserModel>> searchUsersByName(
            @Parameter(description = "Имя для поиска") @RequestParam String name) {
        List<UserModel> users = userService.searchUsersByName(name);
        return ResponseEntity.ok(users);
    }
    
    @PutMapping("/{id}/subscription")
    @Operation(summary = "Изменить тип подписки", description = "Изменяет тип подписки пользователя")
    public ResponseEntity<UserModel> changeSubscriptionType(
            @Parameter(description = "ID пользователя") @PathVariable Long id,
            @Parameter(description = "Новый тип подписки") @RequestParam User.SubscriptionType subscriptionType) {
        UserModel user = userService.changeSubscriptionType(id, subscriptionType);
        if (user != null) {
            return ResponseEntity.ok(user);
        }
        return ResponseEntity.notFound().build();
    }
}

