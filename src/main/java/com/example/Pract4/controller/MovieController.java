package com.example.Pract4.controller;

import com.example.Pract4.entity.User;
import com.example.Pract4.model.MovieModel;
import com.example.Pract4.service.MovieService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/movies")
@Tag(name = "Movie Management", description = "API для управления фильмами")
public class MovieController {
    
    @Autowired
    private MovieService movieService;
    
    @GetMapping
    @Operation(summary = "Получить все фильмы", description = "Возвращает список всех фильмов")
    public ResponseEntity<List<MovieModel>> getAllMovies() {
        List<MovieModel> movies = movieService.getAllMovies();
        return ResponseEntity.ok(movies);
    }
    
    @GetMapping("/{id}")
    @Operation(summary = "Получить фильм по ID", description = "Возвращает фильм по указанному ID")
    public ResponseEntity<MovieModel> getMovieById(
            @Parameter(description = "ID фильма") @PathVariable Long id) {
        return movieService.getMovieById(id)
                .map(movie -> ResponseEntity.ok(movie))
                .orElse(ResponseEntity.notFound().build());
    }
    
    @GetMapping("/search")
    @Operation(summary = "Поиск фильмов", description = "Ищет фильмы по названию, режиссеру или актерам")
    public ResponseEntity<List<MovieModel>> searchMovies(
            @Parameter(description = "Поисковый запрос") @RequestParam String query) {
        List<MovieModel> movies = movieService.searchMovies(query);
        return ResponseEntity.ok(movies);
    }
    
    
    @GetMapping("/genre/{genre}")
    @Operation(summary = "Получить фильмы по жанру", description = "Возвращает фильмы определенного жанра")
    public ResponseEntity<List<MovieModel>> getMoviesByGenre(
            @Parameter(description = "Жанр фильма") @PathVariable String genre) {
        List<MovieModel> movies = movieService.getMoviesByGenre(genre);
        return ResponseEntity.ok(movies);
    }
    
    @GetMapping("/year/{year}")
    @Operation(summary = "Получить фильмы по году", description = "Возвращает фильмы определенного года")
    public ResponseEntity<List<MovieModel>> getMoviesByYear(
            @Parameter(description = "Год выпуска") @PathVariable Integer year) {
        List<MovieModel> movies = movieService.getMoviesByYear(year);
        return ResponseEntity.ok(movies);
    }
    
    @GetMapping("/country/{country}")
    @Operation(summary = "Получить фильмы по стране", description = "Возвращает фильмы определенной страны")
    public ResponseEntity<List<MovieModel>> getMoviesByCountry(
            @Parameter(description = "Страна производства") @PathVariable String country) {
        List<MovieModel> movies = movieService.getMoviesByCountry(country);
        return ResponseEntity.ok(movies);
    }
    
    @GetMapping("/top/views")
    @Operation(summary = "Топ фильмов по просмотрам", description = "Возвращает самые популярные фильмы по количеству просмотров")
    public ResponseEntity<List<MovieModel>> getTopMoviesByViewCount() {
        List<MovieModel> movies = movieService.getTopMoviesByViewCount();
        return ResponseEntity.ok(movies);
    }
    
    @GetMapping("/top/likes")
    @Operation(summary = "Топ фильмов по лайкам", description = "Возвращает самые популярные фильмы по количеству лайков")
    public ResponseEntity<List<MovieModel>> getTopMoviesByLikeCount() {
        List<MovieModel> movies = movieService.getTopMoviesByLikeCount();
        return ResponseEntity.ok(movies);
    }
    
    @GetMapping("/top/rating")
    @Operation(summary = "Топ фильмов по рейтингу", description = "Возвращает самые популярные фильмы по пользовательскому рейтингу")
    public ResponseEntity<List<MovieModel>> getTopMoviesByUserRating() {
        List<MovieModel> movies = movieService.getTopMoviesByUserRating();
        return ResponseEntity.ok(movies);
    }
    
    @GetMapping("/latest")
    @Operation(summary = "Последние фильмы", description = "Возвращает последние добавленные фильмы")
    public ResponseEntity<List<MovieModel>> getLatestMovies() {
        List<MovieModel> movies = movieService.getLatestMovies();
        return ResponseEntity.ok(movies);
    }
    
    @GetMapping("/featured")
    @Operation(summary = "Рекомендуемые фильмы", description = "Возвращает рекомендуемые фильмы")
    public ResponseEntity<List<MovieModel>> getFeaturedMovies() {
        List<MovieModel> movies = movieService.getFeaturedMovies();
        return ResponseEntity.ok(movies);
    }
    
    @GetMapping("/available")
    @Operation(summary = "Доступные фильмы", description = "Возвращает доступные для просмотра фильмы")
    public ResponseEntity<List<MovieModel>> getAvailableMovies() {
        List<MovieModel> movies = movieService.getAvailableMovies();
        return ResponseEntity.ok(movies);
    }
    
    @GetMapping("/imdb-rating")
    @Operation(summary = "Фильмы по рейтингу IMDB", description = "Возвращает фильмы с рейтингом IMDB выше указанного")
    public ResponseEntity<List<MovieModel>> getMoviesByImdbRating(
            @Parameter(description = "Минимальный рейтинг IMDB") @RequestParam Double minRating) {
        List<MovieModel> movies = movieService.getMoviesByImdbRating(minRating);
        return ResponseEntity.ok(movies);
    }
    
    @GetMapping("/genres")
    @Operation(summary = "Получить все жанры", description = "Возвращает список всех доступных жанров")
    public ResponseEntity<List<String>> getAllGenres() {
        List<String> genres = movieService.getAllGenres();
        return ResponseEntity.ok(genres);
    }
    
    @GetMapping("/countries")
    @Operation(summary = "Получить все страны", description = "Возвращает список всех стран производства")
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
    
    @PostMapping("/{id}/view")
    @Operation(summary = "Просмотреть фильм", description = "Увеличивает счетчик просмотров фильма")
    public ResponseEntity<MovieModel> viewMovie(
            @Parameter(description = "ID фильма") @PathVariable Long id) {
        MovieModel movie = movieService.viewMovie(id);
        if (movie != null) {
            return ResponseEntity.ok(movie);
        }
        return ResponseEntity.notFound().build();
    }
    
    @PostMapping("/{id}/like")
    @Operation(summary = "Лайкнуть фильм", description = "Добавляет лайк фильму")
    public ResponseEntity<MovieModel> likeMovie(
            @Parameter(description = "ID фильма") @PathVariable Long id,
            Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        MovieModel movie = movieService.likeMovie(id, user);
        if (movie != null) {
            return ResponseEntity.ok(movie);
        }
        return ResponseEntity.notFound().build();
    }
    
    @PostMapping("/{id}/unlike")
    @Operation(summary = "Убрать лайк с фильма", description = "Убирает лайк с фильма")
    public ResponseEntity<MovieModel> unlikeMovie(
            @Parameter(description = "ID фильма") @PathVariable Long id,
            Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        MovieModel movie = movieService.unlikeMovie(id, user);
        if (movie != null) {
            return ResponseEntity.ok(movie);
        }
        return ResponseEntity.notFound().build();
    }
    
    @PostMapping("/{id}/watchlist")
    @Operation(summary = "Добавить в список просмотра", description = "Добавляет фильм в список просмотра пользователя")
    public ResponseEntity<MovieModel> addToWatchlist(
            @Parameter(description = "ID фильма") @PathVariable Long id,
            Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        MovieModel movie = movieService.addToWatchlist(id, user);
        if (movie != null) {
            return ResponseEntity.ok(movie);
        }
        return ResponseEntity.notFound().build();
    }
    
    @DeleteMapping("/{id}/watchlist")
    @Operation(summary = "Удалить из списка просмотра", description = "Удаляет фильм из списка просмотра пользователя")
    public ResponseEntity<MovieModel> removeFromWatchlist(
            @Parameter(description = "ID фильма") @PathVariable Long id,
            Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        MovieModel movie = movieService.removeFromWatchlist(id, user);
        if (movie != null) {
            return ResponseEntity.ok(movie);
        }
        return ResponseEntity.notFound().build();
    }
    
    @PostMapping("/{id}/rate")
    @Operation(summary = "Оценить фильм", description = "Позволяет пользователю оценить фильм")
    public ResponseEntity<MovieModel> rateMovie(
            @Parameter(description = "ID фильма") @PathVariable Long id,
            @Parameter(description = "Оценка от 1 до 10") @RequestParam Double rating,
            Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        if (rating < 1.0 || rating > 10.0) {
            return ResponseEntity.badRequest().build();
        }
        MovieModel movie = movieService.rateMovie(id, user, rating);
        if (movie != null) {
            return ResponseEntity.ok(movie);
        }
        return ResponseEntity.notFound().build();
    }
    
    @PostMapping("/{id}/progress")
    @Operation(summary = "Обновить прогресс просмотра", description = "Обновляет прогресс просмотра фильма")
    public ResponseEntity<MovieModel> updateWatchProgress(
            @Parameter(description = "ID фильма") @PathVariable Long id,
            @Parameter(description = "Прогресс в процентах (0-100)") @RequestParam Integer progress,
            Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        if (progress < 0 || progress > 100) {
            return ResponseEntity.badRequest().build();
        }
        MovieModel movie = movieService.updateWatchProgress(id, user, progress);
        if (movie != null) {
            return ResponseEntity.ok(movie);
        }
        return ResponseEntity.notFound().build();
    }
}






