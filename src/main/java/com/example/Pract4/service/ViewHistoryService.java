package com.example.Pract4.service;

import com.example.Pract4.model.MovieModel;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import jakarta.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;

@Service
public class ViewHistoryService {

    private static final String VIEW_HISTORY_KEY = "viewHistory";
    private static final int MAX_HISTORY_SIZE = 10;

    public void addToHistory(MovieModel movie) {
        HttpSession session = getCurrentSession();
        if (session != null) {
            @SuppressWarnings("unchecked")
            ConcurrentLinkedQueue<MovieModel> history = (ConcurrentLinkedQueue<MovieModel>) 
                session.getAttribute(VIEW_HISTORY_KEY);
            
            if (history == null) {
                history = new ConcurrentLinkedQueue<>();
            }
            
            // Удаляем фильм, если он уже есть в истории
            history.removeIf(m -> m.getImdbId() != null && m.getImdbId().equals(movie.getImdbId()));
            
            // Добавляем фильм в начало истории
            history.offer(movie);
            
            // Ограничиваем размер истории
            while (history.size() > MAX_HISTORY_SIZE) {
                history.poll();
            }
            
            session.setAttribute(VIEW_HISTORY_KEY, history);
        }
    }

    public List<MovieModel> getViewHistory() {
        HttpSession session = getCurrentSession();
        if (session != null) {
            @SuppressWarnings("unchecked")
            ConcurrentLinkedQueue<MovieModel> history = (ConcurrentLinkedQueue<MovieModel>) 
                session.getAttribute(VIEW_HISTORY_KEY);
            
            if (history != null) {
                return new ArrayList<>(history);
            }
        }
        return new ArrayList<>();
    }

    public void clearHistory() {
        HttpSession session = getCurrentSession();
        if (session != null) {
            session.removeAttribute(VIEW_HISTORY_KEY);
        }
    }

    private HttpSession getCurrentSession() {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        return attributes != null ? attributes.getRequest().getSession() : null;
    }
}
