package com.example.Pract4.service;

import com.example.Pract4.model.MovieModel;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class SessionWatchlistService {
    
    private static final String WATCHLIST_SESSION_KEY = "user_watchlist";
    private static final int MAX_WATCHLIST_SIZE = 50; // Максимальное количество фильмов в списке
    
    /**
     * Добавляет фильм в список просмотров пользователя (хранится в сессии)
     */
    public boolean addMovieToWatchlist(String imdbId, String title, String posterUrl, HttpSession session) {
        try {
            List<MovieModel> watchlist = getWatchlist(session);
            
            // Проверяем, не добавлен ли уже этот фильм
            boolean alreadyExists = watchlist.stream()
                    .anyMatch(movie -> imdbId.equals(movie.getImdbId()));
            
            if (alreadyExists) {
                return false; // Фильм уже в списке
            }
            
            // Создаем новый фильм
            MovieModel movie = new MovieModel();
            movie.setImdbId(imdbId);
            movie.setTitle(title);
            movie.setPosterUrl(posterUrl);
            movie.setIsAvailable(true);
            
            // Добавляем в начало списка
            watchlist.add(0, movie);
            
            // Ограничиваем размер списка
            if (watchlist.size() > MAX_WATCHLIST_SIZE) {
                watchlist = watchlist.subList(0, MAX_WATCHLIST_SIZE);
            }
            
            // Сохраняем в сессию
            session.setAttribute(WATCHLIST_SESSION_KEY, watchlist);
            
            return true;
            
        } catch (Exception e) {
            return false;
        }
    }
    
    /**
     * Получает список просмотров пользователя из сессии
     */
    public List<MovieModel> getWatchlist(HttpSession session) {
        @SuppressWarnings("unchecked")
        List<MovieModel> watchlist = (List<MovieModel>) session.getAttribute(WATCHLIST_SESSION_KEY);
        
        if (watchlist == null) {
            watchlist = new ArrayList<>();
            session.setAttribute(WATCHLIST_SESSION_KEY, watchlist);
        }
        
        return watchlist;
    }
    
    /**
     * Удаляет фильм из списка просмотров
     */
    public boolean removeMovieFromWatchlist(String imdbId, HttpSession session) {
        try {
            List<MovieModel> watchlist = getWatchlist(session);
            
            boolean removed = watchlist.removeIf(movie -> imdbId.equals(movie.getImdbId()));
            
            if (removed) {
                session.setAttribute(WATCHLIST_SESSION_KEY, watchlist);
            }
            
            return removed;
            
        } catch (Exception e) {
            return false;
        }
    }
    
    /**
     * Проверяет, есть ли фильм в списке просмотров
     */
    public boolean isMovieInWatchlist(String imdbId, HttpSession session) {
        try {
            List<MovieModel> watchlist = getWatchlist(session);
            return watchlist.stream()
                    .anyMatch(movie -> imdbId.equals(movie.getImdbId()));
        } catch (Exception e) {
            return false;
        }
    }
    
    /**
     * Получает количество фильмов в списке просмотров
     */
    public int getWatchlistSize(HttpSession session) {
        return getWatchlist(session).size();
    }
    
    /**
     * Очищает весь список просмотров
     */
    public void clearWatchlist(HttpSession session) {
        session.removeAttribute(WATCHLIST_SESSION_KEY);
    }
}
