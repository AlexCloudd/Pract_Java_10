package com.example.ratingservice.controller;

import com.example.ratingservice.dto.RatingRequest;
import com.example.ratingservice.dto.RatingResponse;
import com.example.ratingservice.service.OmdbService;
import com.example.ratingservice.service.RatingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.validation.Valid;
import java.util.List;

@Controller
@RequestMapping("/reviews")
public class ReviewWebController {
    
    @Autowired
    private RatingService ratingService;
    
    @Autowired
    private OmdbService omdbService;
    
    @GetMapping("/search")
    public String searchMovies(@RequestParam(required = false) String query, Model model) {
        System.out.println("=== SEARCH MOVIES START ===");
        System.out.println("Query: " + query);
        
        if (query != null && !query.trim().isEmpty()) {
            System.out.println("Searching for: " + query);
            try {
                List<OmdbService.OmdbMovie> movies = omdbService.searchMovies(query);
                System.out.println("Found movies: " + movies.size());
                model.addAttribute("movies", movies);
                model.addAttribute("query", query);
            } catch (Exception e) {
                System.err.println("Error in search: " + e.getMessage());
                e.printStackTrace();
                model.addAttribute("error", "Ошибка при поиске фильмов: " + e.getMessage());
            }
        } else {
            System.out.println("No query provided");
        }
        
        System.out.println("=== SEARCH MOVIES END ===");
        return "reviews/search";
    }
    
    @GetMapping("/movie/{imdbId}")
    public String movieDetails(@PathVariable String imdbId, 
                             @RequestParam(required = false) Long userId,
                             @RequestParam(required = false) String username,
                             Model model) {
        OmdbService.OmdbMovie movie = omdbService.getMovieByImdbId(imdbId);
        if (movie == null || !movie.isSuccess()) {
            model.addAttribute("error", "Фильм не найден");
            return "reviews/error";
        }
        
        model.addAttribute("movie", movie);
        
        
        List<RatingResponse> existingRatings = ratingService.getRatingsByMovie(getMovieIdFromImdbId(imdbId));
        model.addAttribute("existingRatings", existingRatings);
        
        Double averageRating = ratingService.getAverageRatingByMovie(getMovieIdFromImdbId(imdbId));
        model.addAttribute("averageRating", averageRating);
        
        Long ratingCount = ratingService.getRatingCountByMovie(getMovieIdFromImdbId(imdbId));
        model.addAttribute("ratingCount", ratingCount);
        
        model.addAttribute("userId", userId);
        model.addAttribute("username", username);
        model.addAttribute("isAuthenticated", userId != null);
        
        
        RatingRequest ratingRequest = new RatingRequest();
        ratingRequest.setMovieId(getMovieIdFromImdbId(imdbId));
        if (userId != null) {
            ratingRequest.setUserId(userId);
        }
        model.addAttribute("ratingRequest", ratingRequest);
        
        return "reviews/movie-details";
    }
    
    @PostMapping("/submit")
    public String submitRating(@Valid @ModelAttribute("ratingRequest") RatingRequest ratingRequest,
                             BindingResult bindingResult,
                             RedirectAttributes redirectAttributes,
                             Model model,
                             @RequestParam(required = false) Long userId,
                             @RequestParam(required = false) String username) {
        
        if (bindingResult.hasErrors()) {
            
            OmdbService.OmdbMovie movie = omdbService.getMovieByImdbId(getImdbIdFromMovieId(ratingRequest.getMovieId()));
            if (movie != null && movie.isSuccess()) {
                model.addAttribute("movie", movie);
                model.addAttribute("existingRatings", ratingService.getRatingsByMovie(ratingRequest.getMovieId()));
                model.addAttribute("averageRating", ratingService.getAverageRatingByMovie(ratingRequest.getMovieId()));
                model.addAttribute("ratingCount", ratingService.getRatingCountByMovie(ratingRequest.getMovieId()));
                model.addAttribute("userId", userId);
                model.addAttribute("username", username);
                model.addAttribute("isAuthenticated", userId != null);
                return "reviews/movie-details";
            }
            return "reviews/error";
        }
        
        try {
            RatingResponse response = ratingService.createRating(ratingRequest);
            redirectAttributes.addFlashAttribute("success", "Отзыв успешно добавлен!");
            
            
            String redirectUrl = "redirect:/reviews/movie/" + getImdbIdFromMovieId(ratingRequest.getMovieId());
            if (userId != null && username != null) {
                redirectUrl += "?userId=" + userId + "&username=" + username;
            }
            return redirectUrl;
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            
            
            String redirectUrl = "redirect:/reviews/movie/" + getImdbIdFromMovieId(ratingRequest.getMovieId());
            if (userId != null && username != null) {
                redirectUrl += "?userId=" + userId + "&username=" + username;
            }
            return redirectUrl;
        }
    }
    
    
    private Long getMovieIdFromImdbId(String imdbId) {
        
        if (imdbId.startsWith("tt")) {
            try {
                return Long.parseLong(imdbId.substring(2));
            } catch (NumberFormatException e) {
                
                return Math.abs(imdbId.hashCode()) % 10000L + 1L;
            }
        }
        return Math.abs(imdbId.hashCode()) % 10000L + 1L;
    }
    
    private String getImdbIdFromMovieId(Long movieId) {
    
        return "tt" + String.format("%07d", movieId);
    }
}
