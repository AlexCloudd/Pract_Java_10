package com.example.Pract4.controller;

import com.example.Pract4.dto.AuthResponse;
import com.example.Pract4.model.MovieModel;
import com.example.Pract4.service.AuthService;
import com.example.Pract4.service.MovieService;
import com.example.Pract4.service.OMDbService;
import com.example.Pract4.service.SessionWatchlistService;
import com.example.Pract4.service.ViewHistoryService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class HomeController {

    @Autowired
    private MovieService movieService;

    @Autowired
    private OMDbService omdbService;

    @Autowired
    private AuthService authService;

    @Autowired
    private ViewHistoryService viewHistoryService;

    @Autowired
    private SessionWatchlistService sessionWatchlistService;

    @GetMapping("/")
    public String home(Model model, HttpSession session) {
        try {
            
            model.addAttribute("featuredMovies", omdbService.getPopularMovies());
           
            model.addAttribute("topMovies", omdbService.getTopRatedMovies());
        
            model.addAttribute("latestMovies", omdbService.getPopularMovies());
        } catch (Exception e) {
            
            model.addAttribute("featuredMovies", java.util.Collections.emptyList());
            model.addAttribute("topMovies", java.util.Collections.emptyList());
            model.addAttribute("latestMovies", java.util.Collections.emptyList());
        }

        
        model.addAttribute("viewHistory", viewHistoryService.getViewHistory());

        
        model.addAttribute("recentWatchlist", sessionWatchlistService.getWatchlist(session));

        
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        boolean isAuthenticated = authentication != null && authentication.isAuthenticated() &&
                                 !(authentication.getPrincipal() instanceof String);

        model.addAttribute("isAuthenticated", isAuthenticated);

        if (isAuthenticated) {
            AuthResponse currentUser = authService.getCurrentUser();
            model.addAttribute("currentUser", currentUser);
        }

        return "index";
    }
    
    @GetMapping("/movies")
    public String movies(@RequestParam(required = false) String search,
                        @RequestParam(required = false) String genre,
                        @RequestParam(required = false) Integer year,
                        @RequestParam(required = false) String country,
                        Model model) {
        model.addAttribute("searchQuery", search);
        model.addAttribute("selectedGenre", genre);
        model.addAttribute("selectedYear", year);
        model.addAttribute("selectedCountry", country);
        
        
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        boolean isAuthenticated = authentication != null && authentication.isAuthenticated() &&
                                 !(authentication.getPrincipal() instanceof String);
        model.addAttribute("isAuthenticated", isAuthenticated);
        
        if (isAuthenticated) {
            AuthResponse currentUser = authService.getCurrentUser();
            model.addAttribute("currentUser", currentUser);
        }
        
        
        try {
            List<MovieModel> movies = java.util.Collections.emptyList();
            
            // Показываем фильмы только если есть поисковый запрос
            if (search != null && !search.trim().isEmpty()) {
                movies = omdbService.advancedSearch(search, genre, year, country);
            }
            
            model.addAttribute("movies", movies);
        } catch (Exception e) {
            model.addAttribute("movies", java.util.Collections.emptyList());
        }
        
        return "movies";
    }
    
    @GetMapping("/watchlists")
    public String watchlists(@RequestParam(required = false) String search,
                            Model model) {
        model.addAttribute("searchQuery", search);
        

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        boolean isAuthenticated = authentication != null && authentication.isAuthenticated() &&
                                 !(authentication.getPrincipal() instanceof String);
        model.addAttribute("isAuthenticated", isAuthenticated);
        
        if (isAuthenticated) {
            AuthResponse currentUser = authService.getCurrentUser();
            model.addAttribute("currentUser", currentUser);
        }

        model.addAttribute("watchlists", java.util.Collections.emptyList());

        return "watchlists";
    }
    
    @GetMapping("/popular")
    public String popular(Model model) {
       
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        boolean isAuthenticated = authentication != null && authentication.isAuthenticated() &&
                                 !(authentication.getPrincipal() instanceof String);
        model.addAttribute("isAuthenticated", isAuthenticated);

        if (isAuthenticated) {
            AuthResponse currentUser = authService.getCurrentUser();
            model.addAttribute("currentUser", currentUser);
        }

        
        try {
            model.addAttribute("movies", omdbService.getPopularMovies());
        } catch (Exception e) {
            
            model.addAttribute("movies", java.util.Collections.emptyList());
        }

        return "movies";
    }
    
    @GetMapping("/trending")
    public String trending(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        boolean isAuthenticated = authentication != null && authentication.isAuthenticated() &&
                                 !(authentication.getPrincipal() instanceof String);
        model.addAttribute("isAuthenticated", isAuthenticated);

        if (isAuthenticated) {
            AuthResponse currentUser = authService.getCurrentUser();
            model.addAttribute("currentUser", currentUser);
        }

        try {
            model.addAttribute("movies", omdbService.getTopRatedMovies());
        } catch (Exception e) {

            model.addAttribute("movies", java.util.Collections.emptyList());
        }

        return "movies";
    }
    
    @GetMapping("/music")
    public String music(@RequestParam(required = false) String search,
                        Model model) {
        model.addAttribute("searchQuery", search);
        
        // Authentication logic
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        boolean isAuthenticated = authentication != null && authentication.isAuthenticated() &&
                                 !(authentication.getPrincipal() instanceof String);
        model.addAttribute("isAuthenticated", isAuthenticated);
        
        if (isAuthenticated) {
            AuthResponse currentUser = authService.getCurrentUser();
            model.addAttribute("currentUser", currentUser);
        }
        
        // For now, return empty list - can be extended with music service later
        try {
            model.addAttribute("tracks", java.util.Collections.emptyList());
        } catch (Exception e) {
            model.addAttribute("tracks", java.util.Collections.emptyList());
        }
        
        return "music";
    }
    
    @GetMapping("/api-docs")
    public String apiDocs(Model model) {
        // Authentication logic
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        boolean isAuthenticated = authentication != null && authentication.isAuthenticated() &&
                                 !(authentication.getPrincipal() instanceof String);
        model.addAttribute("isAuthenticated", isAuthenticated);
        
        if (isAuthenticated) {
            AuthResponse currentUser = authService.getCurrentUser();
            model.addAttribute("currentUser", currentUser);
        }
        
        return "api-docs";
    }
    
}
