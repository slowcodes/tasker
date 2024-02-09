package com.oasis.tasker.repositories;

import com.oasis.tasker.entities.Task;
import com.oasis.tasker.enums.Priority;
import com.oasis.tasker.enums.Status;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public interface TaskRepository extends JpaRepository<Task, Long> {

    List<Task> findByOwnerId(Long ownerId);

    Page<Task> findByOwnerId(Long ownerId, Pageable pageable);

    long countByOwnerId(Long ownerId);

    Page<Task> findByOwnerIdAndDueDateAndPriorityAndStatus(
            Long ownerId,
            LocalDateTime dueDate,
            Priority priority,
            Status status,
            Pageable pageable
    );


    // Filter by owner ID and due date
    Page<Task> findByOwnerIdAndDueDate(Long ownerId, LocalDateTime dueDate, Pageable pageable);

    // Filter by owner ID and priority
    Page<Task> findByOwnerIdAndPriority(Long ownerId, Priority priority, Pageable pageable);

    // Filter by owner ID and status
    Page<Task> findByOwnerIdAndStatus(Long ownerId, Status status, Pageable pageable);

    // Filter by owner ID, priority, and status
    Page<Task> findByOwnerIdAndPriorityAndStatus(Long ownerId, Priority priority, Status status, Pageable pageable);

    // Filter by owner ID, priority, and due date
    Page<Task> findByOwnerIdAndPriorityAndDueDate(Long ownerId, Priority priority, LocalDateTime dueDate, Pageable pageable);

    // Filter by owner ID, due date, and status
    Page<Task> findByOwnerIdAndDueDateAndStatus(Long ownerId, LocalDateTime dueDate, Status status, Pageable pageable);


    Page<Task> findByOwnerIdAndNameContainingAndDueDateAndPriorityAndStatus(
            Long ownerId,
            String name,
            LocalDateTime dueDate,
            Priority priority,
            Status status,
            Pageable pageable
    );

    @Query("SELECT t FROM Task t WHERE t.owner.id = ?1 AND t.name LIKE %?2%")
    Page<Task> findByOwnerIdAndNameContaining(Long ownerId, String name, Pageable pageable);
}
