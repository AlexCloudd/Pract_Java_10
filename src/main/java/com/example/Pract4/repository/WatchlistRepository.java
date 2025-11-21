package com.example.Pract4.repository;

import com.example.Pract4.entity.Watchlist;
import com.example.Pract4.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface WatchlistRepository extends JpaRepository<Watchlist, Long> {
    
    List<Watchlist> findByUser(User user);
    
    List<Watchlist> findByUserAndIsPublicTrue(User user);
    
    List<Watchlist> findByIsPublicTrue();
    
    List<Watchlist> findByNameContainingIgnoreCase(String name);
    
    @Query("SELECT w FROM Watchlist w WHERE w.user = :user AND " +
           "LOWER(w.name) LIKE LOWER(CONCAT('%', :query, '%'))")
    List<Watchlist> searchUserWatchlists(@Param("user") User user, @Param("query") String query);
    
    @Query("SELECT w FROM Watchlist w WHERE w.isPublic = true AND " +
           "LOWER(w.name) LIKE LOWER(CONCAT('%', :query, '%'))")
    List<Watchlist> searchPublicWatchlists(@Param("query") String query);
    
    @Query("SELECT w FROM Watchlist w ORDER BY w.likeCount DESC")
    List<Watchlist> findTopWatchlistsByLikeCount();
    
    @Query("SELECT w FROM Watchlist w ORDER BY w.viewCount DESC")
    List<Watchlist> findTopWatchlistsByViewCount();
    
    @Query("SELECT w FROM Watchlist w ORDER BY w.createdAt DESC")
    List<Watchlist> findLatestWatchlists();
    
    Optional<Watchlist> findByUserAndName(User user, String name);
    
    @Query("SELECT w FROM Watchlist w WHERE w.user = :user AND w.id = :id")
    Optional<Watchlist> findByUserAndId(@Param("user") User user, @Param("id") Long id);
}






