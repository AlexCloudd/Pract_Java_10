package com.example.movieservice.controller;

import com.example.movieservice.entity.Movie;
import com.example.movieservice.service.MovieService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/movies")
@Tag(name = "Movie Management", description = "API для управления фильмами")
public class MovieController {
    
    @Autowired
    private MovieService movieService;
    
    @GetMapping
    @Operation(summary = "Получить все фильмы", description = "Возвращает список всех фильмов")
    public ResponseEntity<List<Movie>> getAllMovies() {
        List<Movie> movies = movieService.getAllMovies();
        return ResponseEntity.ok(movies);
    }
    
    @GetMapping("/{id}")
    @Operation(summary = "Получить фильм по ID", description = "Возвращает фильм по указанному ID")
    public ResponseEntity<Movie> getMovieById(
            @Parameter(description = "ID фильма") @PathVariable Long id) {
        Optional<Movie> movie = movieService.getMovieById(id);
        return movie.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    @GetMapping("/tmdb/{tmdbId}")
    @Operation(summary = "Получить фильм по TMDB ID", description = "Возвращает фильм по TMDB ID")
    public ResponseEntity<Movie> getMovieByTmdbId(
            @Parameter(description = "TMDB ID фильма") @PathVariable String tmdbId) {
        Optional<Movie> movie = movieService.getMovieByTmdbId(tmdbId);
        return movie.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    @GetMapping("/search")
    @Operation(summary = "Поиск фильмов", description = "Поиск фильмов по названию, режиссеру или актерам")
    public ResponseEntity<List<Movie>> searchMovies(
            @Parameter(description = "Поисковый запрос") @RequestParam String query) {
        List<Movie> movies = movieService.searchMovies(query);
        return ResponseEntity.ok(movies);
    }
    
    @GetMapping("/popular")
    @Operation(summary = "Популярные фильмы", description = "Возвращает список популярных фильмов")
    public ResponseEntity<List<Movie>> getPopularMovies() {
        List<Movie> movies = movieService.getTopMoviesByViewCount();
        return ResponseEntity.ok(movies);
    }
    
    @GetMapping("/trending")
    @Operation(summary = "Трендовые фильмы", description = "Возвращает список трендовых фильмов")
    public ResponseEntity<List<Movie>> getTrendingMovies() {
        List<Movie> movies = movieService.getLatestMovies();
        return ResponseEntity.ok(movies);
    }
    
    @GetMapping("/featured")
    @Operation(summary = "Рекомендуемые фильмы", description = "Возвращает список рекомендуемых фильмов")
    public ResponseEntity<List<Movie>> getFeaturedMovies() {
        List<Movie> movies = movieService.getFeaturedMovies();
        return ResponseEntity.ok(movies);
    }
    
    @GetMapping("/genre/{genre}")
    @Operation(summary = "Фильмы по жанру", description = "Возвращает фильмы определенного жанра")
    public ResponseEntity<List<Movie>> getMoviesByGenre(
            @Parameter(description = "Жанр фильма") @PathVariable String genre) {
        List<Movie> movies = movieService.getMoviesByGenre(genre);
        return ResponseEntity.ok(movies);
    }
    
    @GetMapping("/year/{year}")
    @Operation(summary = "Фильмы по году", description = "Возвращает фильмы определенного года")
    public ResponseEntity<List<Movie>> getMoviesByYear(
            @Parameter(description = "Год выпуска") @PathVariable Integer year) {
        List<Movie> movies = movieService.getMoviesByYear(year);
        return ResponseEntity.ok(movies);
    }
    
    @GetMapping("/genres")
    @Operation(summary = "Получить все жанры", description = "Возвращает список всех жанров")
    public ResponseEntity<List<String>> getAllGenres() {
        List<String> genres = movieService.getAllGenres();
        return ResponseEntity.ok(genres);
    }
    
    @GetMapping("/countries")
    @Operation(summary = "Получить все страны", description = "Возвращает список всех стран")
    public ResponseEntity<List<String>> getAllCountries() {
        List<String> countries = movieService.getAllCountries();
        return ResponseEntity.ok(countries);
    }
    
    @GetMapping("/years")
    @Operation(summary = "Получить все годы", description = "Возвращает список всех годов выпуска")
    public ResponseEntity<List<Integer>> getAllYears() {
        List<Integer> years = movieService.getAllYears();
        return ResponseEntity.ok(years);
    }
    
    @PostMapping
    @Operation(summary = "Создать фильм", description = "Создает новый фильм")
    public ResponseEntity<Movie> createMovie(@Valid @RequestBody Movie movie) {
        Movie createdMovie = movieService.createMovie(movie);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdMovie);
    }
    
    @PutMapping("/{id}")
    @Operation(summary = "Обновить фильм", description = "Обновляет существующий фильм")
    public ResponseEntity<Movie> updateMovie(
            @Parameter(description = "ID фильма") @PathVariable Long id,
            @Valid @RequestBody Movie movie) {
        Optional<Movie> updatedMovie = movieService.updateMovie(id, movie);
        return updatedMovie.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    @DeleteMapping("/{id}")
    @Operation(summary = "Удалить фильм", description = "Удаляет фильм по ID")
    public ResponseEntity<Void> deleteMovie(
            @Parameter(description = "ID фильма") @PathVariable Long id) {
        boolean deleted = movieService.deleteMovie(id);
        return deleted ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }
}




