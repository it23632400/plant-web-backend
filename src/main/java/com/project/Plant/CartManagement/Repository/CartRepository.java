package com.project.Plant.CartManagement.Repository;

import com.project.Plant.CartManagement.Entity.Cart;
import com.project.Plant.ItemManagement.Entity.Item;
import com.project.Plant.UserManagement.Entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CartRepository extends JpaRepository<Cart, Long> {
    List<Cart> findByUser(UserEntity user);
    Optional<Cart> findByUserAndItem(UserEntity user, Item item);
    void deleteByUser(UserEntity user);
}