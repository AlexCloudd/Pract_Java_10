package com.example.Pract4.repository;

import com.example.Pract4.entity.Movie;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MovieRepository extends JpaRepository<Movie, Long> {
    
    List<Movie> findByTitleContainingIgnoreCase(String title);
    
    List<Movie> findByDirectorContainingIgnoreCase(String director);
    
    List<Movie> findByCastContainingIgnoreCase(String cast);
    
    List<Movie> findByGenreIgnoreCase(String genre);
    
    List<Movie> findByYear(Integer year);
    
    List<Movie> findByYearBetween(Integer startYear, Integer endYear);
    
    List<Movie> findByCountryIgnoreCase(String country);
    
    List<Movie> findByAgeRating(String ageRating);
    
    List<Movie> findByIsAvailableTrue();
    
    List<Movie> findByIsFeaturedTrue();
    
    List<Movie> findByIsPremiumTrue();
    
    @Query("SELECT m FROM Movie m WHERE " +
           "LOWER(m.title) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
           "LOWER(m.originalTitle) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
           "LOWER(m.director) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
           "LOWER(m.cast) LIKE LOWER(CONCAT('%', :query, '%'))")
    List<Movie> searchMovies(@Param("query") String query);
    
    @Query("SELECT m FROM Movie m ORDER BY m.viewCount DESC")
    List<Movie> findTopMoviesByViewCount();
    
    @Query("SELECT m FROM Movie m ORDER BY m.likeCount DESC")
    List<Movie> findTopMoviesByLikeCount();
    
    @Query("SELECT m FROM Movie m ORDER BY m.userRating DESC")
    List<Movie> findTopMoviesByUserRating();
    
    @Query("SELECT m FROM Movie m ORDER BY m.createdAt DESC")
    List<Movie> findLatestMovies();
    
    @Query("SELECT m FROM Movie m WHERE m.year = :year ORDER BY m.userRating DESC")
    List<Movie> findTopMoviesByYear(@Param("year") Integer year);
    
    @Query("SELECT m FROM Movie m WHERE m.genre = :genre ORDER BY m.userRating DESC")
    List<Movie> findTopMoviesByGenre(@Param("genre") String genre);
    
    @Query("SELECT DISTINCT m.genre FROM Movie m WHERE m.genre IS NOT NULL ORDER BY m.genre")
    List<String> findAllGenres();
    
    @Query("SELECT DISTINCT m.country FROM Movie m WHERE m.country IS NOT NULL ORDER BY m.country")
    List<String> findAllCountries();
    
    @Query("SELECT DISTINCT m.year FROM Movie m WHERE m.year IS NOT NULL ORDER BY m.year DESC")
    List<Integer> findAllYears();
    
    @Query("SELECT m FROM Movie m WHERE m.imdbRating >= :minRating ORDER BY m.imdbRating DESC")
    List<Movie> findMoviesByImdbRatingGreaterThanEqual(@Param("minRating") Double minRating);
    
    @Query("SELECT m FROM Movie m WHERE m.kinopoiskRating >= :minRating ORDER BY m.kinopoiskRating DESC")
    List<Movie> findMoviesByKinopoiskRatingGreaterThanEqual(@Param("minRating") Double minRating);
    
    Optional<Movie> findByTmdbId(String tmdbId);
    
    Optional<Movie> findByImdbId(String imdbId);
}




