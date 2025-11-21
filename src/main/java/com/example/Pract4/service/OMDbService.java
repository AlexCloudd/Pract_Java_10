package com.example.Pract4.service;

import com.example.Pract4.dto.OMDbMovieDetail;
import com.example.Pract4.dto.OMDbResponse;
import com.example.Pract4.entity.Movie;
import com.example.Pract4.model.MovieModel;
import com.example.Pract4.repository.MovieRepository;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class OMDbService {

    private static final Logger logger = LoggerFactory.getLogger(OMDbService.class);

    private final WebClient webClient;

    @Autowired
    private MovieRepository movieRepository;

    @Value("${omdb.api.key}")
    private String omdbApiKey;

    @Value("${omdb.api.base.url}")
    private String omdbBaseUrl;

    public OMDbService(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder
                .baseUrl("http://www.omdbapi.com")
                .defaultHeader("Accept", "application/json")
                .build();
    }

    @CircuitBreaker(name = "omdb-api", fallbackMethod = "fallbackGetPopularMovies")
    @Retry(name = "omdb-api")
    public List<MovieModel> getPopularMovies() {
        try {
            logger.info("Fetching popular movies from OMDb API");

            // Популярные фильмы - используем поиск по популярным названиям
            String[] popularTitles = {
                "Avengers", "Batman", "Spider-Man", "Iron Man", "Superman",
                "Titanic", "Avatar", "Star Wars", "Harry Potter", "Lord of the Rings"
            };

            List<MovieModel> allMovies = new ArrayList<>();
            
            for (String title : popularTitles) {
                try {
                    OMDbResponse response = webClient.get()
                            .uri("?apikey={key}&s={title}&type=movie", omdbApiKey, title)
                            .retrieve()
                            .bodyToMono(OMDbResponse.class)
                            .block();

                    logger.info("Response for title '{}': {}", title, response);
                    if (response != null && response.getSearch() != null && !response.getSearch().isEmpty()) {
                        // Берем первый результат из каждого поиска
                        OMDbResponse.OMDbMovie movie = response.getSearch().get(0);
                        MovieModel movieModel = convertToMovieModel(movie);
                        allMovies.add(movieModel);
                        logger.info("Added movie: {} ({})", movie.getTitle(), movie.getYear());
                    } else {
                        logger.warn("Empty response for title: {} - response: {}", title, response);
                    }
                } catch (Exception e) {
                    logger.warn("Failed to fetch movie for title '{}': {}", title, e.getMessage());
                }
            }

            logger.info("Successfully fetched {} popular movies", allMovies.size());
            return allMovies;

        } catch (WebClientResponseException e) {
            logger.error("OMDb API error for popular movies: {}", e.getMessage());
            return new ArrayList<>();
        } catch (Exception e) {
            logger.error("Unexpected error fetching popular movies: {}", e.getMessage());
            return new ArrayList<>();
        }
    }

    @CircuitBreaker(name = "omdb-api", fallbackMethod = "fallbackGetTopRatedMovies")
    @Retry(name = "omdb-api")
    public List<MovieModel> getTopRatedMovies() {
        try {
            logger.info("Fetching top rated movies from OMDb API");

            // Топ рейтинговые фильмы - используем поиск по известным высокорейтинговым фильмам
            String[] topRatedTitles = {
                "The Shawshank Redemption", "The Godfather", "The Dark Knight", "Pulp Fiction",
                "Forrest Gump", "Inception", "The Matrix", "Goodfellas", "The Lord of the Rings",
                "Fight Club", "The Silence of the Lambs", "Schindler's List"
            };

            List<MovieModel> allMovies = new ArrayList<>();
            
            for (String title : topRatedTitles) {
                try {
                    OMDbResponse response = webClient.get()
                            .uri("?apikey={key}&s={title}&type=movie", omdbApiKey, title)
                            .retrieve()
                            .bodyToMono(OMDbResponse.class)
                            .block();

                    if (response != null && response.getSearch() != null && !response.getSearch().isEmpty()) {
                        // Берем первый результат из каждого поиска
                        OMDbResponse.OMDbMovie movie = response.getSearch().get(0);
                        MovieModel movieModel = convertToMovieModel(movie);
                        allMovies.add(movieModel);
                        logger.info("Added movie: {} ({})", movie.getTitle(), movie.getYear());
                    } else {
                        logger.warn("Empty response for title: {}", title);
                    }
                } catch (Exception e) {
                    logger.warn("Failed to fetch movie for title '{}': {}", title, e.getMessage());
                }
            }

            logger.info("Successfully fetched {} top rated movies", allMovies.size());
            return allMovies;

        } catch (WebClientResponseException e) {
            logger.error("OMDb API error for top rated movies: {}", e.getMessage());
            return new ArrayList<>();
        } catch (Exception e) {
            logger.error("Unexpected error fetching top rated movies: {}", e.getMessage());
            return new ArrayList<>();
        }
    }

    @CircuitBreaker(name = "omdb-api", fallbackMethod = "fallbackSearchMovies")
    @Retry(name = "omdb-api")
    public List<MovieModel> searchMovies(String query) {
        try {
            logger.info("Searching movies from OMDb API: {}", query);

            OMDbResponse response = webClient.get()
                    .uri("?apikey={key}&s={query}&type=movie", omdbApiKey, query)
                    .retrieve()
                    .bodyToMono(OMDbResponse.class)
                    .block();

            if (response != null && response.getSearch() != null) {
                logger.info("Successfully searched movies: {} results", response.getSearch().size());
                return response.getSearch().stream()
                        .map(this::convertToMovieModel)
                        .collect(Collectors.toList());
            } else {
                logger.warn("Failed to search movies: empty response");
                return new ArrayList<>();
            }

        } catch (WebClientResponseException e) {
            logger.error("OMDb API error for search '{}': {}", query, e.getMessage());
            return new ArrayList<>();
        } catch (Exception e) {
            logger.error("Unexpected error searching movies '{}': {}", query, e.getMessage());
            return new ArrayList<>();
        }
    }

    @CircuitBreaker(name = "omdb-api", fallbackMethod = "fallbackAdvancedSearch")
    @Retry(name = "omdb-api")
    public List<MovieModel> advancedSearch(String query, String genre, Integer year, String country) {
        try {
            logger.info("Advanced search from OMDb API - Query: {}, Genre: {}, Year: {}, Country: {}", 
                       query, genre, year, country);

            List<MovieModel> allResults = new ArrayList<>();
            
            // Если есть поисковый запрос, ищем по нему
            if (query != null && !query.trim().isEmpty()) {
                // Поиск по названию
                OMDbResponse response = webClient.get()
                        .uri("?apikey={key}&s={query}&type=movie", omdbApiKey, query)
                        .retrieve()
                        .bodyToMono(OMDbResponse.class)
                        .block();

                if (response != null && response.getSearch() != null) {
                    List<MovieModel> searchResults = response.getSearch().stream()
                            .map(this::convertToMovieModel)
                            .collect(Collectors.toList());
                    
                    // Фильтруем результаты по дополнительным параметрам
                    List<MovieModel> filteredResults = searchResults.stream()
                            .filter(movie -> {
                                boolean matchesGenre = genre == null || genre.trim().isEmpty() || 
                                    (movie.getGenre() != null && movie.getGenre().toLowerCase().contains(genre.toLowerCase()));
                                boolean matchesYear = year == null || movie.getYear() == null || movie.getYear().equals(year);
                                boolean matchesCountry = country == null || country.trim().isEmpty() || 
                                    (movie.getCountry() != null && movie.getCountry().toLowerCase().contains(country.toLowerCase()));
                                
                                return matchesGenre && matchesYear && matchesCountry;
                            })
                            .limit(20) // Ограничиваем до 20 результатов
                            .collect(Collectors.toList());
                    
                    allResults.addAll(filteredResults);
                }
            } else if (year != null) {
                // Если есть только год, ищем популярные фильмы этого года
                List<String> popularTitles = Arrays.asList(
                    "movie", "film", "cinema", "action", "drama", "comedy", "thriller", 
                    "horror", "romance", "adventure", "sci-fi", "fantasy", "crime", 
                    "mystery", "animation", "documentary", "war", "western", "musical", "biography"
                );
                
                // Пробуем найти фильмы по популярным запросам для данного года
                for (String title : popularTitles) {
                    if (allResults.size() >= 20) break;
                    
                    OMDbResponse response = webClient.get()
                            .uri("?apikey={key}&s={title}&type=movie&y={year}", omdbApiKey, title, year)
                            .retrieve()
                            .bodyToMono(OMDbResponse.class)
                            .block();

                    if (response != null && response.getSearch() != null) {
                        List<MovieModel> searchResults = response.getSearch().stream()
                                .map(this::convertToMovieModel)
                                .filter(movie -> movie.getYear() != null && movie.getYear().equals(year))
                                .limit(5) // Берем максимум 5 фильмов из каждого запроса
                                .collect(Collectors.toList());
                        
                        allResults.addAll(searchResults);
                    }
                }
                
                // Убираем дубликаты и ограничиваем результат
                allResults = allResults.stream()
                        .distinct()
                        .limit(20)
                        .collect(Collectors.toList());
                        
            } else if (genre != null && !genre.trim().isEmpty()) {
                // Если есть только жанр, ищем по популярным названиям этого жанра
                List<String> genreTitles = Arrays.asList(
                    "action", "drama", "comedy", "thriller", "horror", "romance", 
                    "adventure", "sci-fi", "fantasy", "crime", "mystery", "animation"
                );
                
                for (String title : genreTitles) {
                    if (allResults.size() >= 20) break;
                    
                    OMDbResponse response = webClient.get()
                            .uri("?apikey={key}&s={title}&type=movie", omdbApiKey, title)
                            .retrieve()
                            .bodyToMono(OMDbResponse.class)
                            .block();

                    if (response != null && response.getSearch() != null) {
                        List<MovieModel> searchResults = response.getSearch().stream()
                                .map(this::convertToMovieModel)
                                .filter(movie -> movie.getGenre() != null && 
                                        movie.getGenre().toLowerCase().contains(genre.toLowerCase()))
                                .limit(5)
                                .collect(Collectors.toList());
                        
                        allResults.addAll(searchResults);
                    }
                }
                
                // Убираем дубликаты и ограничиваем результат
                allResults = allResults.stream()
                        .distinct()
                        .limit(20)
                        .collect(Collectors.toList());
                        
            } else {
                // Если нет никаких параметров, показываем популярные фильмы
                allResults = getPopularMovies();
            }

            logger.info("Advanced search completed: {} results", allResults.size());
            return allResults;

        } catch (WebClientResponseException e) {
            logger.error("OMDb API error for advanced search: {}", e.getMessage());
            return new ArrayList<>();
        } catch (Exception e) {
            logger.error("Unexpected error in advanced search: {}", e.getMessage());
            return new ArrayList<>();
        }
    }

    @CircuitBreaker(name = "omdb-api", fallbackMethod = "fallbackGetMovieDetails")
    @Retry(name = "omdb-api")
    public Optional<MovieModel> getMovieDetails(String imdbId) {
        try {
            logger.info("Fetching movie details from OMDb API: {}", imdbId);

            OMDbMovieDetail response = webClient.get()
                    .uri("?apikey={key}&i={imdbId}&plot=full", omdbApiKey, imdbId)
                    .retrieve()
                    .bodyToMono(OMDbMovieDetail.class)
                    .block();

            if (response != null && "True".equals(response.getResponse())) {
                logger.info("Successfully fetched movie details: {}", response.getTitle());
                return Optional.of(convertToMovieModel(response));
            } else {
                logger.warn("Failed to fetch movie details: {}", response != null ? response.getError() : "empty response");
                return Optional.empty();
            }

        } catch (WebClientResponseException e) {
            logger.error("OMDb API error for movie ID '{}': {}", imdbId, e.getMessage());
            return Optional.empty();
        } catch (Exception e) {
            logger.error("Unexpected error fetching movie details '{}': {}", imdbId, e.getMessage());
            return Optional.empty();
        }
    }

    public MovieModel saveMovieFromOMDb(String imdbId) {
        Optional<MovieModel> movieModel = getMovieDetails(imdbId);
        if (movieModel.isPresent()) {
            Movie movie = convertToMovie(movieModel.get());
            Movie savedMovie = movieRepository.save(movie);
            logger.info("Saved movie from OMDb to database: {}", savedMovie.getTitle());
            return new MovieModel(savedMovie);
        }
        return null;
    }

    private MovieModel convertToMovieModel(OMDbResponse.OMDbMovie omdbMovie) {
        MovieModel movieModel = new MovieModel();

        movieModel.setImdbId(omdbMovie.getImdbID());
        movieModel.setTitle(omdbMovie.getTitle());
        movieModel.setYear(omdbMovie.getYear() != null ? Integer.parseInt(omdbMovie.getYear()) : null);
        movieModel.setPosterUrl(omdbMovie.getPoster());
        movieModel.setIsAvailable(true);

        return movieModel;
    }

    private MovieModel convertToMovieModel(OMDbMovieDetail omdbMovieDetail) {
        MovieModel movieModel = new MovieModel();

        // Основная информация
        movieModel.setImdbId(omdbMovieDetail.getImdbID());
        movieModel.setTitle(omdbMovieDetail.getTitle());
        movieModel.setOriginalTitle(omdbMovieDetail.getTitle());
        movieModel.setOverview(omdbMovieDetail.getPlot());
        movieModel.setDescription(omdbMovieDetail.getPlot());
        
        // Год выпуска
        if (omdbMovieDetail.getYear() != null && !omdbMovieDetail.getYear().equals("N/A")) {
            try {
                movieModel.setYear(Integer.parseInt(omdbMovieDetail.getYear()));
            } catch (Exception e) {
                logger.warn("Failed to parse year: {}", omdbMovieDetail.getYear());
            }
        }
        
        // Дата релиза
        if (omdbMovieDetail.getReleased() != null && !omdbMovieDetail.getReleased().equals("N/A")) {
            try {
                movieModel.setReleaseDate(LocalDate.parse(omdbMovieDetail.getReleased(), DateTimeFormatter.ofPattern("dd MMM yyyy")));
            } catch (Exception e) {
                logger.warn("Failed to parse release date: {}", omdbMovieDetail.getReleased());
            }
        }

        // Продолжительность
        if (omdbMovieDetail.getRuntime() != null && !omdbMovieDetail.getRuntime().equals("N/A")) {
            try {
                movieModel.setDuration(Integer.parseInt(omdbMovieDetail.getRuntime().replaceAll("\\D", "")));
            } catch (Exception e) {
                logger.warn("Failed to parse runtime: {}", omdbMovieDetail.getRuntime());
            }
        }
        
        // Возрастной рейтинг
        movieModel.setAgeRating(omdbMovieDetail.getRated());
        
        // Язык
        movieModel.setLanguage(omdbMovieDetail.getLanguage());
        
        // Жанр
        movieModel.setGenre(omdbMovieDetail.getGenre());
        
        // Режиссер
        movieModel.setDirector(omdbMovieDetail.getDirector());
        
        // Сценарист (из Writer)
        movieModel.setScreenwriter(omdbMovieDetail.getWriter());
        
        // Актеры
        movieModel.setCast(omdbMovieDetail.getActors());
        
        // Страна
        movieModel.setCountry(omdbMovieDetail.getCountry());
        
        // Награды
        if (omdbMovieDetail.getAwards() != null && !omdbMovieDetail.getAwards().equals("N/A")) {
            movieModel.setAwards(omdbMovieDetail.getAwards());
            logger.debug("Awards: {}", omdbMovieDetail.getAwards());
        }

        // Рейтинг IMDB
        if (omdbMovieDetail.getImdbRating() != null && !omdbMovieDetail.getImdbRating().equals("N/A")) {
            try {
                movieModel.setImdbRating(Double.parseDouble(omdbMovieDetail.getImdbRating()));
            } catch (Exception e) {
                logger.warn("Failed to parse IMDB rating: {}", omdbMovieDetail.getImdbRating());
            }
        }
        
        // Количество голосов IMDB
        if (omdbMovieDetail.getImdbVotes() != null && !omdbMovieDetail.getImdbVotes().equals("N/A")) {
            try {
                movieModel.setRatingCount(Long.parseLong(omdbMovieDetail.getImdbVotes().replaceAll(",", "")));
            } catch (Exception e) {
                logger.warn("Failed to parse IMDB votes: {}", omdbMovieDetail.getImdbVotes());
            }
        }

        // Постер
        movieModel.setPosterUrl(omdbMovieDetail.getPoster());
        
        // Статус доступности
        movieModel.setIsAvailable(true);
        
        // Дополнительные поля для удобства
        movieModel.setCastList(parseCastList(omdbMovieDetail.getActors()));
        movieModel.setFormattedDuration(formatDuration(movieModel.getDuration()));

        return movieModel;
    }
    
    private List<String> parseCastList(String cast) {
        if (cast == null || cast.trim().isEmpty() || cast.equals("N/A")) {
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

    private Movie convertToMovie(MovieModel movieModel) {
        Movie movie = new Movie();

        // Основная информация
        movie.setImdbId(movieModel.getImdbId());
        movie.setTitle(movieModel.getTitle());
        movie.setOriginalTitle(movieModel.getOriginalTitle());
        movie.setOverview(movieModel.getOverview());
        movie.setDescription(movieModel.getDescription());
        movie.setYear(movieModel.getYear());
        movie.setReleaseDate(movieModel.getReleaseDate());
        movie.setDuration(movieModel.getDuration());
        movie.setAgeRating(movieModel.getAgeRating());
        movie.setCountry(movieModel.getCountry());
        movie.setLanguage(movieModel.getLanguage());
        movie.setGenre(movieModel.getGenre());
        movie.setDirector(movieModel.getDirector());
        movie.setScreenwriter(movieModel.getScreenwriter());
        movie.setCast(movieModel.getCast());
        movie.setImdbRating(movieModel.getImdbRating());
        movie.setRatingCount(movieModel.getRatingCount());
        movie.setPosterUrl(movieModel.getPosterUrl());
        movie.setIsAvailable(true);

        return movie;
    }

    // Fallback methods
    public List<MovieModel> fallbackGetPopularMovies(Exception ex) {
        logger.warn("OMDb API fallback triggered for popular movies: {}", ex.getMessage());
        return new ArrayList<>();
    }

    public List<MovieModel> fallbackGetTopRatedMovies(Exception ex) {
        logger.warn("OMDb API fallback triggered for top rated movies: {}", ex.getMessage());
        return new ArrayList<>();
    }

    public List<MovieModel> fallbackSearchMovies(String query, Exception ex) {
        logger.warn("OMDb API fallback triggered for search '{}': {}", query, ex.getMessage());
        return new ArrayList<>();
    }

    public List<MovieModel> fallbackAdvancedSearch(String query, String genre, Integer year, String country, Exception ex) {
        logger.warn("OMDb API fallback triggered for advanced search - Query: {}, Genre: {}, Year: {}, Country: {}: {}", 
                   query, genre, year, country, ex.getMessage());
        return new ArrayList<>();
    }

    public Optional<MovieModel> fallbackGetMovieDetails(String imdbId, Exception ex) {
        logger.warn("OMDb API fallback triggered for movie ID '{}': {}", imdbId, ex.getMessage());
        return Optional.empty();
    }
}
