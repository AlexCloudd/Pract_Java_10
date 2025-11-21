package com.example.Pract4.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "movies")
public class Movie {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @NotBlank
    @Column(name = "tmdb_id", unique = true)
    private String tmdbId;
    
    @Column(name = "imdb_id")
    private String imdbId;
    
    @NotBlank
    @Column(name = "title")
    private String title;
    
    @Column(name = "original_title")
    private String originalTitle;
    
    @Column(name = "overview", length = 2000)
    private String overview;
    
    @Column(name = "description", length = 2000)
    private String description;
    
    @Column(name = "short_description", length = 500)
    private String shortDescription;
    
    @Column(name = "year")
    private Integer year;
    
    @Column(name = "release_date")
    private LocalDate releaseDate;
    
    @Column(name = "duration")
    private Integer duration; // в минутах
    
    @Column(name = "age_rating")
    private String ageRating; // 0+, 6+, 12+, 16+, 18+
    
    @Column(name = "country")
    private String country;
    
    @Column(name = "language")
    private String language;
    
    @Column(name = "genre")
    private String genre;
    
    @Column(name = "director")
    private String director;
    
    @Column(name = "movie_cast", length = 2000)
    private String cast; // актеры через запятую
    
    @Column(name = "producer")
    private String producer;
    
    @Column(name = "screenwriter")
    private String screenwriter;
    
    @Column(name = "budget")
    private Long budget;
    
    @Column(name = "box_office")
    private Long boxOffice;
    
    @Column(name = "imdb_rating")
    private Double imdbRating;
    
    @Column(name = "kinopoisk_rating")
    private Double kinopoiskRating;
    
    @Column(name = "metascore")
    private Integer metascore;
    
    @Column(name = "user_rating")
    private Double userRating = 0.0;
    
    @Column(name = "rating_count")
    private Long ratingCount = 0L;
    
    @Column(name = "view_count")
    private Long viewCount = 0L;
    
    @Column(name = "like_count")
    private Long likeCount = 0L;
    
    @Column(name = "watchlist_count")
    private Long watchlistCount = 0L;
    
    @Column(name = "poster_url")
    private String posterUrl;
    
    @Column(name = "backdrop_url")
    private String backdropUrl;
    
    @Column(name = "trailer_url")
    private String trailerUrl;
    
    @Column(name = "video_url")
    private String videoUrl;
    
    @Column(name = "is_available")
    private Boolean isAvailable = true;
    
    @Column(name = "is_premium")
    private Boolean isPremium = false;
    
    @Column(name = "is_featured")
    private Boolean isFeatured = false;
    
    @Column(name = "created_at")
    private LocalDateTime createdAt;
    
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    
    @ManyToMany(mappedBy = "movies")
    private Set<Watchlist> watchlists = new HashSet<>();
    
    @OneToMany(mappedBy = "movie", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<UserMovieInteraction> userInteractions = new HashSet<>();
    
    // Constructors
    public Movie() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }
    
    public Movie(String title, String director, Integer year) {
        this();
        this.title = title;
        this.director = director;
        this.year = year;
    }
    
    // Getters and Setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public String getTmdbId() {
        return tmdbId;
    }
    
    public void setTmdbId(String tmdbId) {
        this.tmdbId = tmdbId;
    }
    
    public String getImdbId() {
        return imdbId;
    }
    
    public void setImdbId(String imdbId) {
        this.imdbId = imdbId;
    }
    
    public String getTitle() {
        return title;
    }
    
    public void setTitle(String title) {
        this.title = title;
    }
    
    public String getOriginalTitle() {
        return originalTitle;
    }
    
    public void setOriginalTitle(String originalTitle) {
        this.originalTitle = originalTitle;
    }
    
    public String getOverview() {
        return overview;
    }
    
    public void setOverview(String overview) {
        this.overview = overview;
    }
    
    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    
    public String getShortDescription() {
        return shortDescription;
    }
    
    public void setShortDescription(String shortDescription) {
        this.shortDescription = shortDescription;
    }
    
    public Integer getYear() {
        return year;
    }
    
    public void setYear(Integer year) {
        this.year = year;
    }
    
    public LocalDate getReleaseDate() {
        return releaseDate;
    }
    
    public void setReleaseDate(LocalDate releaseDate) {
        this.releaseDate = releaseDate;
    }
    
    public Integer getDuration() {
        return duration;
    }
    
    public void setDuration(Integer duration) {
        this.duration = duration;
    }
    
    public String getAgeRating() {
        return ageRating;
    }
    
    public void setAgeRating(String ageRating) {
        this.ageRating = ageRating;
    }
    
    public String getCountry() {
        return country;
    }
    
    public void setCountry(String country) {
        this.country = country;
    }
    
    public String getLanguage() {
        return language;
    }
    
    public void setLanguage(String language) {
        this.language = language;
    }
    
    public String getGenre() {
        return genre;
    }
    
    public void setGenre(String genre) {
        this.genre = genre;
    }
    
    public String getDirector() {
        return director;
    }
    
    public void setDirector(String director) {
        this.director = director;
    }
    
    public String getCast() {
        return cast;
    }
    
    public void setCast(String cast) {
        this.cast = cast;
    }
    
    public String getProducer() {
        return producer;
    }
    
    public void setProducer(String producer) {
        this.producer = producer;
    }
    
    public String getScreenwriter() {
        return screenwriter;
    }
    
    public void setScreenwriter(String screenwriter) {
        this.screenwriter = screenwriter;
    }
    
    public Long getBudget() {
        return budget;
    }
    
    public void setBudget(Long budget) {
        this.budget = budget;
    }
    
    public Long getBoxOffice() {
        return boxOffice;
    }
    
    public void setBoxOffice(Long boxOffice) {
        this.boxOffice = boxOffice;
    }
    
    public Double getImdbRating() {
        return imdbRating;
    }
    
    public void setImdbRating(Double imdbRating) {
        this.imdbRating = imdbRating;
    }
    
    public Double getKinopoiskRating() {
        return kinopoiskRating;
    }

    public void setKinopoiskRating(Double kinopoiskRating) {
        this.kinopoiskRating = kinopoiskRating;
    }

    public Integer getMetascore() {
        return metascore;
    }

    public void setMetascore(Integer metascore) {
        this.metascore = metascore;
    }
    
    public Double getUserRating() {
        return userRating;
    }
    
    public void setUserRating(Double userRating) {
        this.userRating = userRating;
    }
    
    public Long getRatingCount() {
        return ratingCount;
    }
    
    public void setRatingCount(Long ratingCount) {
        this.ratingCount = ratingCount;
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
    
    public Long getWatchlistCount() {
        return watchlistCount;
    }
    
    public void setWatchlistCount(Long watchlistCount) {
        this.watchlistCount = watchlistCount;
    }
    
    public String getPosterUrl() {
        return posterUrl;
    }
    
    public void setPosterUrl(String posterUrl) {
        this.posterUrl = posterUrl;
    }
    
    public String getBackdropUrl() {
        return backdropUrl;
    }
    
    public void setBackdropUrl(String backdropUrl) {
        this.backdropUrl = backdropUrl;
    }
    
    public String getTrailerUrl() {
        return trailerUrl;
    }
    
    public void setTrailerUrl(String trailerUrl) {
        this.trailerUrl = trailerUrl;
    }
    
    public String getVideoUrl() {
        return videoUrl;
    }
    
    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
    }
    
    public Boolean getIsAvailable() {
        return isAvailable;
    }
    
    public void setIsAvailable(Boolean isAvailable) {
        this.isAvailable = isAvailable;
    }
    
    public Boolean getIsPremium() {
        return isPremium;
    }
    
    public void setIsPremium(Boolean isPremium) {
        this.isPremium = isPremium;
    }
    
    public Boolean getIsFeatured() {
        return isFeatured;
    }
    
    public void setIsFeatured(Boolean isFeatured) {
        this.isFeatured = isFeatured;
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
    
    public Set<Watchlist> getWatchlists() {
        return watchlists;
    }
    
    public void setWatchlists(Set<Watchlist> watchlists) {
        this.watchlists = watchlists;
    }
    
    public Set<UserMovieInteraction> getUserInteractions() {
        return userInteractions;
    }
    
    public void setUserInteractions(Set<UserMovieInteraction> userInteractions) {
        this.userInteractions = userInteractions;
    }
    
    @PreUpdate
    public void preUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
    
    public void incrementViewCount() {
        this.viewCount++;
    }
    
    public void incrementLikeCount() {
        this.likeCount++;
    }
    
    public void incrementWatchlistCount() {
        this.watchlistCount++;
    }
    
    public void addRating(Double rating) {
        if (this.ratingCount == 0) {
            this.userRating = rating;
        } else {
            this.userRating = (this.userRating * this.ratingCount + rating) / (this.ratingCount + 1);
        }
        this.ratingCount++;
    }
}
