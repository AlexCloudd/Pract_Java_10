package com.example.ratingservice.controller;

import com.example.ratingservice.dto.RatingRequest;
import com.example.ratingservice.dto.RatingResponse;
import com.example.ratingservice.service.RatingService;
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
@RequestMapping("/api/ratings")
@Tag(name = "Rating Service", description = "API для управления рейтингами фильмов")
public class RatingController {
    
    @Autowired
    private RatingService ratingService;
    
    @PostMapping
    @Operation(summary = "Создать новый рейтинг", description = "Создает новый рейтинг фильма от пользователя")
    public ResponseEntity<RatingResponse> createRating(@Valid @RequestBody RatingRequest request) {
        try {
            RatingResponse response = ratingService.createRating(request);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    @PutMapping("/{ratingId}")
    @Operation(summary = "Обновить рейтинг", description = "Обновляет существующий рейтинг")
    public ResponseEntity<RatingResponse> updateRating(
            @Parameter(description = "ID рейтинга") @PathVariable Long ratingId,
            @Valid @RequestBody RatingRequest request) {
        try {
            RatingResponse response = ratingService.updateRating(ratingId, request);
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    @GetMapping("/{ratingId}")
    @Operation(summary = "Получить рейтинг по ID", description = "Возвращает рейтинг по его ID")
    public ResponseEntity<RatingResponse> getRatingById(
            @Parameter(description = "ID рейтинга") @PathVariable Long ratingId) {
        try {
            RatingResponse response = ratingService.getRatingById(ratingId);
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }
    
    @GetMapping("/user/{userId}/movie/{movieId}")
    @Operation(summary = "Получить рейтинг пользователя для фильма", description = "Возвращает рейтинг конкретного пользователя для конкретного фильма")
    public ResponseEntity<RatingResponse> getRatingByUserAndMovie(
            @Parameter(description = "ID пользователя") @PathVariable Long userId,
            @Parameter(description = "ID фильма") @PathVariable Long movieId) {
        RatingResponse response = ratingService.getRatingByUserAndMovie(userId, movieId);
        if (response == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(response);
    }
    
    @GetMapping("/user/{userId}")
    @Operation(summary = "Получить все рейтинги пользователя", description = "Возвращает все рейтинги конкретного пользователя")
    public ResponseEntity<List<RatingResponse>> getRatingsByUser(
            @Parameter(description = "ID пользователя") @PathVariable Long userId) {
        List<RatingResponse> responses = ratingService.getRatingsByUser(userId);
        return ResponseEntity.ok(responses);
    }
    
    @GetMapping("/movie/{movieId}")
    @Operation(summary = "Получить все рейтинги фильма", description = "Возвращает все рейтинги конкретного фильма")
    public ResponseEntity<List<RatingResponse>> getRatingsByMovie(
            @Parameter(description = "ID фильма") @PathVariable Long movieId) {
        List<RatingResponse> responses = ratingService.getRatingsByMovie(movieId);
        return ResponseEntity.ok(responses);
    }
    
    @GetMapping("/movie/{movieId}/average")
    @Operation(summary = "Получить средний рейтинг фильма", description = "Возвращает средний рейтинг конкретного фильма")
    public ResponseEntity<Double> getAverageRatingByMovie(
            @Parameter(description = "ID фильма") @PathVariable Long movieId) {
        Double averageRating = ratingService.getAverageRatingByMovie(movieId);
        return ResponseEntity.ok(averageRating);
    }
    
    @GetMapping("/movie/{movieId}/count")
    @Operation(summary = "Получить количество рейтингов фильма", description = "Возвращает количество рейтингов конкретного фильма")
    public ResponseEntity<Long> getRatingCountByMovie(
            @Parameter(description = "ID фильма") @PathVariable Long movieId) {
        Long count = ratingService.getRatingCountByMovie(movieId);
        return ResponseEntity.ok(count);
    }
    
    @GetMapping("/top-rated")
    @Operation(summary = "Получить топ фильмов по рейтингу", description = "Возвращает список топ фильмов по среднему рейтингу")
    public ResponseEntity<List<Object[]>> getTopRatedMovies(
            @Parameter(description = "Минимальное количество рейтингов") @RequestParam(defaultValue = "5") Long minRatings) {
        List<Object[]> topMovies = ratingService.getTopRatedMovies(minRatings);
        return ResponseEntity.ok(topMovies);
    }
    
    @GetMapping("/reviews")
    @Operation(summary = "Получить рейтинги с отзывами", description = "Возвращает все рейтинги, которые содержат отзывы")
    public ResponseEntity<List<RatingResponse>> getRatingsWithReviews() {
        List<RatingResponse> responses = ratingService.getRatingsWithReviews();
        return ResponseEntity.ok(responses);
    }
    
    @GetMapping("/user/{userId}/reviews")
    @Operation(summary = "Получить рейтинги пользователя с отзывами", description = "Возвращает рейтинги конкретного пользователя, которые содержат отзывы")
    public ResponseEntity<List<RatingResponse>> getRatingsWithReviewsByUser(
            @Parameter(description = "ID пользователя") @PathVariable Long userId) {
        List<RatingResponse> responses = ratingService.getRatingsWithReviewsByUser(userId);
        return ResponseEntity.ok(responses);
    }
    
    @GetMapping("/movie/{movieId}/reviews")
    @Operation(summary = "Получить рейтинги фильма с отзывами", description = "Возвращает рейтинги конкретного фильма, которые содержат отзывы")
    public ResponseEntity<List<RatingResponse>> getRatingsWithReviewsByMovie(
            @Parameter(description = "ID фильма") @PathVariable Long movieId) {
        List<RatingResponse> responses = ratingService.getRatingsWithReviewsByMovie(movieId);
        return ResponseEntity.ok(responses);
    }
    
    @GetMapping("/range")
    @Operation(summary = "Получить рейтинги по диапазону оценок", description = "Возвращает рейтинги в указанном диапазоне оценок")
    public ResponseEntity<List<RatingResponse>> getRatingsByRange(
            @Parameter(description = "Минимальная оценка") @RequestParam Integer minRating,
            @Parameter(description = "Максимальная оценка") @RequestParam Integer maxRating) {
        List<RatingResponse> responses = ratingService.getRatingsByRange(minRating, maxRating);
        return ResponseEntity.ok(responses);
    }
    
    @GetMapping("/user/{userId}/range")
    @Operation(summary = "Получить рейтинги пользователя по диапазону оценок", description = "Возвращает рейтинги пользователя в указанном диапазоне оценок")
    public ResponseEntity<List<RatingResponse>> getRatingsByUserAndRange(
            @Parameter(description = "ID пользователя") @PathVariable Long userId,
            @Parameter(description = "Минимальная оценка") @RequestParam Integer minRating,
            @Parameter(description = "Максимальная оценка") @RequestParam Integer maxRating) {
        List<RatingResponse> responses = ratingService.getRatingsByUserAndRange(userId, minRating, maxRating);
        return ResponseEntity.ok(responses);
    }
    
    @GetMapping("/movie/{movieId}/range")
    @Operation(summary = "Получить рейтинги фильма по диапазону оценок", description = "Возвращает рейтинги фильма в указанном диапазоне оценок")
    public ResponseEntity<List<RatingResponse>> getRatingsByMovieAndRange(
            @Parameter(description = "ID фильма") @PathVariable Long movieId,
            @Parameter(description = "Минимальная оценка") @RequestParam Integer minRating,
            @Parameter(description = "Максимальная оценка") @RequestParam Integer maxRating) {
        List<RatingResponse> responses = ratingService.getRatingsByMovieAndRange(movieId, minRating, maxRating);
        return ResponseEntity.ok(responses);
    }
    
    @DeleteMapping("/{ratingId}")
    @Operation(summary = "Удалить рейтинг", description = "Удаляет рейтинг по его ID")
    public ResponseEntity<Void> deleteRating(
            @Parameter(description = "ID рейтинга") @PathVariable Long ratingId) {
        try {
            ratingService.deleteRating(ratingId);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }
    
    @DeleteMapping("/user/{userId}/movie/{movieId}")
    @Operation(summary = "Удалить рейтинг пользователя для фильма", description = "Удаляет рейтинг конкретного пользователя для конкретного фильма")
    public ResponseEntity<Void> deleteRatingByUserAndMovie(
            @Parameter(description = "ID пользователя") @PathVariable Long userId,
            @Parameter(description = "ID фильма") @PathVariable Long movieId) {
        try {
            ratingService.deleteRatingByUserAndMovie(userId, movieId);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
