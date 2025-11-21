package com.example.Pract4.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

public class TMDBResponse {
    
    @JsonProperty("page")
    private Integer page;
    
    @JsonProperty("results")
    private List<TMDBMovie> results;
    
    @JsonProperty("total_pages")
    private Integer totalPages;
    
    @JsonProperty("total_results")
    private Integer totalResults;

    // Constructors
    public TMDBResponse() {}

    // Getters and Setters
    public Integer getPage() {
        return page;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    public List<TMDBMovie> getResults() {
        return results;
    }

    public void setResults(List<TMDBMovie> results) {
        this.results = results;
    }

    public Integer getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(Integer totalPages) {
        this.totalPages = totalPages;
    }

    public Integer getTotalResults() {
        return totalResults;
    }

    public void setTotalResults(Integer totalResults) {
        this.totalResults = totalResults;
    }

    // Inner class for TMDB Movie
    public static class TMDBMovie {
        
        @JsonProperty("adult")
        private Boolean adult;
        
        @JsonProperty("backdrop_path")
        private String backdropPath;
        
        @JsonProperty("genre_ids")
        private List<Integer> genreIds;
        
        @JsonProperty("id")
        private Integer id;
        
        @JsonProperty("original_language")
        private String originalLanguage;
        
        @JsonProperty("original_title")
        private String originalTitle;
        
        @JsonProperty("overview")
        private String overview;
        
        @JsonProperty("popularity")
        private Double popularity;
        
        @JsonProperty("poster_path")
        private String posterPath;
        
        @JsonProperty("release_date")
        private String releaseDate;
        
        @JsonProperty("title")
        private String title;
        
        @JsonProperty("video")
        private Boolean video;
        
        @JsonProperty("vote_average")
        private Double voteAverage;
        
        @JsonProperty("vote_count")
        private Integer voteCount;

        // Constructors
        public TMDBMovie() {}

        // Getters and Setters
        public Boolean getAdult() {
            return adult;
        }

        public void setAdult(Boolean adult) {
            this.adult = adult;
        }

        public String getBackdropPath() {
            return backdropPath;
        }

        public void setBackdropPath(String backdropPath) {
            this.backdropPath = backdropPath;
        }

        public List<Integer> getGenreIds() {
            return genreIds;
        }

        public void setGenreIds(List<Integer> genreIds) {
            this.genreIds = genreIds;
        }

        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }

        public String getOriginalLanguage() {
            return originalLanguage;
        }

        public void setOriginalLanguage(String originalLanguage) {
            this.originalLanguage = originalLanguage;
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

        public Double getPopularity() {
            return popularity;
        }

        public void setPopularity(Double popularity) {
            this.popularity = popularity;
        }

        public String getPosterPath() {
            return posterPath;
        }

        public void setPosterPath(String posterPath) {
            this.posterPath = posterPath;
        }

        public String getReleaseDate() {
            return releaseDate;
        }

        public void setReleaseDate(String releaseDate) {
            this.releaseDate = releaseDate;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public Boolean getVideo() {
            return video;
        }

        public void setVideo(Boolean video) {
            this.video = video;
        }

        public Double getVoteAverage() {
            return voteAverage;
        }

        public void setVoteAverage(Double voteAverage) {
            this.voteAverage = voteAverage;
        }

        public Integer getVoteCount() {
            return voteCount;
        }

        public void setVoteCount(Integer voteCount) {
            this.voteCount = voteCount;
        }
    }
}




