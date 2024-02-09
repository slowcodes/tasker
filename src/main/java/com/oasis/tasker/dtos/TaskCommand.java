package com.oasis.tasker.dtos;

import com.oasis.tasker.enums.Priority;
import com.oasis.tasker.enums.Status;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TaskCommand {
    private Long id;
    private String name;
    private String description;
    private CategoryCommand category;
    private Long ownerId;
    private LocalDateTime dueDate;
    private Priority priority;
    private Status status;
}
