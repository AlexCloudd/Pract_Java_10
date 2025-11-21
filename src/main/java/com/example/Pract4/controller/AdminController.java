package com.example.Pract4.controller;

import com.example.Pract4.model.MovieModel;
import com.example.Pract4.model.UserModel;
import com.example.Pract4.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private AdminService adminService;

    @GetMapping
    public String adminDashboard(Model model) {
        long totalMovies = adminService.getTotalMovies();
        long totalUsers = 0; 
        long totalRatings = adminService.getTotalRatings();
        
        model.addAttribute("totalMovies", totalMovies);
        model.addAttribute("totalUsers", totalUsers);
        model.addAttribute("totalRatings", totalRatings);
        
        return "admin/dashboard";
    }


    @GetMapping("/movies")
    public String moviesList(Model model) {
        List<MovieModel> movies = adminService.getAllMovies();
        model.addAttribute("movies", movies);
        return "admin/movies/list";
    }

    @GetMapping("/movies/add")
    public String addMovieForm(Model model) {
        model.addAttribute("movie", new MovieModel());
        return "admin/movies/add";
    }

    @PostMapping("/movies/add")
    public String addMovie(@ModelAttribute MovieModel movie, RedirectAttributes redirectAttributes) {
        try {
            MovieModel savedMovie = adminService.createMovie(movie);
            redirectAttributes.addFlashAttribute("success", "Фильм '" + savedMovie.getTitle() + "' успешно добавлен!");
            return "redirect:/admin/movies";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Ошибка при добавлении фильма: " + e.getMessage());
            return "redirect:/admin/movies/add";
        }
    }

    @GetMapping("/movies/edit/{id}")
    public String editMovieForm(@PathVariable Long id, Model model) {
        MovieModel movie = adminService.getMovieById(id);
        if (movie == null) {
            return "redirect:/admin/movies";
        }
        model.addAttribute("movie", movie);
        return "admin/movies/edit";
    }

    @PostMapping("/movies/edit/{id}")
    public String updateMovie(@PathVariable Long id, @ModelAttribute MovieModel movie, RedirectAttributes redirectAttributes) {
        try {
            MovieModel updatedMovie = adminService.updateMovie(id, movie);
            redirectAttributes.addFlashAttribute("success", "Фильм '" + updatedMovie.getTitle() + "' успешно обновлен!");
            return "redirect:/admin/movies";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Ошибка при обновлении фильма: " + e.getMessage());
            return "redirect:/admin/movies/edit/" + id;
        }
    }

    @PostMapping("/movies/delete/{id}")
    public String deleteMovie(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            String movieTitle = adminService.getMovieById(id).getTitle();
            adminService.deleteMovie(id);
            redirectAttributes.addFlashAttribute("success", "Фильм '" + movieTitle + "' успешно удален!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Ошибка при удалении фильма: " + e.getMessage());
        }
        return "redirect:/admin/movies";
    }


    @GetMapping("/users")
    public String usersList(Model model) {
        List<UserModel> users = adminService.getAllUsers();
        model.addAttribute("users", users);
        return "admin/users/list";
    }

    @GetMapping("/users/add")
    public String addUserForm(Model model) {
        model.addAttribute("user", new UserModel());
        return "admin/users/add";
    }

    @PostMapping("/users/add")
    public String addUser(@ModelAttribute UserModel user, RedirectAttributes redirectAttributes) {
        try {
            UserModel savedUser = adminService.createUser(user);
            redirectAttributes.addFlashAttribute("success", "Пользователь '" + savedUser.getUsername() + "' успешно добавлен!");
            return "redirect:/admin/users";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Ошибка при добавлении пользователя: " + e.getMessage());
            return "redirect:/admin/users/add";
        }
    }

    @GetMapping("/users/edit/{id}")
    public String editUserForm(@PathVariable Long id, Model model) {
        UserModel user = adminService.getUserById(id);
        if (user == null) {
            return "redirect:/admin/users";
        }
        model.addAttribute("user", user);
        return "admin/users/edit";
    }

    @PostMapping("/users/edit/{id}")
    public String updateUser(@PathVariable Long id, @ModelAttribute UserModel user, RedirectAttributes redirectAttributes) {
        try {
            UserModel updatedUser = adminService.updateUser(id, user);
            redirectAttributes.addFlashAttribute("success", "Пользователь '" + updatedUser.getUsername() + "' успешно обновлен!");
            return "redirect:/admin/users";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Ошибка при обновлении пользователя: " + e.getMessage());
            return "redirect:/admin/users/edit/" + id;
        }
    }

    @PostMapping("/users/delete/{id}")
    public String deleteUser(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            String username = adminService.getUserById(id).getUsername();
            adminService.deleteUser(id);
            redirectAttributes.addFlashAttribute("success", "Пользователь '" + username + "' успешно удален!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Ошибка при удалении пользователя: " + e.getMessage());
        }
        return "redirect:/admin/users";
    }

    @PostMapping("/users/toggle-status/{id}")
    public String toggleUserStatus(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            UserModel user = adminService.getUserById(id);
            user.setIsActive(!user.getIsActive());
            adminService.updateUser(id, user);
            String status = user.getIsActive() ? "активирован" : "деактивирован";
            redirectAttributes.addFlashAttribute("success", "Пользователь '" + user.getUsername() + "' " + status + "!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Ошибка при изменении статуса пользователя: " + e.getMessage());
        }
        return "redirect:/admin/users";
    }


    @GetMapping("/movies/search")
    public String searchMovies(@RequestParam(required = false) String query, Model model) {
        if (query != null && !query.trim().isEmpty()) {
            List<MovieModel> movies = adminService.searchMovies(query);
            model.addAttribute("movies", movies);
            model.addAttribute("query", query);
        } else {
            List<MovieModel> movies = adminService.getAllMovies();
            model.addAttribute("movies", movies);
        }
        return "admin/movies/list";
    }

    @GetMapping("/users/search")
    public String searchUsers(@RequestParam(required = false) String query, Model model) {
        if (query != null && !query.trim().isEmpty()) {
            List<UserModel> users = adminService.searchUsers(query);
            model.addAttribute("users", users);
            model.addAttribute("query", query);
        } else {
            List<UserModel> users = adminService.getAllUsers();
            model.addAttribute("users", users);
        }
        return "admin/users/list";
    }
}
