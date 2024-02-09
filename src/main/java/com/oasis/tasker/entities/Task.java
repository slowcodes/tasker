package com.oasis.tasker.entities;

import com.oasis.tasker.enums.Priority;
import com.oasis.tasker.enums.Status;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Task extends BaseModel{

    private String name;
    private String description;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User owner;

    private LocalDateTime dueDate;

    @Enumerated(EnumType.ORDINAL)
    private Priority priority;

    @Enumerated(EnumType.ORDINAL)
    private Status status;
}