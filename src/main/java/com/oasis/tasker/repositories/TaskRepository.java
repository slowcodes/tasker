package com.oasis.tasker.repositories;

import com.oasis.tasker.entities.Task;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface TaskRepository extends JpaRepository<Task, Long> {

    List<Task> findByOwnerId(Long ownerId);

    Page<Task> findByOwnerId(Long ownerId, Pageable pageable);

    long countByOwnerId(Long ownerId);

    @Query("SELECT t FROM Task t WHERE t.owner.id = ?1 AND t.name LIKE %?2%")
    Page<Task> findByOwnerIdAndNameContaining(Long ownerId, String name, Pageable pageable);
}
