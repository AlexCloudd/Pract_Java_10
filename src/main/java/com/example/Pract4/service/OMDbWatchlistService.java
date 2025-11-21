package com.example.Pract4.service;

import com.example.Pract4.entity.Movie;
import com.example.Pract4.entity.User;
import com.example.Pract4.entity.Watchlist;
import com.example.Pract4.model.MovieModel;
import com.example.Pract4.model.WatchlistModel;
import com.example.Pract4.repository.MovieRepository;
import com.example.Pract4.repository.WatchlistRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class OMDbWatchlistService {

    private static final Logger logger = LoggerFactory.getLogger(OMDbWatchlistService.class);

    @Autowired
    private WatchlistRepository watchlistRepository;

    @Autowired
    private MovieRepository movieRepository;

    /**
     * Создает или получает список просмотров по умолчанию для пользователя
     */
    public WatchlistModel getOrCreateDefaultWatchlist(User user) {
        String defaultWatchlistName = "Мой список просмотров";
        
        Optional<Watchlist> existingWatchlist = watchlistRepository.findByUserAndName(user, defaultWatchlistName);
        
        if (existingWatchlist.isPresent()) {
            return new WatchlistModel(existingWatchlist.get());
        }
        
        Watchlist newWatchlist = new Watchlist(defaultWatchlistName, user);
        newWatchlist.setDescription("Список фильмов, которые я хочу посмотреть");
        Watchlist savedWatchlist = watchlistRepository.save(newWatchlist);
        
        logger.info("Created default watchlist '{}' for user {}", defaultWatchlistName, user.getUsername());
        return new WatchlistModel(savedWatchlist);
    }

    /**
     * Добавляет OMDb фильм в список просмотров пользователя
     */
    public WatchlistModel addOMDbMovieToWatchlist(String imdbId, String title, String posterUrl, User user) {
        try {
            logger.info("Starting to add movie to watchlist: imdbId={}, title={}, user={}", imdbId, title, user.getUsername());
            
            // Получаем или создаем список просмотров по умолчанию
            WatchlistModel watchlistModel = getOrCreateDefaultWatchlist(user);
            logger.info("Got watchlist model: id={}, name={}", watchlistModel.getId(), watchlistModel.getName());
            
            Watchlist watchlist = watchlistRepository.findById(watchlistModel.getId()).orElse(null);
            
            if (watchlist == null) {
                logger.error("Watchlist not found for user {}", user.getUsername());
                return null;
            }
            logger.info("Found watchlist: id={}, name={}", watchlist.getId(), watchlist.getName());

            // Проверяем, есть ли уже такой фильм в базе данных
            logger.info("Searching for existing movie with imdbId: {}", imdbId);
            Optional<Movie> existingMovie = movieRepository.findByImdbId(imdbId);
            Movie movie;

            if (existingMovie.isPresent()) {
                movie = existingMovie.get();
                logger.info("Found existing movie: id={}, title={}", movie.getId(), movie.getTitle());
            } else {
                logger.info("Creating new movie: imdbId={}, title={}", imdbId, title);
                // Создаем новый фильм в базе данных
                movie = new Movie();
                // Для OMDb фильмов используем префикс "omdb_" для tmdbId чтобы избежать конфликтов
                movie.setTmdbId("omdb_" + imdbId);
                movie.setImdbId(imdbId);
                movie.setTitle(title);
                movie.setPosterUrl(posterUrl);
                movie.setIsAvailable(true);
                movie = movieRepository.save(movie);
                logger.info("Created new movie: id={}, title={}", movie.getId(), movie.getTitle());
            }

            // Проверяем, не добавлен ли уже фильм в этот список
            logger.info("Checking if movie is already in watchlist");
            if (!watchlist.getMovies().contains(movie)) {
                logger.info("Adding movie to watchlist");
                watchlist.addMovie(movie);
                Watchlist savedWatchlist = watchlistRepository.save(watchlist);
                logger.info("Successfully added movie '{}' to watchlist '{}' for user {}", 
                          movie.getTitle(), watchlist.getName(), user.getUsername());
                return new WatchlistModel(savedWatchlist);
            } else {
                logger.info("Movie '{}' already exists in watchlist '{}'", 
                          movie.getTitle(), watchlist.getName());
                return new WatchlistModel(watchlist);
            }

        } catch (Exception e) {
            logger.error("Error adding movie to watchlist: {}", e.getMessage(), e);
            return null;
        }
    }

    /**
     * Получает все фильмы из списка просмотров пользователя
     */
    public List<MovieModel> getWatchlistMovies(User user) {
        try {
            WatchlistModel watchlistModel = getOrCreateDefaultWatchlist(user);
            Watchlist watchlist = watchlistRepository.findById(watchlistModel.getId()).orElse(null);
            
            if (watchlist == null) {
                return List.of();
            }

            return watchlist.getMovies().stream()
                    .map(MovieModel::new)
                    .collect(Collectors.toList());

        } catch (Exception e) {
            logger.error("Error getting watchlist movies: {}", e.getMessage(), e);
            return List.of();
        }
    }

    /**
     * Удаляет фильм из списка просмотров пользователя
     */
    public boolean removeMovieFromWatchlist(String imdbId, User user) {
        try {
            WatchlistModel watchlistModel = getOrCreateDefaultWatchlist(user);
            Watchlist watchlist = watchlistRepository.findById(watchlistModel.getId()).orElse(null);
            
            if (watchlist == null) {
                return false;
            }

            Optional<Movie> movie = movieRepository.findByImdbId(imdbId);
            if (movie.isPresent() && watchlist.getMovies().contains(movie.get())) {
                watchlist.removeMovie(movie.get());
                watchlistRepository.save(watchlist);
                logger.info("Removed movie '{}' from watchlist '{}' for user {}", 
                          movie.get().getTitle(), watchlist.getName(), user.getUsername());
                return true;
            }

            return false;

        } catch (Exception e) {
            logger.error("Error removing movie from watchlist: {}", e.getMessage(), e);
            return false;
        }
    }

    /**
     * Проверяет, есть ли фильм в списке просмотров пользователя
     */
    public boolean isMovieInWatchlist(String imdbId, User user) {
        try {
            WatchlistModel watchlistModel = getOrCreateDefaultWatchlist(user);
            Watchlist watchlist = watchlistRepository.findById(watchlistModel.getId()).orElse(null);
            
            if (watchlist == null) {
                return false;
            }

            Optional<Movie> movie = movieRepository.findByImdbId(imdbId);
            return movie.isPresent() && watchlist.getMovies().contains(movie.get());

        } catch (Exception e) {
            logger.error("Error checking if movie is in watchlist: {}", e.getMessage(), e);
            return false;
        }
    }
}
