package com.example.Pract4.controller;

import com.example.Pract4.dto.AuthResponse;
import com.example.Pract4.model.MovieModel;
import com.example.Pract4.service.AuthService;
import com.example.Pract4.service.OMDbService;
import com.example.Pract4.service.ViewHistoryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Optional;

@Controller
public class MovieDetailController {

    private static final Logger logger = LoggerFactory.getLogger(MovieDetailController.class);

    @Autowired
    private OMDbService omdbService;

    @Autowired
    private AuthService authService;

    @Autowired
    private ViewHistoryService viewHistoryService;

    @GetMapping("/movie/{movieId}")
    public String movieDetail(@PathVariable String movieId, Model model) {
        
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        boolean isAuthenticated = authentication != null && authentication.isAuthenticated() &&
                                 !(authentication.getPrincipal() instanceof String);
        model.addAttribute("isAuthenticated", isAuthenticated);

        if (isAuthenticated) {
            AuthResponse currentUser = authService.getCurrentUser();
            model.addAttribute("currentUser", currentUser);
        }

            try {
                Optional<MovieModel> movie = omdbService.getMovieDetails(movieId);
                if (movie.isPresent()) {
                    MovieModel movieModel = movie.get();
                    
                    // Добавляем фильм в историю просмотров
                    viewHistoryService.addToHistory(movieModel);
                    
                    model.addAttribute("movie", movieModel);
                    return "movie-detail";
                } else {
                    model.addAttribute("error", "Фильм не найден");
                    return "error";
                }
            } catch (Exception e) {
                model.addAttribute("error", "Ошибка при загрузке фильма: " + e.getMessage());
                return "error";
            }
    }

    @GetMapping("/search")
    public String searchMovies(@RequestParam(required = false) String query,
                              @RequestParam(required = false) String genre,
                              @RequestParam(required = false) Integer year,
                              @RequestParam(required = false) String country,
                              Model model) {
        // Проверяем аутентификацию
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        boolean isAuthenticated = authentication != null && authentication.isAuthenticated() &&
                                 !(authentication.getPrincipal() instanceof String);
        model.addAttribute("isAuthenticated", isAuthenticated);

        if (isAuthenticated) {
            AuthResponse currentUser = authService.getCurrentUser();
            model.addAttribute("currentUser", currentUser);
        }

        model.addAttribute("searchQuery", query);
        model.addAttribute("selectedGenre", genre);
        model.addAttribute("selectedYear", year);
        model.addAttribute("selectedCountry", country);

        // Используем расширенный поиск
        try {
            List<MovieModel> movies = omdbService.advancedSearch(query, genre, year, country);
            model.addAttribute("movies", movies);
        } catch (Exception e) {
            model.addAttribute("movies", java.util.Collections.emptyList());
            model.addAttribute("error", "Ошибка при поиске фильмов: " + e.getMessage());
        }

        return "movies";
    }

}



