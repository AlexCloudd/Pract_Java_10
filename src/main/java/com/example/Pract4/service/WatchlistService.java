package com.example.Pract4.service;

import com.example.Pract4.entity.Movie;
import com.example.Pract4.entity.User;
import com.example.Pract4.entity.Watchlist;
import com.example.Pract4.model.WatchlistModel;
import com.example.Pract4.repository.WatchlistRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class WatchlistService {
    
    private static final Logger logger = LoggerFactory.getLogger(WatchlistService.class);
    
    @Autowired
    private WatchlistRepository watchlistRepository;
    
    @Autowired
    private MovieService movieService;
    
    public List<WatchlistModel> getAllWatchlists() {
        return watchlistRepository.findAll().stream()
                .map(WatchlistModel::new)
                .collect(Collectors.toList());
    }
    
    public List<WatchlistModel> getPublicWatchlists() {
        return watchlistRepository.findByIsPublicTrue().stream()
                .map(WatchlistModel::new)
                .collect(Collectors.toList());
    }
    
    public List<WatchlistModel> getUserWatchlists(User user) {
        return watchlistRepository.findByUser(user).stream()
                .map(WatchlistModel::new)
                .collect(Collectors.toList());
    }
    
    public List<WatchlistModel> searchWatchlists(String query) {
        return watchlistRepository.searchPublicWatchlists(query).stream()
                .map(WatchlistModel::new)
                .collect(Collectors.toList());
    }
    
    public List<WatchlistModel> searchUserWatchlists(User user, String query) {
        return watchlistRepository.searchUserWatchlists(user, query).stream()
                .map(WatchlistModel::new)
                .collect(Collectors.toList());
    }
    
    @Transactional(readOnly = true)
    public List<WatchlistModel> getTopWatchlistsByLikeCount() {
        return watchlistRepository.findTopWatchlistsByLikeCount().stream()
                .map(WatchlistModel::new)
                .collect(Collectors.toList());
    }
    
    @Transactional(readOnly = true)
    public List<WatchlistModel> getTopWatchlistsByViewCount() {
        return watchlistRepository.findTopWatchlistsByViewCount().stream()
                .map(WatchlistModel::new)
                .collect(Collectors.toList());
    }
    
    public List<WatchlistModel> getLatestWatchlists() {
        return watchlistRepository.findLatestWatchlists().stream()
                .map(WatchlistModel::new)
                .collect(Collectors.toList());
    }
    
    public Optional<WatchlistModel> getWatchlistById(Long id) {
        return watchlistRepository.findById(id)
                .map(WatchlistModel::new);
    }
    
    public Optional<WatchlistModel> getUserWatchlistById(User user, Long id) {
        return watchlistRepository.findByUserAndId(user, id)
                .map(WatchlistModel::new);
    }
    
    public WatchlistModel createWatchlist(String name, String description, User user) {
        Watchlist watchlist = new Watchlist(name, user);
        watchlist.setDescription(description);
        Watchlist savedWatchlist = watchlistRepository.save(watchlist);
        logger.info("Created watchlist '{}' for user {}", name, user.getUsername());
        return new WatchlistModel(savedWatchlist);
    }
    
    public WatchlistModel updateWatchlist(Long id, String name, String description, User user) {
        return watchlistRepository.findByUserAndId(user, id)
                .map(watchlist -> {
                    watchlist.setName(name);
                    watchlist.setDescription(description);
                    Watchlist savedWatchlist = watchlistRepository.save(watchlist);
                    logger.info("Updated watchlist '{}' for user {}", name, user.getUsername());
                    return new WatchlistModel(savedWatchlist);
                })
                .orElse(null);
    }
    
    public boolean deleteWatchlist(Long id, User user) {
        return watchlistRepository.findByUserAndId(user, id)
                .map(watchlist -> {
                    watchlistRepository.delete(watchlist);
                    logger.info("Deleted watchlist '{}' for user {}", watchlist.getName(), user.getUsername());
                    return true;
                })
                .orElse(false);
    }
    
    public WatchlistModel addMovieToWatchlist(Long watchlistId, Long movieId, User user) {
        return watchlistRepository.findByUserAndId(user, watchlistId)
                .flatMap(watchlist -> movieService.getMovieById(movieId)
                        .map(movieModel -> {
                            // Здесь нужно получить сущность Movie из MovieModel
                            // Для упрощения, создадим новый Movie объект
                            Movie movie = new Movie();
                            movie.setId(movieModel.getId());
                            movie.setTitle(movieModel.getTitle());
                            
                            watchlist.addMovie(movie);
                            Watchlist savedWatchlist = watchlistRepository.save(watchlist);
                            logger.info("Added movie '{}' to watchlist '{}' for user {}", 
                                      movieModel.getTitle(), watchlist.getName(), user.getUsername());
                            return new WatchlistModel(savedWatchlist);
                        }))
                .orElse(null);
    }
    
    public WatchlistModel removeMovieFromWatchlist(Long watchlistId, Long movieId, User user) {
        return watchlistRepository.findByUserAndId(user, watchlistId)
                .flatMap(watchlist -> movieService.getMovieById(movieId)
                        .map(movieModel -> {
                            // Здесь нужно получить сущность Movie из MovieModel
                            Movie movie = new Movie();
                            movie.setId(movieModel.getId());
                            movie.setTitle(movieModel.getTitle());
                            
                            watchlist.removeMovie(movie);
                            Watchlist savedWatchlist = watchlistRepository.save(watchlist);
                            logger.info("Removed movie '{}' from watchlist '{}' for user {}", 
                                      movieModel.getTitle(), watchlist.getName(), user.getUsername());
                            return new WatchlistModel(savedWatchlist);
                        }))
                .orElse(null);
    }
    
    public WatchlistModel likeWatchlist(Long watchlistId, User user) {
        return watchlistRepository.findById(watchlistId)
                .map(watchlist -> {
                    watchlist.incrementLikeCount();
                    Watchlist savedWatchlist = watchlistRepository.save(watchlist);
                    logger.info("Watchlist '{}' liked by user {}, new like count: {}", 
                              watchlist.getName(), user.getUsername(), savedWatchlist.getLikeCount());
                    return new WatchlistModel(savedWatchlist);
                })
                .orElse(null);
    }
    
    public WatchlistModel viewWatchlist(Long watchlistId) {
        return watchlistRepository.findById(watchlistId)
                .map(watchlist -> {
                    watchlist.incrementViewCount();
                    Watchlist savedWatchlist = watchlistRepository.save(watchlist);
                    logger.info("Watchlist '{}' viewed, new view count: {}", 
                              watchlist.getName(), savedWatchlist.getViewCount());
                    return new WatchlistModel(savedWatchlist);
                })
                .orElse(null);
    }
    
    public boolean isWatchlistNameAvailable(String name, User user) {
        return !watchlistRepository.findByUserAndName(user, name).isPresent();
    }
}






