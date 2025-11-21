package com.example.Pract4.client;

import com.example.Pract4.model.MovieModel;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(name = "movie-service", url = "http://localhost:8083")
public interface MovieServiceClient {

    @GetMapping("/api/movies")
    List<MovieModel> getAllMovies();

    @GetMapping("/api/movies/{id}")
    MovieModel getMovieById(@PathVariable("id") Long id);

    @PostMapping("/api/movies")
    MovieModel createMovie(@RequestBody MovieModel movie);

    @PutMapping("/api/movies/{id}")
    MovieModel updateMovie(@PathVariable("id") Long id, @RequestBody MovieModel movie);

    @DeleteMapping("/api/movies/{id}")
    void deleteMovie(@PathVariable("id") Long id);

    @GetMapping("/api/movies/search")
    List<MovieModel> searchMovies(@RequestParam("query") String query);

    @GetMapping("/api/movies/popular")
    List<MovieModel> getPopularMovies();

    @GetMapping("/api/movies/trending")
    List<MovieModel> getTrendingMovies();

    @GetMapping("/api/movies/featured")
    List<MovieModel> getFeaturedMovies();

    @GetMapping("/api/movies/genre/{genre}")
    List<MovieModel> getMoviesByGenre(@PathVariable("genre") String genre);

    @GetMapping("/api/movies/year/{year}")
    List<MovieModel> getMoviesByYear(@PathVariable("year") Integer year);
}
