package com.example.ratingservice.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public class RatingRequest {
    
    @NotNull(message = "User ID is required")
    private Long userId;
    
    @NotNull(message = "Movie ID is required")
    private Long movieId;
    
    @NotNull(message = "Rating is required")
    @Min(value = 1, message = "Rating must be at least 1")
    @Max(value = 10, message = "Rating must be at most 10")
    private Integer rating;
    
    private String review;
    
    
    public RatingRequest() {}
    
    public RatingRequest(Long userId, Long movieId, Integer rating, String review) {
        this.userId = userId;
        this.movieId = movieId;
        this.rating = rating;
        this.review = review;
    }
    
    
    public Long getUserId() {
        return userId;
    }
    
    public void setUserId(Long userId) {
        this.userId = userId;
    }
    
    public Long getMovieId() {
        return movieId;
    }
    
    public void setMovieId(Long movieId) {
        this.movieId = movieId;
    }
    
    public Integer getRating() {
        return rating;
    }
    
    public void setRating(Integer rating) {
        this.rating = rating;
    }
    
    public String getReview() {
        return review;
    }
    
    public void setReview(String review) {
        this.review = review;
    }
    
    @Override
    public String toString() {
        return "RatingRequest{" +
                "userId=" + userId +
                ", movieId=" + movieId +
                ", rating=" + rating +
                ", review='" + review + '\'' +
                '}';
    }
}
