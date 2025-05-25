package com.project.Plant.WishlistManagement.Repository;

import com.project.Plant.WishlistManagement.Entity.Wishlist;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface WishlistRepository extends JpaRepository<Wishlist, Long> {

    List<Wishlist> findByUserId(Long userId);

    @Query("SELECT w FROM Wishlist w WHERE w.user.id = :userId AND w.item.itemId = :itemId")
    Optional<Wishlist> findByUserIdAndItemId(@Param("userId") Long userId, @Param("itemId") Long itemId);

    void deleteByUserIdAndItemItemId(Long userId, Long itemId);
}