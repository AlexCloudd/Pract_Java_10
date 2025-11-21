package com.example.Pract4.client;

import com.example.Pract4.model.UserModel;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(name = "user-service", url = "http://localhost:8082")
public interface UserServiceClient {

    @GetMapping("/api/users")
    List<UserModel> getAllUsers();

    @GetMapping("/api/users/{id}")
    UserModel getUserById(@PathVariable("id") Long id);

    @PostMapping("/api/users")
    UserModel createUser(@RequestBody UserModel user);

    @PutMapping("/api/users/{id}")
    UserModel updateUser(@PathVariable("id") Long id, @RequestBody UserModel user);

    @DeleteMapping("/api/users/{id}")
    void deleteUser(@PathVariable("id") Long id);

    @GetMapping("/api/users/search")
    List<UserModel> searchUsers(@RequestParam("query") String query);

    @GetMapping("/api/users/username/{username}")
    UserModel getUserByUsername(@PathVariable("username") String username);

    @GetMapping("/api/users/email/{email}")
    UserModel getUserByEmail(@PathVariable("email") String email);
}
