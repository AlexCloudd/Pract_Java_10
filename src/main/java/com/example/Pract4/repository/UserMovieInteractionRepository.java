package com.example.Pract4.repository;

import com.example.Pract4.entity.Movie;
import com.example.Pract4.entity.User;
import com.example.Pract4.entity.UserMovieInteraction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserMovieInteractionRepository extends JpaRepository<UserMovieInteraction, Long> {
    
    Optional<UserMovieInteraction> findByUserAndMovie(User user, Movie movie);
    
    List<UserMovieInteraction> findByUser(User user);
    
    List<UserMovieInteraction> findByMovie(Movie movie);
    
    List<UserMovieInteraction> findByUserAndIsLikedTrue(User user);
    
    List<UserMovieInteraction> findByUserAndIsWatchedTrue(User user);
    
    List<UserMovieInteraction> findByUserAndIsInWatchlistTrue(User user);
    
    @Query("SELECT umi FROM UserMovieInteraction umi WHERE umi.user = :user AND umi.userRating IS NOT NULL ORDER BY umi.userRating DESC")
    List<UserMovieInteraction> findUserRatedMovies(@Param("user") User user);
    
    @Query("SELECT umi FROM UserMovieInteraction umi WHERE umi.user = :user AND umi.watchProgress > 0 ORDER BY umi.lastWatchedAt DESC")
    List<UserMovieInteraction> findUserWatchingMovies(@Param("user") User user);
    
    @Query("SELECT umi FROM UserMovieInteraction umi WHERE umi.user = :user AND umi.watchProgress > 0 AND umi.watchProgress < 100 ORDER BY umi.lastWatchedAt DESC")
    List<UserMovieInteraction> findUserInProgressMovies(@Param("user") User user);
    
    @Query("SELECT umi FROM UserMovieInteraction umi WHERE umi.user = :user AND umi.isWatched = true ORDER BY umi.lastWatchedAt DESC")
    List<UserMovieInteraction> findUserWatchedMovies(@Param("user") User user);
    
    @Query("SELECT COUNT(umi) FROM UserMovieInteraction umi WHERE umi.movie = :movie AND umi.isLiked = true")
    Long countLikesByMovie(@Param("movie") Movie movie);
    
    @Query("SELECT COUNT(umi) FROM UserMovieInteraction umi WHERE umi.movie = :movie AND umi.isWatched = true")
    Long countWatchesByMovie(@Param("movie") Movie movie);
    
    @Query("SELECT COUNT(umi) FROM UserMovieInteraction umi WHERE umi.movie = :movie AND umi.isInWatchlist = true")
    Long countWatchlistByMovie(@Param("movie") Movie movie);
    
    @Query("SELECT AVG(umi.userRating) FROM UserMovieInteraction umi WHERE umi.movie = :movie AND umi.userRating IS NOT NULL")
    Double getAverageRatingByMovie(@Param("movie") Movie movie);
    
    @Query("SELECT COUNT(umi) FROM UserMovieInteraction umi WHERE umi.movie = :movie AND umi.userRating IS NOT NULL")
    Long countRatingsByMovie(@Param("movie") Movie movie);
    
    boolean existsByUserAndMovie(User user, Movie movie);
    
    boolean existsByUserAndMovieAndIsLikedTrue(User user, Movie movie);
    
    boolean existsByUserAndMovieAndIsWatchedTrue(User user, Movie movie);
    
    boolean existsByUserAndMovieAndIsInWatchlistTrue(User user, Movie movie);
}






