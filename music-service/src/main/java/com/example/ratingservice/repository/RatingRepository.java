package com.example.ratingservice.repository;

import com.example.ratingservice.entity.Rating;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RatingRepository extends JpaRepository<Rating, Long> {
    
    
    List<Rating> findByUserId(Long userId);
    
    List<Rating> findByMovieId(Long movieId);
    
    
    Optional<Rating> findByUserIdAndMovieId(Long userId, Long movieId);
    
    boolean existsByUserIdAndMovieId(Long userId, Long movieId);
    
    List<Rating> findByRatingBetween(Integer minRating, Integer maxRating);
    
    List<Rating> findByUserIdAndRatingBetween(Long userId, Integer minRating, Integer maxRating);
    
    List<Rating> findByMovieIdAndRatingBetween(Long movieId, Integer minRating, Integer maxRating);
    
    
    @Query("SELECT AVG(r.rating) FROM Rating r WHERE r.movieId = :movieId")
    Double getAverageRatingByMovieId(@Param("movieId") Long movieId);
    
    @Query("SELECT COUNT(r) FROM Rating r WHERE r.movieId = :movieId")
    Long getRatingCountByMovieId(@Param("movieId") Long movieId);
    
    @Query("SELECT r.movieId, AVG(r.rating) as avgRating, COUNT(r) as ratingCount " +
           "FROM Rating r GROUP BY r.movieId " +
           "HAVING COUNT(r) >= :minRatings " +
           "ORDER BY avgRating DESC")
    List<Object[]> getTopRatedMovies(@Param("minRatings") Long minRatings);
    
    List<Rating> findByReviewIsNotNull();
    
    List<Rating> findByUserIdAndReviewIsNotNull(Long userId);
    
    List<Rating> findByMovieIdAndReviewIsNotNull(Long movieId);
}
