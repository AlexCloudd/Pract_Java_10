package com.example.Pract4.controller;

import com.example.Pract4.model.MovieModel;
import com.example.Pract4.service.OMDbService;
import com.example.Pract4.service.SessionWatchlistService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/watchlist")
@Tag(name = "Watchlist Management", description = "Управление списками просмотров")
public class WatchlistController {

    private static final Logger logger = LoggerFactory.getLogger(WatchlistController.class);

    @Autowired
    private SessionWatchlistService sessionWatchlistService;

    @Autowired
    private OMDbService omdbService;

    /**
     * Добавляет фильм в список просмотров пользователя (сессия)
     */
    @PostMapping("/add/{imdbId}")
    @Operation(summary = "Добавить фильм в список просмотров", description = "Добавляет фильм в список просмотров текущего пользователя")
    public ResponseEntity<String> addMovieToWatchlist(
            @Parameter(description = "IMDb ID фильма") @PathVariable String imdbId,
            HttpSession session) {
        
        try {
            // Получаем детали фильма из OMDb
            Optional<MovieModel> movieOpt = omdbService.getMovieDetails(imdbId);
            if (movieOpt.isEmpty()) {
                return ResponseEntity.badRequest().body("Фильм не найден");
            }

            MovieModel movie = movieOpt.get();
            
            // Добавляем фильм в список просмотров
            boolean added = sessionWatchlistService.addMovieToWatchlist(
                imdbId, movie.getTitle(), movie.getPosterUrl(), session);
            
            if (added) {
                logger.info("Successfully added movie '{}' to watchlist", movie.getTitle());
                return ResponseEntity.ok("Фильм добавлен в список просмотров");
            } else {
                return ResponseEntity.ok("Фильм уже в списке просмотров");
            }

        } catch (Exception e) {
            logger.error("Error adding movie to watchlist: {}", e.getMessage(), e);
            return ResponseEntity.internalServerError().body("Внутренняя ошибка сервера");
        }
    }

    /**
     * Удаляет фильм из списка просмотров пользователя (сессия)
     */
    @DeleteMapping("/remove/{imdbId}")
    @Operation(summary = "Удалить фильм из списка просмотров", description = "Удаляет фильм из списка просмотров текущего пользователя")
    public ResponseEntity<String> removeMovieFromWatchlist(
            @Parameter(description = "IMDb ID фильма") @PathVariable String imdbId,
            HttpSession session) {
        
        try {
            boolean removed = sessionWatchlistService.removeMovieFromWatchlist(imdbId, session);
            
            if (removed) {
                logger.info("Successfully removed movie '{}' from watchlist", imdbId);
                return ResponseEntity.ok("Фильм удален из списка просмотров");
            } else {
                return ResponseEntity.badRequest().body("Фильм не найден в списке просмотров");
            }

        } catch (Exception e) {
            logger.error("Error removing movie from watchlist: {}", e.getMessage(), e);
            return ResponseEntity.internalServerError().body("Внутренняя ошибка сервера");
        }
    }

    /**
     * Проверяет, есть ли фильм в списке просмотров пользователя (сессия)
     */
    @GetMapping("/check/{imdbId}")
    @Operation(summary = "Проверить наличие фильма в списке", description = "Проверяет, есть ли фильм в списке просмотров текущего пользователя")
    public ResponseEntity<Boolean> isMovieInWatchlist(
            @Parameter(description = "IMDb ID фильма") @PathVariable String imdbId,
            HttpSession session) {
        
        try {
            boolean isInWatchlist = sessionWatchlistService.isMovieInWatchlist(imdbId, session);
            return ResponseEntity.ok(isInWatchlist);

        } catch (Exception e) {
            logger.error("Error checking if movie is in watchlist: {}", e.getMessage(), e);
            return ResponseEntity.internalServerError().body(false);
        }
    }

    /**
     * Получает список просмотров пользователя (сессия)
     */
    @GetMapping("/my")
    @Operation(summary = "Мой список просмотров", description = "Возвращает список просмотров текущего пользователя")
    public String getMyWatchlist(HttpSession session, Model model) {
        
        try {
            List<MovieModel> movies = sessionWatchlistService.getWatchlist(session);
            model.addAttribute("movies", movies);
            model.addAttribute("isAuthenticated", true);
            model.addAttribute("currentUser", null); // Для совместимости с шаблоном
            
            return "watchlist";

        } catch (Exception e) {
            logger.error("Error getting watchlist: {}", e.getMessage(), e);
            model.addAttribute("movies", List.of());
            model.addAttribute("error", "Ошибка при загрузке списка просмотров");
            model.addAttribute("currentUser", null);
            return "watchlist";
        }
    }

    /**
     * AJAX endpoint для добавления фильма в список просмотров (сессия)
     */
    @PostMapping("/ajax/add")
    @ResponseBody
    public ResponseEntity<String> addMovieToWatchlistAjax(
            @RequestParam String imdbId,
            @RequestParam String title,
            @RequestParam String posterUrl,
            HttpSession session) {
        
        try {
            boolean added = sessionWatchlistService.addMovieToWatchlist(imdbId, title, posterUrl, session);
            
            if (added) {
                return ResponseEntity.ok("Фильм добавлен в список просмотров");
            } else {
                return ResponseEntity.ok("Фильм уже в списке просмотров");
            }

        } catch (Exception e) {
            logger.error("Error adding movie to watchlist via AJAX: {}", e.getMessage(), e);
            return ResponseEntity.internalServerError().body("Внутренняя ошибка сервера");
        }
    }
}