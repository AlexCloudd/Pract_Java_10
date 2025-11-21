package com.example.Pract4.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Entity
@Table(name = "user_movie_interactions")
public class UserMovieInteraction {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    @NotNull
    private User user;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "movie_id")
    @NotNull
    private Movie movie;
    
    @Column(name = "is_liked")
    private Boolean isLiked = false;
    
    @Column(name = "is_watched")
    private Boolean isWatched = false;
    
    @Column(name = "is_in_watchlist")
    private Boolean isInWatchlist = false;
    
    @Column(name = "user_rating")
    private Double userRating;
    
    @Column(name = "watch_progress")
    private Integer watchProgress = 0; // в процентах (0-100)
    
    @Column(name = "last_watched_at")
    private LocalDateTime lastWatchedAt;
    
    @Column(name = "created_at")
    private LocalDateTime createdAt;
    
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    
    // Constructors
    public UserMovieInteraction() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }
    
    public UserMovieInteraction(User user, Movie movie) {
        this();
        this.user = user;
        this.movie = movie;
    }
    
    // Getters and Setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public User getUser() {
        return user;
    }
    
    public void setUser(User user) {
        this.user = user;
    }
    
    public Movie getMovie() {
        return movie;
    }
    
    public void setMovie(Movie movie) {
        this.movie = movie;
    }
    
    public Boolean getIsLiked() {
        return isLiked;
    }
    
    public void setIsLiked(Boolean isLiked) {
        this.isLiked = isLiked;
    }
    
    public Boolean getIsWatched() {
        return isWatched;
    }
    
    public void setIsWatched(Boolean isWatched) {
        this.isWatched = isWatched;
    }
    
    public Boolean getIsInWatchlist() {
        return isInWatchlist;
    }
    
    public void setIsInWatchlist(Boolean isInWatchlist) {
        this.isInWatchlist = isInWatchlist;
    }
    
    public Double getUserRating() {
        return userRating;
    }
    
    public void setUserRating(Double userRating) {
        this.userRating = userRating;
    }
    
    public Integer getWatchProgress() {
        return watchProgress;
    }
    
    public void setWatchProgress(Integer watchProgress) {
        this.watchProgress = watchProgress;
    }
    
    public LocalDateTime getLastWatchedAt() {
        return lastWatchedAt;
    }
    
    public void setLastWatchedAt(LocalDateTime lastWatchedAt) {
        this.lastWatchedAt = lastWatchedAt;
    }
    
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
    
    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
    
    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
    
    @PreUpdate
    public void preUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
    
    public void markAsWatched() {
        this.isWatched = true;
        this.watchProgress = 100;
        this.lastWatchedAt = LocalDateTime.now();
    }
    
    public void updateWatchProgress(Integer progress) {
        this.watchProgress = Math.min(100, Math.max(0, progress));
        if (this.watchProgress == 100) {
            this.isWatched = true;
            this.lastWatchedAt = LocalDateTime.now();
        }
    }
}






