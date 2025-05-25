package com.project.Plant.OrderManagement.Repository;

import com.project.Plant.OrderManagement.Entity.Order;
import com.project.Plant.UserManagement.Entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByUser(UserEntity user);
    List<Order> findAllByOrderByOrderDateDesc();
}