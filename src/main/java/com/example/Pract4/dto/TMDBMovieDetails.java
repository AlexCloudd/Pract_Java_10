package com.example.Pract4.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

public class TMDBMovieDetails {
    
    @JsonProperty("adult")
    private Boolean adult;
    
    @JsonProperty("backdrop_path")
    private String backdropPath;
    
    @JsonProperty("belongs_to_collection")
    private Object belongsToCollection;
    
    @JsonProperty("budget")
    private Long budget;
    
    @JsonProperty("genres")
    private List<TMDBGenre> genres;
    
    @JsonProperty("homepage")
    private String homepage;
    
    @JsonProperty("id")
    private Integer id;
    
    @JsonProperty("imdb_id")
    private String imdbId;
    
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
    
    @JsonProperty("production_companies")
    private List<TMDBProductionCompany> productionCompanies;
    
    @JsonProperty("production_countries")
    private List<TMDBProductionCountry> productionCountries;
    
    @JsonProperty("release_date")
    private String releaseDate;
    
    @JsonProperty("revenue")
    private Long revenue;
    
    @JsonProperty("runtime")
    private Integer runtime;
    
    @JsonProperty("spoken_languages")
    private List<TMDBSpokenLanguage> spokenLanguages;
    
    @JsonProperty("status")
    private String status;
    
    @JsonProperty("tagline")
    private String tagline;
    
    @JsonProperty("title")
    private String title;
    
    @JsonProperty("video")
    private Boolean video;
    
    @JsonProperty("vote_average")
    private Double voteAverage;
    
    @JsonProperty("vote_count")
    private Integer voteCount;

    // Constructors
    public TMDBMovieDetails() {}

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

    public Object getBelongsToCollection() {
        return belongsToCollection;
    }

    public void setBelongsToCollection(Object belongsToCollection) {
        this.belongsToCollection = belongsToCollection;
    }

    public Long getBudget() {
        return budget;
    }

    public void setBudget(Long budget) {
        this.budget = budget;
    }

    public List<TMDBGenre> getGenres() {
        return genres;
    }

    public void setGenres(List<TMDBGenre> genres) {
        this.genres = genres;
    }

    public String getHomepage() {
        return homepage;
    }

    public void setHomepage(String homepage) {
        this.homepage = homepage;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getImdbId() {
        return imdbId;
    }

    public void setImdbId(String imdbId) {
        this.imdbId = imdbId;
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

    public List<TMDBProductionCompany> getProductionCompanies() {
        return productionCompanies;
    }

    public void setProductionCompanies(List<TMDBProductionCompany> productionCompanies) {
        this.productionCompanies = productionCompanies;
    }

    public List<TMDBProductionCountry> getProductionCountries() {
        return productionCountries;
    }

    public void setProductionCountries(List<TMDBProductionCountry> productionCountries) {
        this.productionCountries = productionCountries;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public Long getRevenue() {
        return revenue;
    }

    public void setRevenue(Long revenue) {
        this.revenue = revenue;
    }

    public Integer getRuntime() {
        return runtime;
    }

    public void setRuntime(Integer runtime) {
        this.runtime = runtime;
    }

    public List<TMDBSpokenLanguage> getSpokenLanguages() {
        return spokenLanguages;
    }

    public void setSpokenLanguages(List<TMDBSpokenLanguage> spokenLanguages) {
        this.spokenLanguages = spokenLanguages;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getTagline() {
        return tagline;
    }

    public void setTagline(String tagline) {
        this.tagline = tagline;
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

    // Inner classes
    public static class TMDBGenre {
        @JsonProperty("id")
        private Integer id;
        
        @JsonProperty("name")
        private String name;

        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }

    public static class TMDBProductionCompany {
        @JsonProperty("id")
        private Integer id;
        
        @JsonProperty("logo_path")
        private String logoPath;
        
        @JsonProperty("name")
        private String name;
        
        @JsonProperty("origin_country")
        private String originCountry;

        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }

        public String getLogoPath() {
            return logoPath;
        }

        public void setLogoPath(String logoPath) {
            this.logoPath = logoPath;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getOriginCountry() {
            return originCountry;
        }

        public void setOriginCountry(String originCountry) {
            this.originCountry = originCountry;
        }
    }

    public static class TMDBProductionCountry {
        @JsonProperty("iso_3166_1")
        private String iso31661;
        
        @JsonProperty("name")
        private String name;

        public String getIso31661() {
            return iso31661;
        }

        public void setIso31661(String iso31661) {
            this.iso31661 = iso31661;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }

    public static class TMDBSpokenLanguage {
        @JsonProperty("english_name")
        private String englishName;
        
        @JsonProperty("iso_639_1")
        private String iso6391;
        
        @JsonProperty("name")
        private String name;

        public String getEnglishName() {
            return englishName;
        }

        public void setEnglishName(String englishName) {
            this.englishName = englishName;
        }

        public String getIso6391() {
            return iso6391;
        }

        public void setIso6391(String iso6391) {
            this.iso6391 = iso6391;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }
}




