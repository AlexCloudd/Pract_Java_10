package com.example.Pract4.model;

import com.example.Pract4.entity.Watchlist;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public class WatchlistModel {
    
    private Long id;
    private String name;
    private String description;
    private String coverArtUrl;
    private Boolean isPublic;
    private Boolean isCollaborative;
    private Long viewCount;
    private Long likeCount;
    private Long movieCount;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String userName;
    private List<MovieModel> movies;
    
    public WatchlistModel() {}
    
    public WatchlistModel(Watchlist watchlist) {
        this.id = watchlist.getId();
        this.name = watchlist.getName();
        this.description = watchlist.getDescription();
        this.coverArtUrl = watchlist.getCoverArtUrl();
        this.isPublic = watchlist.getIsPublic();
        this.isCollaborative = watchlist.getIsCollaborative();
        this.viewCount = watchlist.getViewCount();
        this.likeCount = watchlist.getLikeCount();
        this.movieCount = watchlist.getMovieCount();
        this.createdAt = watchlist.getCreatedAt();
        this.updatedAt = watchlist.getUpdatedAt();
        this.userName = watchlist.getUser() != null ? watchlist.getUser().getUsername() : null;
        this.movies = watchlist.getMovies() != null ? 
                watchlist.getMovies().stream()
                        .map(MovieModel::new)
                        .collect(Collectors.toList()) : List.of();
    }
    
    // Getters and Setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    
    public String getCoverArtUrl() {
        return coverArtUrl;
    }
    
    public void setCoverArtUrl(String coverArtUrl) {
        this.coverArtUrl = coverArtUrl;
    }
    
    public Boolean getIsPublic() {
        return isPublic;
    }
    
    public void setIsPublic(Boolean isPublic) {
        this.isPublic = isPublic;
    }
    
    public Boolean getIsCollaborative() {
        return isCollaborative;
    }
    
    public void setIsCollaborative(Boolean isCollaborative) {
        this.isCollaborative = isCollaborative;
    }
    
    public Long getViewCount() {
        return viewCount;
    }
    
    public void setViewCount(Long viewCount) {
        this.viewCount = viewCount;
    }
    
    public Long getLikeCount() {
        return likeCount;
    }
    
    public void setLikeCount(Long likeCount) {
        this.likeCount = likeCount;
    }
    
    public Long getMovieCount() {
        return movieCount;
    }
    
    public void setMovieCount(Long movieCount) {
        this.movieCount = movieCount;
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
    
    public String getUserName() {
        return userName;
    }
    
    public void setUserName(String userName) {
        this.userName = userName;
    }
    
    public List<MovieModel> getMovies() {
        return movies;
    }
    
    public void setMovies(List<MovieModel> movies) {
        this.movies = movies;
    }
}






