package com.example.Pract4.controller;

import com.example.Pract4.dto.AuthResponse;
import com.example.Pract4.dto.LoginRequest;
import com.example.Pract4.dto.RegisterRequest;
import com.example.Pract4.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/auth")
public class AuthWebController {
    
    @Autowired
    private AuthService authService;
    
    @GetMapping("/login")
    public String loginPage(Model model) {
        if (isAuthenticated()) {
            return "redirect:/";
        }
        
        model.addAttribute("loginRequest", new LoginRequest());
        return "auth/login";
    }
    
    @GetMapping("/register")
    public String registerPage(Model model) {
        if (isAuthenticated()) {
            return "redirect:/";
        }
        
        model.addAttribute("registerRequest", new RegisterRequest());
        return "auth/register";
    }
    
    @PostMapping("/register")
    public String register(@Valid @ModelAttribute("registerRequest") RegisterRequest registerRequest,
                          BindingResult bindingResult,
                          RedirectAttributes redirectAttributes) {
        
        if (bindingResult.hasErrors()) {
            return "auth/register";
        }
        
        AuthResponse response = authService.register(registerRequest);
        
        if (response.isSuccess()) {
            redirectAttributes.addFlashAttribute("successMessage", response.getMessage());
            return "redirect:/auth/login";
        } else {
            redirectAttributes.addFlashAttribute("errorMessage", response.getMessage());
            return "redirect:/auth/register";
        }
    }
    
    @PostMapping("/logout")
    public String logout(RedirectAttributes redirectAttributes) {
        authService.logout();
        redirectAttributes.addFlashAttribute("successMessage", "Вы успешно вышли из системы");
        return "redirect:/";
    }
    
    private boolean isAuthenticated() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication != null && authentication.isAuthenticated() && 
               !(authentication.getPrincipal() instanceof String);
    }
}
