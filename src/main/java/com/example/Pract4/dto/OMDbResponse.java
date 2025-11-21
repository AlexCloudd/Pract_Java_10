package com.example.Pract4.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

public class OMDbResponse {
    @JsonProperty("Search")
    private List<OMDbMovie> search;
    @JsonProperty("totalResults")
    private String totalResults;
    @JsonProperty("Response")
    private String response;
    @JsonProperty("Error")
    private String error;

    // Getters and Setters
    public List<OMDbMovie> getSearch() {
        return search;
    }

    public void setSearch(List<OMDbMovie> search) {
        this.search = search;
    }

    public String getTotalResults() {
        return totalResults;
    }

    public void setTotalResults(String totalResults) {
        this.totalResults = totalResults;
    }

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public static class OMDbMovie {
        @JsonProperty("Title")
        private String title;
        @JsonProperty("Year")
        private String year;
        @JsonProperty("imdbID")
        private String imdbID;
        @JsonProperty("Type")
        private String type;
        @JsonProperty("Poster")
        private String poster;

        // Getters and Setters
        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getYear() {
            return year;
        }

        public void setYear(String year) {
            this.year = year;
        }

        public String getImdbID() {
            return imdbID;
        }

        public void setImdbID(String imdbID) {
            this.imdbID = imdbID;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getPoster() {
            return poster;
        }

        public void setPoster(String poster) {
            this.poster = poster;
        }
    }
}
