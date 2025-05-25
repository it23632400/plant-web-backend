package com.project.Plant.WorkerManagement.Repository;

import com.project.Plant.WorkerManagement.Entity.WorkerEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface WorkerRepository extends JpaRepository<WorkerEntity, Long> {
    Optional<WorkerEntity> findByEmail(String email);
}
