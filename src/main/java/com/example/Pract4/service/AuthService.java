package com.example.Pract4.service;

import com.example.Pract4.dto.AuthResponse;
import com.example.Pract4.dto.LoginRequest;
import com.example.Pract4.dto.RegisterRequest;
import com.example.Pract4.entity.User;
import com.example.Pract4.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;

@Service
@Transactional
public class AuthService {
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private PasswordEncoder passwordEncoder;
    
    public AuthResponse register(RegisterRequest registerRequest) {
        // Проверяем, что пароли совпадают
        if (!registerRequest.getPassword().equals(registerRequest.getConfirmPassword())) {
            return new AuthResponse("Пароли не совпадают", false);
        }
        
        // Проверяем, что пользователь с таким username не существует
        if (userRepository.existsByUsername(registerRequest.getUsername())) {
            return new AuthResponse("Пользователь с таким именем уже существует", false);
        }
        
        // Проверяем, что пользователь с таким email не существует
        if (userRepository.existsByEmail(registerRequest.getEmail())) {
            return new AuthResponse("Пользователь с таким email уже существует", false);
        }
        
        // Создаем нового пользователя
        User user = new User();
        user.setUsername(registerRequest.getUsername());
        user.setEmail(registerRequest.getEmail());
        user.setPassword(passwordEncoder.encode(registerRequest.getPassword())); // Хешируем пароль
        user.setFirstName(registerRequest.getFirstName());
        user.setLastName(registerRequest.getLastName());
        user.setSubscriptionType(User.SubscriptionType.FREE);
        user.setIsActive(true);
        user.setRoles(new HashSet<>());
        user.getRoles().add(User.Role.USER);
        
        User savedUser = userRepository.save(user);
        
        return new AuthResponse(savedUser, "Регистрация прошла успешно", true);
    }
    
    public AuthResponse login(LoginRequest loginRequest) {
        try {
            System.out.println("Attempting login for: " + loginRequest.getUsernameOrEmail());
            
            // Сначала проверим, существует ли пользователь
            User user = userRepository.findByUsernameOrEmail(
                loginRequest.getUsernameOrEmail(), 
                loginRequest.getUsernameOrEmail()
            ).orElse(null);
            
            if (user == null) {
                System.out.println("User not found: " + loginRequest.getUsernameOrEmail());
                return new AuthResponse("Пользователь не найден", false);
            }
            
            System.out.println("User found: " + user.getUsername() + ", active: " + user.getIsActive());
            
            // Простая проверка пароля без AuthenticationManager
            if (!passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
                System.out.println("Invalid password for user: " + user.getUsername());
                return new AuthResponse("Неверный пароль", false);
            }
            
            if (user != null) {
                return new AuthResponse(user, "Вход выполнен успешно", true);
            } else {
                return new AuthResponse("Ошибка аутентификации", false);
            }
            
        } catch (Exception e) {
            System.out.println("Login error: " + e.getMessage());
            e.printStackTrace();
            return new AuthResponse("Неверное имя пользователя или пароль", false);
        }
    }
    
    public AuthResponse getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        
        if (authentication != null && authentication.isAuthenticated() && 
            !(authentication.getPrincipal() instanceof String)) {
            
            User user = userRepository.findByUsername(authentication.getName()).orElse(null);
            if (user != null) {
                return new AuthResponse(user, "Пользователь найден", true);
            }
        }
        
        return new AuthResponse("Пользователь не аутентифицирован", false);
    }
    
    public void logout() {
        SecurityContextHolder.clearContext();
    }
}
