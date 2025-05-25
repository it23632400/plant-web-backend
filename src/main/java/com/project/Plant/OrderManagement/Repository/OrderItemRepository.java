package com.project.Plant.OrderManagement.Repository;

import com.project.Plant.OrderManagement.Entity.Order;
import com.project.Plant.OrderManagement.Entity.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {
    List<OrderItem> findByOrder(Order order);
}