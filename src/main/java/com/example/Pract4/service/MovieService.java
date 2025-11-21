package com.example.Pract4.service;

import com.example.Pract4.entity.Movie;
import com.example.Pract4.entity.User;
import com.example.Pract4.entity.UserMovieInteraction;
import com.example.Pract4.model.MovieModel;
import com.example.Pract4.repository.MovieRepository;
import com.example.Pract4.repository.UserMovieInteractionRepository;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class MovieService {
    
    private static final Logger logger = LoggerFactory.getLogger(MovieService.class);
    
    @Autowired
    private MovieRepository movieRepository;
    
    @Autowired
    private UserMovieInteractionRepository userMovieInteractionRepository;
    
    @Autowired
    private WebClient.Builder webClientBuilder;
    
    
    public List<MovieModel> getAllMovies() {
        return movieRepository.findAll().stream()
                .map(MovieModel::new)
                .collect(Collectors.toList());
    }
    
    public List<MovieModel> searchMovies(String query) {
        return movieRepository.searchMovies(query).stream()
                .map(MovieModel::new)
                .collect(Collectors.toList());
    }
    
    public List<MovieModel> getMoviesByGenre(String genre) {
        return movieRepository.findByGenreIgnoreCase(genre).stream()
                .map(MovieModel::new)
                .collect(Collectors.toList());
    }
    
    public List<MovieModel> getMoviesByYear(Integer year) {
        return movieRepository.findByYear(year).stream()
                .map(MovieModel::new)
                .collect(Collectors.toList());
    }
    
    public List<MovieModel> getMoviesByCountry(String country) {
        return movieRepository.findByCountryIgnoreCase(country).stream()
                .map(MovieModel::new)
                .collect(Collectors.toList());
    }
    
    public List<MovieModel> getTopMoviesByViewCount() {
        return movieRepository.findTopMoviesByViewCount().stream()
                .map(MovieModel::new)
                .collect(Collectors.toList());
    }
    
    public List<MovieModel> getTopMoviesByLikeCount() {
        return movieRepository.findTopMoviesByLikeCount().stream()
                .map(MovieModel::new)
                .collect(Collectors.toList());
    }
    
    public List<MovieModel> getTopMoviesByUserRating() {
        return movieRepository.findTopMoviesByUserRating().stream()
                .map(MovieModel::new)
                .collect(Collectors.toList());
    }
    
    public List<MovieModel> getLatestMovies() {
        return movieRepository.findLatestMovies().stream()
                .map(MovieModel::new)
                .collect(Collectors.toList());
    }
    
    public List<MovieModel> getFeaturedMovies() {
        // Возвращаем последние добавленные фильмы как рекомендуемые
        return movieRepository.findLatestMovies().stream()
                .limit(4) // Например, 4 последних фильма
                .map(MovieModel::new)
                .collect(Collectors.toList());
    }
    
    public List<MovieModel> getAvailableMovies() {
        return movieRepository.findAll().stream()
                .filter(movie -> movie.getIsAvailable() != null && movie.getIsAvailable())
                .map(MovieModel::new)
                .collect(Collectors.toList());
    }
    
    public List<MovieModel> getMoviesByImdbRating(Double minRating) {
        return movieRepository.findAll().stream()
                .filter(movie -> movie.getImdbRating() != null && movie.getImdbRating() >= minRating)
                .map(MovieModel::new)
                .collect(Collectors.toList());
    }
    
    public Optional<MovieModel> getMovieById(Long id) {
        return movieRepository.findById(id)
                .map(MovieModel::new);
    }
    
    public List<String> getAllGenres() {
        return movieRepository.findAllGenres();
    }
    
    public List<String> getAllCountries() {
        return movieRepository.findAllCountries();
    }
    
    public List<Integer> getAllYears() {
        return movieRepository.findAllYears();
    }
    
    
    public MovieModel viewMovie(Long movieId) {
        return movieRepository.findById(movieId)
                .map(movie -> {
                    movie.incrementViewCount();
                    Movie savedMovie = movieRepository.save(movie);
                    logger.info("Movie {} viewed, new view count: {}", movieId, savedMovie.getViewCount());
                    return new MovieModel(savedMovie);
                })
                .orElse(null);
    }
    
    public MovieModel likeMovie(Long movieId, User user) {
        return movieRepository.findById(movieId)
                .map(movie -> {
                    UserMovieInteraction interaction = userMovieInteractionRepository
                            .findByUserAndMovie(user, movie)
                            .orElse(new UserMovieInteraction(user, movie));
                    
                    if (!interaction.getIsLiked()) {
                        movie.incrementLikeCount();
                        interaction.setIsLiked(true);
                        movieRepository.save(movie);
                        userMovieInteractionRepository.save(interaction);
                        logger.info("Movie {} liked by user {}, new like count: {}", 
                                  movieId, user.getUsername(), movie.getLikeCount());
                    }
                    return new MovieModel(movie);
                })
                .orElse(null);
    }
    
    public MovieModel unlikeMovie(Long movieId, User user) {
        return movieRepository.findById(movieId)
                .map(movie -> {
                    userMovieInteractionRepository.findByUserAndMovie(user, movie)
                            .ifPresent(interaction -> {
                                if (interaction.getIsLiked()) {
                                    movie.setLikeCount(Math.max(0, movie.getLikeCount() - 1));
                                    interaction.setIsLiked(false);
                                    movieRepository.save(movie);
                                    userMovieInteractionRepository.save(interaction);
                                    logger.info("Movie {} unliked by user {}, new like count: {}", 
                                              movieId, user.getUsername(), movie.getLikeCount());
                                }
                            });
                    return new MovieModel(movie);
                })
                .orElse(null);
    }
    
    public MovieModel addToWatchlist(Long movieId, User user) {
        return movieRepository.findById(movieId)
                .map(movie -> {
                    UserMovieInteraction interaction = userMovieInteractionRepository
                            .findByUserAndMovie(user, movie)
                            .orElse(new UserMovieInteraction(user, movie));
                    
                    if (!interaction.getIsInWatchlist()) {
                        movie.incrementWatchlistCount();
                        interaction.setIsInWatchlist(true);
                        movieRepository.save(movie);
                        userMovieInteractionRepository.save(interaction);
                        logger.info("Movie {} added to watchlist by user {}, new watchlist count: {}", 
                                  movieId, user.getUsername(), movie.getWatchlistCount());
                    }
                    return new MovieModel(movie);
                })
                .orElse(null);
    }
    
    public MovieModel removeFromWatchlist(Long movieId, User user) {
        return movieRepository.findById(movieId)
                .map(movie -> {
                    userMovieInteractionRepository.findByUserAndMovie(user, movie)
                            .ifPresent(interaction -> {
                                if (interaction.getIsInWatchlist()) {
                                    movie.setWatchlistCount(Math.max(0, movie.getWatchlistCount() - 1));
                                    interaction.setIsInWatchlist(false);
                                    movieRepository.save(movie);
                                    userMovieInteractionRepository.save(interaction);
                                    logger.info("Movie {} removed from watchlist by user {}, new watchlist count: {}", 
                                              movieId, user.getUsername(), movie.getWatchlistCount());
                                }
                            });
                    return new MovieModel(movie);
                })
                .orElse(null);
    }
    
    public MovieModel rateMovie(Long movieId, User user, Double rating) {
        return movieRepository.findById(movieId)
                .map(movie -> {
                    UserMovieInteraction interaction = userMovieInteractionRepository
                            .findByUserAndMovie(user, movie)
                            .orElse(new UserMovieInteraction(user, movie));
                    
                    Double oldRating = interaction.getUserRating();
                    interaction.setUserRating(rating);
                    userMovieInteractionRepository.save(interaction);
                    
                    // Обновляем общий рейтинг фильма
                    if (oldRating == null) {
                        movie.addRating(rating);
                    } else {
                        // Пересчитываем средний рейтинг
                        movie.setUserRating((movie.getUserRating() * movie.getRatingCount() - oldRating + rating) / movie.getRatingCount());
                    }
                    movieRepository.save(movie);
                    
                    logger.info("Movie {} rated {} by user {}, new average rating: {}", 
                              movieId, rating, user.getUsername(), movie.getUserRating());
                    return new MovieModel(movie);
                })
                .orElse(null);
    }
    
    public MovieModel updateWatchProgress(Long movieId, User user, Integer progress) {
        return movieRepository.findById(movieId)
                .map(movie -> {
                    UserMovieInteraction interaction = userMovieInteractionRepository
                            .findByUserAndMovie(user, movie)
                            .orElse(new UserMovieInteraction(user, movie));
                    
                    interaction.updateWatchProgress(progress);
                    userMovieInteractionRepository.save(interaction);
                    
                    logger.info("Movie {} watch progress updated to {}% by user {}", 
                              movieId, progress, user.getUsername());
                    return new MovieModel(movie);
                })
                .orElse(null);
    }
    
}
