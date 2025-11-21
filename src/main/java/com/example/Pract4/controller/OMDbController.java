package com.example.Pract4.controller;

import com.example.Pract4.model.MovieModel;
import com.example.Pract4.service.OMDbService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/omdb")
@Tag(name = "OMDb Integration", description = "API для интеграции с OMDb")
public class OMDbController {

    @Autowired
    private OMDbService omdbService;

    @GetMapping("/popular")
    @Operation(summary = "Получить популярные фильмы", description = "Получает список популярных фильмов из OMDb API")
    public ResponseEntity<List<MovieModel>> getPopularMovies() {
        List<MovieModel> movies = omdbService.getPopularMovies();
        return ResponseEntity.ok(movies);
    }

    @GetMapping("/top-rated")
    @Operation(summary = "Получить топ фильмы", description = "Получает список топ рейтинговых фильмов из OMDb API")
    public ResponseEntity<List<MovieModel>> getTopRatedMovies() {
        List<MovieModel> movies = omdbService.getTopRatedMovies();
        return ResponseEntity.ok(movies);
    }

    @GetMapping("/search")
    @Operation(summary = "Поиск фильмов", description = "Ищет фильмы в OMDb API по запросу")
    public ResponseEntity<List<MovieModel>> searchMovies(
            @Parameter(description = "Поисковый запрос") @RequestParam String query) {
        List<MovieModel> movies = omdbService.searchMovies(query);
        return ResponseEntity.ok(movies);
    }

    @GetMapping("/movie/{imdbId}")
    @Operation(summary = "Получить детали фильма", description = "Получает детальную информацию о фильме из OMDb API по IMDb ID")
    public ResponseEntity<MovieModel> getMovieDetails(
            @Parameter(description = "IMDb ID фильма") @PathVariable String imdbId) {
        return omdbService.getMovieDetails(imdbId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/save/{imdbId}")
    @Operation(summary = "Сохранить фильм из OMDb", description = "Получает фильм из OMDb API и сохраняет в базу данных")
    public ResponseEntity<MovieModel> saveMovieFromOMDb(
            @Parameter(description = "IMDb ID фильма для сохранения") @PathVariable String imdbId) {
        MovieModel savedMovie = omdbService.saveMovieFromOMDb(imdbId);
        if (savedMovie != null) {
            return ResponseEntity.ok(savedMovie);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/test")
    @Operation(summary = "Тест OMDb API", description = "Тестирует подключение к OMDb API")
    public ResponseEntity<String> testOMDbConnection() {
        try {
            List<MovieModel> movies = omdbService.getPopularMovies();
            if (!movies.isEmpty()) {
                return ResponseEntity.ok("OMDb API работает! Найдено фильмов: " + movies.size() +
                        ". Первый фильм: " + movies.get(0).getTitle());
            } else {
                return ResponseEntity.ok("OMDb API недоступен или фильмы не найдены");
            }
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body("Ошибка при тестировании OMDb API: " + e.getMessage());
        }
    }
}
