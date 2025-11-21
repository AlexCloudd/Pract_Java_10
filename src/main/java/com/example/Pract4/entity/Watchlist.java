package com.example.Pract4.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "watchlists")
public class Watchlist {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @NotBlank
    @Column(name = "name")
    private String name;
    
    @Column(name = "description", length = 1000)
    private String description;
    
    @Column(name = "cover_art_url")
    private String coverArtUrl;
    
    @Column(name = "is_public")
    private Boolean isPublic = true;
    
    @Column(name = "is_collaborative")
    private Boolean isCollaborative = false;
    
    @Column(name = "view_count")
    private Long viewCount = 0L;
    
    @Column(name = "like_count")
    private Long likeCount = 0L;
    
    @Column(name = "movie_count")
    private Long movieCount = 0L;
    
    @Column(name = "created_at")
    private LocalDateTime createdAt;
    
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    @NotNull
    private User user;
    
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "watchlist_movies",
        joinColumns = @JoinColumn(name = "watchlist_id"),
        inverseJoinColumns = @JoinColumn(name = "movie_id")
    )
    private Set<Movie> movies = new HashSet<>();
    
    // Constructors
    public Watchlist() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }
    
    public Watchlist(String name, User user) {
        this();
        this.name = name;
        this.user = user;
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
    
    public User getUser() {
        return user;
    }
    
    public void setUser(User user) {
        this.user = user;
    }
    
    public Set<Movie> getMovies() {
        return movies;
    }
    
    public void setMovies(Set<Movie> movies) {
        this.movies = movies;
    }
    
    @PreUpdate
    public void preUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
    
    public void addMovie(Movie movie) {
        this.movies.add(movie);
        movie.getWatchlists().add(this);
        this.movieCount = (long) this.movies.size();
    }
    
    public void removeMovie(Movie movie) {
        this.movies.remove(movie);
        movie.getWatchlists().remove(this);
        this.movieCount = (long) this.movies.size();
    }
    
    public void incrementViewCount() {
        this.viewCount++;
    }
    
    public void incrementLikeCount() {
        this.likeCount++;
    }
}






