package com.example.Pract4.service;

import com.example.Pract4.client.MovieServiceClient;
import com.example.Pract4.client.UserServiceClient;
import com.example.Pract4.model.MovieModel;
import com.example.Pract4.model.UserModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AdminService {

    @Autowired
    private MovieServiceClient movieServiceClient;

    @Autowired
    private UserServiceClient userServiceClient;

    // ========== УПРАВЛЕНИЕ ФИЛЬМАМИ ==========

    public List<MovieModel> getAllMovies() {
        // Получаем данные из Movie Service и конвертируем в MovieModel
        return movieServiceClient.getAllMovies().stream()
                .map(this::convertToMovieModel)
                .collect(Collectors.toList());
    }

    public MovieModel getMovieById(Long id) {
        return convertToMovieModel(movieServiceClient.getMovieById(id));
    }

    public MovieModel createMovie(MovieModel movie) {
        return convertToMovieModel(movieServiceClient.createMovie(movie));
    }

    public MovieModel updateMovie(Long id, MovieModel movie) {
        return convertToMovieModel(movieServiceClient.updateMovie(id, movie));
    }

    public void deleteMovie(Long id) {
        movieServiceClient.deleteMovie(id);
    }

    public List<MovieModel> searchMovies(String query) {
        return movieServiceClient.searchMovies(query).stream()
                .map(this::convertToMovieModel)
                .collect(Collectors.toList());
    }

    // ========== УПРАВЛЕНИЕ ПОЛЬЗОВАТЕЛЯМИ ==========

    public List<UserModel> getAllUsers() {
        return userServiceClient.getAllUsers();
    }

    public UserModel getUserById(Long id) {
        return userServiceClient.getUserById(id);
    }

    public UserModel createUser(UserModel user) {
        return userServiceClient.createUser(user);
    }

    public UserModel updateUser(Long id, UserModel user) {
        return userServiceClient.updateUser(id, user);
    }

    public void deleteUser(Long id) {
        userServiceClient.deleteUser(id);
    }

    public List<UserModel> searchUsers(String query) {
        return userServiceClient.searchUsers(query);
    }

    // ========== СТАТИСТИКА ==========

    public long getTotalMovies() {
        List<MovieModel> movies = getAllMovies();
        return movies != null ? movies.size() : 0;
    }

    public long getTotalUsers() {
        List<UserModel> users = getAllUsers();
        return users != null ? users.size() : 0;
    }

    public long getTotalRatings() {
        // Здесь можно добавить вызов к Rating Service для получения общего количества отзывов
        // Пока возвращаем 0
        return 0;
    }

    // ========== КОНВЕРТЕРЫ ==========

    private MovieModel convertToMovieModel(Object movieData) {
        // Простая конвертация - в реальном приложении здесь должна быть более сложная логика
        MovieModel movieModel = new MovieModel();
        
        // Используем рефлексию для копирования полей
        try {
            java.lang.reflect.Field[] fields = movieData.getClass().getDeclaredFields();
            for (java.lang.reflect.Field field : fields) {
                field.setAccessible(true);
                Object value = field.get(movieData);
                
                try {
                    java.lang.reflect.Field modelField = MovieModel.class.getDeclaredField(field.getName());
                    modelField.setAccessible(true);
                    modelField.set(movieModel, value);
                } catch (NoSuchFieldException e) {
                    // Поле не найдено в MovieModel, пропускаем
                }
            }
        } catch (Exception e) {
            // В случае ошибки возвращаем пустую модель
            e.printStackTrace();
        }
        
        return movieModel;
    }
}
