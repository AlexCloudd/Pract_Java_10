package com.example.ratingservice.service;

import com.example.ratingservice.dto.RatingRequest;
import com.example.ratingservice.dto.RatingResponse;
import com.example.ratingservice.entity.Rating;
import com.example.ratingservice.repository.RatingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class RatingService {
    
    @Autowired
    private RatingRepository ratingRepository;
    
    public RatingResponse createRating(RatingRequest request) {
        if (ratingRepository.existsByUserIdAndMovieId(request.getUserId(), request.getMovieId())) {
            throw new IllegalArgumentException("User has already rated this movie");
        }
        
        Rating rating = new Rating(
            request.getUserId(),
            request.getMovieId(),
            request.getRating(),
            request.getReview()
        );
        
        Rating savedRating = ratingRepository.save(rating);
        return convertToResponse(savedRating);
    }
    
    public RatingResponse updateRating(Long ratingId, RatingRequest request) {
        Optional<Rating> optionalRating = ratingRepository.findById(ratingId);
        if (optionalRating.isEmpty()) {
            throw new IllegalArgumentException("Rating not found with id: " + ratingId);
        }
        
        Rating rating = optionalRating.get();
        
        if (!rating.getUserId().equals(request.getUserId())) {
            throw new IllegalArgumentException("User can only update their own ratings");
        }
        
        rating.setRating(request.getRating());
        rating.setReview(request.getReview());
        rating.preUpdate();
        
        Rating updatedRating = ratingRepository.save(rating);
        return convertToResponse(updatedRating);
    }
    
    public RatingResponse getRatingById(Long ratingId) {
        Optional<Rating> optionalRating = ratingRepository.findById(ratingId);
        if (optionalRating.isEmpty()) {
            throw new IllegalArgumentException("Rating not found with id: " + ratingId);
        }
        return convertToResponse(optionalRating.get());
    }
    
    public RatingResponse getRatingByUserAndMovie(Long userId, Long movieId) {
        Optional<Rating> optionalRating = ratingRepository.findByUserIdAndMovieId(userId, movieId);
        if (optionalRating.isEmpty()) {
            return null; 
        }
        return convertToResponse(optionalRating.get());
    }

    public List<RatingResponse> getRatingsByUser(Long userId) {
        List<Rating> ratings = ratingRepository.findByUserId(userId);
        return ratings.stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    public List<RatingResponse> getRatingsByMovie(Long movieId) {
        List<Rating> ratings = ratingRepository.findByMovieId(movieId);
        return ratings.stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }
    
    
    public Double getAverageRatingByMovie(Long movieId) {
        Double averageRating = ratingRepository.getAverageRatingByMovieId(movieId);
        return averageRating != null ? Math.round(averageRating * 100.0) / 100.0 : 0.0;
    }
    

    public Long getRatingCountByMovie(Long movieId) {
        return ratingRepository.getRatingCountByMovieId(movieId);
    }
    
    public List<Object[]> getTopRatedMovies(Long minRatings) {
        return ratingRepository.getTopRatedMovies(minRatings);
    }
    
    public void deleteRating(Long ratingId) {
        if (!ratingRepository.existsById(ratingId)) {
            throw new IllegalArgumentException("Rating not found with id: " + ratingId);
        }
        ratingRepository.deleteById(ratingId);
    }
    
    public void deleteRatingByUserAndMovie(Long userId, Long movieId) {
        Optional<Rating> optionalRating = ratingRepository.findByUserIdAndMovieId(userId, movieId);
        if (optionalRating.isEmpty()) {
            throw new IllegalArgumentException("Rating not found for user " + userId + " and movie " + movieId);
        }
        ratingRepository.delete(optionalRating.get());
    }
    
    public List<RatingResponse> getRatingsWithReviews() {
        List<Rating> ratings = ratingRepository.findByReviewIsNotNull();
        return ratings.stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }
    
    
    public List<RatingResponse> getRatingsWithReviewsByUser(Long userId) {
        List<Rating> ratings = ratingRepository.findByUserIdAndReviewIsNotNull(userId);
        return ratings.stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }
    
    public List<RatingResponse> getRatingsWithReviewsByMovie(Long movieId) {
        List<Rating> ratings = ratingRepository.findByMovieIdAndReviewIsNotNull(movieId);
        return ratings.stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }
    
    public List<RatingResponse> getRatingsByRange(Integer minRating, Integer maxRating) {
        List<Rating> ratings = ratingRepository.findByRatingBetween(minRating, maxRating);
        return ratings.stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }
    
    public List<RatingResponse> getRatingsByUserAndRange(Long userId, Integer minRating, Integer maxRating) {
        List<Rating> ratings = ratingRepository.findByUserIdAndRatingBetween(userId, minRating, maxRating);
        return ratings.stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }
    
    
    public List<RatingResponse> getRatingsByMovieAndRange(Long movieId, Integer minRating, Integer maxRating) {
        List<Rating> ratings = ratingRepository.findByMovieIdAndRatingBetween(movieId, minRating, maxRating);
        return ratings.stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }
    
    private RatingResponse convertToResponse(Rating rating) {
        return new RatingResponse(
            rating.getId(),
            rating.getUserId(),
            rating.getMovieId(),
            rating.getRating(),
            rating.getReview(),
            rating.getCreatedAt(),
            rating.getUpdatedAt()
        );
    }
}
