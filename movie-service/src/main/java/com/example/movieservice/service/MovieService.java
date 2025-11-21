package com.example.movieservice.service;

import com.example.movieservice.entity.Movie;
import com.example.movieservice.repository.MovieRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class MovieService {
    
    @Autowired
    private MovieRepository movieRepository;
    
    public List<Movie> getAllMovies() {
        return movieRepository.findAll();
    }
    
    public Optional<Movie> getMovieById(Long id) {
        return movieRepository.findById(id);
    }
    
    public Optional<Movie> getMovieByTmdbId(String tmdbId) {
        return movieRepository.findByTmdbId(tmdbId);
    }
    
    public List<Movie> searchMovies(String query) {
        return movieRepository.searchMovies(query);
    }
    
    public List<Movie> getTopMoviesByViewCount() {
        return movieRepository.findTopMoviesByViewCount();
    }
    
    public List<Movie> getLatestMovies() {
        return movieRepository.findLatestMovies();
    }
    
    public List<Movie> getFeaturedMovies() {
        return movieRepository.findByIsFeaturedTrue();
    }
    
    public List<Movie> getMoviesByGenre(String genre) {
        return movieRepository.findByGenreIgnoreCase(genre);
    }
    
    public List<Movie> getMoviesByYear(Integer year) {
        return movieRepository.findByYear(year);
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
    
    public Movie createMovie(Movie movie) {
        if (movie.getTmdbId() != null && movieRepository.existsByTmdbId(movie.getTmdbId())) {
            throw new RuntimeException("Movie with TMDB ID already exists");
        }
        return movieRepository.save(movie);
    }
    
    public Optional<Movie> updateMovie(Long id, Movie movieDetails) {
        return movieRepository.findById(id)
                .map(movie -> {
                    movie.setTitle(movieDetails.getTitle());
                    movie.setOriginalTitle(movieDetails.getOriginalTitle());
                    movie.setOverview(movieDetails.getOverview());
                    movie.setDescription(movieDetails.getDescription());
                    movie.setShortDescription(movieDetails.getShortDescription());
                    movie.setYear(movieDetails.getYear());
                    movie.setReleaseDate(movieDetails.getReleaseDate());
                    movie.setDuration(movieDetails.getDuration());
                    movie.setAgeRating(movieDetails.getAgeRating());
                    movie.setCountry(movieDetails.getCountry());
                    movie.setLanguage(movieDetails.getLanguage());
                    movie.setGenre(movieDetails.getGenre());
                    movie.setDirector(movieDetails.getDirector());
                    movie.setCast(movieDetails.getCast());
                    movie.setProducer(movieDetails.getProducer());
                    movie.setScreenwriter(movieDetails.getScreenwriter());
                    movie.setBudget(movieDetails.getBudget());
                    movie.setBoxOffice(movieDetails.getBoxOffice());
                    movie.setImdbRating(movieDetails.getImdbRating());
                    movie.setKinopoiskRating(movieDetails.getKinopoiskRating());
                    movie.setPosterUrl(movieDetails.getPosterUrl());
                    movie.setBackdropUrl(movieDetails.getBackdropUrl());
                    movie.setTrailerUrl(movieDetails.getTrailerUrl());
                    movie.setVideoUrl(movieDetails.getVideoUrl());
                    movie.setIsAvailable(movieDetails.getIsAvailable());
                    movie.setIsPremium(movieDetails.getIsPremium());
                    movie.setIsFeatured(movieDetails.getIsFeatured());
                    return movieRepository.save(movie);
                });
    }
    
    public boolean deleteMovie(Long id) {
        if (movieRepository.existsById(id)) {
            movieRepository.deleteById(id);
            return true;
        }
        return false;
    }
    
    public void incrementViewCount(Long movieId) {
        movieRepository.findById(movieId)
                .ifPresent(Movie::incrementViewCount);
    }
    
    public void incrementLikeCount(Long movieId) {
        movieRepository.findById(movieId)
                .ifPresent(Movie::incrementLikeCount);
    }
    
    public void addRating(Long movieId, Double rating) {
        movieRepository.findById(movieId)
                .ifPresent(movie -> movie.addRating(rating));
    }
}




