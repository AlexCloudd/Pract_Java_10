package com.example.Pract4.model;

import com.example.Pract4.entity.Movie;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public class MovieModel {
    
    private Long id;
    private String tmdbId;
    private String imdbId;
    private String title;
    private String originalTitle;
    private String overview;
    private String description;
    private String shortDescription;
    private Integer year;
    private LocalDate releaseDate;
    private Integer duration;
    private String ageRating;
    private String country;
    private String language;
    private String genre;
    private String director;
    private String cast;
    private String producer;
    private String screenwriter;
    private String awards;
    private Long budget;
    private Long boxOffice;
    private Double imdbRating;
    private Double kinopoiskRating;
    private Double userRating;
    private Integer metascore;
    private Long ratingCount;
    private Long viewCount;
    private Long likeCount;
    private Long watchlistCount;
    private String posterUrl;
    private String backdropUrl;
    private String trailerUrl;
    private String videoUrl;
    private Boolean isAvailable;
    private Boolean isPremium;
    private Boolean isFeatured;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private List<String> castList;
    private String formattedDuration;
    private String formattedBudget;
    private String formattedBoxOffice;
    
    public MovieModel() {}
    
    public MovieModel(Movie movie) {
        this.id = movie.getId();
        this.tmdbId = movie.getTmdbId();
        this.imdbId = movie.getImdbId();
        this.title = movie.getTitle();
        this.originalTitle = movie.getOriginalTitle();
        this.overview = movie.getOverview();
        this.description = movie.getDescription();
        this.shortDescription = movie.getShortDescription();
        this.year = movie.getYear();
        this.releaseDate = movie.getReleaseDate();
        this.duration = movie.getDuration();
        this.ageRating = movie.getAgeRating();
        this.country = movie.getCountry();
        this.language = movie.getLanguage();
        this.genre = movie.getGenre();
        this.director = movie.getDirector();
        this.cast = movie.getCast();
        this.producer = movie.getProducer();
        this.screenwriter = movie.getScreenwriter();
        this.budget = movie.getBudget();
        this.boxOffice = movie.getBoxOffice();
        this.imdbRating = movie.getImdbRating();
        this.kinopoiskRating = movie.getKinopoiskRating();
        this.userRating = movie.getUserRating();
        this.metascore = movie.getMetascore();
        this.ratingCount = movie.getRatingCount();
        this.viewCount = movie.getViewCount();
        this.likeCount = movie.getLikeCount();
        this.watchlistCount = movie.getWatchlistCount();
        this.posterUrl = movie.getPosterUrl();
        this.backdropUrl = movie.getBackdropUrl();
        this.trailerUrl = movie.getTrailerUrl();
        this.videoUrl = movie.getVideoUrl();
        this.isAvailable = movie.getIsAvailable();
        this.isPremium = movie.getIsPremium();
        this.isFeatured = movie.getIsFeatured();
        this.createdAt = movie.getCreatedAt();
        this.updatedAt = movie.getUpdatedAt();
        
        // Дополнительные поля для удобства
        this.castList = parseCastList(movie.getCast());
        this.formattedDuration = formatDuration(movie.getDuration());
        this.formattedBudget = formatMoney(movie.getBudget());
        this.formattedBoxOffice = formatMoney(movie.getBoxOffice());
    }
    
    private List<String> parseCastList(String cast) {
        if (cast == null || cast.trim().isEmpty()) {
            return List.of();
        }
        return List.of(cast.split(","))
                .stream()
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .collect(Collectors.toList());
    }
    
    private String formatDuration(Integer duration) {
        if (duration == null) {
            return "Не указано";
        }
        int hours = duration / 60;
        int minutes = duration % 60;
        if (hours > 0) {
            return String.format("%d ч %d мин", hours, minutes);
        } else {
            return String.format("%d мин", minutes);
        }
    }
    
    private String formatMoney(Long amount) {
        if (amount == null) {
            return "Не указано";
        }
        if (amount >= 1_000_000_000) {
            return String.format("%.1f млрд $", amount / 1_000_000_000.0);
        } else if (amount >= 1_000_000) {
            return String.format("%.1f млн $", amount / 1_000_000.0);
        } else if (amount >= 1_000) {
            return String.format("%.1f тыс $", amount / 1_000.0);
        } else {
            return String.format("%d $", amount);
        }
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
    
    public String getAwards() {
        return awards;
    }
    
    public void setAwards(String awards) {
        this.awards = awards;
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
    
    public Double getUserRating() {
        return userRating;
    }
    
    public void setUserRating(Double userRating) {
        this.userRating = userRating;
    }
    
    public Integer getMetascore() {
        return metascore;
    }
    
    public void setMetascore(Integer metascore) {
        this.metascore = metascore;
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
    
    public List<String> getCastList() {
        return castList;
    }
    
    public void setCastList(List<String> castList) {
        this.castList = castList;
    }
    
    public String getFormattedDuration() {
        return formattedDuration;
    }
    
    public void setFormattedDuration(String formattedDuration) {
        this.formattedDuration = formattedDuration;
    }
    
    public String getFormattedBudget() {
        return formattedBudget;
    }
    
    public void setFormattedBudget(String formattedBudget) {
        this.formattedBudget = formattedBudget;
    }
    
    public String getFormattedBoxOffice() {
        return formattedBoxOffice;
    }
    
    public void setFormattedBoxOffice(String formattedBoxOffice) {
        this.formattedBoxOffice = formattedBoxOffice;
    }
}




