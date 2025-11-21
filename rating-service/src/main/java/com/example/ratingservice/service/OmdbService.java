package com.example.ratingservice.service;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
public class OmdbService {
    
    @Value("${omdb.api.key:your_omdb_api_key}")
    private String omdbApiKey;
    
    private final RestTemplate restTemplate;
    private final String OMDB_BASE_URL = "http://www.omdbapi.com/";
    
    public OmdbService() {
        this.restTemplate = new RestTemplate();
    }
    
    public OmdbMovie getMovieByTitle(String title) {
        String url = OMDB_BASE_URL + "?apikey=" + omdbApiKey + "&t=" + title.replace(" ", "+");
        try {
            return restTemplate.getForObject(url, OmdbMovie.class);
        } catch (Exception e) {
            return null;
        }
    }
    
    public OmdbMovie getMovieByImdbId(String imdbId) {
        String url = OMDB_BASE_URL + "?apikey=" + omdbApiKey + "&i=" + imdbId;
        System.out.println("OMDB Movie URL: " + url);
        try {
            OmdbMovie result = restTemplate.getForObject(url, OmdbMovie.class);
            System.out.println("OMDB Movie Response: " + (result != null ? result.getResponse() : "null"));
            if (result != null && result.getError() != null) {
                System.out.println("OMDB Error: " + result.getError());
            }
            return result;
        } catch (Exception e) {
            System.out.println("OMDB Movie API error: " + e.getMessage());
            return null;
        }
    }
    
    public List<OmdbMovie> searchMovies(String query) {
        String url = OMDB_BASE_URL + "?apikey=" + omdbApiKey + "&s=" + query.replace(" ", "+");
        System.out.println("OMDB Search URL: " + url);
        try {
            String jsonResponse = restTemplate.getForObject(url, String.class);
            System.out.println("Raw JSON Response: " + jsonResponse);
            
            OmdbSearchResponse response = restTemplate.getForObject(url, OmdbSearchResponse.class);
            System.out.println("OMDB Search Response: " + (response != null ? response.getResponse() : "null"));
            if (response != null && response.getError() != null) {
                System.out.println("OMDB Error: " + response.getError());
                return List.of();
            }
            if (response != null && response.getSearch() != null) {
                System.out.println("Found " + response.getSearch().size() + " movies");
                return response.getSearch();
            }
            System.out.println("No movies found or response is null");
            return List.of();
        } catch (Exception e) {
            System.out.println("OMDB Search API error: " + e.getMessage());
            e.printStackTrace();
            return List.of();
        }
    }
    
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class OmdbMovie {
        @JsonProperty("Title")
        private String title;
        
        @JsonProperty("Year")
        private String year;
        
        @JsonProperty("Rated")
        private String rated;
        
        @JsonProperty("Released")
        private String released;
        
        @JsonProperty("Runtime")
        private String runtime;
        
        @JsonProperty("Genre")
        private String genre;
        
        @JsonProperty("Director")
        private String director;
        
        @JsonProperty("Writer")
        private String writer;
        
        @JsonProperty("Actors")
        private String actors;
        
        @JsonProperty("Plot")
        private String plot;
        
        @JsonProperty("Language")
        private String language;
        
        @JsonProperty("Country")
        private String country;
        
        @JsonProperty("Awards")
        private String awards;
        
        @JsonProperty("Poster")
        private String poster;
        
        @JsonProperty("Ratings")
        private List<Rating> ratings;
        
        @JsonProperty("Metascore")
        private String metascore;
        
        @JsonProperty("imdbRating")
        private String imdbRating;
        
        @JsonProperty("imdbVotes")
        private String imdbVotes;
        
        @JsonProperty("imdbID")
        private String imdbId;
        
        @JsonProperty("Type")
        private String type;
        
        @JsonProperty("DVD")
        private String dvd;
        
        @JsonProperty("BoxOffice")
        private String boxOffice;
        
        @JsonProperty("Production")
        private String production;
        
        @JsonProperty("Website")
        private String website;
        
        @JsonProperty("Response")
        private String response;
        
        @JsonProperty("Error")
        private String error;
        
        public String getTitle() { return title; }
        public void setTitle(String title) { this.title = title; }
        
        public String getYear() { return year; }
        public void setYear(String year) { this.year = year; }
        
        public String getRated() { return rated; }
        public void setRated(String rated) { this.rated = rated; }
        
        public String getReleased() { return released; }
        public void setReleased(String released) { this.released = released; }
        
        public String getRuntime() { return runtime; }
        public void setRuntime(String runtime) { this.runtime = runtime; }
        
        public String getGenre() { return genre; }
        public void setGenre(String genre) { this.genre = genre; }
        
        public String getDirector() { return director; }
        public void setDirector(String director) { this.director = director; }
        
        public String getWriter() { return writer; }
        public void setWriter(String writer) { this.writer = writer; }
        
        public String getActors() { return actors; }
        public void setActors(String actors) { this.actors = actors; }
        
        public String getPlot() { return plot; }
        public void setPlot(String plot) { this.plot = plot; }
        
        public String getLanguage() { return language; }
        public void setLanguage(String language) { this.language = language; }
        
        public String getCountry() { return country; }
        public void setCountry(String country) { this.country = country; }
        
        public String getAwards() { return awards; }
        public void setAwards(String awards) { this.awards = awards; }
        
        public String getPoster() { return poster; }
        public void setPoster(String poster) { this.poster = poster; }
        
        public List<Rating> getRatings() { return ratings; }
        public void setRatings(List<Rating> ratings) { this.ratings = ratings; }
        
        public String getMetascore() { return metascore; }
        public void setMetascore(String metascore) { this.metascore = metascore; }
        
        public String getImdbRating() { return imdbRating; }
        public void setImdbRating(String imdbRating) { this.imdbRating = imdbRating; }
        
        public String getImdbVotes() { return imdbVotes; }
        public void setImdbVotes(String imdbVotes) { this.imdbVotes = imdbVotes; }
        
        public String getImdbId() { return imdbId; }
        public void setImdbId(String imdbId) { this.imdbId = imdbId; }
        
        public String getType() { return type; }
        public void setType(String type) { this.type = type; }
        
        public String getDvd() { return dvd; }
        public void setDvd(String dvd) { this.dvd = dvd; }
        
        public String getBoxOffice() { return boxOffice; }
        public void setBoxOffice(String boxOffice) { this.boxOffice = boxOffice; }
        
        public String getProduction() { return production; }
        public void setProduction(String production) { this.production = production; }
        
        public String getWebsite() { return website; }
        public void setWebsite(String website) { this.website = website; }
        
        public String getResponse() { return response; }
        public void setResponse(String response) { this.response = response; }
        
        public String getError() { return error; }
        public void setError(String error) { this.error = error; }
        
        public boolean isSuccess() {
            return "True".equals(response);
        }
    }
    
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class OmdbSearchResponse {
        @JsonProperty("Search")
        private List<OmdbMovie> search;
        
        @JsonProperty("totalResults")
        private String totalResults;
        
        @JsonProperty("Response")
        private String response;
        
        @JsonProperty("Error")
        private String error;
        
        public List<OmdbMovie> getSearch() { return search; }
        public void setSearch(List<OmdbMovie> search) { this.search = search; }
        
        public String getTotalResults() { return totalResults; }
        public void setTotalResults(String totalResults) { this.totalResults = totalResults; }
        
        public String getResponse() { return response; }
        public void setResponse(String response) { this.response = response; }
        
        public String getError() { return error; }
        public void setError(String error) { this.error = error; }
    }
    
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Rating {
        @JsonProperty("Source")
        private String source;
        
        @JsonProperty("Value")
        private String value;
        
        public String getSource() { return source; }
        public void setSource(String source) { this.source = source; }
        
        public String getValue() { return value; }
        public void setValue(String value) { this.value = value; }
    }
}
